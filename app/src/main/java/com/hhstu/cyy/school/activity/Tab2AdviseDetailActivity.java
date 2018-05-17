package com.hhstu.cyy.school.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.adapter.PingLunListAdapter;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Data;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Tab2AdviseDetailActivity extends BaseActivity {
    private RecyclerView rv_list;
    private PingLunListAdapter adapter;
    LinearLayoutManager layoutManager;
    EditText et_content;
    Button btn_commit;
    Intent inintent;
    JSONObject object;

    RoundImageView iv_icon;
    TextView tv_name, tv_content, tv_ding_count, tv_cai_count, tv_comment_count;
    ImageView nv_content;
    ImageView iv_ding, iv_cai, iv_comment;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1_find_detaill);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_commit).setOnClickListener(this);
        context = getContext();
        iv_icon = (RoundImageView) findViewById(R.id.iv_icon);
        iv_icon.setImageResource(R.mipmap.ic_launcher);
        nv_content = (ImageView) findViewById(R.id.nv_content);
        nv_content.setImageResource(R.mipmap.ic_launcher);
        nv_content.setMinimumHeight(Utils.getScreenWidth(context) - 20);
        nv_content.setMinimumWidth(Utils.getScreenWidth(context) - 20);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_ding_count = (TextView) findViewById(R.id.tv_ding_count);
        tv_cai_count = (TextView) findViewById(R.id.tv_cai_count);
        tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
        iv_ding = (ImageView) findViewById(R.id.iv_ding);
        iv_cai = (ImageView) findViewById(R.id.iv_cai);
        iv_comment = (ImageView) findViewById(R.id.iv_comment);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_content.getText().toString().length() > 0) {
                    btn_commit.setBackgroundResource(R.drawable.shape_radius_3_soild_red);
                } else {
                    btn_commit.setBackgroundResource(R.drawable.shape_radius_3_soild_gray);
                }
            }
        });
        try {
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws JSONException {
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
        inintent = getIntent();
        object = new JSONObject(inintent.getStringExtra("data"));
        if (object.has("image")) {
            nv_content.setVisibility(View.VISIBLE);
            Bitmap b = getBitmap(object.getString("image"));
            if (b == null) {
                nv_content.setImageResource(R.mipmap.ic_launcher);
            } else {
                nv_content.setImageBitmap(b);
            }
        } else {
            nv_content.setVisibility(View.GONE);
        }
        if (object.has("uimage")) {
            Bitmap b = getBitmap(object.getString("uimage"));
            if (b == null) {
                iv_icon.setImageResource(R.mipmap.ic_launcher);
            } else {
                iv_icon.setImageBitmap(b);
            }
        } else {
            iv_icon.setImageResource(R.mipmap.ic_launcher);
        }
        tv_name.setText(object.has("uname") ? object.getString("uname") : "无名");
        tv_content.setText(object.has("content") ? object.getString("content") : "");
        tv_ding_count.setText(object.has("topCount") ? String.valueOf(object.getInt("topCount")) : "0");
        tv_cai_count.setText(object.has("bottomCount") ? String.valueOf(object.getInt("bottomCount")) : "0");
        tv_comment_count.setText(object.has("commitCount") ? String.valueOf(object.getInt("commitCount")) : "0");
        if (object.has("topId") && Utils.isLogin(context)) {
            iv_ding.setImageResource(object.getString("topId").contains("[" + Utils.getLoginId(context) + "]") ? R.mipmap.ic_ding_press : R.mipmap.ic_ding);
        } else {
            iv_ding.setImageResource(R.mipmap.ic_ding);
        }
        if (object.has("bottomId") && Utils.isLogin(context)) {
            iv_cai.setImageResource(object.getString("bottomId").contains("[" + Utils.getLoginId(context) + "]") ? R.mipmap.ic_cai_press : R.mipmap.ic_cai);
        } else {
            iv_cai.setImageResource(R.mipmap.ic_cai);
        }
        if (object.has("commitId") && Utils.isLogin(context)) {
            iv_comment.setImageResource(object.getString("commitId").contains("[" + Utils.getLoginId(context) + "]") ? R.mipmap.ic_pinglun_press : R.mipmap.ic_pinglun);
        } else {
            iv_comment.setImageResource(R.mipmap.ic_pinglun);
        }
        initData();

    }

    private void initData() {
        try {
            if (adapter != null) {
                if (page == 1) {
                    adapter.refresh(Data.getCommitData2(helper, page, object.getInt("fid")));
                } else {
                    adapter.append(Data.getCommitData2(helper, page, object.getInt("fid")));
                }
            } else {
                adapter = new PingLunListAdapter(Data.getCommitData2(helper, page, object.getInt("fid")), getContext());
                adapter.setOnItemChildViewClickListener(Tab2AdviseDetailActivity.this);
                rv_list.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private Bitmap getBitmap(String protraitPath) {
        Bitmap protraitBitmap = null;
        if (!Utils.isEmpty(protraitPath)) {
            File protraitFile = new File(protraitPath);
            if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 500, 500);
            } else {
                Utils.showToast(context, "图像不存在，可能已被删除");
            }
        }
        return protraitBitmap;
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.btn_commit:
                    add();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void add() throws JSONException {
        SQLiteDatabase db1 = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("fid", String.valueOf(object.getInt("fid")));
        values.put("uid", String.valueOf(Utils.getLoginId(getContext())));
        values.put("uname", Utils.getLoginName(getContext()));
        values.put("uimage", Utils.getLoginImage(getContext()));
        values.put("content", et_content.getText().toString());
        long i = db1.insert("adviseTabCommit", null, values);
        if (i == -1) {
            //失败
            Utils.showToast(getContext(), "操作失败");
        } else {
            //成功
            String commitCount;
            String commitId = object.has("commitId") ? object.getString("commitId") : "";
            commitCount = String.valueOf(object.has("commitCount") ? object.getInt("commitCount") + 1 : 1);
            if (object.has("commitId") && object.getString("commitId").contains("[" + Utils.getLoginId(getContext()) + "]")) {
            } else {
                commitId = String.valueOf(object.has("commitId") ? object.getString("commitId") + "[" + Utils.getLoginId(getContext()) + "]" : "[" + Utils.getLoginId(getContext()) + "]");
            }
            values.clear();
            values.put("commitCount", commitCount);
            values.put("commitId", commitId);
            int num = db1.update("adviseTab", values, "fid = ?", new String[]{String.valueOf(object.getInt("fid"))});
            db1.close();
            if (num < 1) {
                Utils.showToast(getContext(), "操作失败");
            } else {
                Utils.showToast(getContext(), "操作成功");
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}
