package com.arkhm.freshlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactInfoActivity extends Activity {

	static ListView finalGroceryLv, finalhouseHoldLv, finalChildServicesLv,
			finalBills;
	static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_info_layout);
		context = this.getApplicationContext();
		initialise();
	}

	private void initialise() {
		finalGroceryLv = (ListView) findViewById(R.id.finalgrocerylv);
		finalhouseHoldLv = (ListView) findViewById(R.id.finalhouseholdlv);
		finalChildServicesLv = (ListView) findViewById(R.id.finalchilderlv);
		finalBills = (ListView) findViewById(R.id.finalbillslv);
		populateHouseHoldlv();
		populateGrocerylv();

	}

	private void populateHouseHoldlv() {
		ArrayList<String> thelist = new ArrayList<>();

		if (HouseHoldActivity.itemArraylist.size() == 0) {
			thelist.add("Empty HouseHold list");
		} else {
			thelist.add(0, "HouseHold list");
			thelist.add(1, "scheduled: " + HouseHoldActivity.listscheduledTime);
			thelist.addAll(HouseHoldActivity.itemArraylist);
		}
		ArrayAdapter<String> householdAdapter = new ArrayAdapter<>(context,
				R.layout.singledata, thelist);
		finalhouseHoldLv.setAdapter(householdAdapter);
	}

	private void populateGrocerylv() {
		ArrayList<String> thelist = new ArrayList<>();

		if (GroceryAndShopingActivity.itemArraylist.size() == 0) {
			thelist.add("Empty Grocery & Shoping list");
		} else {
			thelist.add("Grocery & Shopping list");
			thelist.add("scheduled: "
					+ GroceryAndShopingActivity.listscheduledTime);
			thelist.add("From: " + GroceryAndShopingActivity.listShop);

			thelist.addAll(GroceryAndShopingActivity.itemArraylist);

		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
				R.layout.singledata, thelist);
		finalGroceryLv.setAdapter(adapter);
	}

}
