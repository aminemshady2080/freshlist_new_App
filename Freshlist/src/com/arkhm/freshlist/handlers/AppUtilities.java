package com.arkhm.freshlist.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AppUtilities {

	public AppUtilities() {
		// TODO Auto-generated constructor stub

	}

	public static SimpleAdapter itemsArrayAdapter(Context context,
			ListView addedList, ArrayList<String> itemsnames,
			ArrayList<String> subdatalist, String subdataprefix) {
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		for (int j = 0; j < itemsnames.size(); j++) {
			Map<String, String> curChildMap = new HashMap<String, String>();

			curChildMap.put("tops", itemsnames.get(j));
			curChildMap.put("subs", subdataprefix + subdatalist.get(j));
			items.add(curChildMap);
		}
		addedList.setHeaderDividersEnabled(true);
		addedList.setFooterDividersEnabled(true);

		SimpleAdapter ad = new SimpleAdapter(context, items,
				android.R.layout.simple_list_item_2, new String[] { "tops",
						"subs" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		return ad;
	}

	public static Toast prompt(Context context, String message) {
		Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
		t.setGravity(Gravity.TOP, 10, 50);
		t.show();
		return t;
	}

	public static void goTo(Context context, String gotoActivity) {
		try {

			Class<?> togo = Class
					.forName("com.arkhm.freshlist." + gotoActivity);
			Intent intent = new Intent(context, togo);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void logUserOut(Context context, LocalDBHandler localDb) {
		localDb.logUserOut();
		SharedPreferences sp = context.getSharedPreferences(
				"FreshlistUserPrefs", 0);
		SharedPreferences.Editor editor = sp.edit();
	//	editor.remove("isLoggedIn");
		editor.putBoolean("isLoggedIn", false);
		editor.commit();
		goTo(context, "WelcomePageActivity");
	}
	
}
