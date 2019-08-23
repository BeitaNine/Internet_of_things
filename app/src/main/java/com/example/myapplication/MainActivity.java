package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.homepage.CUser;
import com.example.myapplication.homepage.RegisterActicity;
import com.example.myapplication.homepage.mySocketHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//public class MainActivity extends AppCompatActivity implements View.OnClickListener{
  public class MainActivity extends AppCompatActivity{
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    //线程池
    private Handler mMainHandler;
    private ExecutorService mThreadPool;
    //输入窗和登录注册
    private Button mLogin;
    private TextView mPreRegister;
    private CheckBox rememberPass;
    private EditText mEdtId,mEdtPassword;
    //客服端和自定义
    private mySocketHelper mSocketHelper;
    private CUser mMySelf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        pref = PreferenceManager.getDefaultSharedPreferences(this);
//        rememberPass = findViewById(R.id.remember_pass);
//        boolean isRemember = pref.getBoolean("remember_password",false);
//        if (isRemember){
//            String account = pref.getString("account","");
//            String password = pref.getString("password","");
//            mEdtId.setText(account);
//            mEdtPassword.setText(password);
//            rememberPass.setChecked(true);
//        }
        manageViewID();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
        mPreRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActicity.class);
                startActivity(intent);

            }
        });
        initData();
        manageHandle();
    }
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_login:
//                login();
//                break;
//            case R.id.btn_pre_register:
//                Intent intent = new Intent(MainActivity.this, RegisterActicity.class);
//                startActivity(intent);
//                break;
//
//        }
//    }
    public void manageViewID(){
        mEdtId = findViewById(R.id.et_1);
        mEdtPassword = findViewById(R.id.et_2);
        mLogin = findViewById(R.id.btn_login);
        mPreRegister = findViewById(R.id.btn_pre_register);
//        editor = pref.edit();
//        mLogin.setOnClickListener(this);
//        mPreRegister.setOnClickListener(this);
    }
    private void manageHandle(){
        mMainHandler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        Intent intent = new Intent(MainActivity.this,NearActivity.class);
                        startActivity(intent);
//                        finish();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this,"用户名不存在,请进行注册",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this,"无法连接服务器,请查看是否连接网络",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this,"账号和密码未输入",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }
    //初始化数据
    private void initData(){
        mMySelf = new CUser();
        mThreadPool = Executors.newCachedThreadPool();
    }
    private void login(){
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                JSONObject jsonObject = new JSONObject();
                mMySelf.mID = mEdtId.getText().toString().trim();
                mMySelf.mPassword = mEdtPassword.getText().toString().trim();
                try {
                    jsonObject.accumulate("KEY","Login");
                    jsonObject.accumulate("username",mMySelf.mID);
                    jsonObject.accumulate("password",mMySelf.mPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if((!mMySelf.mID.equals("") && !mMySelf.mPassword.equals(""))){
                    if((mSocketHelper = new mySocketHelper()).checkSocket()){
                        mSocketHelper.sendDataString(jsonObject);
                        String sRcv = mSocketHelper.getDataString();
                        if(sRcv.equals("return_success")){
                            msg.what = 0;
                        }else if(sRcv.equals("return_id_empty")){
                            msg.what = 1;
                        }else if(sRcv.equals("return_fail")){
                            msg.what = 2;
                        }
                        mSocketHelper.closeSocket();
                    }else {
                        msg.what = 3;
                    }
                }else{
                    msg.what = 4;
                }
                mMainHandler.sendMessage(msg);
            }
        });
    }
}
