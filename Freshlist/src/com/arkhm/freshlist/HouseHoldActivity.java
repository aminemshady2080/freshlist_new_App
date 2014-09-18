package com.arkhm.freshlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.arkhm.freshlist.models.Item;
import com.arkhm.freshlist.models.ItemList;

public class HouseHoldActivity extends Activity {

	EditText listname, listschedule;
	AutoCompleteTextView itemfield;
	Button addtolist, donebut, cancelbut;
	ListView addedList;
	static ArrayList<String> itemArraylist = new ArrayList<String>();
	public static String listscheduledTime;
	String itemName = "";
	Item item = new Item();
	ItemList itemlist = new ItemList();
	protected int selectedItemPostion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.household_layout);
		setTitle("HouseHold Services");
		initialise();

	}

	private void initialise() {
		listname = (EditText) findViewById(R.id.listnamehousehold);
		listschedule = (EditText) findViewById(R.id.schedulefieldhousehold);
		itemfield = (AutoCompleteTextView) findViewById(R.id.householddescription);
		addtolist = (Button) findViewById(R.id.addhouseholdbut);
		donebut = (Button) findViewById(R.id.donehouseholdbut);
		cancelbut = (Button) findViewById(R.id.cancelhouseholdbut);

		addedList = (ListView) findViewById(R.id.householdlv);

		String[] groceries = getResources().getStringArray(
				R.array.householdservices);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				HouseHoldActivity.this, R.layout.singledescription, groceries);
		itemfield.setAdapter(adapter);

		addtolist.setOnClickListener(new clicklisteners());
		donebut.setOnClickListener(new clicklisteners());
		cancelbut.setOnClickListener(new clicklisteners());

		listviewclickslistener();
	}

	class clicklisteners implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.addhouseholdbut:
				if (itemfield.getText().toString().equals("")) {
					WelcomePageActivity
							.prompt("No Item Described \n  \t example \n Wash all clothes and uniforms");
				} else {
					itemName = itemfield.getText().toString();
					item.setItemName(itemName);
					itemArraylist.add(itemName);
					itemlist.add(item);
					refreshlists();
					itemfield.setText("");
				}
				break;
			case R.id.donehouseholdbut:
				if (listschedule.getText().toString().equals("")) {
					WelcomePageActivity.prompt("No Time and date scheduled");
				} else if (addedList.getChildCount() == 0) {
					WelcomePageActivity
							.prompt("No HouseHold activity added \n on HouseHold list");
				} else {

					listscheduledTime = listschedule.getText().toString();
					try {
						Class<?> togo = Class
								.forName("com.arkhm.freshlist.ServicesActivity");
						Intent intent = new Intent(HouseHoldActivity.this, togo);
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				break;
			case R.id.cancelhouseholdbut:
				try {
					Class<?> togo = Class
							.forName("com.arkhm.freshlist.ServicesActivity");
					Intent intent = new Intent(HouseHoldActivity.this, togo);
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}

		}

	}

	private void listviewclickslistener() {
		addedList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItemPostion = position;
			}
		});
		addedList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItemPostion = position;
				return false;
			}
		});
		addedList.setOnCreateContextMenuListener(this);
	}

	private void removeDatafromLists() {
		itemArraylist.remove(selectedItemPostion);
		itemlist.remove(selectedItemPostion);
	}

	private void refreshlists() {
		addedList.removeAllViewsInLayout();
		addedList.setAdapter(new ArrayAdapter<String>(HouseHoldActivity.this,
				R.layout.singledata, itemArraylist));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.lvmenu1, menu);
		registerForContextMenu(addedList);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menudel:

			Toast.makeText(
					getApplicationContext(),
					"Deleted "
							+ addedList.getItemAtPosition(selectedItemPostion),
					Toast.LENGTH_SHORT).show();
			removeDatafromLists();
			refreshlists();
			break;
		case R.id.menuedit:
			itemfield.setText(itemArraylist.get(selectedItemPostion));
			removeDatafromLists();
			refreshlists();
			break;
		}
		return super.onContextItemSelected(item);
	}

}
