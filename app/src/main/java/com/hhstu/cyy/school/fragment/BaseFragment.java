package com.hhstu.cyy.school.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hhstu.cyy.school.listener.OnItemChildViewClickListener;


/**
 * Created by Administrator on 15-11-12.
 */
public class BaseFragment extends Fragment implements View.OnClickListener, OnItemChildViewClickListener {
    boolean REFRESHABLE = true;
    int page = 1, LAST_VISIABLE_IETM_INDEX, FIRST_VISIABLE_IETM_INDEX;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemChildViewClickListener(int id, int position) {

    }
}
