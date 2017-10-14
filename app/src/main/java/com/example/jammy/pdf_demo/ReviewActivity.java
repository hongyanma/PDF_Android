package com.example.jammy.pdf_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.com.adapter.SearchHisAdapter;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/16.
 */

public class ReviewActivity extends Activity {

    @Bind(R.id.editText1_r)
    EditText editText1_r;
    @Bind(R.id.search_r)
    TextView search_r;
    @Bind(R.id.tv_time_r)
    TextView tv_time_r;
    @Bind(R.id.img_exit_r)
    ImageView img_exit_r;
    @Bind(R.id.Cannot)
    TextView Cannot;
    @Bind(R.id.listview_his)
    ListView listview_his;

    private ProgressDialog dialog;
    private Handler handler;
    private loadDataThreah ldt;
    private List<User> editorlist;
    private String color;
    private int role_id;
    private int userId;
    private String timeExpress;
    private String urlpath = "";
    private String schedule;
    private String order_id; //订单id
    private String tagKEY="false";//判断能否编写
    private long mExitTime;//退出时的时间
    private SearchHisAdapter adapter;
    private SetFileorder listfileFileorder;
    private String ordernum;
    private boolean visable = false;
      private String edittime;
    private boolean result;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review);
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
        tv_time_r.setText(timeExpress + "好!" + rolename + "：" + nickname);
    }


    private void SetListent() {

        img_exit_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReviewActivity.this, LoginActivity.class);
                startActivity(intent);
                SPUtils.clear(ReviewActivity.this);
                finish();
            }
        });

        search_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordernum = editText1_r.getText().toString().trim();
                if (ordernum.equals("")) {
                    Toast.makeText(ReviewActivity.this, "搜索框不能为空！", Toast.LENGTH_SHORT).show();

                } else {

                    Log.i("TTTTT", "UUU" + ordernum);
                    HashMap<String, String> searchjson = new Fileorder().searchJson(ordernum);
                    new HttpTask(ordersearchhttpCallback, MyURL.SEARCHOREDE).execute(searchjson);

                }
            }
        });


    }

    private HttpCallback ordersearchhttpCallback = new HttpCallback() {  //搜索订单
        @Override
        public void getResult(JSONObject json) {  //按订单号查找
            CBLL cBllorder = CBLL.getInstance();
            JSONEntity entity = cBllorder.jsonsearch(json);
            if (entity.isFlag()) {  //有订单
                Fileorder fileorder = (Fileorder) entity.getData();
                Toast.makeText(ReviewActivity.this, "搜索成功", Toast.LENGTH_SHORT).show();
                listview_his.setVisibility(View.VISIBLE);
                Cannot.setVisibility(View.GONE);
                HashMap<String, String> compiled = new Fileorder().toCompeile(fileorder.getOrder_id());
                new HttpTask(orderhisCallback, MyURL.COMPlET).execute(compiled);
                schedule=fileorder.getSchedule();
            } else {  //没有待处理订单
                listview_his.setVisibility(View.GONE);
                Cannot.setText("无此订单");
                Toast.makeText(ReviewActivity.this, "没有此订单", Toast.LENGTH_SHORT).show();
                Cannot.setVisibility(View.VISIBLE);
            }
        }
    };


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
            Intent intent = new Intent(ReviewActivity.this, PDFActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.putExtra("order_id", order_id);
            intent.putExtra("tag",tagKEY);
            intent.putExtra("urlpath", urlpath);
            intent.putExtra("userid", userId);
            intent.putExtra("color", color);
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
//                    HashMap<String, String> editingjson = new Fileorder().towait(role_id,userId);
//                    new HttpTask(orderhttpCallback, MyURL.WAITORDER).execute(editingjson);
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
     * 返回按钮，退出个文件
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
            Toast.makeText(ReviewActivity.this, "再按一次退出PDF批注", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }

    }
