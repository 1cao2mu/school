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

import com.hhstu.cyy.school.R;
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
public class PingLunListAdapter extends RecyclerView.Adapter<PingLunListAdapter.ViewHolder> {
    JSONArray array;
    Context context;
    OnItemChildViewClickListener onItemChildViewClickListener;

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        this.onItemChildViewClickListener = onItemChildViewClickListener;
    }

    public PingLunListAdapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pinglun_thing, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        viewHolder.iv_icon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            final JSONObject object = array.getJSONObject(position);
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
            holder.tv_name.setText(object.getString("uname"));
            holder.tv_content.setText(object.getString("content"));
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemChildViewClickListener != null) {
                        onItemChildViewClickListener.onItemChildViewClickListener(-1, position);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public int getItemCount() {
        return array.length();
    }

    public JSONObject getItem(int position) throws JSONException {
        return array.getJSONObject(position);
    }

    @Nullable
    private Bitmap getBitmap(String protraitPath) {
        Bitmap protraitBitmap = null;
        if (!Utils.isEmpty(protraitPath)) {
            File protraitFile = new File(protraitPath);
            if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
            } else {
                Utils.showToast(context, "图像不存在，可能已被删除");
            }
        }
        return protraitBitmap;
    }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            this.item = itemView;
        }

        TextView tv_name, tv_content;
        ImageView iv_icon;
        View item;

    }
}
