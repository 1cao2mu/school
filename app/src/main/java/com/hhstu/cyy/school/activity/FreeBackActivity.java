package com.hhstu.cyy.school.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;

import java.text.SimpleDateFormat;

/**
 * 意见反馈
 * Created by Administrator on 2015/11/13.
 */
public class FreeBackActivity extends BaseActivity {
    EditText et_freeback;
    Button btn_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeback);
        findViewById(R.id.iv_back).setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("意见反馈");
        initView();
    }

    private void initView() {
        btn_commit = (Button) findViewById(R.id.btn_commit);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        et_freeback = (EditText) findViewById(R.id.et_freeback);

        et_freeback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmpty(et_freeback.getText().toString())) {
                    btn_commit.setBackgroundResource(R.drawable.shape_radius_10_soild_gray);
                } else {
                    btn_commit.setBackgroundResource(R.drawable.shape_radius_10_soild_red);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                try {
                    sendFreeBack();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 提价评价
     */
    private void sendFreeBack() throws JSONException {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        if (Data.addHelp(helper, Utils.getLoginId(getContext()), date, et_freeback.getText().toString())) {
            Utils.showToast(getContext(), "反馈成功,请耐心等待");
            finish();
        } else {
            Utils.showToast(getContext(), "反馈失败,请重试");
        }
    }
}
