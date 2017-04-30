package com.atguigu.imapp.modle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.atguigu.imapp.controller.domain.GroupInfo;
import com.atguigu.imapp.controller.domain.InvitationInfo;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.utils.Contants;
import com.atguigu.imapp.modle.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;

/**
 * Created by acer on 2017/3/25 9:24.
 * 作用：全局事件监听
 */
public class EventListener
{

    //    private final Context mContext;
    //    private final LocalBroadcastManager lbm;

    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context)
    {
        mContext = context;

        // 创建一个发送广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(mContext);

        // 注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);

        //        注册一个群信息变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(eMGroupChangeListener);
    }

    private final EMGroupChangeListener eMGroupChangeListener = new EMGroupChangeListener()
    {

        //收到 群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String
                reason)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            invitationInfo.setReason(reason);
            GroupInfo groupInfo = new GroupInfo(groupName,groupId,inviter);
            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_INVITE);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }

        //收到 群申请通知
        @Override
        public void onApplicationReceived(String groupId, String groupName, String applicant,
                                          String reason)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            invitationInfo.setReason(reason);
            GroupInfo groupInfo = new GroupInfo(groupName,groupId,applicant);
            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被接受
        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            GroupInfo groupInfo = new GroupInfo(groupName,groupId,accepter);
            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被拒绝
        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner,
                                          String reason)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            invitationInfo.setReason(reason);
            GroupInfo groupInfo = new GroupInfo(groupName,groupId,decliner);

            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被同意
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            GroupInfo groupInfo = new GroupInfo(groupId,groupId,inviter);
            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            GroupInfo groupInfo = new GroupInfo(groupId,groupId,inviter);
            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }

        //收到 群成员被删除
        @Override
        public void onUserRemoved(String groupId, String groupName)
        {

        }

        //收到 群被解散
        @Override
        public void onGroupDestroyed(String groupId, String groupName)
        {

        }

        //收到 群邀请被自动接受
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String
                inviteMessage)
        {
            //数据库更新
            InvitationInfo invitationInfo=new InvitationInfo();
            invitationInfo.setReason(inviteMessage);
            GroupInfo groupInfo = new GroupInfo(groupId,groupId,inviter);
            invitationInfo.setGroup(groupInfo);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);
            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Contants.GROUP_INVITE_CHANGED));
        }
    };

    // 注册一个联系人变化的监听
    private final EMContactListener emContactListener = new EMContactListener()
    {
        // 联系人增加后执行的方法
        @Override
        public void onContactAdded(String hxid)
        {
            // 数据更新
            Modle.getInstance().getDBManager().getContactTabDao().saveContact(new UserInfo(hxid),
                    true);

            // 发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contants.CONTACT_CHANGED));
        }

        // 联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxid)
        {
            // 数据更新
            Modle.getInstance().getDBManager().getContactTabDao().deleteContactByHxId(hxid);
            Modle.getInstance().getDBManager().getInviteTableDao().removeInvitation(hxid);

            // 发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Contants.CONTACT_CHANGED));
        }

        // 接受到联系人的新邀请
        @Override
        public void onContactInvited(String hxid, String reason)
        {
            // 数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setReason(reason);
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);// 新邀请

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);

            // 红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Contants.CONTACT_INVITE_CHANGED));
        }

        // 别人同意了你的好友邀请
        @Override
        public void onContactAgreed(String hxid)
        {
            // 数据库更新
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);//
            // 别人同意了你的邀请

            Modle.getInstance().getDBManager().getInviteTableDao().addInvitation(invitationInfo);

            // 红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Contants.CONTACT_INVITE_CHANGED));
        }

        // 别人拒绝了你好友邀请
        @Override
        public void onContactRefused(String s)
        {
            // 红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Contants.CONTACT_INVITE_CHANGED));
        }
    };

    //    public EventListener(Context context)
    //    {
    //        mContext = context;
    //
    //        //发送广播的管理者对象
    //        lbm = LocalBroadcastManager.getInstance(context);
    //
    //        //注册一个联系人变化的监听
    //        EMClient.getInstance().contactManager().setContactListener(new EMContactListener()
    //        {
    //            //被别人请求添加为联系人的时候回调
    //            @Override
    //            public void onContactAdded(String hxid)
    //            {
    //                Log.e("TAG", "onContactAdded===");
    //                //数据库改变
    //                Modle.getInstance().getDBManager().getContactTabDao().saveContact(new UserInfo
    //                        (hxid), true);
    //                //发送添加联系人的广播
    //                lbm.sendBroadcast(new Intent(Contants.CONTACT_CHANGED));
    //            }
    //
    //            //
    //            @Override
    //            public void onContactDeleted(String hxid)
    //            {
    //                Log.e("TAG", "onContactDeleted===");
    //                //联系人的表改变
    //                Modle.getInstance().getDBManager().getContactTabDao().deleteContactByHxId
    // (hxid);
    //                //发送删除联系人的广播
    //                lbm.sendBroadcast(new Intent(Contants.CONTACT_CHANGED));
    //            }
    //
    //            //当收到新的联系人邀请信息的时候
    //            @Override
    //            public void onContactInvited(String hxid, String reason)
    //            {
    //                //邀请信息的表改变
    //                InvitationInfo invitationInfo=new InvitationInfo();
    //                invitationInfo.setUser(new UserInfo(hxid));
    //                invitationInfo.setReason(reason);
    //                //将状态设置为新的联系人邀请
    //                invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);
    //                Modle.getInstance().getDBManager().getInviteTableDao().addInvitation
    // (invitationInfo);
    //
    //
    //                //红点的处理 保存到sp中
    //
    //                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE,true);
    //
    //                //发送邀请信息的广播
    //                lbm.sendBroadcast(new Intent(Contants.CONTACT_INVITE_CHANGED));
    //            }
    //
    //            //当别人同意了你的邀请
    //            @Override
    //            public void onContactAgreed(String hxid)
    //            {
    //                // 数据库更新
    //                InvitationInfo invitationInfo = new InvitationInfo();
    //                invitationInfo.setUser(new UserInfo(hxid));
    //                invitationInfo.setStatus(InvitationInfo.InvitationStatus
    // .INVITE_ACCEPT_BY_PEER);//
    //                // 别人同意了你的邀请
    //
    //                Modle.getInstance().getDBManager().getInviteTableDao().addInvitation
    // (invitationInfo);
    //
    //                // 红点的处理
    //                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
    //
    //                // 发送邀请信息变化的广播
    //                lbm.sendBroadcast(new Intent(Contants.CONTACT_INVITE_CHANGED));
    //            }
    //
    //            //别人拒绝了你的邀请
    //            @Override
    //            public void onContactRefused(String hxid)
    //            {
    //
    //
    //                // 红点的处理
    //                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
    //
    //                // 发送邀请信息变化的广播
    //                lbm.sendBroadcast(new Intent(Contants.CONTACT_INVITE_CHANGED));
    //            }
    //        });
    //    }

}