private HttpCallback zhengshenCallback=new HttpCallback() {
    @Override
    public void getResult(JSONObject json) {
        try {
            result=json.getBoolean("result");

            if(result==true){
                Toast.makeText(ReviewActivity.this,"终审完成！",Toast.LENGTH_SHORT).show();
                HashMap<String, String> searchjson = new Fileorder().searchJson(ordernum);
                new HttpTask(ordersearchhttpCallback, MyURL.SEARCHOREDE).execute(searchjson);
            }else {
                Toast.makeText(ReviewActivity.this,"终审失败！",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
};

    private HttpCallback orderhisCallback = new HttpCallback() {

        @Override
        public void getResult(JSONObject json) {

            CBLL cBllorder = CBLL.getInstance();
            JSONEntity entity = cBllorder.jsonsearchhis(json);

            listfileFileorder = (SetFileorder) entity.getData();


            adapter = new SearchHisAdapter(ReviewActivity.this, listfileFileorder);
            listview_his.setAdapter(adapter);


            //编辑按钮的监听
            adapter.setOnItemDeleteClickListener(new SearchHisAdapter.onItemDeleteListener() {
                @Override
                public void onDeleteClick(int i) {
                    urlpath = MyURL.DOWNLOADFILE + listfileFileorder.getItem(i).getFile_path();
                    order_id = listfileFileorder.getItem(i).getOrder_id();

                    if (schedule.equals("2")) {
                        tagKEY="false";
                        DownloadFile();
                    }
                    else {


                        if (SPUtils.get(ReviewActivity.this, "KEY", 0).equals(1)) {
                            tagKEY = "ture";
                            SPUtils.put(ReviewActivity.this, "KEY",1);
                            DownloadFile();


                        }else{


                            if (listfileFileorder.getItem(i).getEdition() == 1) {  //最新版本
                                if (listfileFileorder.getItem(i).getLocked() == 1) {
                                    tagKEY = "false";
                                    DownloadFile();
                                } else {
                                    tagKEY = "true";
                                    //枷锁
                                    HashMap<String, String> searchjson = new Fileorder().lockfile(order_id);
                                    new HttpTask(localfilehttpcallback, MyURL.SEARCHOREDE).execute(searchjson);
                                    SPUtils.put(ReviewActivity.this, "KEY", 1);
                                }


                            } else {
                                tagKEY = "false";
                                DownloadFile();
                            }



                        }
                        }


                }
            });

            adapter.setOnItemfinalClickListener(new SearchHisAdapter.onItemfinalListener() {
                @Override
                public void onfinal(int i) {
                    order_id = listfileFileorder.getItem(i).getOrder_id();
                    edittime=listfileFileorder.getItem(i).getEdit_time();
                    Log.i("fff","fff"+edittime);
                    if (listfileFileorder.getItem(i).getEdition() == 1) {


                        if (listfileFileorder.getItem(i).getLocked() == 1) {
                            Toast.makeText(ReviewActivity.this, "订单正在被编辑，暂不能终审存档！", Toast.LENGTH_SHORT).show();
                        } else {
                            //终身存档发userid 和 orderid

                            AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                            builder.setTitle("终审存档？");
                            builder.setTitle("是否确定将文件终审存档？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    HashMap<String, String> filelocked = new Fileorder().FileLocked(order_id, userId + "");
                                    new HttpTask(zhengshenCallback, MyURL.COMPlET).execute(filelocked);



                                    adapter.notifyDataSetChanged();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }
                    } else {
                        if (listfileFileorder.getItem(0).getLocked() == 1) {
                            Toast.makeText(ReviewActivity.this, "订单正在被编辑，暂不能退至此订单，请稍后！", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                            builder.setTitle("是否确定退至此订单操作？");
                            builder.setTitle("确定后此订单将为最新订单 是否确定？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    HashMap<String, String> filelocked = new Fileorder().FileLocked(order_id, userId ,edittime);

                                    new HttpTask(null, MyURL.COMPlET).execute(filelocked);

                                    HashMap<String, String> searchjson = new Fileorder().searchJson(ordernum);
                                    new HttpTask(ordersearchhttpCallback, MyURL.SEARCHOREDE).execute(searchjson);

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                        }


                    }

                }
            });
        }
    };

    HttpCallback localfilehttpcallback=new HttpCallback() {
        @Override
        public void getResult(JSONObject json) {
            try {
                Boolean flag=json.getBoolean("result");
                if(flag){
                    tagKEY = "true";
                    DownloadFile();
                }else{
                    tagKEY = "false";
                    DownloadFile();
                    Toast.makeText(ReviewActivity.this,"正在有人编辑订单，请重新搜索！",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
