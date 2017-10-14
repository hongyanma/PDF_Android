package com.example.jammy.pdf_demo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdf.MuPDFCore;
import com.artifex.mupdf.MuPDFPageAdapter;
import com.artifex.mupdf.ReaderView;
import com.artifex.mupdf.SavePdf;
import com.com.Http.HttpTask;
import com.com.entity.Fileorder;
import com.com.tool.MyURL;
import com.com.tool.SPUtils;
import com.com.tool.UploadUtil;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.jammy.pdf_demo.R.id.parent;


/**
 * Created by Jammy on 2016/6/23.
 */
public class PDFActivity extends Activity {

    @Bind(R.id.readerView)
    ReaderView readerView;
    @Bind(R.id.rl_sign)
    RelativeLayout rlSign;
    @Bind(R.id.rl_update)
    RelativeLayout rlUpdate;
    @Bind(R.id.rl_save)
    RelativeLayout rlSave;
    @Bind(R.id.tv_page)
    TextView tv_page;
    @Bind(R.id.tv_zhushi)
    TextView tv_zhushi;
    @Bind(R.id.backout)
    RelativeLayout backout;
    boolean isUpdate = false;
    String in_path;
    String out_path;
    String update_inpath;
    private PopupWindow popupWindow;
    private SignatureView signatureView;
    private boolean iBack = false;
    private float density; //屏幕分辨率密度
    private int first = 1;
    private String file_id;
    private ProgressDialog progressDialog;
    ProgressDialog UploadprogressDialog;
    MuPDFCore muPDFCore;
    Save_Pdf save_pdf;
    private SavePdf savePdf;
    private final int    TAP_PAGE_MARGIN = 5;
    private Boolean tag=true;
    private  String INPATH= Environment.getExternalStorageDirectory().getPath()+"/MyMobileDownlod/订单.pdf";
    private String OUTPATH=Environment.getExternalStorageDirectory().getPath()+"/MyMobileDownlod/订单复制.pdf";
    private String showtag;//判断能否显示
    private String order_id;
    private int user_id;
    private String urlpath;
    private int schedule;
    private String color;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ButterKnife.bind(this);

        View screenView = this.getWindow().getDecorView(); //防止放大变卡
        screenView.setDrawingCacheEnabled(true);
        screenView.buildDrawingCache();

        //计算分辨率密度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        density = metric.density;
        Log.i("TAG",density+"");
        Intent intent =getIntent();
        showtag= intent.getStringExtra("tag");
        order_id=intent.getStringExtra("order_id");

        user_id=intent.getIntExtra("userid",-1);
        urlpath=intent.getStringExtra("urlpath");
        color= intent.getStringExtra("color");



        if (muPDFCore == null) {
            in_path = Environment.getExternalStorageDirectory().getPath()+"/MyMobileDownlod/订单.pdf";
        }
            out_path = Environment.getExternalStorageDirectory().getPath()+"/MyMobileDownlod/订单复制.pdf";

        try {
            muPDFCore = new MuPDFCore(in_path);//PDF的文件路径

            if(showtag.equals("false")){
                rlSign.setVisibility(View.GONE);
                tv_page.setVisibility(View.VISIBLE);
                tv_page.setText("共"+muPDFCore.countPages()+"页");
            }

            readerView.setAdapter(new MuPDFPageAdapter(this, muPDFCore));
            View view = LayoutInflater.from(this).inflate(R.layout.signature_layout, null);
            signatureView = (SignatureView) view.findViewById(R.id.qianming);
            readerView.setDisplayedViewIndex(0);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);

