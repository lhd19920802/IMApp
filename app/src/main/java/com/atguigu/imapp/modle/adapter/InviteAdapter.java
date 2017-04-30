package com.atguigu.imapp.modle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.InvitationInfo;
import com.atguigu.imapp.controller.domain.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/3/26 9:17.
 * 作用：
 */
public class InviteAdapter extends BaseAdapter
{
    private Context mContext;
    private List<InvitationInfo> mInvitationInfos;
    private OnInviteListener onInviteListener;

    public InviteAdapter(Context context, OnInviteListener onInviteListener)
    {
        mContext = context;
        mInvitationInfos = new ArrayList<>();
        this.onInviteListener = onInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvitationInfo> invitationInfos)
    {
        if (invitationInfos != null && invitationInfos.size() >= 0)
        {
            //添加前清除数据
            mInvitationInfos.clear();
            mInvitationInfos.addAll(invitationInfos);

            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount()
    {
        return mInvitationInfos.size();
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
            convertView = View.inflate(mContext, R.layout.item_invite, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_invite_name);
            viewHolder.reason = (TextView) convertView.findViewById(R.id.tv_invite_reason);
            viewHolder.accept = (Button) convertView.findViewById(R.id.bt_invite_accept);
            viewHolder.reject = (Button) convertView.findViewById(R.id.bt_invite_reject);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final InvitationInfo invitationInfo = mInvitationInfos.get(position);
        UserInfo user = invitationInfo.getUser();


        if (user != null)
        {
            //联系人邀请信息


            //名称显示
            viewHolder.name.setText(invitationInfo.getUser().getName());
            viewHolder.accept.setVisibility(View.GONE);
            viewHolder.reject.setVisibility(View.GONE);
            if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE)
            {
                //新的邀请
                if (invitationInfo.getReason() == null)
                {
                    viewHolder.reason.setText("新的邀请");
                }
                else
                {
                    viewHolder.reason.setText(invitationInfo.getReason());
                }

                viewHolder.accept.setVisibility(View.VISIBLE);
                viewHolder.reject.setVisibility(View.VISIBLE);
            }
            else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT)
            {
                //接受邀请
                if (invitationInfo.getReason() == null)
                {
                    viewHolder.reason.setText("接受邀请");
                }
                else
                {
                    viewHolder.reason.setText(invitationInfo.getReason());
                }

            }
            else if (invitationInfo.getStatus() == InvitationInfo.InvitationStatus
                    .INVITE_ACCEPT_BY_PEER)
            {
                //邀请被接受
                if (invitationInfo.getReason() == null)
                {
                    viewHolder.reason.setText("邀请被接受");
                }
                else
                {
                    viewHolder.reason.setText(invitationInfo.getReason());
                }
            }

            //接受和拒绝按钮的点击事件
            viewHolder.accept.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (onInviteListener != null)
                    {
                        onInviteListener.accept(invitationInfo);
                    }
                }
            });
            viewHolder.reject.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (onInviteListener != null)
                    {
                        onInviteListener.reject(invitationInfo);
                    }
                }
            });

        }
        else
        {// 群组
            // 显示名称
            viewHolder.name.setText(invitationInfo.getGroup().getInvitePerson());

            viewHolder.accept.setVisibility(View.GONE);
            viewHolder.reject.setVisibility(View.GONE);

            // 显示原因
            switch (invitationInfo.getStatus())
            {
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    viewHolder.reason.setText("您的群申请请已经被接受");
                    break;
                //  您的群邀请已经被接收
                case GROUP_INVITE_ACCEPTED:
                    viewHolder.reason.setText("您的群邀请已经被接收");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    viewHolder.reason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
                    viewHolder.reason.setText("您的群邀请已经被拒绝");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITE:
                    viewHolder.accept.setVisibility(View.VISIBLE);
                    viewHolder.reject.setVisibility(View.VISIBLE);

                    // 接受邀请
                    viewHolder.accept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onInviteListener.onInviteAccept(invitationInfo);
                        }
                    });

                    // 拒绝邀请
                    viewHolder.reject.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onInviteListener.onInviteReject(invitationInfo);
                        }
                    });

                    viewHolder.reason.setText("您收到了群邀请");
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    viewHolder.accept.setVisibility(View.VISIBLE);
                    viewHolder.reject.setVisibility(View.VISIBLE);

                    // 接受申请
                    viewHolder.accept.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onInviteListener.onApplicationAccept(invitationInfo);
                        }
                    });

                    // 拒绝申请
                    viewHolder.reject.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            onInviteListener.onApplicationReject(invitationInfo);
                        }
                    });

                    viewHolder.reason.setText("您收到了群申请");
                    break;

                // 你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    viewHolder.reason.setText("你接受了群邀请");
                    break;

                // 您批准了群申请
                case GROUP_ACCEPT_APPLICATION:
                    viewHolder.reason.setText("您批准了群申请");
                    break;

                // 您拒绝了群邀请
                case GROUP_REJECT_INVITE:
                    viewHolder.reason.setText("您拒绝了群邀请");
                    break;

                // 您拒绝了群申请
                case GROUP_REJECT_APPLICATION:
                    viewHolder.reason.setText("您拒绝了群申请");
                    break;
            }
        }
        return convertView;
    }

    class ViewHolder
    {
        TextView name;
        TextView reason;
        Button accept;
        Button reject;
    }

    public interface OnInviteListener
    {
        //联系人接受按钮的点击事件
        void accept(InvitationInfo invitationInfo);

        //联系人拒绝按钮的点击事件
        void reject(InvitationInfo invitationInfo);

        // 接受邀请按钮处理
        void onInviteAccept(InvitationInfo invationInfo);

        // 拒绝邀请按钮处理
        void onInviteReject(InvitationInfo invationInfo);

        // 接受申请按钮处理
        void onApplicationAccept(InvitationInfo invationInfo);

        // 拒绝申请按钮处理
        void onApplicationReject(InvitationInfo invationInfo);
    }


}
