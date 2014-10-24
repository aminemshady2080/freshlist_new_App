package com.arkhm.freshlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.models.User;

public class UserDashBoardActivity extends ActionBarActivity {

	ExpandableListView expandService1;
	ExpandableListView expandService2;
	ExpandableListAdapter adapter;
	ImageView newListBut;
	public static ImageView userPic;
	static TextView userNameTv;
	User user = new User();
	LocalDBHandler localDb = null;
	int itempostion;
	public static String db_item_fromlist;
	static Context context;
	SharedPreferences prefs;
	LinearLayout topcontainer;
	private TextView userMailTv, userAddressTv,userPhoneTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar();
		setContentView(R.layout.user_dashboard_layout);
		prefs = getSharedPreferences("FreshlistUserPrefs", 0);
		context = UserDashBoardActivity.this;
		localDb = new LocalDBHandler(context);
		initialize();

	}

	private void initialize() {
		expandService1 = (ExpandableListView) findViewById(R.id.expandableListView1);
		expandService2 = (ExpandableListView) findViewById(R.id.expandableListView2);
		userNameTv = (TextView) findViewById(R.id.dashBoarduserName);
		userMailTv = (TextView) findViewById(R.id.dashBoardusermail);
		userAddressTv = (TextView) findViewById(R.id.dashBoarduseraddress);
		userPhoneTv=  (TextView) findViewById(R.id.dashBoardphonenumber);
		topcontainer = (LinearLayout) findViewById(R.id.topcontainer);
		
		userPic = (ImageView) findViewById(R.id.userPic);
		postUserImage();
		newListBut = (ImageView) findViewById(R.id.dashbonewlist);
		newListBut.setBackgroundColor(Color.argb(100, 255, 255, 255));

		LinearLayout seriveceslistcont = (LinearLayout) findViewById(R.id.serviceslistcontainer);
		seriveceslistcont.setBackgroundColor(Color.argb(60, 0, 0, 255));

		String[] service1 = { "Groceries and Shopping" };
		String[] service2 = { "Packages Delivery" };

		String LIST_NAME = "list_name";
		String LIST_TIMESTAMP = "list_timestamp";

		final String service1ListsName[] = localDb.getAllListsNames(
				"Groceries and Shopping", LIST_NAME);
		final String service1ListsTimeStamps[] = localDb.getAllListsNames(
				"Groceries and Shopping", LIST_TIMESTAMP);
		final String service2ListsNames[] = localDb.getAllListsNames(
				"Packages Delivery", LIST_NAME);

		final String service2ListsTimeStamps[] = localDb.getAllListsNames(
				"Packages Delivery", LIST_TIMESTAMP);

		populateServicesLists(expandService1, service1, service1ListsName,
				service1ListsTimeStamps);
		populateServicesLists(expandService2, service2, service2ListsNames,
				service2ListsTimeStamps);

		newListBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtilities.goTo(context, "BuildAListActivity");
			}
		});

		expandService1.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				getAndSendDataToEdit("Groceries and Shopping",
						service1ListsName, service1ListsTimeStamps,
						childPosition);

				return true;
			}
		});
		expandService2.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				getAndSendDataToEdit("Packages Delivery", service2ListsNames,
						service2ListsTimeStamps, childPosition);
				return true;
			}
		});
		topcontainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtilities.goTo(context, "UserAccountSettingsActivity");
			}
		});

	}

	private void postUserImage() {
		byte[] img = null;
		if (localDb.getUserPic() != null) {
			img = localDb.getUserPic();
			userPic.setImageBitmap(BitmapFactory.decodeByteArray(img, 0,
					img.length));
		} else {

		}
	}



	private void populateServicesLists(ExpandableListView theList,
			String[] services, String[] lists, String[] createdAt) {

		List<Map<String, String>> ServicesArray = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> ListsMadeArray = new ArrayList<List<Map<String, String>>>();

		for (int i = 0; i < services.length; i++) {
			Map<String, String> curGroupMap = new HashMap<String, String>();
			ServicesArray.add(curGroupMap);
			curGroupMap.put("name", services[i]);
			curGroupMap.put("Servicename", services[i]);

			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (int j = 0; j < lists.length; j++) {
				Map<String, String> curChildMap = new HashMap<String, String>();
				children.add(curChildMap);
				curChildMap.put("listName", lists[j]);
				curChildMap.put("subList", "Created on: " + createdAt[j]);
			}
			ListsMadeArray.add(children);
		}

		adapter = new SimpleExpandableListAdapter(context, ServicesArray,
				android.R.layout.simple_expandable_list_item_1,
				new String[] { "name" }, new int[] { android.R.id.text1 },
				ListsMadeArray, android.R.layout.simple_expandable_list_item_2,
				new String[] { "listName", "subList" }, new int[] {
						android.R.id.text1, android.R.id.text2 });

		theList.setAdapter(adapter);
	}

	private void getAndSendDataToEdit(String whichService,
			final String[] serviceListsName, final String[] listschedules,
			int childPosition) {
		String LIST_SCHEDULE = "list_schedule";
		String schedule = localDb.getAllListsNames(whichService, LIST_SCHEDULE)[childPosition];
		String ITEM_NAME = "item_name", ITEM_NOTE = "item_note";
		itempostion = childPosition;
		db_item_fromlist = serviceListsName[childPosition];
		String timeStamp = timeStramp(listschedules, childPosition);
		String allItems[] = localDb.getAllItemsNames(db_item_fromlist,
				ITEM_NAME);
		String allItemsNote[] = localDb.getAllItemsNames(db_item_fromlist,
				ITEM_NOTE);
		for (int i = 0; i < allItems.length; i++) {
			Log.d("item " + i + " : ", allItems[i] + "note : "
					+ allItemsNote[i]);
		}

		try {
			Class<?> togo = Class
					.forName("com.arkhm.freshlist.EditListActivity");
			Intent intent = new Intent(UserDashBoardActivity.this, togo);
			intent.putExtra("allItemsExtraData", allItems);
			intent.putExtra("allItemsNotesExtraData", allItemsNote);
			intent.putExtra("sentListName", db_item_fromlist);
			intent.putExtra("time", timeStamp);
			intent.putExtra("schedule", schedule);
			intent.putExtra("service", whichService);
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String timeStramp(String[] listschedule, int listIndex) {
		return listschedule[listIndex];
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.optionmenu1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			AppUtilities.goTo(context, "UserAccountSettingsActivity");
			break;
		case R.id.action_logout:
			AppUtilities.logUserOut(context, localDb);
			break;

		case R.id.action_help:
			AppUtilities.goTo(context, "HelpAndAboutActivity");
			break;
		case R.id.action_about:
			Class<?> togo;
			try {
				togo = Class
						.forName("com.arkhm.freshlist.HelpAndAboutActivity");
				Intent intent = new Intent(context, togo);
				intent.putExtra("need_about", "#about");
				startActivity(intent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initialize();
		String USER_NAME = "user_name";
		String USER_MAIL = "user_mail";
		String uname="Username: " + localDb.getCurrentUser(USER_NAME);
		userNameTv.setText(uname);
		userMailTv.setText("Email: " + localDb.getCurrentUser(USER_MAIL));
		userAddressTv.setText("Address: " + prefs.getString("address1", "") + "\nAt: "
				+ prefs.getString("address2", ""));
		userPhoneTv.setText("Phone Number: "+prefs.getString("phone_number", ""));
	}

	@Override
	public void onBackPressed() {
		finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
