package com.arkhm.freshlist;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TimePicker;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.models.ItemList;

public class BuildAListActivity extends ActionBarActivity {
	TextView clickToschedule;
	String timeSet = "";
	TimePicker timepicker;
	DatePicker datePicker;
	Button setTimeBut, cancelTimeBut, confirmListbut, CancelListBut;
	LinearLayout setTimeLinearl, confirmLinearl;
	RadioButton serviceGrocery, servicePackageDelivey;
	EditText listNameField;
	RadioGroup choseServiceLinearL;

	ItemList itemsList = new ItemList();
	protected Context context;
	public static String db_item_list;
	TabHost tabhost;
	LocalDBHandler localDBHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build_a_list_layout);
		context = this;
		localDBHandler = new LocalDBHandler(context);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initialize();
		listeners();
	}

	private void initialize() {
		clickToschedule = (TextView) findViewById(R.id.clicktoschedule);
		datePicker = (DatePicker) findViewById(R.id.datePicker1);
		datePicker.setBackgroundColor(Color.argb(150, 255, 255, 255));
		timepicker = (TimePicker) findViewById(R.id.timePicker1);
		timepicker.setBackgroundColor(Color.argb(150, 255, 255, 255));
		setTimeLinearl = (LinearLayout) findViewById(R.id.timesetLinearLayout);
		choseServiceLinearL = (RadioGroup) findViewById(R.id.choseSeriveLinearLayout);
		confirmLinearl = (LinearLayout) findViewById(R.id.confirmlistLinearLayout);
		setTimeBut = (Button) findViewById(R.id.setit);
		cancelTimeBut = (Button) findViewById(R.id.cancelit);
		confirmListbut = (Button) findViewById(R.id.finishbuildListBut);
		CancelListBut = (Button) findViewById(R.id.cancebuildListBut);
		setTimeBut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		cancelTimeBut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		confirmListbut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		CancelListBut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		serviceGrocery = (RadioButton) findViewById(R.id.seriviceradioButton1);
		servicePackageDelivey = (RadioButton) findViewById(R.id.seriviceradioButton2);
		listNameField = (EditText) findViewById(R.id.listnameEditText);

		tabhost = (TabHost) findViewById(R.id.datesTabhost);
		tabhost.setup();
		tabhost.setBackgroundColor(Color.WHITE);
		TabSpec dateSpecs = tabhost.newTabSpec("tag1");
		dateSpecs.setContent(R.id.datePicker1layout);
		dateSpecs.setIndicator("Day");
		tabhost.addTab(dateSpecs);
		dateSpecs = tabhost.newTabSpec("tag2");
		dateSpecs.setContent(R.id.timePickerlayout);
		dateSpecs.setIndicator("Time");
		tabhost.addTab(dateSpecs);

		customizeDatePicker();

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void customizeDatePicker() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			datePicker.setCalendarViewShown(false);
		}
	}

	private void listeners() {
		clickToschedule.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTimeLinearl.setVisibility(View.VISIBLE);
				listNameField.setVisibility(View.GONE);
				choseServiceLinearL.setVisibility(View.GONE);
				confirmLinearl.setVisibility(View.GONE);
				clickToschedule.setVisibility(View.GONE);
			}
		});
		setTimeBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buildTimeToString();
				setTimeLinearl.setVisibility(View.GONE);
				listNameField.setVisibility(View.VISIBLE);
				choseServiceLinearL.setVisibility(View.VISIBLE);
				confirmLinearl.setVisibility(View.VISIBLE);
				clickToschedule.setVisibility(View.VISIBLE);
			}
		});
		cancelTimeBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setTimeLinearl.setVisibility(View.GONE);
				AppUtilities.prompt(context,
						"You Must Set the time this service list is scheduled");
				clickToschedule.setVisibility(View.VISIBLE);
				listNameField.setVisibility(View.VISIBLE);
			}
		});
		confirmListbut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String listName = "";
				String listService = "";
				if (listNameField.getText().toString().length() == 0) {
					AppUtilities
							.prompt(context, "List Name is Required Please");
				}
				else if(localDBHandler.getListFieldNumber(listNameField.getText().toString())>0){
					AppUtilities
					.prompt(context, "List Name Already Exists\nPick a new Listname");
				}
				
				else {
					listName = listNameField.getText().toString();
					db_item_list = listNameField.getText().toString();
					LocalDBHandler localDb = new LocalDBHandler(
							getApplicationContext());
					if (serviceGrocery.isChecked()) {
						listService = serviceGrocery.getText().toString();
						finishToBuildList(listName, listService, localDb);

					} else if (servicePackageDelivey.isChecked()) {
						listService = servicePackageDelivey.getText()
								.toString();
						finishToBuildList(listName, listService, localDb);

					} else {
						AppUtilities
								.prompt(context,
										"You need to check for which Service this list belongs to");
					}
				}
			}

			private void finishToBuildList(String listName, String listService,
					LocalDBHandler localDb) {
				Date date = new Date();
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				String createdDate = cal.get(Calendar.DATE) + "/"
						+ cal.get(Calendar.MONTH) + "/"
						+ cal.get(Calendar.YEAR) + " at "
						+ cal.get(Calendar.HOUR) + ":"
						+ cal.get(Calendar.MINUTE);

				itemsList.setTitle(listName);
				itemsList.setTimeStamp(createdDate);
				itemsList.setScheduledTime(timeSet);
				itemsList.setListServices(listService);
				System.out.println(itemsList.toString());

				localDb.addList(listName, timeSet, createdDate, listService);

				localDb.printDbData("LIST");
				try {
					Class<?> togo = Class
							.forName("com.arkhm.freshlist.AddItemsToListActivity");
					Intent intent = new Intent(BuildAListActivity.this, togo);
					intent.putExtra("service", listService);
					intent.putExtra("scheduletime", timeSet);
					intent.putExtra("nowListname", listName);
					intent.putExtra("createdat", createdDate);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		CancelListBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppUtilities.goTo(context, "UserDashBoardActivity");
			}
		});
	}

	private void buildTimeToString() {
		String day = datePicker.getYear() + "/" + (datePicker.getMonth() + 1)
				+ "/" + datePicker.getDayOfMonth();
		String time = timepicker.getCurrentHour() + " : "
				+ timepicker.getCurrentMinute();
		timeSet = day + " at " + time;
		clickToschedule.setText("You Have Set Time to \n " + timeSet);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			AppUtilities.goTo(context, "UserDashBoardActivity");
		}
		return true;
	}

}
