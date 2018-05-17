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

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 基本模板
 * Created by Administrator on 2015/12/14.
 */
public class ResetPasswordActivity extends BaseActivity {
    private EditText et_old_password, et_new_password_first, et_new_password_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("重置密码");
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password_first = (EditText) findViewById(R.id.et_new_password_first);
        et_new_password_second = (EditText) findViewById(R.id.et_new_password_second);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(11);
                finish();
                break;
            case R.id.btn_commit:
                try {
                    resetPassword();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(11);
        finish();
    }

    private void resetPassword() throws JSONException {
        String old_password = et_old_password.getText().toString();
        String new_password_first = et_new_password_first.getText().toString();
        String new_password_second = et_new_password_second.getText().toString();

        if (TextUtils.isEmpty(old_password)) {
            Utils.showToast(getApplicationContext(), "请输入您的旧密码");
            return;
        } else if (old_password.length() < 6) {
            Utils.showToast(getApplicationContext(), "密码至少为六位");
            return;
        }
        if (TextUtils.isEmpty(new_password_first) || TextUtils.isEmpty(new_password_second)) {
            Utils.showToast(getApplicationContext(), "请输入您的新密码");
            return;
        } else if (new_password_first.length() < 6 || new_password_second.length() < 6) {
            Utils.showToast(getApplicationContext(), "密码至少为六位");
            return;
        }
        if (!new_password_first.equalsIgnoreCase(new_password_second)) {
            Utils.showToast(getContext(), "两次输入密码不一致");
            return;
        }
        SQLiteDatabase db2 = helper.getReadableDatabase();
        Cursor cursor = db2.query("person", null, "_id=" + Utils.getLoginId(getContext()), null, null, null, null);
        if (cursor.getCount() == 0) {
            Utils.showToast(getContext(), "此手机号未注册");
        } else {
            while (cursor.moveToNext()) {
//				int id = cursor.getInt(0);
//				String name = cursor.getString(1);
//				int age = cursor.getInt(2);
                //根据字段名称  得到下标
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                String tel = cursor.getString(cursor.getColumnIndex("tel"));
                if (pwd.equalsIgnoreCase(old_password)) {
                    ContentValues values2 = new ContentValues();
                    values2.put("pwd", new_password_first);
                    /**
                     * 参数1:表名
                     * 参数2：要修改的值
                     * 参数3：where 条件语句  _id = ?
                     * 参数4：条件语句的值   new String []{"1"}
                     */
                    int num = db2.update("person", values2, "_id = ?", new String[]{String.valueOf(id)});
                    if (num > 0) {
                        Utils.showToast(getContext(), "重置成功");
                        JSONObject object = new JSONObject(getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).getString(Constants.SP_KEU_USER_LOGIN_INFO, null));
                        object.put("pwd", new_password_first);
                        SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).edit();
                        editor.putString(Constants.SP_KEU_USER_LOGIN_INFO, object.toString());
                        editor.commit();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Utils.showToast(getContext(), "重置失败");
                    }
                }
            }
        }
        db2.close();
    }
}
