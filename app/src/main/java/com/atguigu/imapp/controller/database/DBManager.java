package com.atguigu.imapp.controller.database;

import android.content.Context;

import com.atguigu.imapp.controller.dao.ContactTabDao;
import com.atguigu.imapp.controller.dao.InviteTableDao;

/**
 * Created by acer on 2017/3/24 21:02.
 * 作用：联系人表和邀请信息表的管理类
 */
public class DBManager
{

    private final DBHelper dbHelper;
    private final ContactTabDao contactTabDao;
    private final InviteTableDao inviteTableDao;

    public DBManager(Context mContext,String name)
    {
        //得到数据库对象
        dbHelper = new DBHelper(mContext, name);
        contactTabDao = new ContactTabDao(dbHelper);
        inviteTableDao = new InviteTableDao(dbHelper);
    }
    //得到contactTabDao的对象
    public ContactTabDao getContactTabDao()
    {
        return contactTabDao;
    }
    //得到InviteTableDao的对象
    public InviteTableDao getInviteTableDao()
    {
        return inviteTableDao;
    }

    //关闭数据库
    public void close()
    {
        dbHelper.close();
    }
}
