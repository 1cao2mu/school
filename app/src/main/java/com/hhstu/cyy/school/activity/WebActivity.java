package com.hhstu.cyy.school.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.hhstu.cyy.school.R;
import com.hhstu.cyy.school.tool.Constants;
import com.hhstu.cyy.school.tool.Utils;
import com.hhstu.cyy.school.tool.WebViewBaseClient;


/**
 * 关于我们
 * Created by Administrator on 2015/11/13.
 */
public class WebActivity extends BaseActivity {
    WebView wv_web;
    SwipeRefreshLayout srl_refresh;
    TextView tv_title;
    Intent inintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        inintent = getIntent();
        findViewById(R.id.iv_back).setOnClickListener(this);
        if (getIntent().getExtras().containsKey(Constants.ACTION_VISIBLE)) {
            findViewById(R.id.iv_action).setOnClickListener(this);
        } else {
            findViewById(R.id.iv_action).setVisibility(View.GONE);
        }
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("");
        initView();
    }

    private void initView() {
        srl_refresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        wv_web = (WebView) findViewById(R.id.wv_web);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wv_web.reload();
                srl_refresh.setRefreshing(false);
            }
        });
        wv_web.setWebViewClient(new WebViewBaseClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http")) {
                    view.loadUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wv_web.loadUrl("javascript:resizepicture()");
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });
        wv_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });
        WebSettings webSettings = wv_web.getSettings();

        // 设置支持加载图片
        webSettings.setBlockNetworkImage(false);
        // 设置支持缓存
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setGeolocationEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getAbsolutePath());
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        String url = getIntent().getExtras().getString("url");
        Utils.sysout(url);
        wv_web.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wv_web.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && wv_web.canGoBack()) {
            wv_web.goBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_action:
                //分享
                break;
        }
    }
}
