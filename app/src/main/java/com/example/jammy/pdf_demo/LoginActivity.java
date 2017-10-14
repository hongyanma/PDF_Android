package com.example.jammy.pdf_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.com.Bll.CBLL;
import com.com.Bll.JSONEntity;
import com.com.Http.HttpCallback;
import com.com.Http.HttpTask;
import com.com.entity.User;
import com.com.tool.MyURL;
import com.com.tool.SPUtils;
import com.com.tool.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends Activity {

    @Bind(R.id.main_btn_login)
    TextView mBtnLogin;
    @Bind(R.id.layout_progress)
    LinearLayout progress;
    @Bind(R.id.edt_account)
    EditText account;
    @Bind(R.id.password)
    EditText password;

    private String  userAccount;
    private String  userPassword;
    //退出时的时间
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AutoLogin();
        setListent();
    }

    private void AutoLogin() {

        if ((Integer) SPUtils.get(LoginActivity.this, "userId", -1) != -1){

            String save_account= (String) SPUtils.get(LoginActivity.this, "userAccount", "");
            String save_paaword= (String) SPUtils.get(LoginActivity.this, "userPassword", "");

            HashMap<String, String> loginjson=new User().toLoginJson(save_account,save_paaword);
            new HttpTask(AutohttpCallback, MyURL.LOGIN).execute(loginjson);
        }

    }

    private void setListent() {
      mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userAccount  = account.getText().toString();
                userPassword = password.getText().toString();
                if(!userAccount.equals("") && !userPassword.equals("")){

                    HashMap<String, String> loginjson=new User().toLoginJson(userAccount,userPassword);
                    new HttpTask(httpCallback, MyURL.LOGIN).execute(loginjson);
                    progress.setVisibility(View.VISIBLE);
                }else{
                    ToastUtil.show(LoginActivity.this, "您的账号或密码有误！");
                }


            }
        });
    }

    private HttpCallback AutohttpCallback=new HttpCallback() {  //自動登錄
        @Override
        public void getResult(JSONObject json) {
            CBLL cBllUser = CBLL.getInstance();
            JSONEntity entity = cBllUser.jsonlogin(json);
            if(entity.isFlag()){

                User UserEntity = (User) entity.getData();
                //保存账号密码等信息，用于自动登录
                Log.i("TAG","AAA"+UserEntity.getRole_id());

                if(UserEntity.getRole_id()==2) {
                    //跳转到主页面 缺少一个颜色
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                    intent.putExtra("nickname", UserEntity.getNickname());
                    intent.putExtra("color", UserEntity.getColor());
                    intent.putExtra("role_name", UserEntity.getRole_name());
                    intent.putExtra("userId", UserEntity.getUser_id());
                    intent.putExtra("role_id", UserEntity.getRole_id());
                    startActivity(intent);
                    finish();
                }else if (UserEntity.getRole_id()==3){
                    Intent intent = new Intent(LoginActivity.this, ReviewActivity.class);
                    intent.putExtra("nickname", UserEntity.getNickname());
                    intent.putExtra("color", UserEntity.getColor());
                    intent.putExtra("role_name", UserEntity.getRole_name());
                    intent.putExtra("userId", UserEntity.getUser_id());
                    intent.putExtra("role_id", UserEntity.getRole_id());
                    startActivity(intent);
                    finish();
                }
                else if (UserEntity.getRole_id()==4){ //普通用户
                    Intent intent = new Intent(LoginActivity.this,SearchActivity.class);
                    intent.putExtra("nickname", UserEntity.getNickname());
                    intent.putExtra("color", UserEntity.getColor());
                    intent.putExtra("role_name", UserEntity.getRole_name());
                    intent.putExtra("userId", UserEntity.getUser_id());
                    intent.putExtra("role_id", UserEntity.getRole_id());
                    startActivity(intent);
                    finish();
                }
            }
        }
    };


    private HttpCallback httpCallback=new HttpCallback() {
        @Override
        public void getResult(JSONObject json) {

            progress.setVisibility(View.GONE);
            CBLL cBllUser = CBLL.getInstance();
            JSONEntity entity = cBllUser.jsonlogin(json);

            if(entity.isFlag()){

                User UserEntity = (User) entity.getData();
                //保存账号密码等信息，用于自动登录
                saveUserLogin(UserEntity.getUser_id(),userAccount,userPassword);

                if(UserEntity.getRole_id()==2) {
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);

                    intent.putExtra("nickname", UserEntity.getNickname());
                    intent.putExtra("color", UserEntity.getColor());
                    intent.putExtra("role_name", UserEntity.getRole_name());
                    intent.putExtra("userId", UserEntity.getUser_id());
                    intent.putExtra("role_id", UserEntity.getRole_id());
                    startActivity(intent);
                    finish();
                }else if(UserEntity.getRole_id()==3){
                    Intent intent = new Intent(LoginActivity.this, ReviewActivity.class);

                    intent.putExtra("nickname", UserEntity.getNickname());
                    intent.putExtra("color", UserEntity.getColor());
                    intent.putExtra("role_name", UserEntity.getRole_name());
                    intent.putExtra("userId", UserEntity.getUser_id());
                    intent.putExtra("role_id", UserEntity.getRole_id());
                    startActivity(intent);
                    finish();
                }
                else if (UserEntity.getRole_id()==4){ //普通用户
                    Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                    intent.putExtra("nickname", UserEntity.getNickname());
                    intent.putExtra("color", UserEntity.getColor());
                    intent.putExtra("role_name", UserEntity.getRole_name());
                    intent.putExtra("userId", UserEntity.getUser_id());
                    intent.putExtra("role_id", UserEntity.getRole_id());
                    startActivity(intent);
                    finish();
                }


            }else{
                if(entity.getMessage()==0){
                    ToastUtil.show(LoginActivity.this, "用户名或密码错误，请重新输入");
                }else if(entity.getMessage()==1){
                    ToastUtil.show(LoginActivity.this, "您没有编辑订单的权限");
                }

            }
        }
    };

    public void saveUserLogin(int userid,String account,String password) {
        // TODO Auto-generated method stub
        SPUtils.put(LoginActivity.this, "userId", userid);
        SPUtils.put(LoginActivity.this, "userAccount", account);
        SPUtils.put(LoginActivity.this, "userPassword",password);
    }

    /**
     * 返回按钮，退出时删除那两个文件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(progress.getVisibility()==View.VISIBLE){
            progress.setVisibility(View.GONE);
            return false;
        }else{
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

                exit();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(LoginActivity.this, "再按一次退出PDF批注", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }

    }
}
