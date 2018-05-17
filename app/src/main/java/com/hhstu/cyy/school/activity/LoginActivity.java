package com.hhstu.cyy.school.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 登录
 * Created by Administrator on 2015/12/14.
 */
public class LoginActivity extends BaseActivity {
   // private CheckBox cb_password;
    private EditText et_phone, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_action).setOnClickListener(this);
     //   cb_password = (CheckBox) findViewById(R.id.cb_password);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("登录");
        ((TextView) findViewById(R.id.tv_action)).setText("注册");
        check();
    }

    private void check() {
//        if (Utils.isAutoLogin(getContext()) && !Utils.isEmpty(getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).getString(Constants.SP_KEY_COUNT, ""))) {
//            et_phone.setText(getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).getString(Constants.SP_KEY_COUNT, ""));
//            et_password.setText(getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).getString(Constants.SP_KEY_PASSWORD, ""));
//            cb_password.setChecked(true);
//        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tv_action://注册
                intent = new Intent(getContext(), RegisterActivity.class);
                startActivityForResult(intent, 10);
                break;
            case R.id.btn_commit://登录
                try {
                    login();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_forget_password:  //忘记密码
                intent = new Intent(getContext(), FindPasswordActivity.class);
                startActivityForResult(intent, 11);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void login() throws JSONException {
        final String phone = et_phone.getText().toString();
        final String passwrod = et_password.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Utils.showToast(getContext(), "请输入手机号");
            return;
        } else if (!Utils.isMobileNO(phone)) {
            Utils.showToast(getContext(), "请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(passwrod)) {
            Utils.showToast(getContext(), "请输入密码");
            return;
        } else if (passwrod.length() < 6) {
            Utils.showToast(getContext(), "密码至少为六位");
            return;
        }
        SQLiteDatabase db2 = helper.getReadableDatabase();
        /**
         * 参数1：表名
         * 参数2：字段数组
         * 参数3：查询的条件  _id = ?
         * 参数4：查询条件的值  new String{"1"}
         * 参数5：分组字段
         * 参数6：在where条件后再次筛选
         * 参数7：查询排序
         *
         */
        Cursor cursor = db2.query("person", null, "tel=" + phone, null, null, null, null);
        if (cursor.getCount()==0){
            Utils.showToast(getContext(), "登录失败,用户名或密码不正确");
        }
        while (cursor.moveToNext()) {
//				int id = cursor.getInt(0);
//				String name = cursor.getString(1);
//				int age = cursor.getInt(2);
            //根据字段名称  得到下标
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String tel = cursor.getString(cursor.getColumnIndex("tel"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            System.out.println("==id=:" + id + "==tel=:" + tel + "==pwd=:" + pwd);
            if (phone.equalsIgnoreCase(tel) && passwrod.equalsIgnoreCase(pwd)) {
                Utils.showToast(getContext(), "登录成功");
                JSONObject object =Utils.CursorToObject(cursor);
                SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).edit();
                editor.putInt(Constants.SP_KEY_OLD_VERSION, Utils.getAppVersionCode(getContext()));
                editor.putBoolean(Constants.SP_KEY_ISLOGIN, true);
                editor.putString(Constants.SP_KEU_USER_LOGIN_INFO, object.toString());
                editor.commit();
                setResult(RESULT_OK);
                finish();
            }else{
                Utils.showToast(getContext(), "密码有误");
            }
        }
        db2.close();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