            signatureView.setcolor(color);
            rlSign.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    if (rlSave.getVisibility() == View.GONE) {

                        if (Build.VERSION.SDK_INT < 24) {
                            popupWindow.showAsDropDown(rlSign,0,60);
                        } else {
                            int[] a = new int[2];
                            rlSign.getLocationInWindow(a);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, rlSign.getHeight()+a[1]+10);
                            popupWindow.update();
                        }
                        rlSave.setVisibility(View.VISIBLE);
                        tv_page.setVisibility(View.VISIBLE);
                     //   tv_page.setText(readerView.getDisplayedViewIndex()+1+"/"+muPDFCore.countPages());
                        Toast.makeText(PDFActivity.this,readerView.getDisplayedViewIndex()+1+"/"+muPDFCore.countPages(),Toast.LENGTH_SHORT).show();
                        rlUpdate.setVisibility(View.VISIBLE);
                        backout.setVisibility(View.VISIBLE);
                        tv_zhushi.setText("返回");

                    } else {
                        popupWindow.dismiss();
                       signatureView.removeAllPaint();
                        tv_page.setVisibility(View.GONE);
                        rlSave.setVisibility(View.GONE);
                        rlUpdate.setVisibility(View.GONE);
                        backout.setVisibility(View.GONE);
                        tv_zhushi.setText("编辑");
                        if(first!=1){
                        }

                    }
                }
            });
            rlSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(tag){  // 为true时原文本为最新、为false时 订单为最新
                        tag=false;
                    }else {
                        tag=true;
                    }
                    tv_zhushi.setText("编辑");
                    float scale = readerView.getmScale();///得到放大因子
                    savePdf = new SavePdf(in_path, out_path);
                    savePdf.setScale(scale);
                    savePdf.setPageNum(readerView.getDisplayedViewIndex() + 1);

                    savePdf.setWidthScale(1.0f * readerView.scrollX / readerView.getDisplayedView().getWidth());//计算宽偏移的百分比
                    savePdf.setHeightScale(1.0f * readerView.scrollY / readerView.getDisplayedView().getHeight());//计算长偏移的百分比


                    savePdf.setDensity(density);
                    Bitmap bitmap = Bitmap.createBitmap(signatureView.getWidth(), signatureView.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    signatureView.draw(canvas);

                    savePdf.setBitmap(bitmap);
                    save_pdf = new Save_Pdf(savePdf);
                    save_pdf.execute();
                    popupWindow.dismiss();
                    iBack = true;
                    rlSave.setVisibility(View.GONE);
                    rlUpdate.setVisibility(View.GONE);
                    backout.setVisibility(View.GONE);
                    tv_page.setVisibility(View.GONE);
                    ///显示隐藏probar
                    progressDialog = ProgressDialog.show(PDFActivity.this, null, "正在存储...");
                   signatureView.removeAllPaint();
                }
            });


            rlSign.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PDFActivity.this);
                    builder.setTitle("确定返回重写将删除之前的保存记录 是否确定？");
                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除缓冲的存储
                            Intent intent =new Intent();
                            intent.putExtra("PATH",urlpath);  //传一个路径过去
                            setResult(RESULT_OK,intent);
                            //删除所有文件
                            File file = new File(INPATH);
                            file.delete();
                            File file1 = new File(OUTPATH);
                            file1.delete();

                            PDFActivity.this.finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();



                    return false;
                }
            });

            backout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signatureView.undo();
                }
            });

            rlUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  //上传文件

                    UploadprogressDialog = new ProgressDialog(PDFActivity.this);
                    UploadprogressDialog.setTitle("上传中");
                    UploadprogressDialog.setMessage("正在上传，请等待");
                    UploadprogressDialog.show();
                    tv_zhushi.setText("编辑");
                    new Thread(new Runnable() {
                        public void run() {
                            File filepath;
                            if(tag){  // 原文件为最新 刪除複製文件
                                filepath = new File(INPATH);
                            }else{
                                filepath = new File(OUTPATH);
                            }

                            UploadUtil uploadUtil = new UploadUtil();
                            int j =schedule+1;
                            Log.i("TAG","GGG"+j);
                            Log.i("TAG","HHH"+order_id);
                            Log.i("TAG","III"+user_id);
                            int result = uploadUtil.uploadFile(filepath, MyURL.UPLOADADDRESS,user_id,j,order_id);

                           if(result==200){
                                Log.i("上传返回码", result + "   ");
                                Message message = new Message();
                                message.obj = "上传完成";
                                handler.sendMessage(message);
                            }else{
                                Message message = new Message();
                                message.obj = "上传失败";
                                handler.sendMessage(message);
                            }

                        }
                    }).start();



                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg.toString());
            if (msg.obj.equals("上传完成")) {
                UploadprogressDialog.dismiss();
                Toast.makeText(PDFActivity.this, "上传成功", Toast.LENGTH_SHORT)
                        .show();
                SPUtils.remove(PDFActivity.this,"KEY");
                Intent intent =new Intent();
                setResult(1997,intent);
                PDFActivity.this.finish();
                super.handleMessage(msg);
            } else {
                UploadprogressDialog.dismiss();
                Toast.makeText(PDFActivity.this, "上传失败", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    };

    /*
    * 用于存储的异步,并上传更新
    * */
    class Save_Pdf extends AsyncTask {

        SavePdf savePdf;

        public Save_Pdf(SavePdf savePdf) {
            this.savePdf = savePdf;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            savePdf.addText();
            if (first == 1) {
                update_inpath = in_path.substring(0, in_path.length() - 4) + ".pdf";
                in_path = in_path.substring(0, in_path.length() - 4) + ".pdf";
                first++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(PDFActivity.this, "存储完成", Toast.LENGTH_SHORT).show();
            try {
                muPDFCore = new MuPDFCore(out_path);
                readerView.setAdapter(new MuPDFPageAdapter(PDFActivity.this, muPDFCore));

                String temp = in_path;
                in_path = out_path;
                out_path = temp;

                readerView.setmScale(1.0f);
                readerView.setDisplayedViewIndex(readerView.getDisplayedViewIndex());
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回按钮，退出时删除那两个文件
     */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否退出编辑浏览界面？");
        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =new Intent();
                setResult(1996,intent);
                SPUtils.remove(PDFActivity.this,"KEY");

                if(rlSign.getVisibility()==View.VISIBLE){
                    HashMap<String, String> filelocked = new Fileorder().FileLocked(order_id);
                    new HttpTask(null, MyURL.COMPlET).execute(filelocked);
                }

                PDFActivity.this.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (first != 1) {

            if(tag){  // 原文件为最新 刪除複製文件
                File file1 = new File(OUTPATH);
            //    if (file1.exists()) file1.delete();
            }else{
                File file = new File(INPATH);
           //     if (file.exists()) file.delete();
            }
        }
    }
}
