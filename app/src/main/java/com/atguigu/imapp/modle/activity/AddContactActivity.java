package com.atguigu.imapp.modle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.imapp.R;
import com.atguigu.imapp.controller.domain.UserInfo;
import com.atguigu.imapp.modle.Modle;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * 添加联系人页面
 */
public class AddContactActivity extends Activity implements View.OnClickListener
{

    private TextView tvAddFind;
    private EditText etAddName;
    private RelativeLayout rlAdd;
    private ImageView ivAddPhoto;
    private TextView tvAddName;
    private Button btAddAdd;
    private UserInfo userInfo;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-23 20:00:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews()
    {
        tvAddFind = (TextView) findViewById(R.id.tv_add_find);
        etAddName = (EditText) findViewById(R.id.et_add_name);
        rlAdd = (RelativeLayout) findViewById(R.id.rl_add);
        ivAddPhoto = (ImageView) findViewById(R.id.iv_add_photo);
        tvAddName = (TextView) findViewById(R.id.tv_add_name);
        btAddAdd = (Button) findViewById(R.id.bt_add_add);

        tvAddFind.setOnClickListener(this);
        btAddAdd.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-23 20:00:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v)
    {
        if (v == btAddAdd)
        {
            // Handle clicks for btAddAdd
            add();
        }
        else if(v==tvAddFind) {
            find();
        }
    }

    //添加好友
    private void add()
    {
        //通过环信服务器发送邀请
        Modle.getInstance().getExecutors().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName(),
                                "想添加你为好友");
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(AddContactActivity.this, "发送添加好友信息成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catch (HyphenateException e)
                {
                    e.printStackTrace();
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(AddContactActivity.this, "发送添加好友信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    //查找好友
    private void find()
    {
        //从环信服务器查找
        Modle.getInstance().getExecutors().execute(new Runnable()
        {
            @Override
            public void run()
            {
                String name = etAddName.getText().toString();
                if(TextUtils.isEmpty(name)) {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(AddContactActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                userInfo = new UserInfo(name);
                //更新UI显示
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        rlAdd.setVisibility(View.VISIBLE);
                        tvAddName.setText(userInfo.getName());
                    }
                });

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        findViews();


    }
}
