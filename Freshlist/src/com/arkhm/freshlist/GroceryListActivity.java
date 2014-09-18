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

public class GroceryListActivity extends Activity implements OnClickListener {
	AutoCompleteTextView description, shopsTextview, unityField;
	EditText quantifiels;
	Button addbut, continuebut;
	ListView addedList;
	ArrayList<String> datalsit = new ArrayList<>();
	ArrayList<String> unityslsit = new ArrayList<>();
	ArrayList<Double> quantitylsit = new ArrayList<>();
	ArrayList<String> fullDatalist = new ArrayList<>();
	int selItem;
	double thenum;
	String thename, theUnity;
	Item item = new Item();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makelist);
		initials();
		setTitle("Grocery And Shoping Service");
	}

	private void initials() {
		description = (AutoCompleteTextView) findViewById(R.id.groceryname);
		quantifiels = (EditText) findViewById(R.id.groceyquantity);
		unityField = (AutoCompleteTextView) findViewById(R.id.groceyunity);
		shopsTextview = (AutoCompleteTextView) findViewById(R.id.shopname);
		addbut = (Button) findViewById(R.id.mkbutadd);
		continuebut = (Button) findViewById(R.id.mkbutcontinue);
		addedList = (ListView) findViewById(R.id.mklv);
		addbut.setOnClickListener(this);
		continuebut.setOnClickListener(this);

		String[] groceries = getResources().getStringArray(R.array.grocelies);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.singledescription, groceries);
		description.setAdapter(adapter);
		String[] grocerydetails = getResources().getStringArray(
				R.array.grocerydetails);
		ArrayAdapter<String> adapterdetails = new ArrayAdapter<>(
				getApplicationContext(), R.layout.singledetail, grocerydetails);
		unityField.setAdapter(adapterdetails);
		String[] shops = getResources().getStringArray(R.array.Shops);
		ArrayAdapter<String> shopsAdapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.singledescription, shops);
		shopsTextview.setAdapter(shopsAdapter);

		listviewclicks();
	}

	private void listviewclicks() {
		addedList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selItem = position;
			}
		});
		addedList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				selItem = position;
				return false;
			}
		});
		addedList.setOnCreateContextMenuListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mkbutadd:

			if (description.getText().toString().equals("")) {
				WelcomePageActivity.prompt("No grocery name given");
			} else if (quantifiels.getText().toString().equals("")) {
				WelcomePageActivity.prompt("No quantity given");
			} else if (unityField.getText().toString().equals("")) {
				WelcomePageActivity.prompt("No Unity given");
			} else {

				thename = description.getText().toString();
				theUnity = unityField.getText().toString();
				thenum = Double.parseDouble(quantifiels.getText().toString());
				item.setItemName(thename);
				item.setItemQuantity(thenum);
				item.setItemUnity(theUnity);

				datalsit.add(thename);
				unityslsit.add(theUnity);
				quantitylsit.add(thenum);

				String data = thename + " \t \t" + thenum + " \t  \t"
						+ theUnity;
				fullDatalist.add(data);
				refreshAddedlist();
				description.setText("");
				quantifiels.setText("");
				unityField.setText("");
			}
			break;
		case R.id.mkbutcontinue:
			try {
				Class<?> togo = Class
						.forName("com.arkhm.freshlist.ServicesActivity");
				Intent intent = new Intent(GroceryListActivity.this, togo);
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

			Toast.makeText(getApplicationContext(),
					"Deleted " + addedList.getItemAtPosition(selItem),
					Toast.LENGTH_SHORT).show();
			removeDatafromLists();
			refreshAddedlist();

			break;
		case R.id.menuedit:
			description.setText(datalsit.get(selItem));
			unityField.setText(unityslsit.get(selItem));
			quantifiels.setText(quantitylsit.get(selItem).toString());
			removeDatafromLists();
			refreshAddedlist();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void removeDatafromLists() {
		fullDatalist.remove(selItem);
		datalsit.remove(selItem);
		unityslsit.remove(selItem);
		quantitylsit.remove(selItem);
	}

	public void makeItem() {

	}

	private void refreshAddedlist() {
		addedList.removeAllViewsInLayout();
		addedList.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.singledata, fullDatalist));

	}
}
