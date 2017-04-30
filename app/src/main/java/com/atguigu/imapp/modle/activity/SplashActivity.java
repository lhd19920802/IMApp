package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.Modle;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends Activity
{

    //    private Handler handler = new Handler();
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case 1 :
                    if(isFinishing()) {
                        return;//如果当前acitivity关闭 则不再处理消息

                    }
                    /*new Thread(){
                        public void run(){
                            toMainOrLogin();
                        }
                    }.start();*/

                    Modle.getInstance().getExecutors().execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            toMainOrLogin();
                        }
                    });
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       /* handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                toMainOrLogin();
            }
        }, 2000);*/
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    private void toMainOrLogin()
    {
        if (EMClient.getInstance().isLoggedInBefore())
        {
            UserInfo accountInfo = Modle.getInstance().getAccountDao().getAccountInfo(EMClient
                    .getInstance().getCurrentUser());
            if(accountInfo==null) {
                //进入登录页面
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
            else {
                //之前登录过并且账号信息存在 直接进入主页面
                //调用创建数据库
                Modle.getInstance().loginSuccess(accountInfo);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }
        else
        {
            //进入登录页面
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
