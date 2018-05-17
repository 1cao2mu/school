package com.hhstu.cyy.school.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.LoadingDialog;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;
import com.hhstu.cyy.school.sqlitedata.MySqliteHelper;
import com.hhstu.cyy.school.tool.SystemBarTintManager;


/**
 * Created by Administrator on 2015/11/26.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener, OnItemChildViewClickListener {
    public static String TAG;
    boolean REFRESHABLE = true;
    int page = 1, LAST_VISIABLE_IETM_INDEX;
    LoadingDialog dialog;
    public MySqliteHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        helper = new MySqliteHelper(getContext());
        TAG = this.getClass().getName();
        dialog = new LoadingDialog(getContext());
        MineApplication.activities.add(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorAccent);//通知栏所需颜色
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(R.color.colorAccent);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MineApplication.activities.remove(this);
        MineApplication.getInstance().cancelPendingRequests(TAG);
    }

    Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//        }
    }

    @Override
    public void onItemChildViewClickListener(int id, int position) {
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // TODO Auto-generated method stub
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View view = getCurrentFocus();
//            if (isHideInput(view, ev)) {
//                HideSoftInput(view.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    // 判定是否需要隐藏
//    private boolean isHideInput(View v, MotionEvent ev) {
//        if (v != null && (v instanceof EditText)) {
//            int[] l = {0, 0};
//            v.getLocationInWindow(l);
//            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
//                    + v.getWidth();
//            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
//                    && ev.getY() < bottom) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // 隐藏软键盘
//    private void HideSoftInput(IBinder token) {
//        if (token != null) {
//            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            manager.hideSoftInputFromWindow(token,
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
}
