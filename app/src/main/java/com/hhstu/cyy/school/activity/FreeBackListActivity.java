package com.hhstu.cyy.school.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.adapter.FreeBackListAdapter;
import com.hhstu.cyy.school.recycleviewdivider.HorizontalDividerItemDecoration;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;

/**
 * 意见反馈
 * Created by Administrator on 2015/11/13.
 */
public class FreeBackListActivity extends BaseActivity {
    private static final int REQUEST_SEND_FREEBACK = 1;
    RecyclerView rv_list;
    FreeBackListAdapter adapter;
    SwipeRefreshLayout srl_refresh;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeback_list);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_action).setVisibility(View.INVISIBLE);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("意见反馈");
        initView();
    }

    private void initView() {
        findViewById(R.id.view_pushFreeback).setOnClickListener(this);
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
        rv_list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
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
                    adapter.refresh(Data.getMyHelpData(helper, page, Utils.getLoginId(getContext())));
                } else {
                    adapter.append(Data.getMyHelpData(helper, page, Utils.getLoginId(getContext())));
                }
            } else {
                adapter = new FreeBackListAdapter(Data.getMyHelpData(helper, page, Utils.getLoginId(getContext())), getContext());
                adapter.setOnItemChildViewClickListener(FreeBackListActivity.this);
                rv_list.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.view_pushFreeback:
                Intent intent = new Intent(this, FreeBackActivity.class);
                startActivityForResult(intent, REQUEST_SEND_FREEBACK);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        REFRESHABLE = true;
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
