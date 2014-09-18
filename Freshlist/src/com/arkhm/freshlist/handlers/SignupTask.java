package com.arkhm.freshlist.handlers;

import org.json.JSONException;
import org.json.JSONObject;

import com.arkhm.freshlist.WelcomePageActivity;
import com.arkhm.freshlist.models.User;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SignupTask extends AsyncTask<String, Void, JSONObject> {
	String email = "", phone, username, password, tag;
	User user = new User();
	Context con = WelcomePageActivity.context;

	@Override
	protected void onPreExecute() {
		email = user.getEmail();
		username = user.getUsername();
		password = user.getPassword();

		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject jsonAuth = user.signUpUser(email, username, password);

		return jsonAuth;
	}

	@Override
	protected void onPostExecute(JSONObject result) {

		try {
			if (!(result.getInt("error") == 0)) {
				Toast.makeText(con, result.getString("error_message"),
						Toast.LENGTH_LONG).show();
			} else if (result.getInt("success") == 1) {
				Toast.makeText(con, "Succefull signup \n \n \t " + username,
						Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			Log.e("json error", e.getMessage());
		}
		super.onPostExecute(result);
	}

}
