package com.example.starxder.stardinline.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.starxder.stardinline.Beans.Category;
import com.example.starxder.stardinline.Beans.Setting;
import com.example.starxder.stardinline.Beans.User;
import com.example.starxder.stardinline.Dao.CategoryDao;
import com.example.starxder.stardinline.Dao.SettingDao;
import com.example.starxder.stardinline.Dao.UserDao;
import com.example.starxder.stardinline.R;
import com.example.starxder.stardinline.Utils.CommonUtil;
import com.example.starxder.stardinline.Utils.GsonUtils;
import com.example.starxder.stardinline.Utils.OkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private CheckBox cb_remPassword;
    private CheckBox cb_autoLogin;
    SettingDao settingDao;
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
        cb_remPassword = (CheckBox) findViewById(R.id.cb_remPassword);
        cb_autoLogin = (CheckBox) findViewById(R.id.cb_autoLogin);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        settingDao = new SettingDao(LoginActivity.this);
        userDao = new UserDao(LoginActivity.this);
        manager = OkManager.getInstance();
        initEvent();
        try {
            HistoryUserSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void initEvent() {
        btn_login.setOnClickListener(this);
        if (settingDao.queryById(1) == null) {
            Setting setting = new Setting(1, "false", "false");
            settingDao.insert(setting);
        } else {
            Setting setting = settingDao.queryById(1);
            if (Boolean.valueOf(setting.getRemPassword())) {
                cb_remPassword.setChecked(true);
            } else {
                cb_remPassword.setChecked(false);
            }
            if (Boolean.valueOf(setting.getAutoLogin())) {
                cb_autoLogin.setChecked(true);
            } else {
                cb_autoLogin.setChecked(false);
            }
        }
        cb_remPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Setting setting = settingDao.queryById(1);
                    setting.setRemPassword("true");
                    settingDao.update(setting);
                } else {
                    Setting setting = settingDao.queryById(1);
                    setting.setRemPassword("false");
                    setting.setAutoLogin("false");
                    settingDao.update(setting);
                    cb_autoLogin.setChecked(false);
                }
            }
        });

        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Setting setting = settingDao.queryById(1);
                    setting.setAutoLogin("true");
                    setting.setRemPassword("true");
                    settingDao.update(setting);
                    cb_remPassword.setChecked(true);
                } else {
                    Setting setting = settingDao.queryById(1);
                    setting.setAutoLogin("false");
                    settingDao.update(setting);
                }
            }
        });
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
                        Log.d(TAG, categoryList.toString() + "成功匹配");
                        CategoryDao categoryDao = new CategoryDao(LoginActivity.this);
                        for (Category category : categoryList) {
                            categoryDao.insert(category);
                        }
                        Toast.makeText(getApplicationContext(), "排队信息同步成功,", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("extra", user.getLoginName());
                        intent.setClass(LoginActivity.this, MainActivity.class);      //运行另外一个类的活动
                        startActivityForResult(intent, 1);
                    } else {
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

    private void HistoryUserSet() throws SQLException {
        if (Boolean.valueOf(settingDao.queryById(1).getRemPassword())) {
            if (userDao.getFirstUser() != null) {
                mUsernameView.setText(userDao.getFirstUser().getLoginName());
                mPasswordView.setText(userDao.getFirstUser().getPassword());
                if (Boolean.valueOf(settingDao.queryById(1).getAutoLogin())) {
                    Login();
                }
            }
        }
    }
}