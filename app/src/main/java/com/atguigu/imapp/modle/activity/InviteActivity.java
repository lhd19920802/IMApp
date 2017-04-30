package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.InvitationInfo;
import com.atguigu.imapp.modle.Modle;
import com.atguigu.imapp.modle.adapter.InviteAdapter;
import com.atguigu.imapp.modle.utils.Contants;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * 邀请信息页面
 */
public class InviteActivity extends Activity
{

    private ListView lv_invite;
    private InviteAdapter adapter;
    private InviteAdapter.OnInviteListener onInvitationListener = new InviteAdapter
            .OnInviteListener()
    {
        //接受邀请
        @Override
        public void accept(final InvitationInfo invitationInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //告诉环信服务器接受了邀请
                    try
                    {
                        EMClient.getInstance().contactManager().acceptInvitation(invitationInfo
                                .getUser().getHxid());

                        //更新数据库
                        Modle.getInstance().getDBManager().getInviteTableDao()
                                .updateInvitationStatus(InvitationInfo.InvitationStatus
                                        .INVITE_ACCEPT, invitationInfo.getUser().getHxid());
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {


                                //刷新界面
                                refresh();
                                //显示通知
                                Toast.makeText(InviteActivity.this, "接受邀请成功", Toast.LENGTH_SHORT)
                                        .show();
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
                                Toast.makeText(InviteActivity.this, "接受邀请失败", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                    }
                }
            });
        }

        //拒绝邀请
        @Override
        public void reject(final InvitationInfo invitationInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //通知环信服务器
                    try
                    {
                        EMClient.getInstance().contactManager().declineInvitation(invitationInfo
                                .getUser().getHxid());

                        //数据库更新
                        Modle.getInstance().getDBManager().getInviteTableDao().removeInvitation
                                (invitationInfo.getUser().getHxid());
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //提示
                                Toast.makeText(InviteActivity.this, "拒绝邀请成功", Toast.LENGTH_SHORT)
                                        .show();
                                //刷新
                                refresh();
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
                                Toast.makeText(InviteActivity.this, "拒绝邀请失败", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                }
            });
        }

        //接受群邀请
        @Override
        public void onInviteAccept(final InvitationInfo invationInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //告诉环信服务器接受了邀请
                    try
                    {
                        EMClient.getInstance().groupManager().acceptInvitation(invationInfo
                                .getGroup().getGroupId(), invationInfo.getGroup().getInvitePerson
                                ());
                        invationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                        //本地数据变化
                        Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invationInfo);


                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(InviteActivity.this, "接受邀请成功", Toast.LENGTH_SHORT)
                                        .show();
                                refresh();
                            }
                        });
                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(InviteActivity.this, "接受邀请失败", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

        //拒绝群邀请
        @Override
        public void onInviteReject(final InvitationInfo invationInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //告诉环信服务器接受了邀请
                    try
                    {
                        EMClient.getInstance().groupManager().declineInvitation(invationInfo
                                .getGroup().getGroupId(), invationInfo.getGroup().getInvitePerson
                                (),"就是想拒绝邀请");
                        //本地数据变化
                        Modle.getInstance().getDBManager().getInviteTableDao().addInvitation
                                (invationInfo);
                        invationInfo.setStatus(InvitationInfo.InvitationStatus
                                .GROUP_REJECT_INVITE);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(InviteActivity.this, "拒绝邀请成功", Toast.LENGTH_SHORT)
                                        .show();
                                refresh();
                            }
                        });
                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(InviteActivity.this, "拒绝邀请失败", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

        //接受群申请
        @Override
        public void onApplicationAccept(final InvitationInfo invationInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //告诉环信服务器接受了邀请
                    try
                    {
                        EMClient.getInstance().groupManager().acceptApplication(invationInfo
                                .getGroup().getGroupId(), invationInfo.getGroup().getInvitePerson
                                ());
                        //本地数据变化
                        Modle.getInstance().getDBManager().getInviteTableDao().addInvitation
                                (invationInfo);
                        invationInfo.setStatus(InvitationInfo.InvitationStatus
                                .GROUP_ACCEPT_APPLICATION);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(InviteActivity.this, "接受申请成功", Toast.LENGTH_SHORT)
                                        .show();
                                refresh();
                            }
                        });
                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(InviteActivity.this, "接受申请失败", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

        //拒绝群申请
        @Override
        public void onApplicationReject(final InvitationInfo invationInfo)
        {
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        EMClient.getInstance().groupManager().declineApplication(invationInfo
                                .getGroup().getGroupId(), invationInfo.getGroup().getInvitePerson
                                (),"就是想拒绝申请");
                        //本地数据变化
                        Modle.getInstance().getDBManager().getInviteTableDao().addInvitation
                                (invationInfo);
                        invationInfo.setStatus(InvitationInfo.InvitationStatus
                                .GROUP_REJECT_APPLICATION);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(InviteActivity.this, "拒绝申请成功", Toast.LENGTH_SHORT)
                                        .show();
                                refresh();
                            }
                        });
                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(InviteActivity.this, "拒绝申请失败", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }
    };
    private LocalBroadcastManager lbm;
    private BroadcastReceiver inviteChangedReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {


            if (intent.getAction() == Contants.CONTACT_INVITE_CHANGED || intent.getAction() ==
                                                                         Contants.GROUP_INVITE_CHANGED)
            {
                //刷新页面
                refresh();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initView();

        //注册邀请邀请信息变化的广播 做到不用切换页面实时显示邀请信息
        lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(inviteChangedReceiver, new IntentFilter(Contants
                .CONTACT_INVITE_CHANGED));
        lbm.registerReceiver(inviteChangedReceiver, new IntentFilter(Contants
                .GROUP_INVITE_CHANGED));
    }

    private void initView()
    {
        lv_invite = (ListView) findViewById(R.id.lv_invite);
        adapter = new InviteAdapter(this, onInvitationListener);

        lv_invite.setAdapter(adapter);
        refresh();
    }

    public void refresh()
    {
        //将数据库中所有的邀请信息传递到adapter里面
        List<InvitationInfo> invitations = Modle.getInstance().getDBManager().getInviteTableDao()
                .getInvitations();
        adapter.refresh(invitations);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        lbm.unregisterReceiver(inviteChangedReceiver);
    }
}
