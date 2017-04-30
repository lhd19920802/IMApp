package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.atguigu.imapp.R;
import com.atguigu.imapp.modle.Modle;
import com.atguigu.imapp.modle.adapter.GroupListAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class GroupListActivity extends Activity
{

    private ListView lv_grouplist;
    private GroupListAdapter groupListAdapter;
    private LinearLayout ll_grouplist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        initView();
        initData();

        initListener();
    }

    private void initListener()
    {
        //群里表条目的点击事件
        lv_grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    return;
                }
                //跳转到会话页面
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);
                //携带数据
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                //携带groupId
                intent.putExtra(EaseConstant.EXTRA_USER_ID, EMClient.getInstance().groupManager()
                        .getAllGroups().get(position - 1).getGroupId());
                startActivity(intent);
            }
        });

        //创建群组的监听
        ll_grouplist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GroupListActivity.this, GroupCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData()
    {
        groupListAdapter = new GroupListAdapter(this);
        lv_grouplist.setAdapter(groupListAdapter);

        //从环信服务器获取群信息
        getGroupsFromServer();
    }

    private void getGroupsFromServer()
    {
        Modle.getInstance().getExecutors().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final List<EMGroup> emGroups = EMClient.getInstance().groupManager()
                            .getJoinedGroupsFromServer();

                    //刷新界面
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //刷新界面
                            refresh();
                        }
                    });
                }
                catch (HyphenateException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refresh()
    {
        groupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initView()
    {
        lv_grouplist = (ListView) findViewById(R.id.lv_grouplist);

        View headerView = View.inflate(this, R.layout.item_group, null);

        lv_grouplist.addHeaderView(headerView);

        //创建群组的条目
        ll_grouplist = (LinearLayout) headerView.findViewById(R.id.ll_grouplist);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refresh();
    }
}
