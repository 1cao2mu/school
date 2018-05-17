package com.hhstu.cyy.school.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.CheckCodeButton;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.CustomRequestString;
import com.hhstu.cyy.school.tool.Utils;
import com.hhstu.cyy.school.tool.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 找回密码
 * Created by Administrator on 2015/12/14.
 */
public class FindPasswordActivity extends BaseActivity {
    private EditText et_phone, et_check_code, et_password;
    private CheckCodeButton bt_get_sms;
    private String checkCode;
    String phone_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("找回密码");
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_check_code = (EditText) findViewById(R.id.et_check_code);
        et_password = (EditText) findViewById(R.id.et_password);

        bt_get_sms = (CheckCodeButton) findViewById(R.id.bt_get_sms);
        bt_get_sms.setCheckView(et_phone);
        bt_get_sms.setOnSendCheckCodeButtonClick(new CheckCodeButton.OnCheckCodeButtonClick() {
            @Override
            public void onClick() {
                checkCode = Utils.getCheckCodeFour();
                et_check_code.setText(checkCode);
                // getSMS();

            }
        });
    }

    private void getSMS() {
        HashMap<String, String> params = new HashMap<>();
        String phone = et_phone.getText().toString();
        phone_check = phone;
        if (TextUtils.isEmpty(phone)) {
            Utils.showToast(getContext(), "请输入手机号");
            return;
        } else if (!Utils.isMobileNO(phone)) {
            Utils.showToast(getContext(), "请输入正确的手机号");
            return;
        }
        params.put("name", Constants.URL_SMS_ACOUNT);
        params.put("pwd", Constants.URL_SMS_PADDWORD);
        params.put("dst", phone);
        params.put("msg", checkCode);
        CustomRequestString request = new CustomRequestString(Request.Method.POST, Constants.URL_SMS, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonString) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (jsonString.contains("success")) {
                    Utils.sysout("传参成功");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Utils.showToast(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));
            }
        });
        dialog.show();
        MineApplication.getInstance().addToRequestQueue(request, TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                try {
                    register();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void register() throws JSONException {
        String user_check = et_check_code.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        String phone = et_phone.getText().toString();
        String passwrod = et_password.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Utils.showToast(getContext(), "请输入手机号");
            return;
        } else if (!Utils.isMobileNO(phone)) {
            Utils.showToast(getContext(), "请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(user_check)) {
            Utils.showToast(getContext(), "请输入验证码");
            return;
        } else if (!user_check.equalsIgnoreCase(checkCode)) {
            Utils.showToast(getContext(), "验证码不正确");
            return;
        }
//        else if (!phone.equalsIgnoreCase(phone_check)) {
//            Utils.showToast(getContext(), "注册手机号与发送验证码手机号不一致");
//            return;
//        }
        if (TextUtils.isEmpty(passwrod)) {
            Utils.showToast(getContext(), "请输入密码");
            return;
        } else if (passwrod.length() < 6) {
            Utils.showToast(getContext(), "密码至少为六位");
            return;
        }
        params.put("username", phone);
        params.put("password", passwrod);
        SQLiteDatabase db2 = helper.getReadableDatabase();
        Cursor cursor = db2.query("person", null, "tel=" + phone, null, null, null, null);
        if (cursor.getCount() == 0) {
            Utils.showToast(getContext(), "此手机号未注册");
        } else {
            ContentValues values2 = new ContentValues();
            values2.put("pwd", passwrod);
            /**
             * 参数1:表名
             * 参数2：要修改的值
             * 参数3：where 条件语句  _id = ?
             * 参数4：条件语句的值   new String []{"1"}
             */
            int num = db2.update("person", values2, "tel = ?", new String[]{phone});
            if (num > 0) {
                Utils.showToast(getContext(), "修改成功");
                finish();
            } else {
                Utils.showToast(getContext(), "修改失败");
            }
        }
        db2.close();
    }
}
