package com.example.gg;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private EditText mEtAccount, mEtPassword;
    private CheckBox mCbSaveAcount, mCbSavePassword;
    private TextView mTvRegister;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏自带标题栏
        setStatusBarTransparent();
        //设置状态栏字体颜色为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setAndroidNativeLightStatusBar(this,true);
        }
        //找到控件
        findViews();
        //点击事件
        View.OnClickListener onClickListener = getOnClickListener();
        //设置点击事件
        mTvRegister.setOnClickListener(onClickListener);
        mBtnLogin.setOnClickListener(onClickListener);
    }

    //点击事件
    @NotNull
    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (v.getId()) {
                        case R.id.tv_register:
                            intent = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.btn_login:
                            String account = mEtAccount.getText().toString();
                            String password = mEtPassword.getText().toString();
                            DatebaseHelper datebaseHelper = new DatebaseHelper(MainActivity.this, "text_db", null, 1);
                            SQLiteDatabase db = datebaseHelper.getWritableDatabase();
                            if (!account.equals("")){
                                @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from user where account=" + account,null);
                                if (cursor.moveToNext()){
                                    cursor.moveToFirst();
                                    String passwordInBd = cursor.getString(cursor.getColumnIndex("password"));
                                    if (passwordInBd.equals(password)){
                                        makeLongToast("登录成功");
                                        intent = new Intent(MainActivity.this,SuccessActivity.class);
                                        startActivity(intent);
                                    }else {
                                        makeLongToast("密码错误");
                                    }
                                }else {
                                    makeLongToast("账号不存在");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            };
    }

    //长时间Toast
    private void makeLongToast(String content){
        Toast.makeText(MainActivity.this,content,Toast.LENGTH_LONG).show();
    }

    //找到控件
    private void findViews() {
        mEtAccount = findViewById(R.id.et_account);
        mEtPassword = findViewById(R.id.et_password);
        mCbSaveAcount = findViewById(R.id.cb_1);
        mCbSavePassword = findViewById(R.id.cb_2);
        mTvRegister = findViewById(R.id.tv_register);
        mBtnLogin = findViewById(R.id.btn_login);
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
