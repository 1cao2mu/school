package com.hhstu.cyy.school.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Constants;


/**
 * 反馈和帮助
 * Created by Administrator on 2015/11/26.
 */
public class FreebackAndHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freebackandhelp);
        findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("反馈帮助");
        initView();
    }

    private void initView() {
        findViewById(R.id.view_call).setOnClickListener(this);
        findViewById(R.id.view_freeback).setOnClickListener(this);
        findViewById(R.id.view_question).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.view_call:
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + Constants.TEL));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                break;
            case R.id.view_freeback:
                intent = new Intent(getContext(), FreeBackListActivity.class);
                startActivity(intent);
                break;
            case R.id.view_question:
                intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", Constants.URL_WAP_FAQ);
                startActivity(intent);
                break;
        }
    }
}
