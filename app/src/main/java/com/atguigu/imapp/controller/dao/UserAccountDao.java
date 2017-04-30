package com.atguigu.imapp.controller.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.atguigu.imapp.controller.database.UserAccountDB;
import com.atguigu.imapp.controller.domain.UserAccountTab;
import com.atguigu.imapp.controller.domain.UserInfo;

/**
 * Created by acer on 2017/3/22 10:02.
 * 作用：数据库操作类
 */
public class UserAccountDao
{
    private UserAccountDB dbHelper;

    public UserAccountDao(Context context)
    {
        dbHelper = new UserAccountDB(context);
    }

    //添加
    public void addAccount(UserInfo userInfo)
    {
        //得到数据库对象
        SQLiteDatabase database = dbHelper.getReadableDatabase();


        //操作数据库
        ContentValues values = new ContentValues();
        values.put(UserAccountTab.COL_NAME, userInfo.getName());
        values.put(UserAccountTab.COL_HXID, userInfo.getHxid());
        values.put(UserAccountTab.COL_NICK, userInfo.getNick());
        values.put(UserAccountTab.COL_PHOTO, userInfo.getPhoto());

        database.replace(UserAccountTab.TAB_NAME, null, values);

    }

    //查询
    public UserInfo getAccountInfo(String hxid)
    {
        //得到数据库连接对象
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        //执行查询操作 通过主键查询
        String sql = "select * from " + UserAccountTab.TAB_NAME + " where " + UserAccountTab
                .COL_HXID + "=?";
        Cursor cursor = database.rawQuery(sql, new String[]{hxid});
        UserInfo userInfo=null;
        while (cursor.moveToNext())
        {
            userInfo = new UserInfo();
            String name = cursor.getString(cursor.getColumnIndex(UserAccountTab.COL_NAME));
            userInfo.setName(name);
            String hxId = cursor.getString(cursor.getColumnIndex(UserAccountTab.COL_HXID));
            userInfo.setHxid(hxId);
            String nick = cursor.getString(cursor.getColumnIndex(UserAccountTab.COL_NICK));
            userInfo.setNick(nick);

            String photo = cursor.getString(cursor.getColumnIndex(UserAccountTab.COL_PHOTO));
            userInfo.setPhoto(photo);
        }

        //关闭资源

        cursor.close();
        //返回数据
        return userInfo;
    }
}
