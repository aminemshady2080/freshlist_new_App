package com.arkhm.freshlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalAuthentication;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.models.User;

public class WelcomePageActivity extends Activity {

	EditText emailfield, unamefield, fnamefield, lnamefield, passfield,
			repitpassfield, emailFieldForLogin;
	Button signupbutton, signupbutton2, loginbutton, loginbutton2;
	TextView passv;
	public static Context context = null;
	public static User user = new User();
	LocalAuthentication localAuth = new LocalAuthentication();
	LocalDBHandler localDb = null;
	public static String usermail = "";
	private String username = "";
	private String userpass;
	private String userpassRepit;
	public static String returnedusermail;
	public static String returnedusername;
	static SharedPreferences sharedprefs = null;
	SharedPreferences.Editor usereditor = null;
	boolean isLoggedIn = false;
	public String returnedpassword;
	AppUtilities apptools = new AppUtilities();
	public String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		localDb = new LocalDBHandler(getApplicationContext());
		sharedprefs = getSharedPreferences("FreshlistUserPrefs", 0);
		usereditor = sharedprefs.edit();
		setTitle("Authentication Page");
		setContentView(R.layout.front_page_login_layout);
		initializeForLogin();
	}

	private void initializeForLogin() {
		loginbutton2 = (Button) findViewById(R.id.loginonlyBut);
		emailFieldForLogin = (EditText) findViewById(R.id.emialEdtloginOnly);
		signupbutton2 = (Button) findViewById(R.id.signuponlyBut);
		loginbutton2.setOnClickListener(new listeners());
		signupbutton2.setOnClickListener(new listeners());
	}

	private void initilizeViewsForSignUp() {
		getViewsFromXml();
		passfield.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() < 6) {
					passv.setText("weak password");
					passv.setTextColor(Color.RED);
				} else if (s.length() == 6 || s.length() < 8) {
					passv.setText("fair password");
					passv.setTextColor(Color.rgb(255, 120, 20));
				} else {
					passv.setText("strong password");
					passv.setTextColor(Color.GREEN);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		loginbutton.setOnClickListener(new listeners());
		signupbutton.setOnClickListener(new listeners());
	}

	private void getViewsFromXml() {
		signupbutton = (Button) findViewById(R.id.signupBut);
		loginbutton = (Button) findViewById(R.id.loginBut);
		emailfield = (EditText) findViewById(R.id.emialEdt);
		unamefield = (EditText) findViewById(R.id.usernameEdt);
		passfield = (EditText) findViewById(R.id.passwordEdt);
		repitpassfield = (EditText) findViewById(R.id.repitpasswordEdt);
		passv = (TextView) findViewById(R.id.passlength);
	}

	private void initilizeVariables() {
		usermail = emailfield.getText().toString();
		username = unamefield.getText().toString();
		userpass = passfield.getText().toString();
		userpassRepit = repitpassfield.getText().toString();
	}

	class listeners implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.loginonlyBut:
				logUserIn(emailFieldForLogin);
				break;
			case R.id.loginBut:
				logUserIn(emailfield);
				break;
			case R.id.signupBut:
				if (isOnLine()) {
					signUpUser();
				} else {
					signUpUser();
				}
				break;
			case R.id.signuponlyBut:
				WelcomePageActivity.this
						.setContentView(R.layout.front_page_layout);
				initilizeViewsForSignUp();
				break;
			}

		}

	}

	private void logUserIn(EditText emailfield) {
		if (isOnLine()) {
			if (emailfield.getText().toString().length() > 0) {
				new LoginTask().execute(new String[] { emailfield.getText()
						.toString() });
				isLoggedIn = true;
				usereditor.putBoolean("isLoggedIn", isLoggedIn);
				usereditor.commit();
			} else {
				AppUtilities.prompt(context, "Email is Needed to Loggin");
			}
		} else {
			AppUtilities.prompt(context, "No Network Available");
			AppUtilities.goTo(context, "UserDashBoardActivity");// to be deleted in finished production
		}
	}

	private void signUpUser() {
		validateSignUpInfor(); 
		initilizeVariables();
		user.setEmail(emailfield.getText().toString());
		user.setUsername(unamefield.getText().toString());
		user.setPassword(passfield.getText().toString());
		user.setRepeatedPass(repitpassfield.getText().toString());
	}

	private void validateSignUpInfor() {
		initilizeVariables();
		if (localAuth.cleanFields(usermail, username, userpass)) {

			if (!localAuth.cleanEmail(usermail)) {
				String message = "Incorrect Email address \n \t example \nyourmail@gmail.com";
				AppUtilities.prompt(context, message);
			} else {
				if (!localAuth.cleanUserName(username)) {

					String message = "Incorrect Username \nmust be atleast 5 charcters no space between";
					AppUtilities.prompt(context, message);
					System.out.println("not a good user name " + username);

				} else {
					if (!localAuth.cleanPassword(userpass)) {
						if (localAuth.isshort) {
							String message = "Retry\nYour Password is too Short";
							AppUtilities.prompt(context, message);
						}
					} else {
						if (localAuth.isSamePassword(userpass, userpassRepit)) {
							if (localAuth.isfair) {
								String message = "Your Password is So So Fair\nYou can change it Later";
								AppUtilities.prompt(context, message);
								if (isOnLine()) {
									new SignupTask().execute();
								} else {
									AppUtilities
											.prompt(context,
													"No Internet Connection Data or Wifi \nPlease Connect and SignUp Again");
								}
							} else if (localAuth.isstrong) {
								if (isOnLine()) {
									new SignupTask().execute();
								} else {
									AppUtilities
											.prompt(context,
													"No Internet Connection Data or Wifi \nPlease Connect and SignUp Again");
								}
							}

						} else {
							String message = "Passwords are not the same\nBoth Passwords must be the same";
							AppUtilities.prompt(context, message);
						}
					}
				}
			}

		} else {
			String message = "Incorrect Info \nusername,email and password can't be empty ";
			AppUtilities.prompt(context, message);
		}
	}

	public class LoginTask extends AsyncTask<String, Void, JSONObject> {
		String email;

		@Override
		protected void onPreExecute() {
			email = user.getEmail();
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject json = user.loginUser(params[0]);
			JSONArray userArray = null;
			JSONObject userObject = null;
			try {
				userArray = json.getJSONArray("user");
				userObject = userArray.getJSONObject(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return userObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				if (!(result.getInt("error") == 0)) {
					AppUtilities.prompt(context,
							result.getString("error_message"));
				} else if (result.getInt("success") == 1) {
					id=result.getString("user_id");
					returnedusername = result.getString("username");
					returnedusermail = result.getString("email");
					returnedpassword = result.getString("password");
					localDb.createUser(id,returnedusername, returnedusermail,
							returnedpassword);
					user.setEmail(returnedusermail);
					user.setUsername(returnedusername);
					user.setPassword(returnedpassword);
					usereditor.putString("usermail", returnedusermail);
					usereditor.commit();
					goToUserDashBoard(returnedusername, returnedusermail);

				}

			} catch (JSONException e) {
				Log.e("json error", e.getMessage());
			}
			super.onPostExecute(result);
		}
	}

	public class SignupTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject jsonAuth = user.signUpUser(usermail, username, userpass);
			return jsonAuth;
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			try {
				if (!(result.getInt("error") == 0)) {
					AppUtilities.prompt(context,
							result.getString("error_message"));
				} else if (result.getInt("success") == 1) {
					String message = result.getString("username")
							+ "\nThank you to SignUp for Our Services\nCheck you Email for confirmation";
					WelcomePageActivity.this.setContentView(R.layout.front_page_login_layout);
					initializeForLogin();
					AppUtilities.prompt(context, message);
				}

			} catch (JSONException e) {
				Log.e("json error", e.getMessage());
			}
			super.onPostExecute(result);
		}
	}

	public boolean isOnLine() {
		boolean itis = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnectedOrConnecting()) {
			itis = true;
		} else {
			itis = false;
		}

		return itis;
	}

	private void goToUserDashBoard(String username, String usermail) {
		try {
			Class<?> togo = Class
					.forName("com.arkhm.freshlist.UserDashBoardActivity");
			Intent intent = new Intent(WelcomePageActivity.this, togo);
			intent.putExtra("usermail", usermail);
			intent.putExtra("username", username);
			intent.putExtra("tag", "from_welcome");
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
