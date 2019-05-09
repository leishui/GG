package com.example.gg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEtRegisterAccount, mEtRegisterPassword;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //设置状态栏透明
        setStatusBarTransparent();
        //设置状态栏字体为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setAndroidNativeLightStatusBar(RegisterActivity.this,true);
        }
        mEtRegisterAccount = findViewById(R.id.et_register_account);
        mEtRegisterPassword = findViewById(R.id.et_register_password);
        mBtnRegister = findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    //注册逻辑
    private void register() {
        String account = mEtRegisterAccount.getText().toString();
        String psasword = mEtRegisterPassword.getText().toString();
        DatebaseHelper datebaseHelper = new DatebaseHelper(RegisterActivity.this, "text_db", null, 1);
        SQLiteDatabase db = datebaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //判断输入的数据是否为空
        if (account.equals("") || psasword.equals("")) {
            makeLongToast("账号或密码不能为空");
        } else {
            //判断账号是否已存在
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from user where account=" + account, null);
            if (!cursor.moveToNext()) {
                //若不存在，则往数据库添加一条信息
                contentValues.put("account", account);
                contentValues.put("password", psasword);
                if (db.insert("user", null, contentValues) != -1) {
                    makeLongToast("注册成功，自动跳至登录界面");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    makeLongToast("注册失败，数据库插入错误");
                }
            } else {
                //若存在，提示注册失败
                makeLongToast("该账号已存在");
            }
        }
    }

    //长时间Toast
    private void makeLongToast(String content){
        Toast.makeText(RegisterActivity.this,content,Toast.LENGTH_LONG).show();
    }

    //设置状态栏透明
    private void setStatusBarTransparent() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            window.setAttributes(attributes);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //设置状态栏字体颜色（黑/白）
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
