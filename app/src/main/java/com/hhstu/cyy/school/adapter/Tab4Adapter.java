package com.hhstu.cyy.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/20.
 */
public class Tab4Adapter extends RecyclerView.Adapter<Tab4Adapter.ViewHolder> {
    JSONArray array;
    Context context;
    OnItemChildViewClickListener onItemChildViewClickListener;

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        this.onItemChildViewClickListener = onItemChildViewClickListener;
    }

    public Tab4Adapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_4, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.v_right = view.findViewById(R.id.v_right);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.iv_icon = (RoundImageView) view.findViewById(R.id.iv_icon);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            final JSONObject object = array.getJSONObject(position);
            if ((position + 1) % 4 == 0) {
                holder.v_right.setVisibility(View.GONE);
            } else {
                holder.v_right.setVisibility(View.VISIBLE);
            }
            if (position==0){
                holder.tv_name.setText("失物招领");
                holder.iv_icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.shiwu));
            }else if (position==1){
                holder.tv_name.setText("美女");
              holder.iv_icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.iv_icon));
            }
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

    @Override
    public int getItemCount() {
        return array.length();
    }

    public JSONObject getItem(int position) throws JSONException {
        return array.getJSONObject(position);
    }

    public void refresh(JSONArray array) {
        this.array = array;
        this.notifyDataSetChanged();
    }

    public void append(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                this.array.put(array.getJSONObject(i));
            }
            this.notifyDataSetChanged();
        } catch (JSONException e) {
        }
    }

    /**
     * 移除item
     *
     * @param position
     */
    public void remove(int position) {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < this.array.length(); i++) {
                if (position == i) {
                    continue;
                }
                array.put(this.array.getJSONObject(i));
            }
            this.notifyDataSetChanged();
        } catch (JSONException e) {
        }
    }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            this.item = itemView;
        }

        RoundImageView iv_icon;
        TextView tv_name;
        View item, v_right;
    }
}
