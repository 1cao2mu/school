package com.hhstu.cyy.school.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
 * 注册
 * Created by Administrator on 2015/12/14.
 */
public class RegisterActivity extends BaseActivity {
    private EditText et_phone, et_check_code, et_password;
    private CheckCodeButton bt_get_sms;
    private String checkCode;
    ProgressDialog pb_dialog;
    String phone_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("注册");
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
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.btn_commit:
                    register();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

        SQLiteDatabase db1 = helper.getReadableDatabase();
        Cursor cursor1 = db1.query("person", null, null, null, null, null, null);
        while (cursor1.moveToNext()) {
            //根据字段名称  得到下标
            int p = cursor1.getInt(cursor1.getColumnIndex("_id"));
            String tel = cursor1.getString(cursor1.getColumnIndex("tel"));
            String pwd = cursor1.getString(cursor1.getColumnIndex("pwd"));
            if (tel.equalsIgnoreCase(phone)) {
                Utils.showToast(getContext(), "此号已注册");
                return;
            }
        }

        ContentValues values = new ContentValues();
        values.put("tel", phone);
        values.put("pwd", passwrod);
        long i = db1.insert("person", null, values);
        if (i == -1) {
            //失败
            Utils.showToast(getContext(), "注册失败");
        } else {
            //成功
            Utils.showToast(getContext(), "注册成功");
            Cursor cursor = db1.query("person", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                //根据字段名称  得到下标
                int p = cursor.getInt(cursor.getColumnIndex("_id"));
                String tel = cursor.getString(cursor.getColumnIndex("tel"));
                String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                if (tel.equalsIgnoreCase(phone)) {
                    JSONObject object =  Utils.CursorToObject(cursor);
                    SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).edit();
                    editor.putInt(Constants.SP_KEY_OLD_VERSION, Utils.getAppVersionCode(getContext()));
                    editor.putBoolean(Constants.SP_KEY_ISLOGIN, true);
                    editor.putString(Constants.SP_KEU_USER_LOGIN_INFO, object.toString());
                    editor.commit();
                    setResult(RESULT_OK);
                    finish();
                }
            }

        }
        /**
         * 参数1：表名  参数2 ：如果添加的数据是null   null  参数3：添加的数据
         */
        db1.close();
    }
}
