package com.atguigu.imapp.modle.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.base.BaseFragment;
import com.atguigu.imapp.modle.Modle;
import com.atguigu.imapp.modle.activity.LoginActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by acer on 2017/3/23 10:22.
 * 作用：
 */
public class SettingsFragment extends BaseFragment
{
    private Button bt_setting_out;

    @Override
    public View initView()
    {
        View view = View.inflate(mContext, R.layout.fragment_settings, null);
        bt_setting_out = (Button) view.findViewById(R.id.bt_setting_out);

        return view;

    }

    private void setListener()
    {
        bt_setting_out.setText("退出登录"+"("+EMClient.getInstance().getCurrentUser()+")");
        bt_setting_out.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //环信服务器退出
                Modle.getInstance().getExecutors().execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        EMClient.getInstance().logout(false, new EMCallBack()
                        {
                            @Override
                            public void onSuccess()
                            {
                                Log.e("TAG", "getActivity====" + getActivity());
                                Log.e("TAG", "mContext======"+mContext);
//                                mContext.runOnUiThread();

                                getActivity().runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        //显示提示
                                        Toast.makeText(mContext, "退出成功", Toast.LENGTH_SHORT).show();


                                        //关闭数据库
                                        Modle.getInstance().getDBManager().close();
                                        //回到登录页面
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        startActivity(intent);

                                        getActivity().finish();
                                    }
                                });

                            }

                            @Override
                            public void onError(int i, final String s)
                            {
                                //显示提示
                                getActivity().runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        //显示提示
                                        Toast.makeText(mContext, "退出失败"+s, Toast
                                                .LENGTH_SHORT).show();
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
        });
    }

    @Override
    public void initData()
    {
        super.initData();
        setListener();
    }
}
