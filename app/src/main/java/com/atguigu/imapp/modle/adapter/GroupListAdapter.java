package com.atguigu.imapp.modle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.imapp.R;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/27 18:34.
 * 作用：
 */
public class GroupListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<EMGroup> mGroups = new ArrayList<>();

    public GroupListAdapter(Context context)
    {
        mContext = context;
    }

    //传递数据
    public void refresh(List<EMGroup> groups)
    {
        if (groups != null && groups.size() >= 0)
        {

            mGroups.clear();
            mGroups.addAll(groups);
            notifyDataSetChanged();
        }


    }

    @Override
    public int getCount()
    {
        return mGroups.size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHodler;
        if (convertView == null)
        {
            viewHodler=new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_group_list, null);
            viewHodler.tv_grouplist_name = (TextView) convertView.findViewById(R.id
                    .tv_grouplist_name);

            convertView.setTag(viewHodler);
        }
        else
        {
            viewHodler = (ViewHolder) convertView.getTag();
        }

        EMGroup emGroup = mGroups.get(position);
        viewHodler.tv_grouplist_name.setText(emGroup.getGroupName());
        return convertView;
    }

    class ViewHolder
    {
        TextView tv_grouplist_name;
    }
}
