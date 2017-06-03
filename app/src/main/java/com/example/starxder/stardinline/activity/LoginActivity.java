package com.example.starxder.stardinline.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.starxder.stardinline.Beans.Category;
import com.example.starxder.stardinline.Beans.User;
import com.example.starxder.stardinline.Dao.CategoryDao;
import com.example.starxder.stardinline.Dao.UserDao;
import com.example.starxder.stardinline.R;
import com.example.starxder.stardinline.Utils.CommonUtil;
import com.example.starxder.stardinline.Utils.GsonUtils;
import com.example.starxder.stardinline.Utils.OkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button btn_login;
    private OkManager manager;
    private List<User> userList;
    private List<Category> categoryList;
    String TAG = "LoginActivity";
    String userName;
    String passWord;
    UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsernameView = (EditText) findViewById(R.id.Username_Edt);
        mPasswordView = (EditText) findViewById(R.id.Password_Edt);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        manager = OkManager.getInstance();


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
        userName = mUsernameView.getText().toString();
        passWord = mPasswordView.getText().toString();
        String jsonpath = CommonUtil.BaseUrl + "/web-frame/user/login.do?loginname=" + userName + "&&password=" + passWord;
        //登陆同步用户数据
        manager.asyncJsonStringByURL(jsonpath, new OkManager.Fun1() {
            @Override
            public void onResponse(String response) {
                Log.i("LoginActivity", response);   //获取JSON字符串
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String result = jsonObject.getString("result");
                    if (error.equals("")) {
                        userList = GsonUtils.getUserByGson("[" + result + "]");
                        Log.d(TAG, userList.toString());
                        userDao = new UserDao(LoginActivity.this);
                        for (User user : userList) {
                            userDao.insert(user);
                        }

                        Toast.makeText(getApplicationContext(), "用户数据同步成功,", Toast.LENGTH_SHORT).show();
                        CategorySynchronize();
                    } else {
                        Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String result) {
                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CategorySynchronize() {
        //登陆同步用户桌号分配数据
        final User user = userDao.queryByLoginName(userName);
        String categorypath = CommonUtil.BaseUrl + "/web-frame/dictionary/getByCategory.do?category=" + user.getTabletype();
        manager.asyncJsonStringByURL(categorypath, new OkManager.Fun1() {
            @Override
            public void onResponse(String response) {
                Log.i("LoginActivity", response);   //获取JSON字符串
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String result = jsonObject.getString("result");
                    if (error.equals("")) {
                        categoryList = GsonUtils.getCategoryByGson(result);
                        Log.d(TAG, categoryList.toString()+"成功匹配");
                        CategoryDao categoryDao = new CategoryDao(LoginActivity.this);
                        for (Category category : categoryList) {
                            categoryDao.insert(category);
                        }
                        Toast.makeText(getApplicationContext(), "排队信息同步成功,", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("extra",user.getOrdertable());
                        intent.setClass(LoginActivity.this, MainActivity.class);      //运行另外一个类的活动
                        startActivityForResult(intent, 1);
                    }else{
                        Toast.makeText(getApplicationContext(), "排队信息同步失败,", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String result) {
                Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

