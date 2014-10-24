package com.arkhm.freshlist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.webClientClass;

public class HelpAndAboutActivity extends ActionBarActivity {

	WebView webview;
	webClientClass webclient = new webClientClass();
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		context = HelpAndAboutActivity.this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		webview = (WebView) findViewById(R.id.webView);
		if (getIntent().getExtras() == null) {
			webview.loadUrl("file:///android_asset/userguide/index.html");
		} else {
			loadurl();
		}
		webview.getSettings().setBuiltInZoomControls(true);
		webview.setWebViewClient(webclient);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
	}

	private void loadurl() {
		if (getIntent().getStringExtra("need_about").equals("#about")) {
			webview.loadUrl("file:///android_asset/userguide/about.html");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			AppUtilities.goTo(context, "UserDashBoardActivity");
		}
		return true;
	}
}
