package com.hhstu.cyy.school.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class GuideActivity extends BaseActivity  {
    ViewPager viewPager;
    ImageView iv_dot1, iv_dot2, iv_dot3;
    TextView tv_go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager = (ViewPager) findViewById(R.id.vp_viewpager);
        iv_dot1 = (ImageView) findViewById(R.id.iv_dot1);
        iv_dot2 = (ImageView) findViewById(R.id.iv_dot2);
        iv_dot3 = (ImageView) findViewById(R.id.iv_dot3);
        tv_go = (TextView) findViewById(R.id.tv_go);
        tv_go.setOnClickListener(this);
        tv_go.setVisibility(View.GONE);
        List<View> views = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view2 = inflater.inflate(R.layout.item_guide2, null);
        View view3 = inflater.inflate(R.layout.item_guide3, null);
        View view4 = inflater.inflate(R.layout.item_guide4, null);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        viewPager.setAdapter(new WelcomePagerAdapter(views));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tv_go.setVisibility(View.GONE);
                        iv_dot1.setImageResource(R.mipmap.icon_dot_green);
                        iv_dot2.setImageResource(R.mipmap.icon_dot_normal);
                        iv_dot3.setImageResource(R.mipmap.icon_dot_normal);
                        break;
                    case 1:
                        tv_go.setVisibility(View.GONE);
                        iv_dot1.setImageResource(R.mipmap.icon_dot_normal);
                        iv_dot2.setImageResource(R.mipmap.icon_dot_green);
                        iv_dot3.setImageResource(R.mipmap.icon_dot_normal);
                        break;
                    case 2:
                        tv_go.setVisibility(View.GONE);
                        iv_dot1.setImageResource(R.mipmap.icon_dot_normal);
                        iv_dot2.setImageResource(R.mipmap.icon_dot_normal);
                        iv_dot3.setImageResource(R.mipmap.icon_dot_green);
                        tv_go.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences(Constants.SP_CONFIG, Context.MODE_PRIVATE).edit();
        editor.putInt(Constants.SP_KEY_OLD_VERSION, Utils.getAppVersionCode(this));
        editor.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WelcomePagerAdapter extends PagerAdapter {
        List<View> views;

        public WelcomePagerAdapter(List<View> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * 這個方法，是從ViewGroup中移出當前View
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        /**
         * 這個方法，return一個對象，這個對象表明了PagerAdapter適配器選擇哪個對象放在當前的ViewPager中
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tv_go.getId()) {
            Intent intent=new Intent(getContext(),MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
