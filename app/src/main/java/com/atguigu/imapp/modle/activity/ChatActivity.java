package com.atguigu.imapp.modle.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import com.atguigu.imapp.R;
import com.atguigu.imapp.modle.utils.Contants;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

//会话页面
public class ChatActivity extends FragmentActivity
{

    private EaseChatFragment easeChatFragment;
    private String hxid;
    private LocalBroadcastManager lbm;
    private BroadcastReceiver exitGroupReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //创建会话页面的fragment
        easeChatFragment = new EaseChatFragment();
        //得到会话类型
        int chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);

        hxid = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);

        //设置参数
        easeChatFragment.setArguments(getIntent().getExtras());

        //替换fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat, easeChatFragment).commit();

        final String groupId = getIntent().getStringExtra(Contants.GROUP_ID);

        lbm = LocalBroadcastManager.getInstance(this);
        if (chatType == EaseConstant.CHATTYPE_GROUP)
        {

            exitGroupReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent)
                {
                    if (hxid == groupId)
                    {

                        finish();
                    }
                }
            };

            lbm.registerReceiver(exitGroupReceiver, new IntentFilter(Contants.EXIT_GROUP));
        }


        //跳转到群详情页面
        easeChatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper()
        {
            @Override
            public void onSetMessageAttributes(EMMessage message)
            {

            }

            @Override
            public void onEnterToChatDetails()
            {
                //跳转到群组详情页面
                Intent intent = new Intent(ChatActivity.this, GroupDetailsActivity.class);
                intent.putExtra(Contants.GROUP_ID, hxid);
                startActivity(intent);
            }

            @Override
            public void onAvatarClick(String username)
            {

            }

            @Override
            public void onAvatarLongClick(String username)
            {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message)
            {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message)
            {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view)
            {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider()
            {
                return null;
            }
        });
    }
}
