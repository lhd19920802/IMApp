package com.atguigu.imapp.controller.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atguigu.imapp.controller.database.DBHelper;
import com.atguigu.imapp.controller.domain.ContactTab;
import com.atguigu.imapp.controller.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/24 16:07.
 * 作用：
 */
public class ContactTabDao
{
    private DBHelper mDbhelper;

    public ContactTabDao(DBHelper dbHelper)
    {
        mDbhelper = dbHelper;
    }

    //获取所有联系人
    public List<UserInfo> getAllContacts()
    {
        //获取数据库连接对象
        SQLiteDatabase database = mDbhelper.getReadableDatabase();


        //执行查询操作
        String sql = "select * from " + ContactTab.TAB_NAME + " where " + ContactTab
                .COL_IS_CONTACT + "=1";
        Cursor cursor = database.rawQuery(sql, null);
        List<UserInfo> contacts = new ArrayList<>();
        while (cursor.moveToNext())
        {
            UserInfo userInfo = new UserInfo();
            String hxid = cursor.getString(cursor.getColumnIndex(ContactTab.COL_HXID));
            userInfo.setHxid(hxid);
            String name = cursor.getString(cursor.getColumnIndex(ContactTab.COL_NAME));
            userInfo.setName(name);
            String nick = cursor.getString(cursor.getColumnIndex(ContactTab.COL_NICK));
            userInfo.setNick(nick);
            String photo = cursor.getString(cursor.getColumnIndex(ContactTab.COL_PHOTO));
            userInfo.setPhoto(photo);
            contacts.add(userInfo);
        }
        //关闭资源
        cursor.close();
        //返回
        return contacts;
    }

    //通过环信id获取单个对应的联系人
    public UserInfo getContact(String hxId)
    {
        if (hxId == null)
        {
            return null;
        }
        SQLiteDatabase database = mDbhelper.getReadableDatabase();


        String sql = "select * from " + ContactTab.TAB_NAME + " where " + ContactTab.COL_HXID +
                     "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{hxId});
        UserInfo userInfo = null;
        if (cursor.moveToNext())
        {
            userInfo = new UserInfo();
            String hxid = cursor.getString(cursor.getColumnIndex(ContactTab.COL_HXID));
            userInfo.setHxid(hxid);
            String name = cursor.getString(cursor.getColumnIndex(ContactTab.COL_NAME));
            userInfo.setName(name);
            String nick = cursor.getString(cursor.getColumnIndex(ContactTab.COL_NICK));
            userInfo.setNick(nick);
            String photo = cursor.getString(cursor.getColumnIndex(ContactTab.COL_PHOTO));
            userInfo.setPhoto(photo);
        }

        cursor.close();

        return userInfo;
    }

    //保存单个联系人
    public void saveContact(UserInfo user, boolean isMyContact)
    {
        if (user == null)
        {
            return;
        }
        //获取数据库连接对象
        SQLiteDatabase database = mDbhelper.getReadableDatabase();
        //进行
        ContentValues values = new ContentValues();
        values.put(ContactTab.COL_HXID, user.getHxid());
        values.put(ContactTab.COL_NAME, user.getName());
        values.put(ContactTab.COL_NICK, user.getNick());
        values.put(ContactTab.COL_PHOTO, user.getPhoto());
        values.put(ContactTab.COL_IS_CONTACT, isMyContact ? 1 : 0);
        database.replace(ContactTab.TAB_NAME, null, values);
    }

    //保存多个联系人信息
    public void saveContacts(List<UserInfo> contacts, boolean isMyContact)
    {
        if (contacts == null || contacts.size() <= 0)
        {
            return;
        }
        for (UserInfo contact : contacts)
        {
            saveContact(contact, isMyContact);
        }
    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId)
    {
        if (hxId == null)
        {
            return;
        }
        SQLiteDatabase database = mDbhelper.getReadableDatabase();
        database.delete(ContactTab.TAB_NAME, ContactTab.COL_HXID + "=?", new String[]{hxId});
    }


}
