package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.dao.UserAccountDao;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.Modle;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class LoginActivity extends Activity implements View.OnClickListener
{

    private EditText etLoginUsername;
    private EditText etLoginPsw;
    private Button btnLoginRegist;
    private Button btnLoginLogin;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-22 09:22:14 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        etLoginUsername = (EditText) findViewById(R.id.et_login_username);
        etLoginPsw = (EditText) findViewById(R.id.et_login_psw);
        btnLoginRegist = (Button) findViewById(R.id.btn_login_regist);
        btnLoginLogin = (Button) findViewById(R.id.btn_login_login);

        btnLoginRegist.setOnClickListener(this);
        btnLoginLogin.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-22 09:22:14 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v)
    {
        //注册
        if (v == btnLoginRegist)
        {
            // Handle clicks for btnLoginRegist
            final String psw = etLoginPsw.getText().toString().trim();
            final String name = etLoginUsername.getText().toString().trim();
            if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(name))
            {
                Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //联网操作进行注册
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        EMClient.getInstance().createAccount(name, psw);

                        //在主线程更新界面
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (final HyphenateException e)
                    {
                        e.printStackTrace();
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(LoginActivity.this, "注册失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //登录
        else if (v == btnLoginLogin)
        {
            // Handle clicks for btnLoginRegist
            final String psw = etLoginPsw.getText().toString().trim();
            final String name = etLoginUsername.getText().toString().trim();
            if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(name))
            {
                Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            //创建子线程进行登录操作
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    EMClient.getInstance().login(name, psw, new EMCallBack()
                    {
                        @Override
                        public void onSuccess()
                        {
                            //用户登录成功后的处理方法
                            Modle.getInstance().loginSuccess(new UserInfo(name));
                            //保存到本地数据库
                            UserAccountDao userAccountDao=Modle.getInstance().getAccountDao();
                            userAccountDao.addAccount(new UserInfo(name));
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    //提示登录成功
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    //跳转到主页面
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);

                                    //销毁当前页面
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void onError(int i, final String s)
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(LoginActivity.this, "登录失败"+s, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s)
                        {

                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

    }
}
