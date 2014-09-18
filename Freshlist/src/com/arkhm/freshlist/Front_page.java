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
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arkhm.freshlist.handlers.LocalAuthentication;
import com.arkhm.freshlist.models.User;

public class Front_page extends Activity {

	EditText emailfield, unamefield, fnamefield, lnamefield, passfield,
			repitpassfield, emailFieldForLogin;
	Button signupbutton, signupbutton2, loginbutton, loginbutton2;
	TextView passv;
	public static Context context = null;
	User user = new User();
	LocalAuthentication localAuth = new LocalAuthentication();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		int loginlayout = R.layout.front_page_login_layout;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		boolean newuser = sp.getBoolean("isSigned", false);
		if (newuser) {
			this.setContentView(loginlayout);
			initializeForLogin();
		} else {
			setContentView(R.layout.front_page_layout);
			initilizeViews();
		}
		setTitle("Authentication Page");
	}

	private void initializeForLogin() {
		loginbutton2 = (Button) findViewById(R.id.loginonlyBut);
		emailFieldForLogin = (EditText) findViewById(R.id.emialEdtlogin);
		signupbutton2 = (Button) findViewById(R.id.signuponlyBut);

		loginbutton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user.setEmail(emailFieldForLogin.getText().toString());
				if (isOnLine()) {
					new LoginTask().execute();
				} else {
					try {
						Class<?> togo = Class
								.forName("com.arkhm.freshlist.GroceryListActivity");
						Intent intent = new Intent(Front_page.this, togo);
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					prompt("No Network Available");
				}

			}
		});
		signupbutton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Front_page.this.setContentView(R.layout.front_page_layout);
				initilizeViews();
			}
		});
	}

	private void initilizeViews() {
		signupbutton = (Button) findViewById(R.id.signupBut);
		loginbutton = (Button) findViewById(R.id.loginBut);
		emailfield = (EditText) findViewById(R.id.emialEdt);
		unamefield = (EditText) findViewById(R.id.usernameEdt);
		passfield = (EditText) findViewById(R.id.passwordEdt);
		repitpassfield = (EditText) findViewById(R.id.repitpasswordEdt);
		passv = (TextView) findViewById(R.id.passlength);

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

		loginbutton.setOnClickListener(new listener());
		signupbutton.setOnClickListener(new listener());
	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.loginBut) {
				user.setEmail(emailfield.getText().toString());
				if (isOnLine()) {
					new LoginTask().execute();
				} else {
					try {
						Class<?> togo = Class
								.forName("com.arkhm.freshlist.ServicesActivity");
						Intent intent = new Intent(Front_page.this, togo);
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					prompt("No Network Available");
				}
			} else if (v.getId() == R.id.signupBut) {
				user.setEmail(emailfield.getText().toString());
				user.setUsername(unamefield.getText().toString());
				user.setPassword(passfield.getText().toString());
				user.setRepeatedPass(repitpassfield.getText().toString());
				validateSignUpInfor();

			}
		}

		private void validateSignUpInfor() {
			if (localAuth.cleanFields(user.getEmail(), user.getUsername(),
					user.getPassword())) {

				if (!localAuth.cleanEmail(user.getEmail())) {
					String message = "Incorrect Email address \n example \n yourmail@gmail.com";
					prompt(message);
				} else {
					if (!localAuth.cleanUserName(user.getUsername())) {

						String message = "Incorrect Username \n must be atleast 5 charcters no space between";
						prompt(message);
						System.out.println("not a good user name "
								+ user.getUsername());

					} else {
						if (!localAuth.cleanPassword(user.getPassword())) {
							if (localAuth.isshort) {
								String message = "\t Retry\n Your Password is too Short";
								prompt(message);
							}
						} else {
							if (localAuth.isSamePassword(user.getPassword(),
									user.getRepeatedPass())) {
								if (localAuth.isfair) {
									String message = "Your Password is So So Fair";
									prompt(message);
									if (isOnLine()) {
										new SignupTask().execute();
										isNewUser(true);
									} else {
										prompt("No Internet Connection Data or Wifi \n Please Connect and SignUp Again");
									}
								} else if (localAuth.isstrong) {
									if (isOnLine()) {
										new SignupTask().execute();
										isNewUser(true);
									} else {
										prompt("No Internet Connection Data or Wifi \n Please Connect and SignUp Again");
									}
								}

							} else {
								String message = "Passwords are not the same\n Both Passwords must be the same";
								prompt(message);
							}

						}
					}
				}

			} else {
				String message = "Incorrect Info \n username,email and password can't be empty ";
				prompt(message);
			}
		}
	}

	public static Toast prompt(String message) {
		Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
		t.setGravity(Gravity.TOP, 10, 50);
		t.show();
		return t;
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

			JSONObject json = user.loginUser(email);
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
					prompt(result.getString("error_message"));
				} else if (result.getInt("success") == 1) {
					prompt(result.getString("username") + " Welcome back");
					try {
						Class<?> togo = Class
								.forName("com.arkhm.freshlist.ServicesActivity");
						Intent intent = new Intent(Front_page.this, togo);
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (JSONException e) {
				Log.e("json error", e.getMessage());
			}
			super.onPostExecute(result);
		}
	}

	public class SignupTask extends AsyncTask<String, Void, JSONObject> {
		String email, username, password, tag;

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
					prompt(result.getString("error_message"));
				} else if (result.getInt("success") == 1) {
					String message = result.getString("username")
							+ " \n Thank you to SignUp for Our Services\n Check you Email Now for confirmation";
					prompt(message);
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

	public void isNewUser(boolean isUserSigned) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor usereditor = sp.edit();
		usereditor.putBoolean("isSigned", isUserSigned);
		usereditor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.front_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
