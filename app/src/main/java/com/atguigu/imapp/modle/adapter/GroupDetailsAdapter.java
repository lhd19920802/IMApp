package com.atguigu.imapp.modle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/28 14:29.
 * 作用：
 */
public class GroupDetailsAdapter extends BaseAdapter
{
    private Context mContext;
    //是否允许添加和删除群成员
    private boolean mIsCanModify;

    private boolean mIsDeleteMode;// 删除模式  true：表示可以删除； false:表示不可以删除
    private List<UserInfo> mUsers = new ArrayList<>();


    //接口的成员变量
    private GroupDetailsListener mGroupDetailsListener;
    public GroupDetailsAdapter(Context context, boolean isCanModify,GroupDetailsListener groupDetailsListener)
    {
        mContext = context;
        mIsCanModify = isCanModify;

        mGroupDetailsListener=groupDetailsListener;
    }

    public boolean ismIsDeleteMode()
    {
        return mIsDeleteMode;
    }

    public void setmIsDeleteMode(boolean mIsDeleteMode)
    {
        this.mIsDeleteMode = mIsDeleteMode;
    }

    //利用刷新的方法传递数据
    public void refresh(List<UserInfo> users)
    {
        if (users != null && users.size() >= 0)
        {
            mUsers.clear();
            //添加加号和减号
            UserInfo add = new UserInfo("add");
            UserInfo delete = new UserInfo("delete");
            mUsers.add(delete);
            mUsers.add(0, add);

            mUsers.addAll(0, users);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount()
    {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_group_detail, null);
            viewHolder.iv_group_detail_photo = (ImageView) convertView.findViewById(R.id
                    .iv_group_detail_photo);
            viewHolder.tv_group_detail_name = (TextView) convertView.findViewById(R.id
                    .tv_group_detail_name);
            viewHolder.iv_group_detail_delete = (ImageView) convertView.findViewById(R.id
                    .iv_group_detail_delete);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final UserInfo userInfo = mUsers.get(position);
        //群主或开放了群权限
        if (mIsCanModify)
        {

            if (position == getCount() - 1)
            {
                //减号
                if(mIsDeleteMode) {
                    //删除模式下
                    convertView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //非删除模式下

                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.tv_group_detail_name.setVisibility(View.INVISIBLE);
                    viewHolder.iv_group_detail_delete.setVisibility(View.GONE);
                    viewHolder.iv_group_detail_photo.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                }
            }
            else if (position == getCount() - 2)
            {
                if(mIsDeleteMode) {
                    //删除模式下
                    convertView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //非删除模式下

                    convertView.setVisibility(View.VISIBLE);
                    viewHolder.tv_group_detail_name.setVisibility(View.INVISIBLE);
                    viewHolder.iv_group_detail_delete.setVisibility(View.GONE);
                    viewHolder.iv_group_detail_photo.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                }
            }
            else
            {
                //成员
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tv_group_detail_name.setVisibility(View.VISIBLE);

                viewHolder.tv_group_detail_name.setText(userInfo.getName());
                viewHolder.iv_group_detail_photo.setImageResource(R.drawable.em_default_avatar);
                if(mIsDeleteMode) {
                    viewHolder.iv_group_detail_delete.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolder.iv_group_detail_delete.setVisibility(View.GONE);
                }
            }


            //点击事件的处理
            if(position==getCount()-1) {
                viewHolder.iv_group_detail_photo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(!mIsDeleteMode) {
                            mIsDeleteMode=true;
                            notifyDataSetChanged();
                        }
                    }
                });
            }else if (position==getCount()-2)
            {
                viewHolder.iv_group_detail_photo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mGroupDetailsListener.addMember();
                    }
                });
            }
            else
            {
                viewHolder.iv_group_detail_delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mGroupDetailsListener.deleteMember(userInfo);
                    }
                });
            }
        }
        else
        {
            //普通群成员
            if (position == getCount() - 1 || position == getCount() - 2)
            {
                //隐藏加号减号
                convertView.setVisibility(View.GONE);
            }
            else
            {
                convertView.setVisibility(View.VISIBLE);
                viewHolder.tv_group_detail_name.setText(userInfo.getName());
                viewHolder.iv_group_detail_delete.setVisibility(View.GONE);
                viewHolder.iv_group_detail_photo.setImageResource(R.drawable.em_default_avatar);
            }
        }

        return convertView;
    }

    class ViewHolder
    {
        ImageView iv_group_detail_photo;
        TextView tv_group_detail_name;
        ImageView iv_group_detail_delete;
    }

    public interface GroupDetailsListener
    {
        void addMember();

        void deleteMember(UserInfo userInfo);
    }
}
