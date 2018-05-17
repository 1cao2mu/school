package com.hhstu.cyy.school.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.NoScrollLinearLayoutManager;
import com.hhstu.cyy.school.activity.LoginActivity;
import com.hhstu.cyy.school.activity.addFindTab1Activity;
import com.hhstu.cyy.school.adapter.Tab1Adapter;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;
import com.hhstu.cyy.school.sqlitedata.MySqliteHelper;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.Utils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.IconHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 15-11-12.
 */
public class TabViewPagerFragment extends Fragment implements View.OnClickListener, OnItemChildViewClickListener {
    RecyclerView rv_list;
    View view;
   // SwipeRefreshLayout srl_refresh;
    NoScrollLinearLayoutManager linearLayoutManager;
    Tab1Adapter adapter;
    TestLoopAdapter adapter2;
    boolean REFRESHABLE = true;
    int page = 1, LAST_VISIABLE_IETM_INDEX, FIRST_VISIABLE_IETM_INDEX;
    RollPagerView mRollViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_tab_viewpager, null);
        mRollViewPager = (RollPagerView) view.findViewById(R.id.roll_view_pager);
        //srl_refresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        ViewGroup.LayoutParams paramsTop = mRollViewPager.getLayoutParams();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        paramsTop.width = display.getWidth();
        paramsTop.height = (int) (display.getWidth() / 2);//按照3:1的比例
        mRollViewPager.setLayoutParams(paramsTop);

        mRollViewPager.setPlayDelay(3000);
        mRollViewPager.setAnimationDurtion(1000);
        // mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));

        mRollViewPager.setHintView(new IconHintView(getContext(), R.mipmap.icon_dot_normal_blue, R.mipmap.icon_dot_blue));


        ((TextView) view.findViewById(R.id.tv_title)).setText("发现");
        view.findViewById(R.id.iv_action).setOnClickListener(this);

//        srl_refresh.setColorSchemeResources(R.color.theme);
//        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                REFRESHABLE = true;
//                page = 1;
//                initData();
//            }
//        });
        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        linearLayoutManager = new NoScrollLinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        rv_list.setLayoutManager(linearLayoutManager);
//        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                if (adapter != null) {
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE && LAST_VISIABLE_IETM_INDEX == adapter.getItemCount() - 1 && REFRESHABLE && LAST_VISIABLE_IETM_INDEX + 1 >= Constants.PAGE_SIZE) {
//                        page++;
//                        initData();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LAST_VISIABLE_IETM_INDEX = linearLayoutManager.findLastVisibleItemPosition();
//            }
//        });
      //  srl_refresh.setRefreshing(true);
        initData();
        return view;
    }

    private void initData() {
        try {
            if (adapter != null) {
                if (page == 1) {
                    adapter.refresh(Data.getFindTabData(new MySqliteHelper(getContext()), page));
                } else {
                    adapter.append(Data.getFindTabData(new MySqliteHelper(getContext()), page));
                }
            } else {
                adapter = new Tab1Adapter(Data.getFindTabData(new MySqliteHelper(getContext()), page), getContext());
                adapter.setOnItemChildViewClickListener(TabViewPagerFragment.this);
                rv_list.setAdapter(adapter);
            }
            if (adapter2 != null) {
                adapter2.setData(Data.getData(5));
                adapter2.notifyDataSetChanged();
            } else {
                adapter2 = new TestLoopAdapter(mRollViewPager);
                adapter2.setData(Data.getData(5));
                mRollViewPager.setAdapter(adapter2);
            }
//            if (srl_refresh.isRefreshing()) {
//                srl_refresh.setRefreshing(false);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        page = 1;
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        page = 1;
        initData();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (Utils.isLogin(getContext())) {
            switch (v.getId()) {
                case R.id.iv_action:
                    intent = new Intent(getContext(), addFindTab1Activity.class);
                    startActivityForResult(intent, 0);
                    break;
            }
        } else {
            intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onItemChildViewClickListener(int id, int position) {
        try {
            JSONObject object = adapter.getItem(position);
            Intent intent;
            if (Utils.isLogin(getContext())) {
                switch (id) {
                    case R.id.tv_action:

                        break;
                }
            } else {
                intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class TestLoopAdapter extends LoopPagerAdapter {
        JSONArray array;

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        public void setData(JSONArray array) {
            this.array = array;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            NetworkImageView view = new NetworkImageView(container.getContext());
            try {
                view.setImageUrl(array.getJSONObject(position).getString("url"), MineApplication.getInstance().getImageLoader());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return array.length();
        }

    }


}