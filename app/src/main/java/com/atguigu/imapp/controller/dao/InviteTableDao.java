package com.atguigu.imapp.controller.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atguigu.imapp.controller.database.DBHelper;
import com.atguigu.imapp.controller.domain.GroupInfo;
import com.atguigu.imapp.controller.domain.InvitationInfo;
import com.atguigu.imapp.controller.domain.InviteTable;
import com.atguigu.imapp.controller.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/24 8:44.
 * 作用：
 */
public class InviteTableDao
{
    private DBHelper mDbHelper;

    public InviteTableDao(DBHelper dbHelper)
    {
        mDbHelper = dbHelper;
    }

    //添加邀请信息
    public void addInvitation(InvitationInfo invitationInfo)
    {
        //得到数据库连接对象
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //进行添加数据操作
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_REASON, invitationInfo.getReason());
        values.put(InviteTable.COL_STATUS, invitationInfo.getStatus().ordinal());
        //根据user是否为空判断是联系人邀请信息还是群组邀请信息
        UserInfo user = invitationInfo.getUser();
        if (user != null)
        {
            //联系人邀请信息
            values.put(InviteTable.COL_USER_HXID, invitationInfo.getUser().getHxid());
            values.put(InviteTable.COL_USER_NAME, invitationInfo.getUser().getName());
        }
        else
        {
            //群组邀请信息
            values.put(InviteTable.COL_GROUP_NAME, invitationInfo.getGroup().getGroupName());
            values.put(InviteTable.COL_GROUP_HXID, invitationInfo.getGroup().getGroupId());
            //将邀请人信息放在user hxid中 因为user hxid是表的唯一标识 不能为空
            values.put(InviteTable.COL_USER_HXID, invitationInfo.getGroup().getInvitePerson());
        }
        database.replace(InviteTable.TAB_NAME, null, values);
    }

    // 获取所有邀请信息
    public List<InvitationInfo> getInvitations()
    {
        //获取数据连接对象
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        //执行查询操作
        String sql = "select * from " + InviteTable.TAB_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        List<InvitationInfo> invitationInfos = new ArrayList<>();
        while (cursor.moveToNext())
        {
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable
                    .COL_REASON)));
            invitationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex
                    (InviteTable.COL_STATUS))));
            String groupId = cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID));
            if (groupId != null)
            {
                //群组的邀请信息
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InviteTable
                        .COL_GROUP_HXID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable
                        .COL_GROUP_NAME)));
                groupInfo.setInvitePerson(cursor.getString(cursor.getColumnIndex(InviteTable
                        .COL_USER_HXID)));

                invitationInfo.setGroup(groupInfo);
            }
            else
            {
                //联系人的邀请信息
                UserInfo userInfo = new UserInfo();
                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable
                        .COL_USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable
                        .COL_USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable
                        .COL_USER_NAME)));

                invitationInfo.setUser(userInfo);

            }
            invitationInfos.add(invitationInfo);

        }
        //关闭资源
        cursor.close();

        //返回
        return invitationInfos;

    }

    // 将int类型状态转换为邀请的状态
    private InvitationInfo.InvitationStatus int2InviteStatus(int intStatus)
    {
        if (intStatus == InvitationInfo.InvitationStatus.NEW_INVITE.ordinal())
        {
            return InvitationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT.ordinal())
        {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal())
        {
            return InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_INVITE.ordinal())
        {
            return InvitationInfo.InvitationStatus.NEW_GROUP_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION.ordinal())
        {
            return InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_INVITE;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION;
        }

        if (intStatus == InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE.ordinal())
        {
            return InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE;
        }

        return null;
    }

    // 删除邀请
    public void removeInvitation(String hxId)
    {
        if (hxId == null)
        {
            return;
        }

        // 获取数据库链接
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.delete(InviteTable.TAB_NAME, InviteTable.COL_USER_HXID + "=?", new String[]{hxId});
    }

    // 更新邀请状态
    public void updateInvitationStatus(InvitationInfo.InvitationStatus invitationStatus, String
            hxId)
    {
        if (hxId == null)
        {
            return;
        }

        // 获取数据库链接
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // 执行更新操作
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_STATUS, invitationStatus.ordinal());

        db.update(InviteTable.TAB_NAME, values, InviteTable.COL_USER_HXID + "=?", new
                String[]{hxId});
    }
}
