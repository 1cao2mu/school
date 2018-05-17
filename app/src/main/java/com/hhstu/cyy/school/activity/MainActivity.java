package com.hhstu.cyy.school.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.hhstu.cyy.school.Application.MineApplication;
import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.View.RoundImageView;
import com.hhstu.cyy.school.View.TabFragment;
import com.hhstu.cyy.school.fragment.Tab1Fragment;
import com.hhstu.cyy.school.fragment.Tab2Fragment;
import com.hhstu.cyy.school.fragment.Tab3Fragment;
import com.hhstu.cyy.school.fragment.Tab4Fragment;
import com.hhstu.cyy.school.tool.ImageUtils;
import com.hhstu.cyy.school.tool.StringUtils;
import com.hhstu.cyy.school.tool.Utils;

import java.io.File;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabFragment tabHost;
    /**
     * Fragment数组界面
     */
    private Class tabFragment[] = {Tab1Fragment.class, Tab2Fragment.class, Tab3Fragment.class,
            Tab4Fragment.class};
    /**
     * 存放按下图片数组liu
     */
    private int tabImgsPassed[] = {R.mipmap.tab_1_blue,
            R.mipmap.tab_2_blue, R.mipmap.tab_3_blue, R.mipmap.tab_4_blue};
    /**
     * 存放图片数组
     */
    private int tabImgsNormal[] = {R.mipmap.tab_1_black,
            R.mipmap.tab_2_black, R.mipmap.tab_3_black, R.mipmap.tab_4_black};

    /**
     * 选修卡文字
     */
    private String tabText[] = {"发现", "建议", "资讯", "更多"};
    /**
     * 基本控件
     */
    private RoundImageView iv_icon;
    private TextView tv_name;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (MineApplication.activitie != null) {
            MineApplication.activitie.finish();
        }
        MineApplication.activitie = this;
        initView();
        checkAutoLogin();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkAutoLogin() {
        if (Utils.isLogin(getContext())) {
            headerView.findViewById(R.id.ll_no_login).setVisibility(View.GONE);
            headerView.findViewById(R.id.ll_logined).setVisibility(View.VISIBLE);
            tv_name.setText(Utils.getLoginName(getContext()));
            String protraitPath = Utils.getLoginImage(getContext());
            if (!Utils.isEmpty(protraitPath)) {
                if (!StringUtils.isEmpty(protraitPath) && new File(protraitPath).exists()) {
                    Bitmap protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath, 200, 200);
                    iv_icon.setImageBitmap(protraitBitmap);
                } else {
                    Utils.showToast(getContext(), "图像不存在，可能已被删除");
                }
            }
        } else {
            tv_name.setText("未登录");
            iv_icon.setImageResource(R.mipmap.iv_icon);
            headerView.findViewById(R.id.ll_no_login).setVisibility(View.VISIBLE);
            headerView.findViewById(R.id.ll_logined).setVisibility(View.GONE);
        }
    }

    private void initView() {
//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(tabText[0]);
//        toolbar.setLogo(R.mipmap.ic_logo_whrite);
//        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawer.setDrawerListener(toggle);
//       toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        iv_icon = (RoundImageView) headerView.findViewById(R.id.iv_icon);
        tv_name = (TextView) headerView.findViewById(R.id.tv_name);
        iv_icon.setImageResource(R.mipmap.iv_icon);
        iv_icon.setOnClickListener(this);
        headerView.findViewById(R.id.tv_login).setOnClickListener(this);
        headerView.findViewById(R.id.tv_register).setOnClickListener(this);
        // 找到TabHost
        tabHost = (TabFragment) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_content);
        // 得到fragment的个数
        int count = tabFragment.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabText[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            tabHost.addTab(tabSpec, tabFragment[i], null);
            // 设置Tab按钮的背景
            tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.mine_tab_bg_normal);
        }
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabText.length; i++) {
                    View view = tabHost.getTabWidget().getChildAt(i);
                    ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
                    TextView textView = (TextView) view.findViewById(R.id.tv_text);
                    if (tabId.equals(tabText[i])) {
                        textView.setTextColor(Color.WHITE);
                        imageView.setImageResource(tabImgsPassed[i]);
                        textView.setTextColor(getResources().getColor(R.color.text_blue));
                        //  toolbar.setTitle(tabText[i]);
                        //   view.setBackgroundColor(getResources().getColor(R.color.mine_tab_bg_passed));
                    } else {
                        imageView.setImageResource(tabImgsNormal[i]);
                        textView.setTextColor(getResources().getColor(R.color.text_black));
                        //view.setBackgroundColor(getResources().getColor(R.color.mine_tab_bg_normal));
                    }
                }
            }
        });
        tabHost.getTabWidget().setDividerDrawable(null);
        tabHost.setCurrentTabByTag(tabText[0]);
        View view = tabHost.getCurrentTabView();
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
        TextView textView = (TextView) view.findViewById(R.id.tv_text);
        imageView.setImageResource(tabImgsPassed[0]);
        textView.setTextColor(getResources().getColor(R.color.text_blue));

        //view.setBackgroundColor(getResources().getColor(R.color.mine_tab_bg_passed));
    }

    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_main_tab, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
        imageView.setImageResource(tabImgsNormal[index]);
        TextView textView = (TextView) view.findViewById(R.id.tv_text);
        textView.setText(tabText[index]);
        view.setBackgroundColor(getResources().getColor(R.color.mine_tab_bg_normal));
        return view;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (v.getId()) {
            case R.id.iv_icon:
                break;
            case R.id.tv_login:
                intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.tv_register:
                intent = new Intent(getContext(), RegisterActivity.class);
                startActivityForResult(intent, 0);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    public void showHome() {
        tabHost.setCurrentTab(0);
    }

    public void showShop() {
        tabHost.setCurrentTab(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.sysout("main" + "---" + resultCode + "---" + requestCode);
        checkAutoLogin();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tabText[3]);
        if (fragment != null && fragment instanceof Tab4Fragment) {
            if (requestCode == 0) {
                ((Tab4Fragment) fragment).initLoginData();
            }
        }
        fragment = getSupportFragmentManager().findFragmentByTag(tabText[0]);
        if (fragment != null && fragment instanceof Tab1Fragment) {
            if (requestCode == 0) {
                ((Tab1Fragment) fragment).initLoginData();
            }
        }
        fragment = getSupportFragmentManager().findFragmentByTag(tabText[1]);
        if (fragment != null && fragment instanceof Tab2Fragment) {
            if (requestCode == 0) {
                ((Tab2Fragment) fragment).initLoginData();
            }
        }
        fragment = getSupportFragmentManager().findFragmentByTag(tabText[2]);
        if (fragment != null && fragment instanceof Tab3Fragment) {
            if (requestCode == 0) {
                ((Tab3Fragment) fragment).initLoginData();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("确定退出吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 点击“确认”后的操作
                            MineApplication.exitApp();
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 点击“返回”后的操作,这里不设置没有任何操作
                }
            }).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        if (Utils.isLogin(getContext())) {
            if (id == R.id.action_settings) {
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            }
            if (id == R.id.action_share) {
                Utils.share(getContext(), "http://www.hhstu.edu.cn/");
                return true;
            }
        } else {
            intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (Utils.isLogin(getContext())) {
            if (id == R.id.nav_person_id) {
                intent = new Intent(getContext(), UserInfoActivity.class);
                startActivityForResult(intent, 0);
            } else if (id == R.id.nav_person_message) {
                intent = new Intent(getContext(), ResetPasswordActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_setting) {
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_share) {
                Utils.share(getContext(), "http://www.hhstu.edu.cn/");
            } else if (id == R.id.nav_help) {
                intent = new Intent(getContext(), FreebackAndHelpActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_out) {
                intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", "file:///android_asset/about.html");
                startActivity(intent);
            }
        } else {
            intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void open() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }
}
