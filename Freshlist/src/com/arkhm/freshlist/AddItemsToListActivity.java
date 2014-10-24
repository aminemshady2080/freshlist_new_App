package com.arkhm.freshlist;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.models.Item;
import com.arkhm.freshlist.models.ItemList;

public class AddItemsToListActivity extends ActionBarActivity {

	EditText itemNoteField;
	AutoCompleteTextView itemfield;
	Button addtolist, donebut, cancelbut;
	public static ListView addedList;
	static ArrayList<String> itemArraylist = new ArrayList<>();
	static ArrayList<String> itemNoteArraylist = new ArrayList<>();
	public static String itemName = "", itemNote = "", itemOfList = "";

	Item item = new Item();
	ItemList itemlist = new ItemList();
	protected int selectedItemPostion;
	LocalDBHandler localdb;
	public Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additems_tolist_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		context = this;
		setTitle("Add Items to "
				+ getIntent().getExtras().getString("nowListname"));
		initialise();

	}

	private void initialise() {
		itemfield = (AutoCompleteTextView) findViewById(R.id.grocerydescription);
		addtolist = (Button) findViewById(R.id.addgrocerybut);
		donebut = (Button) findViewById(R.id.donegrocerybut);
		cancelbut = (Button) findViewById(R.id.cancelgrocerybut);
		donebut.setBackgroundColor(Color.argb(80, 0, 0, 0));
		cancelbut.setBackgroundColor(Color.argb(80, 0, 0, 0));

		addedList = (ListView) findViewById(R.id.grocerylv);

		String[] groceries = getResources().getStringArray(R.array.grocelies);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.singledescription, groceries);
		itemfield.setAdapter(adapter);
		addtolist.setOnClickListener(new clicklisteners());
		donebut.setOnClickListener(new clicklisteners());
		cancelbut.setOnClickListener(new clicklisteners());

		listviewclickslistener();

		itemfield.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				dealWithAddButton();
				return true;
			}
		});
	}

	protected void dealWithAddButton() {
		if (itemfield.getText().toString().equals("")) {
			AppUtilities.prompt(context,
					"No Item Described \n  \t example \n 5 packes of coffee");
		} else {
			AlertDialog.Builder dialogBuilder = buildDialog();
			dialogBuilder.show();
		}
	}

	class clicklisteners implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.addgrocerybut:
				dealWithAddButton();
				break;
			case R.id.donegrocerybut:
				if (addedList.getChildCount() == 0) {
					AppUtilities.prompt(context, "No items added on list");
				} else {
					String listname = getIntent().getExtras().getString(
							"nowListname");
					String scheduletime = getIntent().getExtras().getString(
							"scheduletime");
					String createdat = getIntent().getExtras().getString(
							"createdat");
					itemOfList = BuildAListActivity.db_item_list;
					for (int i = 0; i < itemArraylist.size(); i++) {
						String db_itemname = itemArraylist.get(i);
						localdb = new LocalDBHandler(getApplicationContext());
						String db_itemNote = itemNoteArraylist.get(i);
						localdb.addItem(db_itemname, db_itemNote, itemOfList);
					}

					try {
						Class<?> togo = Class
								.forName("com.arkhm.freshlist.ContactInfoActivity");
						Intent intent = new Intent(AddItemsToListActivity.this,
								togo);
						intent.putExtra("createdat", createdat);
						intent.putExtra("listname", listname);
						intent.putExtra("scheduletime", scheduletime);
						intent.putExtra("builtList", itemArraylist);
						intent.putExtra("addedNotes", itemNoteArraylist);
						intent.putExtra("service",
								getIntent().getStringExtra("service"));
						startActivity(intent);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				break;
			case R.id.cancelgrocerybut:
				AppUtilities.goTo(context, "UserDashBoardActivity");
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
		itemNoteArraylist.remove(selectedItemPostion);
		itemlist.remove(selectedItemPostion);
	}

	private void refreshlists() {
		addedList.removeAllViewsInLayout();
		new AppUtilities();
		SimpleAdapter adapter = AppUtilities.itemsArrayAdapter(
				AddItemsToListActivity.this, addedList, itemArraylist,
				itemNoteArraylist, "Note: ");
		addedList.setAdapter(adapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.itemslistmenu, menu);
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
			itemNoteField.setText(itemNote);
			removeDatafromLists();
			refreshlists();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private AlertDialog.Builder buildDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				AddItemsToListActivity.this);
		itemNoteField = new EditText(AddItemsToListActivity.this);
		itemNoteField.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME);
		itemNoteField.setImeOptions(EditorInfo.IME_ACTION_DONE);
		dialogBuilder.setView(itemNoteField);
		dialogBuilder.setTitle("add note to this item");
		dialogBuilder.setPositiveButton("Add Note",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						itemNote = itemNoteField.getText().toString();
						itemName = itemfield.getText().toString();
						item.setItemName(itemName);
						itemArraylist.add(itemName);
						itemNoteArraylist.add(itemNote);
						itemlist.add(item);
						dialog.dismiss();
						itemfield.setText("");
						refreshlists();
					}
				});
		dialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();

					}
				});
		return dialogBuilder;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			AppUtilities.goTo(context, "UserDashBoardActivity");
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		itemArraylist.clear();
		itemNoteArraylist.clear();
		refreshlists();
	}
}
