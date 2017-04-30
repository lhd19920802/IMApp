package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.modle.Modle;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

public class GroupCreateActivity extends Activity implements View.OnClickListener
{

    private EditText etNewgroupName;
    private EditText etNewgroupDesc;
    private CheckBox cbNewgroupPublic;
    private CheckBox cbNewgroupInvite;
    private Button btNewgroupCreate;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-27 19:05:53 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        etNewgroupName = (EditText) findViewById(R.id.et_newgroup_name);
        etNewgroupDesc = (EditText) findViewById(R.id.et_newgroup_desc);
        cbNewgroupPublic = (CheckBox) findViewById(R.id.cb_newgroup_public);
        cbNewgroupInvite = (CheckBox) findViewById(R.id.cb_newgroup_invite);
        btNewgroupCreate = (Button) findViewById(R.id.bt_newgroup_create);

        btNewgroupCreate.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-27 19:05:53 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v)
    {
        if (v == btNewgroupCreate)
        {
            // Handle clicks for btNewgroupCreate
            //跳转到选择联系人页面
            Intent intent = new Intent(this, SelectContactsActivity.class);
            startActivityForResult(intent, 1);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        findViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {

            createGroup(data.getStringArrayExtra("members"));
        }
    }

    // 创建群
    private void createGroup(final String[] members)
    {
        // 获取群名称
        final String groupName = etNewgroupName.getText().toString();
        // 获取群的描述信息
        final String groupDesc = etNewgroupDesc.getText().toString();

        // 联网
        Modle.getInstance().getExecutors().execute(new Runnable()
        {
            @Override
            public void run()
            {
                String reason = "申请加入群";
                EMGroupManager.EMGroupStyle groupStyle = null;

                // 群公开
                if (cbNewgroupPublic.isChecked())
                {
                    if (cbNewgroupInvite.isChecked())
                    {// 群邀请公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }
                    else
                    {// 群邀请不公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }
                else
                {// 群不公开
                    if (cbNewgroupInvite.isChecked())
                    {// 群邀请公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }
                    else
                    {// 群邀请不公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                // 群参数设置
                EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
                // 群最多多少人
                options.maxUsers = 200;
                // 创建群的类型
                options.style = groupStyle;

                try
                {
                    // 联网创建群
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc,
                            members, reason, options);


                    // 更新ui
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(GroupCreateActivity.this, "创建群：" + groupName + "成功",
                                    Toast.LENGTH_SHORT).show();
                            // 结束当前页面
                            finish();
                        }
                    });

                }
                catch (HyphenateException e)
                {
                    e.printStackTrace();

                    // 更新ui
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(GroupCreateActivity.this, "创建群：" + groupName + "失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
