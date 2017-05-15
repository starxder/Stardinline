package com.example.starxder.stardinline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.starxder.stardinline.R;
import com.example.starxder.stardinline.Utils.CommonUtil;
import com.example.starxder.stardinline.db.DatabaseHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button btn_login;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameView = (EditText) findViewById(R.id.Username_Edt);
        mPasswordView = (EditText) findViewById(R.id.Password_Edt);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                Login();

                break;

        }
    }


    private void Login() {
        String userName = mUsernameView.getText().toString();
        String passWord = mPasswordView.getText().toString();

        updata(userName, passWord);

    }


    private void updata(String username, String password) {
        //确定取号执行方法，发送请求！

        //首先创建请求的客户端对象
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60*1000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(60*1000,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(60*1000,TimeUnit.SECONDS)//设置连接超时时间
                .build();
        //使用Request.Builder来创建请求对象
        Request.Builder builder = new Request.Builder();
        //指定使用GET请求,并且指定要请求的地址
        Request request = builder.get().url(CommonUtil.BaseUrl +"/web-frame/user/login.do?loginname="+username+"&&password="+ password).build();
        //将请求加入请求队列,将请求封装成Call对象
        Call call = client.newCall(request);
        //使用异步的方式来得到请求的响应并且处理
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("e", e.toString());
                //请求失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //请求成功
                //此处非UI线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String abc = response.body().string();
                            Log.d("登录", abc);
                            if (abc.equals("true")) {
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                createDatabase();
                                Intent serverIntent = new Intent(LoginActivity.this, MainActivity.class);      //运行另外一个类的活动
                                startActivityForResult(serverIntent, 1);
                            } else {
                                Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    private void createDatabase() {
        dbHelper = new DatabaseHelper(this,"StardinLine.db",null,1);
        dbHelper.getWritableDatabase();
    }
}

