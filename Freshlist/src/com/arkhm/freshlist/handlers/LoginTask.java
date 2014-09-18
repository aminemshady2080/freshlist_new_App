package com.arkhm.freshlist.handlers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.arkhm.freshlist.Front_page;
import com.arkhm.freshlist.models.User;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	Context con = Front_page.context;
	ProgressDialog dialog;
	String email="", phone, tag;
	User user = new User();

	@Override
	protected void onPreExecute() {

		// dialog = new ProgressDialog(con);
		// dialog.setTitle("Login");
		// dialog.setMessage("Logging in ...");
		// dialog.setIndeterminate(false);
		// dialog.setCancelable(true);
		// dialog.
		// dialog.show();
		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... params) {

		email = user.getEmail();
		Log.d("mail", "is : "+email);
		JSONObject json = user.loginUser(email);

		return json;
	}

	@Override
	protected void onPostExecute(JSONObject result) {

		try {
			if (!(result.getInt("error") == 0)) {
				// dialog.setMessage("incorrect email in loggin");
				Toast.makeText(con, "incorrect email in loggin",
						Toast.LENGTH_LONG).show();
			} else if (result.getInt("success") == 1) {
				String mail = result.getString("email");
				String username = mail.substring(0, mail.indexOf("@"));
				// dialog.setMessage("correct email \n \t Welcome" + username);
				Toast.makeText(con, "correct email  Welcome" + username,
						Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			Log.e("json error", e.getMessage());
		}
		super.onPostExecute(result);
	}
}
