package com.thirtydays.kotlin.ui.hook;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.elvishew.xlog.XLog;
import com.seabreeze.robot.base.hook.annotation.ClickLimit;
import com.seabreeze.robot.base.hook.annotation.TimeSpend;
import com.seabreeze.robot.base.ui.activity.SimpleActivity;
import com.thirtydays.kotlin.R;
import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;


/**
 * <pre>
 * author : 76515
 * time   : 2020/7/21
 * desc   :
 * </pre>
 */
@Xml(layouts = "activity_hook")
public class HookActivity extends SimpleActivity {
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @TimeSpend("测试时间")
    private void tvAgree() {

        XLog.e("click tvAgree");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void setLayout() {
        X2C.setContentView(this, R.layout.activity_hook);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initData() {
        findViewById(R.id.tvHook).setOnClickListener(new View.OnClickListener() {
            @Override
            @ClickLimit
            public void onClick(View view) {
                tvAgree();
            }
        });
    }
}
