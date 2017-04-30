package com.atguigu.imapp.modle.fragment;

import android.content.Intent;

import com.atguigu.imapp.modle.activity.ChatActivity;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

/**
 * Created by acer on 2017/3/23 10:18.
 * 作用：
 */
public class CharFragment extends EaseConversationListFragment
{
    @Override
    protected void initView()
    {
        super.initView();
        //添加联系人消息的监听事件
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener()
        {
            //当消息被接收的时候
            @Override
            public void onMessageReceived(List<EMMessage> list)
            {
                //设置数据
                EaseUI.getInstance().getNotifier().onNewMesg(list);

                //刷新页面
                refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list)
            {

            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list)
            {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list)
            {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o)
            {

            }
        });

        //点击会话条目调转
        setConversationListItemClickListener(new EaseConversationListItemClickListener()
        {
            @Override
            public void onListItemClicked(EMConversation conversation)
            {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                //携带数据 conversationId就是hxid
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                //判断是否为群聊
                if (conversation.getType() == EMConversation.EMConversationType.GroupChat)
                {
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                }
                startActivity(intent);
            }
        });

        // 清空当前会话列表数据，准备加载新的数据
        conversationList.clear();


    }
}
