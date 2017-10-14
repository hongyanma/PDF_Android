package com.example.jammy.pdf_demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.Bll.CBLL;
import com.com.Bll.JSONEntity;
import com.com.Http.HttpCallback;
import com.com.Http.HttpTask;
import com.com.adapter.SearchAdapter;
import com.com.entity.Fileorder;
import com.com.entity.SetFileorder;
import com.com.entity.User;
import com.com.tool.MyURL;
import com.com.tool.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends Activity {

    @Bind(R.id.editText1)
    EditText editText1;
    @Bind(R.id.search)
    TextView search;
    @Bind(R.id.img_exit)
    ImageView img_exit;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.Cannot)
    TextView Cannot;
    private ProgressDialog dialog;
    private Handler handler;
    private loadDataThreah ldt;
    private String timeExpress;
    private int userId;
    private SearchAdapter adapter;
    // urlpath是网络的PDF地址，要换成服务器端的PDF地址
    //  private String urlpath = "http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
    private SetFileorder insurancelist;
    private String urlpath = "";
    private List<User> editorlist;
    private Boolean tag;//判断能否编写
    private String order_id; //订单id
    private String color;
    private String schedule;
    private long mExitTime;//退出时的时间
    private int role_id; //角色id
    private String tagKEY;
    //service变量
    private Intent intent;
    private Fileorder fileorder;
    private String ordernum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);

        UpdateUI();
        SetListent();
    }


    private void UpdateUI() {
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        String nickname = intent.getStringExtra("nickname");
        color = intent.getStringExtra("color");
        String rolename = intent.getStringExtra("role_name");

        role_id = intent.getIntExtra("role_id", -1);
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int hour = mCalendar.get(Calendar.HOUR);
        int apm = mCalendar.get(Calendar.AM_PM);
        if (apm == 0) { //表示上午
            timeExpress = "上午";
        } else if (apm == 1) {  //表示下午
            timeExpress = "下午";
        }
        ;
        tv_time.setText(timeExpress + "好!" + rolename + "：" + nickname);
    }


    private HttpCallback ordersearchhttpCallback = new HttpCallback() {  //搜索订单
        @Override
        public void getResult(JSONObject json) {  //按订单号查找
            CBLL cBllorder = CBLL.getInstance();
            JSONEntity entity = cBllorder.jsonsearch(json);

            if (entity.isFlag()) {  //有订单
                fileorder = (Fileorder) entity.getData();
                List<Fileorder> data = new ArrayList<Fileorder>();
                data.add(fileorder);
                insurancelist = new SetFileorder(data);
                schedule = fileorder.getSchedule();

                Toast.makeText(SearchActivity.this, "搜索成功", Toast.LENGTH_SHORT).show();
                adapter = new SearchAdapter(SearchActivity.this, insurancelist,role_id+"");
                listview.setAdapter(adapter);


                adapter.setOnItemDeleteClickListener(new SearchAdapter.onItemDeleteListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        urlpath = MyURL.DOWNLOADFILE + insurancelist.getItem(position).getFile_path();
                        order_id = insurancelist.getItem(position).getOrder_id() + "";

                        if (role_id == 4) {
                            tagKEY = "false"; //浏览
                            DownloadFile();
                        } else {

                        if (schedule.equals("2")) {
                            tagKEY = "false"; //浏览
                            DownloadFile();
                        } else {
                            if (SPUtils.get(SearchActivity.this, "KEY", 0).equals(1)) {

                                tagKEY = "true";

                                SPUtils.put(SearchActivity.this, "KEY", 1);
                                HashMap<String, String> searchjson = new Fileorder().lockfile(order_id);
                                new HttpTask(localfilehttpcallback, MyURL.SEARCHOREDE).execute(searchjson);


                            } else {

                                if (fileorder.getLocked() == 0) {
                                    tagKEY = "true";
                                    SPUtils.put(SearchActivity.this, "KEY", 1);
                                    HashMap<String, String> searchjson = new Fileorder().lockfile(order_id);
                                    new HttpTask(localfilehttpcallback, MyURL.SEARCHOREDE).execute(searchjson);
                                    Log.i("TAG", "11111111111111111");
                                } else if (fileorder.getLocked() == 1) {
                                    tagKEY = "false";
                                    Log.i("TAG", "2222");
                                    DownloadFile();
                                }


                            }

                        }
                    }
                    }
                });

                listview.setVisibility(View.VISIBLE);
                Cannot.setVisibility(View.GONE);
            } else {  //没有待处理订单
                listview.setVisibility(View.GONE);
                Cannot.setText("无此订单!");
                Toast.makeText(SearchActivity.this, "没有此订单", Toast.LENGTH_SHORT).show();
                Cannot.setVisibility(View.VISIBLE);

            }
        }
    };

    HttpCallback localfilehttpcallback = new HttpCallback() {
        @Override
        public void getResult(JSONObject json) {
            try {
                Boolean flag = json.getBoolean("result");
                if (flag) {
                    tagKEY = "true";
                    DownloadFile();
                } else {
                    tagKEY = "false";
                    DownloadFile();
                    Toast.makeText(SearchActivity.this, "正在有人编辑订单，请重新搜索！", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void SetListent() {

        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, LoginActivity.class);
                startActivity(intent);
                SPUtils.clear(SearchActivity.this);

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordernum = editText1.getText().toString().trim();
                if (ordernum.equals("")) {
                    //获得所有待处理订单
                    Toast.makeText(SearchActivity.this, "请输入订单号！", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> searchjson = new Fileorder().searchJson(ordernum);
                    new HttpTask(ordersearchhttpCallback, MyURL.SEARCHOREDE).execute(searchjson);

                }

            }
        });
    }


    // 进度条线程 下载操作放进线程中
    class loadDataThreah extends Thread {
        public void run() {
            try {
                showPDF();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    }

    // 从服务器下载PDF并且跳转到MUPDF的ACTIVITY
    public void showPDF() throws Exception {
        Log.i("TAG", "ccccccccccccccc" + urlpath);
        URL u = new URL(urlpath + "");
        String path = createDir("订单.pdf");
        byte[] buffer = new byte[1024 * 8];
        int read;
        int ava = 0;
        long start = System.currentTimeMillis();
        BufferedInputStream bin;
        try {
            HttpURLConnection urlcon = (HttpURLConnection) u.openConnection();
            double fileLength = (double) urlcon.getContentLength();
            bin = new BufferedInputStream(u.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(path));
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                int a = (int) Math.floor((ava / fileLength * 100));
                dialog.setProgress(a);
                long speed = ava / (System.currentTimeMillis() - start);
                System.out.println("Download: " + ava + " byte(s)"
                        + "    avg speed: " + speed + "  (kb/s)");
            }
            bout.flush();
            bout.close();
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(SearchActivity.this, PDFActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.putExtra("order_id", order_id);
            intent.putExtra("tag", tagKEY);
            intent.putExtra("urlpath", urlpath);
            intent.putExtra("userid", userId);
            intent.putExtra("color", color);
            intent.putExtra("schedule", schedule);
            startActivityForResult(intent, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {  //重新下載打開的接口
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.i("TAG", "YYYYYY" + urlpath);
                    urlpath = data.getStringExtra("PATH");
                    ldt = new loadDataThreah();
                    ldt.start();
                } else if (resultCode == 1997) {
                    HashMap<String, String> searchjson = new Fileorder().searchJson(ordernum);
                    new HttpTask(ordersearchhttpCallback, MyURL.SEARCHOREDE).execute(searchjson);
                }
        }
    }

    private void DownloadFile() {
        dialog.setTitle("正在联网下载数据...");
        dialog.setMessage("请稍后...");
        // 设置进度条风格，风格为长形
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // 设置ProgressDialog 标题
        dialog.setTitle("提示");

        // 设置ProgressDialog 提示信息
        dialog.setMessage("正在下载数据，请稍等....");

        // 设置ProgressDialog 进度条进度
        dialog.setProgress(0);

        // 设置ProgressDialog 的进度条是否不明确
        dialog.setIndeterminate(false);

        // 设置ProgressDialog 是否可以按退回按键取消
        dialog.setCancelable(true);
        dialog.show();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                dialog.cancel();
                dialog.setProgress(0);
            }
        };
        ldt = new loadDataThreah();
        ldt.start();

    }

    /*
    * 创建存储文件的路径
    */
    private String createDir(String filename) {
        File sdcardDir = Environment.getExternalStorageDirectory();
        // 得到一个路径，内容是sdcard的文件夹路径和名字
        String path = sdcardDir.getPath() + "/MyMobileDownlod";
        File path1 = new File(path);
        if (!path1.exists())
            // 若不存在，创建目录，可以在应用启动的时候创建
            path1.mkdirs();
        path = path + "/" + filename;
        return path;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ldt != null && ldt.isAlive()) {
            ldt.interrupt();
            ldt = null;
        }
    }

    /**
     * 返回按钮，退出时删除那两个文件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(SearchActivity.this, "再按一次退出PDF批注", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }

    }
}
