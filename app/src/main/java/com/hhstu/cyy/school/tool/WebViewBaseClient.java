package com.hhstu.cyy.school.tool;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewBaseClient extends WebViewClient {

	@Override
	public void onReceivedError(final WebView view, int errorCode, String description, final String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		view.loadUrl("file:///android_asset/error_page.html");
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//		super.onReceivedError(view, request, error);
		if (request.isForMainFrame()) {
			view.loadUrl("file:///android_asset/error_page.html");
		}
	}
}
