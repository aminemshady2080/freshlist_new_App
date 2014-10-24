package com.arkhm.freshlist;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.handlers.NetworkJsonHandler;
import com.arkhm.freshlist.models.User;

public class UserAccountSettingsActivity extends ActionBarActivity {
	ListView accountListView;
	LinearLayout botoomlayout;
	Button sendbut, cancelbut;
	LocalDBHandler localDb;
	NetworkJsonHandler jsonParser;
	Context context;
	TextView topview, downview;
	ImageView imgv;
	int galleryrequestkode = 1, camerareqstkode = 2;
	Bitmap userprofilepic;
	String username;
	String user_id;
	String usermail;
	String userpass;
	String resetusername, resetmail, resetpass;
	private String useraddress, useraddressmore;
	SharedPreferences sharedprefs = null;
	byte[] imageBytes = null;
	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usersetting_layout);
		setTitle("User Settings");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		context = UserAccountSettingsActivity.this;
		localDb = new LocalDBHandler(context);
		jsonParser = new NetworkJsonHandler();
		sharedprefs = getSharedPreferences("FreshlistUserPrefs", 0);
		initilize();

	}

	private void initilize() {

		accountListView = (ListView) findViewById(R.id.useraccountListview);
		accountListView.setBackgroundColor(Color.argb(50, 255, 255, 255));
		botoomlayout = (LinearLayout) findViewById(R.id.useraccountbottomlayout);
		sendbut = (Button) findViewById(R.id.useraccountsendbut);
		cancelbut = (Button) findViewById(R.id.useraccountcancelbut);
		sendbut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		cancelbut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		sendbut.setOnClickListener(new clicklisteners());
		cancelbut.setOnClickListener(new clicklisteners());
		displayList();

	}

	private void displayList() {
		String tasks[] = { "Profile Picture", "Change Username",
				"Change Email", "Change Password", "Change Address",
				"Delivery Phone Number" };
		ArrayList<String> taskslist = new ArrayList<>();
		for (String i : tasks) {
			taskslist.add(i);
		}
		getDataFromDb();
		ArrayList<String> vals = new ArrayList<>();
		vals.add("");
		vals.add(username);
		vals.add(usermail);
		vals.add("******");
		vals.add(useraddress + "  " + useraddressmore);
		vals.add(sharedprefs.getString("phone_number", ""));
		accountListView.setAdapter(AppUtilities.itemsArrayAdapter(context,
				accountListView, taskslist, vals, "Now is: "));
		accountListView.setOnItemClickListener(new listclicks());
	}

	private void getDataFromDb() {
		user_id = localDb.getCurrentUser("user_id");
		resetusername = localDb.getCurrentUser("user_name");
		username = localDb.getCurrentUser("user_name");
		resetmail = localDb.getCurrentUser("user_mail");
		usermail = localDb.getCurrentUser("user_mail");
		resetpass = localDb.getCurrentUser("user_password");
		userpass = localDb.getCurrentUser("user_password");
		useraddress = sharedprefs.getString("address1", "");
		useraddressmore = sharedprefs.getString("address2", "");
	}

	class clicklisteners implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.useraccountsendbut:
				if (isOnLine()) {
					new updateTask().execute();
					AppUtilities.goTo(context, "UserDashBoardActivity");
				} else {
					AppUtilities
							.prompt(context,
									"No Internet Connection\nOnly Picture and Address \ncan be Updated Without Internet\nTurn Internet On and Retry");
				}

				break;
			case R.id.useraccountcancelbut:
				localDb.updateUser(user_id, resetmail, resetusername, resetpass);
				AppUtilities.prompt(context,
						"You didnt Update or Change your Settings");
				AppUtilities.goTo(context, "UserDashBoardActivity");
				break;
			default:
				break;
			}
		}

	}

	class listclicks implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0) {
				picoptions().create().show();
			} else if (position == 1) {
				userdetails("change username", "new username", "user_name",
						position).create().show();
			} else if (position == 2) {
				userdetails("change email", "new email", "user_mail", position)
						.create().show();
			} else if (position == 3) {
				userdetails("change password", "new password", "user_password",
						position).create().show();
			} else if (position == 4) {
				addressDialog().create().show();
			} else if (position == 5) {
				phoneDialog().create().show();
			}

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			AppUtilities.goTo(context, "UserDashBoardActivity");
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == galleryrequestkode && resultCode == RESULT_OK
				&& null != data) {
			Uri imgurl = data.getData();

			String filepath[] = { MediaStore.Images.Media.DATA };
			Cursor cur = getContentResolver().query(imgurl, filepath, null,
					null, null);
			cur.moveToFirst();
			int index = cur.getColumnIndex(filepath[0]);
			String imgpath = cur.getString(index);
			cur.close();
			userprofilepic = BitmapFactory.decodeFile(imgpath);

			UserDashBoardActivity.userPic.setImageBitmap(userprofilepic);
			AppUtilities.prompt(context,
					"Successfully Picked Picture from Gallery");
			dealWithPic(userprofilepic);

		} else if (requestCode == camerareqstkode && resultCode == RESULT_OK
				&& null != data) {
			Bundle databundle = data.getExtras();
			userprofilepic = (Bitmap) databundle.get("data");
			AppUtilities.prompt(context, "Successfully took Picture");
			dealWithPic(userprofilepic);
			UserDashBoardActivity.userPic.setImageBitmap(userprofilepic);
		}
	}

	private void dealWithPic(Bitmap pic) {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		pic.compress(Bitmap.CompressFormat.PNG, 100, outstream);
		imageBytes = outstream.toByteArray();
		localDb.createUserSettings(usermail, imageBytes, useraddress);
	}

	protected AlertDialog.Builder picoptions() {
		AlertDialog.Builder dia = new Builder(context);
		dia.setTitle("Picture Options");
		dia.setSingleChoiceItems(new String[] { "Picture From Gallery",
				"Take a Picture now" }, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent gallery = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(gallery, galleryrequestkode);
							dialog.dismiss();
						} else if (which == 1) {
							Intent camera = new Intent(
									android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(camera, camerareqstkode);
							dialog.dismiss();
						}
					}
				});
		return dia;
	}

	AlertDialog.Builder userdetails(String title, String hint2,
			final String column, final int detailPostion) {
		AlertDialog.Builder dia = new Builder(context);
		dia.setTitle(title);
		final EditText field1 = new EditText(context);
		field1.setHint("You Need Password here");
		field1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		field1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		final EditText field2 = new EditText(context);
		field2.setHint(hint2);
		field2.setInputType(EditorInfo.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
		field2.setImeOptions(EditorInfo.IME_ACTION_DONE);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(field1);
		layout.addView(field2);
		dia.setView(layout);
		dia.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (field1.getText().toString().equals(userpass)) {
					if (detailPostion == 1) {
						username = field2.getText().toString();
					} else if (detailPostion == 2) {
						usermail = field2.getText().toString();
					} else if (detailPostion == 3) {
						userpass = field2.getText().toString();
					}
					localDb.updateUser(user_id, usermail, username, userpass);
					displayList();
				} else {
					AppUtilities.prompt(context,
							"Wrong Password\nNo Changes Made");
				}
			}

		});
		dia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return dia;
	}

	AlertDialog.Builder phoneDialog() {
		AlertDialog.Builder dia = new AlertDialog.Builder(context);
		dia.setTitle("Change Phone Number");
		final EditText field1 = new EditText(context);
		field1.setHint("You Need Password here");
		field1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		field1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		final EditText field2 = new EditText(context);
		field2.setHint("Phone Number");
		field2.setInputType(InputType.TYPE_CLASS_PHONE);
		field2.setImeOptions(EditorInfo.IME_ACTION_DONE);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(field1);
		layout.addView(field2);
		dia.setView(layout);
		dia.setPositiveButton("Update", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (field1.getText().toString().equals(userpass)) {
					sharedprefs.edit().remove("phone_number");
					sharedprefs.edit().commit();
					sharedprefs = getSharedPreferences("FreshlistUserPrefs", 0);
					SharedPreferences.Editor editor = sharedprefs.edit();

					editor.putString("phone_number", field2.getText()
							.toString());
					editor.commit();
					displayList();
				} else {
					AppUtilities.prompt(context,
							"Wrong Password\nNo Changes Made");
				}
			}

		});
		dia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		return dia;
	}

	AlertDialog.Builder addressDialog() {
		AlertDialog.Builder dia = new AlertDialog.Builder(context);
		dia.setTitle("Change Address Info");
		final EditText field1 = new EditText(context);
		field1.setHint("You Need Password here");
		field1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		field1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		final EditText field2 = new EditText(context);
		field2.setHint("Address Info");
		field2.setInputType(EditorInfo.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
		field2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		final EditText field3 = new EditText(context);
		field3.setHint("Address More Info/Comments");
		field3.setInputType(EditorInfo.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
		field3.setImeOptions(EditorInfo.IME_ACTION_DONE);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(field1);
		layout.addView(field2);
		layout.addView(field3);
		dia.setView(layout);
		dia.setPositiveButton("Update", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (field1.getText().toString().equals(userpass)) {
					sharedprefs.edit().remove("address1");
					sharedprefs.edit().remove("address2");
					sharedprefs.edit().commit();
					sharedprefs = getSharedPreferences("FreshlistUserPrefs", 0);
					SharedPreferences.Editor editor = sharedprefs.edit();

					editor.putString("address1", field2.getText().toString());
					editor.putString("address2", field3.getText().toString());
					editor.commit();
					displayList();
				} else {
					AppUtilities.prompt(context,
							"Wrong Password\nNo Changes Made");
				}
			}

		});
		dia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		return dia;
	}

	class updateTask extends AsyncTask<String, Void, JSONObject> {

		JSONObject updateObject = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			updateObject = user.upDateUser(user_id, usermail, username,
					userpass);
			return updateObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				if (!(result.getInt("error") == 0)) {
					AppUtilities.prompt(context,
							result.getString("error_message"));
				} else if (result.getInt("success") == 1) {
					String message = result.getString("username")
							+ "\nSuccessfully Updated Your Details";
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

}
