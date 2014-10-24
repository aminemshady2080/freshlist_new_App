package com.arkhm.freshlist;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.models.ItemList;
import com.arkhm.freshlist.models.Order;

public class ContactInfoActivity extends ActionBarActivity {

	static ListView finalGroceryLv, finalpackDeliveryLv;
	static Context context;
	Button sendOrder, saveOrder;
	LocalDBHandler localdb;
	EditText addressfield, addressmoreinfofield, phonefield;
	Order order = new Order();
	static final int DIALOG1_KEY = 0, DIALOG2_KEY = 1;
	Bundle b;
	com.arkhm.freshlist.models.Item item;
	ArrayList<String> thelistofname;
	ArrayList<String> thelistofNotes;
	boolean showprogess = false;
	String messagesent;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	ItemList listofItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		b = savedInstanceState;
		setContentView(R.layout.contact_info_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle("Address Infomation");
		prefs = getSharedPreferences("FreshlistUserPrefs", 0);
		editor = prefs.edit();

		thelistofname = getIntent().getStringArrayListExtra("builtList");
		thelistofNotes = getIntent().getStringArrayListExtra("addedNotes");

		context = ContactInfoActivity.this;
		localdb = new LocalDBHandler(context);
		initialise();
	}

	private void initialise() {
		addressfield = (EditText) findViewById(R.id.addressfield);
		addressmoreinfofield = (EditText) findViewById(R.id.addressmorefield);
		phonefield = (EditText) findViewById(R.id.phonenumber);
		addressfield.setText(prefs.getString("address1", ""));
		addressmoreinfofield.setText(prefs.getString("address2", ""));
		phonefield.setText(prefs.getString("phone_number", ""));
		finalGroceryLv = (ListView) findViewById(R.id.finalgrocerylv);
		finalpackDeliveryLv = (ListView) findViewById(R.id.finalpackagedeliverylv);
		if (getIntent().getStringExtra("service").equals("Packages Delivery")) {
			populatelv(finalpackDeliveryLv, "Packages Delivery");
		} else if (getIntent().getStringExtra("service").equals(
				"Groceries and Shopping")) {
			populatelv(finalGroceryLv, "Groceries and Shopping");
		}
		sendOrder = (Button) findViewById(R.id.sendOrder);
		saveOrder = (Button) findViewById(R.id.saveOrderOnly);
		sendOrder.setBackgroundColor(Color.argb(80, 0, 0, 0));
		saveOrder.setBackgroundColor(Color.argb(80, 0, 0, 0));
		sendOrder.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isOnLine()) {
					if (addressfield.getText().toString().equals("")
							|| phonefield.getText().toString().equals("")) {
						AppUtilities
								.prompt(context,
										"Address & Phone Cant be Empty \nGive Us Contacts Info and address");
					} else {
						storeaddresslocaly();
						int countsentdata = 0;
						for (int i = 0; i < thelistofname.size(); i++) {
							String index = Integer.toString(i);
							new task().execute("item", index);
							countsentdata++;
						}
						if (countsentdata == thelistofname.size()) {
							new task().execute("order");
							new task().execute("list");
							AppUtilities.goTo(context, "UserDashBoardActivity");
						}
					}
				} else {
					storeaddresslocaly();
					orderDialog(
							"No Internet Or Wifi Connection \nYou cant Send Order Without connection",
							"No Connection");
				}
			}
		});
	}

	private void populatelv(ListView listview, String SeriveList) {
		ArrayList<String> finalList = new ArrayList<>();

		if (thelistofname.size() == 0) {
			thelistofname.add("empty " + SeriveList);
		} else {
			finalList.add(0, SeriveList + " list");
			for (int i = 0; i < thelistofname.size(); i++) {
				finalList.add(thelistofname.get(i));
			}

		}
		listview.setAdapter(AppUtilities.itemsArrayAdapter(context, listview,
				thelistofname, thelistofNotes, "Note: "));
	}

	class task extends AsyncTask<String, Void, JSONObject> {
		ProgressDialog dialog;

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(DIALOG1_KEY, b);
			showprogess = true;
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			String phonenumber = phonefield.getText().toString();
			String address = addressfield.getText().toString();
			String comment = addressmoreinfofield.getText().toString();
			String fullAddress = address + "\n" + comment;

			String listCreateAt = getIntent().getExtras()
					.getString("createdat");
			String ordertimestamp = new Date().toString();
			String orderItemsList = getIntent().getExtras().getString(
					"listname");
			String orderschedule = getIntent().getExtras().getString(
					"scheduletime");
			// localdb.addOrder(fullAddress, ordertimestamp, orderItemsList,
			// "Started", orderschedule);
			String service = getIntent().getExtras().getString("service");
			order = new Order(orderItemsList, fullAddress, "Started",
					ordertimestamp, orderschedule);
			listofItem = new ItemList(listCreateAt, orderItemsList,
					orderschedule, service);
			if (params[0].equals("item")) {
				int index = Integer.parseInt(params[1]);
				item = new com.arkhm.freshlist.models.Item(
						thelistofname.get(index), thelistofNotes.get(index),
						orderItemsList);
				JSONObject object = item.addItem(thelistofname.get(index),
						thelistofNotes.get(index), orderItemsList);
				return object;

			} else if (params[0].equals("order")) {
				return order.sendOrder(phonenumber, address, "Started",
						ordertimestamp, comment);
			} else if (params[0].equals("list")) {
				return listofItem.addItemList(ordertimestamp, orderItemsList,
						orderschedule, service);
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			dismissDialog(DIALOG1_KEY);
			try {
				JSONObject meta = result.getJSONObject("meta");
				if (meta.getInt("total_count") > 0) {
					JSONArray objects = result.getJSONArray("objects");
					for (int i = 0; i < objects.length(); i++) {
						JSONObject obj = objects.getJSONObject(i);
						Log.i("obj no: " + i, obj.toString());
					}
				} else {
					AppUtilities.prompt(context, "didnt get data");
				}
				//
				// if (result.getInt("success") == 1) {
				// messagesent = result.getString("message");
				// showprogess = false;
				// Log.d("inserted item", result.getString("message"));
				// if (result.getString("message").equals(
				// "Order successfully Sent and Received")) {
				// AppUtilities.prompt(context,
				// result.getString("message"));
				// }
				// } else if (result.getInt("success") == 0) {
				// messagesent = result.getString("message");
				// AppUtilities.prompt(context, result.getString("message"));
				// }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle b) {
		switch (id) {
		case DIALOG1_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Sending Order ...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		}
		case DIALOG2_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Order Sent");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	private void orderDialog(String message, String title) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setPositiveButton("Put Wifi On",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						manager.setWifiEnabled(true);
						dialog.dismiss();
					}

				});

		dialog.setNegativeButton("Put Data On",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							setMobileDataEnabled(context, true);
						} catch (NoSuchFieldException | ClassNotFoundException
								| IllegalArgumentException
								| IllegalAccessException
								| NoSuchMethodException
								| InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
		dialog.create();
		dialog.show();
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

	private void storeaddresslocaly() {
		if (prefs.getString("address1", "") != null) {
			editor.remove("address1");
			editor.remove("address2");
			editor.commit();
		}

		editor.putString("address1", addressfield.getText().toString());
		editor.putString("address2", addressmoreinfofield.getText().toString());
		editor.putString("phone_number", phonefield.getText().toString());
		editor.commit();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		AppUtilities.goTo(context, "UserDashBoardActivity");
	}

	private static void setMobileDataEnabled(Context context, boolean enabled)
			throws NoSuchFieldException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final Class<?> conmanClass = Class.forName(conman.getClass().getName());

		final Field iConnectivityManagerField = conmanClass
				.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = iConnectivityManagerField
				.get(conman);
		final Class<?> iConnectivityManagerClass = Class
				.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass
				.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			AppUtilities.goTo(context, "UserDashBoardActivity");
		}
		return true;
	}
}
