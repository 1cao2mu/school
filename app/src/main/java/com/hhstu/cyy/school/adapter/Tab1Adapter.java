package com.hhstu.cyy.school.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.toolbox.NetworkImageView;
import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.View.RoundNetImageView;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2015/11/20.
 */
public class Tab1Adapter extends RecyclerView.Adapter<Tab1Adapter.ViewHolder> {
    JSONArray array;
    Context context;
    OnItemChildViewClickListener onItemChildViewClickListener;

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        this.onItemChildViewClickListener = onItemChildViewClickListener;
    }

    public Tab1Adapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_1, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
        viewHolder.iv_icon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.nv_content = (ImageView) view.findViewById(R.id.nv_content);
        viewHolder.nv_content.setImageResource(R.mipmap.ic_launcher);
        viewHolder.nv_content.setMinimumHeight(Utils.getScreenWidth(context) - 20);
        viewHolder.nv_content.setMinimumWidth(Utils.getScreenWidth(context) - 20);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
        viewHolder.tv_ding_count = (TextView) view.findViewById(R.id.tv_ding_count);
        viewHolder.tv_cai_count = (TextView) view.findViewById(R.id.tv_cai_count);
        viewHolder.tv_comment_count = (TextView) view.findViewById(R.id.tv_comment_count);
        viewHolder.iv_ding = (ImageView) view.findViewById(R.id.iv_ding);
        viewHolder.iv_cai = (ImageView) view.findViewById(R.id.iv_cai);
        viewHolder.iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            final JSONObject object = array.getJSONObject(position);
            if (object.has("image") && !Utils.isEmpty(object.getString("image"))) {
                holder.nv_content.setVisibility(View.VISIBLE);
                Bitmap b = getBitmap(object.getString("image"));
                if (b == null) {
                    holder.nv_content.setImageResource(R.mipmap.ic_launcher);
                } else {
                    holder.nv_content.setImageBitmap(b);
                }
            } else {
                holder.nv_content.setVisibility(View.GONE);
            }
            if (object.has("uimage")) {
                Bitmap b = getBitmap(object.getString("uimage"));
                if (b == null) {
                    holder.iv_icon.setImageResource(R.mipmap.ic_launcher);
                } else {
                    holder.iv_icon.setImageBitmap(b);
                }
            } else {
                holder.iv_icon.setImageResource(R.mipmap.ic_launcher);
            }
            holder.tv_name.setText(object.has("uname") ? object.getString("uname") : "无名");
            holder.tv_content.setText(object.has("content") ? object.getString("content") : "");
            holder.tv_ding_count.setText(object.has("topCount") ? String.valueOf(object.getInt("topCount")) : "0");
            holder.tv_cai_count.setText(object.has("bottomCount") ? String.valueOf(object.getInt("bottomCount")) : "0");
            holder.tv_comment_count.setText(object.has("commitCount") ? String.valueOf(object.getInt("commitCount")) : "0");
            if (object.has("topId") && Utils.isLogin(context)) {
                holder.iv_ding.setImageResource(object.getString("topId").contains("[" + Utils.getLoginId(context) + "]") ? R.mipmap.ic_ding_press : R.mipmap.ic_ding);
            } else {
                holder.iv_ding.setImageResource(R.mipmap.ic_ding);
            }
            if (object.has("bottomId") && Utils.isLogin(context)) {
                holder.iv_cai.setImageResource(object.getString("bottomId").contains("[" + Utils.getLoginId(context) + "]") ? R.mipmap.ic_cai_press : R.mipmap.ic_cai);
            } else {
                holder.iv_cai.setImageResource(R.mipmap.ic_cai);
            }
            if (object.has("commitId") && Utils.isLogin(context)) {
                holder.iv_comment.setImageResource(object.getString("commitId").contains("[" + Utils.getLoginId(context) + "]") ? R.mipmap.ic_pinglun_press : R.mipmap.ic_pinglun);
            } else {
                holder.iv_comment.setImageResource(R.mipmap.ic_pinglun);
            }
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemChildViewClickListener != null) {
                        onItemChildViewClickListener.onItemChildViewClickListener(-1, position);
                    }
                }
            });
            holder.iv_ding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemChildViewClickListener != null) {
                        onItemChildViewClickListener.onItemChildViewClickListener(v.getId(), position);
                    }
                }
            });
            holder.iv_cai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemChildViewClickListener != null) {
                        onItemChildViewClickListener.onItemChildViewClickListener(v.getId(), position);
                    }
                }
            });

            holder.iv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemChildViewClickListener != null) {
                        onItemChildViewClickListener.onItemChildViewClickListener(v.getId(), position);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public JSONObject getItem(int position) throws JSONException {
        return array.getJSONObject(position);
    }

    public void append(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                this.array.put(array.getJSONObject(i));
            }
            this.notifyItemRangeInserted(this.array.length() - array.length() - 1, array.length());
            this.notifyItemRangeChanged(0, this.array.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refresh(JSONArray array) {
        int old_length = this.array.length();
        this.array = array;
        int new_length = this.array.length();
        this.notifyItemRangeRemoved(new_length - 1, old_length - new_length);
        this.notifyItemRangeChanged(0, new_length);
    }

    public void refresh() {
        this.notifyItemRangeChanged(0, this.array.length());
    }

    public void remove(int position) {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < this.array.length(); i++) {
                if (position == i) {
                    continue;
                }
                array.put(this.array.getJSONObject(i));
            }
            this.array = array;
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(0, this.array.length());
        } catch (JSONException e) {
        }
    }

    public void top(int position, String topCount, String topId) throws JSONException {
        JSONObject object = array.getJSONObject(position);
        object.put("topCount", topCount);
        object.put("topId", topId);
        this.notifyItemChanged(position);
    }

    public void bottom(int position, String bottomCount, String bottomId) throws JSONException {
        JSONObject object = array.getJSONObject(position);
        object.put("bottomId", bottomId);
        object.put("bottomCount", bottomCount);
        this.notifyItemChanged(position);
    }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            this.item = itemView;
        }

        RoundImageView iv_icon;
        TextView tv_name, tv_content, tv_ding_count, tv_cai_count, tv_comment_count;
        ImageView nv_content;
        ImageView iv_ding, iv_cai, iv_comment;
        View item;
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
}
