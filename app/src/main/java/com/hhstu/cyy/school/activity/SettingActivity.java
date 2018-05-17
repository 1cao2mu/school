package com.hhstu.cyy.school.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Utils;


import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/11/26.
 */
public class SettingActivity extends BaseActivity {
    TextView tv_ban_ben, tv_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("设置");
        tv_ban_ben = (TextView) findViewById(R.id.tv_ban_ben);
        tv_cache = (TextView) findViewById(R.id.tv_cache);
        initView();
    }

    private void initView() {
        findViewById(R.id.view_clearcache).setOnClickListener(this);
        findViewById(R.id.view_updateversion).setOnClickListener(this);
        tv_ban_ben.setText("V " + Utils.getAppVersionName(getContext()));
        double i = (Utils.getDirSize(getCacheDir()));
        Utils.sysout(i);
        tv_cache.setText(new DecimalFormat("#0.00").format(i)+"MB");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.view_clearcache:
                clearCache();
                break;
            case R.id.view_updateversion:
                new AlertDialog.Builder(getContext()).setTitle("提示")
                        .setMessage("已是最新版本")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                            }
                        }).show();
                updateVersion();
                break;
        }
    }

    private void clearCache() {
        Utils.deleteDir(getCacheDir());
        if (!Utils.isEmpty(Utils.getSDPath())) {
            Utils.deleteDir(new File(Utils.getIMGPath()));
        }
        tv_cache.setText("0MB");
        Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show();
    }

    private void updateVersion() {

    }
}
