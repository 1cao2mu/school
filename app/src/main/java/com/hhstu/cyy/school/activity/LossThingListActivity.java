package com.hhstu.cyy.school.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.adapter.LossThingListAdapter;
import com.hhstu.cyy.school.adapter.LossThingListMyAdapter;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;
import com.hhstu.cyy.school.recycleviewdivider.HorizontalDividerItemDecoration;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 我的车辆
 * Created by Administrator on 2015/11/25.
 */
public class LossThingListActivity extends BaseActivity implements View.OnClickListener, OnItemChildViewClickListener {
    private RecyclerView rv_list;
    private LossThingListAdapter adapter;
    SwipeRefreshLayout srl_refresh;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss_thing_list);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_action).setOnClickListener(this);
        findViewById(R.id.iv_action).setVisibility(View.GONE);
        findViewById(R.id.tv_action).setVisibility(View.VISIBLE);
        TextView tv_action = (TextView) findViewById(R.id.tv_action);
        tv_action.setText("我的");
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("失物列表");
        initView();
    }

    private void initView() {
        srl_refresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        srl_refresh.setColorSchemeResources(R.color.theme);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                REFRESHABLE = true;
                initData();
            }
        });
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        //  rv_list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && LAST_VISIABLE_IETM_INDEX == adapter.getItemCount() - 1 && REFRESHABLE && LAST_VISIABLE_IETM_INDEX + 1 >= Constants.PAGE_SIZE) {
                        page++;
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LAST_VISIABLE_IETM_INDEX = layoutManager.findLastVisibleItemPosition();
            }
        });
        srl_refresh.setRefreshing(true);
        initData();
    }

    private void initData() {
        try {
            if (srl_refresh.isRefreshing()) {
                srl_refresh.setRefreshing(false);
            }
            if (adapter != null) {
                if (page == 1) {
                    adapter.refresh(Data.getLossThingData(helper, page));
                } else {
                    adapter.append(Data.getLossThingData(helper, page));
                }
            } else {
                adapter = new LossThingListAdapter(Data.getLossThingData(helper, page), getContext());
                adapter.setOnItemChildViewClickListener(LossThingListActivity.this);
                rv_list.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_action:
                Intent intent = new Intent(getContext(), LossThingListMyActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemChildViewClickListener(int id, final int position) {
        try {
            Intent intent;
            JSONObject object = adapter.getItem(position);
            switch (id) {
                case -1:
                    break;
                case R.id.iv_et_msg:
                    intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + object.getString("tel")));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
