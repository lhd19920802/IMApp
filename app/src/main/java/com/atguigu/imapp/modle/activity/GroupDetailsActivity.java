package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.Modle;
import com.atguigu.imapp.modle.adapter.GroupDetailsAdapter;
import com.atguigu.imapp.modle.utils.Contants;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailsActivity extends Activity
{

    private GridView gv_groupdetail;
    private Button bt_groupdetail_out;
    private EMGroup emGroup;
    private String groupId;
    private LocalBroadcastManager lbm;
    private List<UserInfo> mUsers;
    private GroupDetailsAdapter groupDetailsAdapter;
    private GroupDetailsAdapter.GroupDetailsListener groupDetailsListener=new GroupDetailsAdapter.GroupDetailsListener()

    {
        @Override
        public void addMember()
        {
            Intent intent = new Intent(GroupDetailsActivity.this, SelectContactsActivity.class);
            intent.putExtra(Contants.GROUP_ID, emGroup.getGroupId());
            startActivityForResult(intent, 2);
        }

        @Override
        public void deleteMember(final UserInfo userInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //从环信服务器删除
                    try
                    {
                        EMClient.getInstance().groupManager().removeUserFromGroup(emGroup
                                .getGroupId(), userInfo.getHxid());

                        // 从环信服务器获取所有的群成员
                        getMembersFromHxServer();

                        //提示
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(GroupDetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();
                        //提示
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(GroupDetailsActivity.this, "删除失败", Toast
                                        .LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            final String[] members = data.getStringArrayExtra("members");
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //去环信服务器发送邀请信息
                    try
                    {
                        EMClient.getInstance().groupManager().addUsersToGroup(emGroup.getGroupId
                                    (),members);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(GroupDetailsActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(GroupDetailsActivity.this, "发送邀请失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);


        initView();



        getData();

        initData();

        initListener();

    }

    private void initListener()
    {
        //GridView的触摸监听
        gv_groupdetail.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(groupDetailsAdapter.ismIsDeleteMode()) {
                    groupDetailsAdapter.setmIsDeleteMode(false);

                    groupDetailsAdapter.notifyDataSetChanged();
                }
                return false;

            }
        });
    }


    private void initData()
    {
        initButtonDisplay();

        initGridView();

        // 从环信服务器获取所有的群成员
        getMembersFromHxServer();
    }

    private void initButtonDisplay()
    {
        if (EMClient.getInstance().getCurrentUser() .equals(emGroup.getOwner()))
        {
            //群主的详情页面
            bt_groupdetail_out.setText("解散群");
            bt_groupdetail_out.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Modle.getInstance().getExecutors().execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //告诉环信服务器解散群
                            try
                            {
                                EMClient.getInstance().groupManager().destroyGroup(groupId);

                                //发解散群的广播
                                exitGroupBroadcast();

                                //更新界面
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(GroupDetailsActivity.this, "解散群成功", Toast
                                                .LENGTH_SHORT).show();
                                        //结束当前页面
                                        finish();
                                    }
                                });
                            }
                            catch (HyphenateException e)
                            {
                                e.printStackTrace();
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(GroupDetailsActivity.this, "解散群失败", Toast
                                                .LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                    });
                }
            });

        }
        else
        {
            //群成员的详情页面
            bt_groupdetail_out.setText("退群");

            bt_groupdetail_out.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Modle.getInstance().getExecutors().execute(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //告诉环信服务器退群
                            try
                            {
                                EMClient.getInstance().groupManager().leaveGroup(groupId);

                                //发解散群的广播
                                exitGroupBroadcast();

                                //更新界面
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(GroupDetailsActivity.this, "退群成功", Toast
                                                .LENGTH_SHORT).show();
                                        //结束当前页面
                                        finish();
                                    }
                                });
                            }
                            catch (HyphenateException e)
                            {
                                e.printStackTrace();
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(GroupDetailsActivity.this, "退群失败", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });
                }
            });


        }
    }


    // 从环信服务器获取所有的群成员
    private void getMembersFromHxServer()
    {
        Modle.getInstance().getExecutors().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    EMGroup groupFromServer = EMClient.getInstance().groupManager()
                            .getGroupFromServer(emGroup.getGroupId());
                    List<String> members = groupFromServer.getMembers();
                    if (members != null && members.size() >= 0)
                    {

                        mUsers = new ArrayList<UserInfo>();
                        for (String member : members)
                        {
                            UserInfo userInfo = new UserInfo(member);
                            mUsers.add(userInfo);

                        }

                        //刷新适配器
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //传递群组成员到adapter中
                                groupDetailsAdapter.refresh(mUsers);
                            }
                        });
                    }
                }
                catch (HyphenateException e)
                {
                    e.printStackTrace();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(GroupDetailsActivity.this, "获取群成员数据失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    //解散或退群的广播
    private void exitGroupBroadcast()
    {
        Intent intent = new Intent(Contants.EXIT_GROUP);
        //传递群id
        intent.putExtra(Contants.GROUP_ID, emGroup.getGroupId());
        lbm = LocalBroadcastManager.getInstance(this);
        lbm.sendBroadcast(intent);

    }

    private void getData()
    {
        groupId = getIntent().getStringExtra(Contants.GROUP_ID);
        if (groupId != null)
        {

            //根据群组id得到群组信息
            emGroup = EMClient.getInstance().groupManager().getGroup(groupId);
        }

    }

    private void initView()
    {
        gv_groupdetail = (GridView) findViewById(R.id.gv_groupdetail);
        bt_groupdetail_out = (Button) findViewById(R.id.bt_groupdetail_out);


    }

    private void initGridView()
    {
        // 你是群主 或者 你这个群是公开  你就可以添加和删除群成员
        boolean mIsCanModify = EMClient.getInstance().getCurrentUser().equals(emGroup.getOwner())
                               || emGroup.isPublic();
        groupDetailsAdapter = new GroupDetailsAdapter(this,mIsCanModify,groupDetailsListener);
        gv_groupdetail.setAdapter(groupDetailsAdapter);
    }


}
