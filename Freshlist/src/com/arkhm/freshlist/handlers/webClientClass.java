package com.arkhm.freshlist.handlers;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webClientClass extends WebViewClient {
	
	public boolean setOverrideUrlLoading(WebView webview,String url){
		webview.loadUrl(url);
		return true;
	}

}
