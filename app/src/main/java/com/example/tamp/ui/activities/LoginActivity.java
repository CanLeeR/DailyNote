package com.example.tamp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tamp.R;
import com.example.tamp.data.AppDatabase;
import com.example.tamp.data.Dao.UserDao;
import com.example.tamp.data.pojo.User;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    //数据库
    private AppDatabase db;
    //持久层
    private UserDao userDao;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginOrRegisterButton;
    CheckBox cb_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);
        //初始化数据库
        initDatabase();
        // 组件初始化
        init();
        //检查有没有记住我
        readSP();

        //登陆按钮监听
        loginOrRegisterButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateInput(username, password)) {
                handleUserLoginOrRegister(username, password);
            }
        });
        //记住我监听
        cb_remember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if ( cb_remember.isChecked() ) {
                rememberme(); //如果选中，将把数据保存到xml文件
            } else {
                unrememberme(); //如果取消选中，则清除xml文件数据
            }
        });
    }


    private void init(){
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginOrRegisterButton = findViewById(R.id.login_or_register_button);
        cb_remember = findViewById(R.id.remember_me);
    }

    private void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "daily").build();
        userDao = db.userDao();
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //读取数据（如果"记住我"为true，则在程序启动时，读取用户名+密码显示在界面输入框中）
    public void readSP() {
        SharedPreferences sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        Boolean remember = sp.getBoolean("rememberme", false);
        if( remember ) {
            cb_remember.setChecked(true);
            String username = sp.getString("username", "");
            String password = sp.getString("password", "");
            usernameEditText.setText(username);
            passwordEditText.setText(password);
            Log.d("flag", "数据读取成功");
        }
    }

    private void handleUserLoginOrRegister(String username, String password) {

        Executors.newSingleThreadExecutor().execute(() -> {
            //访问持久层来查询数据库数据
            User existingUser = userDao.getUserByUsername(username);
            if (existingUser != null) {
                if (existingUser.getPassword().equals(password)) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show());
                    // 启动 MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                    // 结束 LoginActivity
                    finish();
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show());
                }
            } else {
                registerNewUser(username, password);
            }
        });
    }

    //保存数据（记住我）
    public void rememberme() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        SharedPreferences sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("rememberme", true);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
        Log.d("flag", "数据保存成功");
    }
    //清除数据（取消记住我）
    public void unrememberme() {
        SharedPreferences sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        Log.d("flag", "数据已删除");
    }

    private void registerNewUser(String username, String password) {
        User newUser = new User(username, password);
        userDao.insertUser(newUser);
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show());
    }
}
