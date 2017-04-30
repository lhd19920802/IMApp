package com.atguigu.imapp.modle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.SelectContactsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/27 19:18.
 * 作用：
 */
public class SelectContactsAdapter extends BaseAdapter
{

    private final Context mContext;
    //选择联系人的集合
    private final List<SelectContactsInfo> mSelects = new ArrayList<>();

    //群中现存的成员
    private List<String> mExistMembers = new ArrayList<>();

    public SelectContactsAdapter(Context context, List<SelectContactsInfo> selects, List<String>
            existMembers)
    {
        mContext = context;
        if (selects != null && selects.size() >= 0)
        {
            mSelects.clear();
            mSelects.addAll(selects);
        }
        if (existMembers != null)
        {
            mExistMembers.clear();
            mExistMembers.addAll(existMembers);
        }

    }


    @Override
    public int getCount()
    {
        return mSelects.size();
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
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_select_contact, null);
            viewHolder.cb_pick = (CheckBox) convertView.findViewById(R.id.cb_pick);
            viewHolder.tv_pick_name = (TextView) convertView.findViewById(R.id.tv_pick_name);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SelectContactsInfo selectContactsInfo = mSelects.get(position);
        viewHolder.tv_pick_name.setText(selectContactsInfo.getUser().getName());
        viewHolder.cb_pick.setChecked(selectContactsInfo.isChecked());


        if (mExistMembers.contains(selectContactsInfo.getUser().getName()))
        {
            //
            viewHolder.cb_pick.setChecked(true);
            selectContactsInfo.setIsChecked(true);
        }

        return convertView;
    }

    class ViewHolder
    {
        CheckBox cb_pick;
        TextView tv_pick_name;
    }

    // 获取选中的联系人
    public List<String> getAddMembers()
    {
        // 准备一个要返回的数据集合
        List<String> names = new ArrayList<>();

        // 遍历集合 选择出选中状态的联系人
        for (SelectContactsInfo select : mSelects)
        {

            if (select.isChecked())
            {
                names.add(select.getUser().getName());
            }
        }

        return names;
    }
}
