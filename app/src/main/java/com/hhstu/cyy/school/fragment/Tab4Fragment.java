package com.hhstu.cyy.school.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.activity.LoginActivity;
import com.hhstu.cyy.school.activity.LossThingListActivity;
import com.hhstu.cyy.school.activity.MainActivity;
import com.hhstu.cyy.school.activity.UserInfoActivity;
import com.hhstu.cyy.school.adapter.Tab4Adapter;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;

import java.io.File;


/**
 * Created by Administrator on 15-11-12.
 */
public class Tab4Fragment extends Fragment implements View.OnClickListener, OnItemChildViewClickListener {

    View view;
    RecyclerView rv_list;
    LinearLayoutManager linearLayoutManager;
    Tab4Adapter adapter;

    TextView tv_login;
    RoundImageView iv_icon;
    FrameLayout view_login;
    Fragment mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        mContext = this;
        view = inflater.inflate(R.layout.fragment_tab_4, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText("个人中心");
        view.findViewById(R.id.iv_action).setOnClickListener(this);
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
        view_login = (FrameLayout) view.findViewById(R.id.view_login);
        view_login.setOnClickListener(this);
        linearLayoutManager = new GridLayoutManager(getContext(), 4);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(linearLayoutManager);
        initData();
        return view;
    }

    private void initData() {
        adapter = new Tab4Adapter(Data.getData(2), getContext());
        adapter.setOnItemChildViewClickListener(Tab4Fragment.this);
        rv_list.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoginData();
    }

    public void initLoginData() {
        if (Utils.isLogin(getContext())) {
            if (Utils.isEmpty(Utils.getLoginName(getContext()))) {
                tv_login.setText("未命名");
            } else {
                tv_login.setText(Utils.getLoginName(getContext()));
            }
            String protraitPath = Utils.getLoginImage(getContext());
            if (!Utils.isEmpty(protraitPath)) {
                if (!StringUtils.isEmpty(protraitPath) && new File(protraitPath).exists()) {
                    Bitmap protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
                    iv_icon.setImageBitmap(protraitBitmap);
                } else {
                    Utils.showToast(getContext(), "图像不存在，可能已被删除");
                }
            } else {
                iv_icon.setImageResource(R.mipmap.ic_launcher);
            }
        } else {
            tv_login.setText("登录/注册");
            iv_icon.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.sysout("tab4" + "---" + resultCode + "---" + requestCode);
        initLoginData();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.view_login:
                if (Utils.isLogin(getContext())) {
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.iv_action:
                ((MainActivity) getActivity()).open();
                break;
        }
    }

    @Override
    public void onItemChildViewClickListener(int id, int position) {
        Intent intent;
        if (Utils.isLogin(getContext())) {
            switch (position) {
                //失物招领
                case 0:
                    intent = new Intent(getContext(), LossThingListActivity.class);
                    startActivity(intent);
                    break;
                //美女
                case 1:

                    break;

            }
        } else {
            intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }
}