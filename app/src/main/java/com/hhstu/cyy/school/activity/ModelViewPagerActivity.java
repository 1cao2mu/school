package com.hhstu.cyy.school.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.fragment.Tab1Fragment;
import com.hhstu.cyy.school.fragment.Tab2Fragment;
import com.hhstu.cyy.school.fragment.Tab3Fragment;
import com.hhstu.cyy.school.fragment.Tab4Fragment;
import com.hhstu.cyy.school.fragment.TabViewPagerFragment;
import com.hhstu.cyy.school.tool.Utils;

public class ModelViewPagerActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar bottomNavigationBar;
    FragmentManager manager;
    FragmentTransaction transaction;
    TabViewPagerFragment tab1Fragment;
    Tab2Fragment tab2Fragment;
    Tab3Fragment tab3Fragment;
    Tab4Fragment tab4Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.unHide();
        bottomNavigationBar.setTabSelectedListener(this);
        refresh();
    }

    @Override
    public void onClick(View v) {

    }

    private void refresh() {
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_CLASSIC);
        //  bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //   bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.tab_1_black, "发现").setActiveColor(R.color.theme)).setInActiveColor(R.color.text_black)
                .addItem(new BottomNavigationItem(R.mipmap.tab_2_black, "建议").setActiveColor(R.color.theme)).setInActiveColor(R.color.text_black)
                .addItem(new BottomNavigationItem(R.mipmap.tab_3_black, "资讯").setActiveColor(R.color.theme)).setInActiveColor(R.color.text_black)
                .addItem(new BottomNavigationItem(R.mipmap.tab_4_black, "更多").setActiveColor(R.color.theme)).setInActiveColor(R.color.text_black)
                .setFirstSelectedPosition(0)
                .initialise();
        setScrollableText(0);
    }

    @Override
    public void onTabSelected(int position) {
        setScrollableText(position);
    }

    private void setScrollableText(int position) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        switch (position) {
            case 0:
                if (tab1Fragment == null) {
                    tab1Fragment = new TabViewPagerFragment();
                }
                transaction.replace(R.id.rl_content, tab1Fragment);
                break;
            case 1:
                if (tab2Fragment == null) {
                    tab2Fragment = new Tab2Fragment();
                }
                transaction.replace(R.id.rl_content, tab2Fragment);
                break;
            case 2:
                if (tab3Fragment == null) {
                    tab3Fragment = new Tab3Fragment();
                }
                transaction.replace(R.id.rl_content, tab3Fragment);
                break;
            case 3:
                if (tab4Fragment == null) {
                    tab4Fragment = new Tab4Fragment();
                }
                transaction.replace(R.id.rl_content, tab4Fragment);
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {
        Utils.showToast(getContext(), String.valueOf(position));
    }
}
