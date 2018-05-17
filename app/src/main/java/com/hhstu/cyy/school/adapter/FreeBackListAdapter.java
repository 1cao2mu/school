package com.hhstu.cyy.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/13.
 */
public class FreeBackListAdapter extends RecyclerView.Adapter<FreeBackListAdapter.ViewHolder> {
    JSONArray array;
    Context context;
    OnItemChildViewClickListener onItemChildViewClickListener;

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        this.onItemChildViewClickListener = onItemChildViewClickListener;
    }

    public FreeBackListAdapter(JSONArray array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_freeback, null);
        ViewHolder viewHolder = new ViewHolder(item);
        viewHolder.tv_question = (TextView) item.findViewById(R.id.tv_question);
        viewHolder.tv_answer = (TextView) item.findViewById(R.id.tv_answer);
        viewHolder.tv_time = (TextView) item.findViewById(R.id.tv_time);
        viewHolder.tv_answer_time = (TextView) item.findViewById(R.id.tv_answer_time);
        viewHolder.ll_answer = (LinearLayout) item.findViewById(R.id.ll_answer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            JSONObject object = array.getJSONObject(position);
            holder.tv_question.setText(object.getString("content"));
            holder.tv_time.setText(object.getString("time"));
            if (!object.has("ReContent")) {
                holder.ll_answer.setVisibility(View.GONE);
            } else {
                holder.ll_answer.setVisibility(View.VISIBLE);
                holder.tv_answer.setText(object.getString("ReContent"));
                holder.tv_answer_time.setText(object.getString("ReTime"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
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

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        TextView tv_question, tv_time;
        TextView tv_answer, tv_answer_time;
        LinearLayout ll_answer;

    }
}
