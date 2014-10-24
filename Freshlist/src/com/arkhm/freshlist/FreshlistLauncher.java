package com.arkhm.freshlist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.arkhm.freshlist.handlers.LocalDBHandler;

public class FreshlistLauncher extends Activity {
	LocalDBHandler localdb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		localdb = new LocalDBHandler(FreshlistLauncher.this);
		launchActivity();
	}

	private void launchActivity() {
		SharedPreferences prefs = getSharedPreferences("FreshlistUserPrefs", 0);
		boolean islogged = prefs.getBoolean("isLoggedIn", false);
		Log.d("user loging", Boolean.toString(islogged));
		String USER_NAME = "user_name";
		String USER_MAIL = "user_mail";

		String username = localdb.getCurrentUser(USER_NAME);
		String usermail = localdb.getCurrentUser(USER_MAIL);

		Intent screen = new Intent();
		if (islogged) {
			screen.setClass(this, UserDashBoardActivity.class);
			screen.putExtra("USER_NAME", username);
			screen.putExtra("USER_MAIL", usermail);
			screen.putExtra("tag", "from_launcher");
		} else {
			screen.setClass(this, WelcomePageActivity.class);
		}
		startActivity(screen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		finish();
	}

}
