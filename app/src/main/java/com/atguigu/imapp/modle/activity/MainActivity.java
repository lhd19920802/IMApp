package com.atguigu.imapp.modle.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.atguigu.imapp.R;
import com.atguigu.imapp.modle.fragment.CharFragment;
import com.atguigu.imapp.modle.fragment.ContactFragment;
import com.atguigu.imapp.modle.fragment.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity
{
    private FrameLayout fl_main;
    private RadioGroup rg_main;

    //点击的位置
    private int position;

    //fragments的集合
    private List<Fragment> fragmentList;

    private Fragment preFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setListener();
        rg_main.check(R.id.rb_main_chat);

    }

    private void initData()
    {
        fragmentList.add(new CharFragment());
        fragmentList.add(new ContactFragment());
        fragmentList.add(new SettingsFragment());
    }

    private void setListener()
    {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.rb_main_chat:
                        position = 0;
                        break;
                    case R.id.rb_main_contact:
                        position = 1;
                        break;
                    case R.id.rb_main_setting:
                        position = 2;
                        break;
                }

                Fragment fragment = fragmentList.get(position);
                switchFragment(preFragment, fragment);
            }
        });
    }

    /**
     * 切换fragment
     * @param tempFragment
     * @param fragment
     */
    private void switchFragment(Fragment tempFragment, Fragment fragment)
    {
        if (tempFragment != fragment)
        {
            preFragment = fragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fragment.isAdded())
            {
                //隐藏tempFragment
                if (tempFragment != null)
                {
                    transaction.hide(tempFragment);
                }
                //添加新的fragment 并提交
                if (fragment != null)
                {
                    transaction.add(R.id.fl_main, fragment).commit();
                }


            }
            else
            {//隐藏tempFragment
                if (tempFragment != null)
                {
                    transaction.hide(tempFragment);
                }
                //显示新的fragment 并提交
                if (fragment != null)
                {
                    transaction.show(fragment).commit();
                }


            }
        }

    }

    private void initView()
    {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        fragmentList = new ArrayList<>();
    }
}
