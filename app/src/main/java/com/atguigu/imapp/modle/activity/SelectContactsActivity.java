package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.SelectContactsInfo;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.Modle;
import com.atguigu.imapp.modle.adapter.SelectContactsAdapter;
import com.atguigu.imapp.modle.utils.Contants;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

//选择联系人的页面
public class SelectContactsActivity extends Activity
{
    private TextView tv_pick_save;
    private ListView lv_pick;
    private SelectContactsAdapter selectContactsAdapter;
    private List<SelectContactsInfo> mSelects;
    private EMGroup group;
    private List<String> existMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);


        getData();
        initView();

        initData();

        setListener();
    }

    private void getData()
    {
        String groupId = getIntent().getStringExtra(Contants.GROUP_ID);
        if (groupId!=null)
        {
            group = EMClient.getInstance().groupManager().getGroup(groupId);
            existMembers = group.getMembers();
        }
        if(existMembers==null)
        {
            existMembers = new ArrayList<>();
        }

    }

    private void setListener()
    {
        lv_pick.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                SelectContactsInfo selectContactsInfo = mSelects.get(position);
                if (selectContactsInfo.isChecked())
                {
                    selectContactsInfo.setIsChecked(false);
                }
                else
                {
                    selectContactsInfo.setIsChecked(true);
                }
                selectContactsAdapter.notifyDataSetChanged();
            }
        });

        //保存按钮的监听
        tv_pick_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent();
                List<String> names = selectContactsAdapter.getAddMembers();

                intent.putExtra("members", names.toArray(new String[0]));
                setResult(RESULT_OK,intent);

                //结束当前页
                finish();
            }
        });



    }

    private void initData()
    {
        // 获取所有联系人的数据
        List<UserInfo> contacts = Modle.getInstance().getDBManager().getContactTabDao()
                .getAllContacts();

        //选择联系人的集合
        mSelects = new ArrayList<>();

        // 校验
        if (contacts != null && contacts.size() >= 0)
        {

            // 将联系人信息转换为选择联系人bean信息

            for (UserInfo contact : contacts)
            {
                SelectContactsInfo selectContactsInfo = new SelectContactsInfo(contact, false);

                mSelects.add(selectContactsInfo);
            }
        }

        selectContactsAdapter = new SelectContactsAdapter(this,mSelects,existMembers);
        lv_pick.setAdapter(selectContactsAdapter);
    }

    private void initView()
    {
        tv_pick_save = (TextView) findViewById(R.id.tv_pick_save);
        lv_pick = (ListView) findViewById(R.id.lv_pick);


    }




}
