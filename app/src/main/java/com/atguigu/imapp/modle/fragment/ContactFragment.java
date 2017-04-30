package com.atguigu.imapp.modle.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.Modle;
import com.atguigu.imapp.modle.activity.AddContactActivity;
import com.atguigu.imapp.modle.activity.ChatActivity;
import com.atguigu.imapp.modle.activity.GroupListActivity;
import com.atguigu.imapp.modle.activity.InviteActivity;
import com.atguigu.imapp.modle.utils.Contants;
import com.atguigu.imapp.modle.utils.SpUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by acer on 2017/3/23 10:22.
 * 作用：
 */
public class ContactFragment extends EaseContactListFragment
{

    private ImageView iv_contact_red;
    private LocalBroadcastManager lbm;
    private LinearLayout ll_contact_invite;
    private BroadcastReceiver contactInviteChangeReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //显示红点
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

        }
    };
    private BroadcastReceiver contactChangeReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            refreshContact();
        }
    };
    private String hxid;
    //群邀请变化的广播接收器
    private BroadcastReceiver groupInviteChangeReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //显示红点
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
        }
    };
    private LinearLayout ll_contact_group;

    /**
     * EaseBaseFragment的抽象方法
     */
    @Override
    protected void initView()
    {
        super.initView();
        //添加加号
        titleBar.setRightImageResource(R.drawable.em_add);
        View headerView = View.inflate(getActivity(), R.layout.fragment_contact_header, null);

        //红点
        iv_contact_red = (ImageView) headerView.findViewById(R.id.iv_contact_red);
        //邀请信息条目
        ll_contact_invite = (LinearLayout) headerView.findViewById(R.id.ll_contact_invite);

        //群组条目
        ll_contact_group = (LinearLayout) headerView.findViewById(R.id.ll_contact_group);

        listView.addHeaderView(headerView);

        //点击listView跳转到会话页面
        setContactListItemClickListener(new EaseContactListItemClickListener()
        {
            @Override
            public void onListItemClicked(EaseUser user)
            {
                if (user == null)
                {
                    return;
                }
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //携带数据
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
                startActivity(intent);
            }
        });


        //群组的点击事件
        ll_contact_group.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), GroupListActivity.class);


                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpView()
    {
        super.setUpView();
        titleBar.setRightLayoutClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
            }
        });


        //初始化红点
        boolean is_new_invite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        iv_contact_red.setVisibility(is_new_invite ? View.VISIBLE : View.GONE);

        //红点的变化 注册广播接收器
        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(contactInviteChangeReceiver, new IntentFilter(Contants
                .CONTACT_INVITE_CHANGED));

        //注册联系人变化的广播接收器
        lbm.registerReceiver(contactChangeReceiver, new IntentFilter(Contants.CONTACT_CHANGED));
        //注册群邀请变化的广播接收器
        lbm.registerReceiver(groupInviteChangeReceiver,new IntentFilter(Contants.GROUP_INVITE_CHANGED));


        //设置点击邀请信息条目的点击事件
        ll_contact_invite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //隐藏红点
                iv_contact_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);

                //跳转到邀请页面
                Intent intent = new Intent(getActivity(), InviteActivity.class);
                startActivity(intent);
            }
        });

        //从环信服务器得到所有联系人信息
        Modle.getInstance().getExecutors().execute(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    List<String> hxids = EMClient.getInstance().contactManager()
                            .getAllContactsFromServer();
                    if (hxids != null && hxids.size() >= 0)
                    {


                        List<UserInfo> contacts = new ArrayList<UserInfo>();
                        for (String hxid : hxids)
                        {
                            UserInfo userInfo = new UserInfo(hxid);
                            contacts.add(userInfo);
                        }
                        //保存到数据库中
                        Modle.getInstance().getDBManager().getContactTabDao().saveContacts
                                (contacts, true);
                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //刷新界面
                                refreshContact();
                            }
                        });

                    }
                }
                catch (HyphenateException e)
                {
                    e.printStackTrace();

                }
            }
        });

        //1 listView绑定contextMenu
        registerForContextMenu(listView);

    }
    //2 加载布局


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;

        EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
        hxid = easeUser.getUsername();

        //加载布局
        getActivity().getMenuInflater().inflate(R.menu.delete, menu);
    }

    //删除联系人


    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.contact_delete)
        {
            //执行删除操作
            Modle.getInstance().getExecutors().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //从环信服务器中删除
                    try
                    {
                        EMClient.getInstance().contactManager().deleteContact(hxid);//根据环信id删除
                        //从数据库中删除
                        Modle.getInstance().getDBManager().getContactTabDao().deleteContactByHxId
                                (hxid);


                        if (getActivity() == null)
                        {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //更新界面
                                Toast.makeText(getActivity(), "删除" + hxid + "成功", Toast
                                        .LENGTH_SHORT).show();
                                refreshContact();
                            }
                        });
                    }
                    catch (HyphenateException e)
                    {
                        e.printStackTrace();

                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                //更新界面
                                Toast.makeText(getActivity(), "删除" + hxid + "失败", Toast
                                        .LENGTH_SHORT).show();
                            }
                        });

                    }


                }
            });
            return true;
        }
        return super.onContextItemSelected(item);
    }

    //刷新联系人列表页面
    private void refreshContact()
    {
        // 获取数据
        List<UserInfo> contacts = Modle.getInstance().getDBManager().getContactTabDao()
                .getAllContacts();

        // 校验
        if (contacts != null && contacts.size() >= 0)
        {

            // 设置数据
            Map<String, EaseUser> contactsMap = new HashMap<>();

            // 转换
            for (UserInfo contact : contacts)
            {
                EaseUser easeUser = new EaseUser(contact.getHxid());

                contactsMap.put(contact.getHxid(), easeUser);
            }

            setContactsMap(contactsMap);

            // 刷新页面
            refresh();
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        lbm.unregisterReceiver(contactInviteChangeReceiver);
        lbm.unregisterReceiver(contactChangeReceiver);
        lbm.unregisterReceiver(groupInviteChangeReceiver);
    }
}
