package com.arkhm.freshlist;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.arkhm.freshlist.handlers.AppUtilities;
import com.arkhm.freshlist.handlers.LocalDBHandler;
import com.arkhm.freshlist.models.Item;
import com.arkhm.freshlist.models.ItemList;

public class EditListActivity extends ActionBarActivity {

	public ArrayList<String> itemArraylist = new ArrayList<>();
	public ArrayList<String> itemNoteArraylist = new ArrayList<>();
	AutoCompleteTextView itemfield;
	Button addItemBut;
	TextView sendListBut, EditlistBut, deleteListBut;
	LinearLayout bottomLnlayout;
	ListView addedList;
	public String itemName = "", itemNote = "", itemOfList = "";
	Item item = new Item();
	ItemList itemlist = new ItemList();
	protected int selectedItemPostion;
	LocalDBHandler localdb;
	Context context;
	EditText itemNoteField;
	String newScheduleTime, newListName, finalListName;
	AppUtilities apptools = new AppUtilities();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editlist_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getIntent().getStringExtra("sentListName"));
		context = this;
		localdb = new LocalDBHandler(getApplicationContext());
		initialize();

	}

	private void initialize() {
		itemfield = (AutoCompleteTextView) findViewById(R.id.editlistgrocerydescription);
		addItemBut = (Button) findViewById(R.id.editlistaddgrocerybut);
		sendListBut = (TextView) findViewById(R.id.editlistdonebut);
		EditlistBut = (TextView) findViewById(R.id.editlistdetailsbut);
		deleteListBut = (TextView) findViewById(R.id.editlistdeletesbut);
		bottomLnlayout = (LinearLayout) findViewById(R.id.editlistLnLayout);
		bottomLnlayout.setBackgroundColor(Color.argb(80, 0, 0, 0));
		addedList = (ListView) findViewById(R.id.editlistlv);
		// addedList.setBackgroundColor(Color.argb(50, 0, 0, 255));
		getDataFromDashBoard();
		String[] groceries = getResources().getStringArray(R.array.grocelies);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				getApplicationContext(), R.layout.singledescription, groceries);
		itemfield.setAdapter(adapter);
		finalListName = getIntent().getStringExtra("sentListName");
		addItemBut.setOnClickListener(new clicklisteners());
		sendListBut.setOnClickListener(new clicklisteners());
		EditlistBut.setOnClickListener(new clicklisteners());
		deleteListBut.setOnClickListener(new clicklisteners());

		itemfield.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				dealWithAddButton();
				return true;
			}
		});
		listviewclickslistener();
	}

	void getDataFromDashBoard() {
		itemOfList = getIntent().getStringExtra("sentListName");
		String[] sentArrayOfItems = getIntent().getExtras().getStringArray(
				"allItemsExtraData");
		String[] sentArrayOfItemsNotes = getIntent().getExtras()
				.getStringArray("allItemsNotesExtraData");
		for (int i = 0; i < sentArrayOfItems.length; i++) {
			System.out.println("item sent: " + sentArrayOfItems[i] + " note: "
					+ sentArrayOfItemsNotes[i]);
			itemArraylist.add(sentArrayOfItems[i]);
			itemNoteArraylist.add(sentArrayOfItemsNotes[i]);
		}
		SimpleAdapter adapter = AppUtilities.itemsArrayAdapter(
				EditListActivity.this, addedList, itemArraylist,
				itemNoteArraylist, "Note: ");
		addedList.setAdapter(adapter);
		newScheduleTime = getIntent().getStringExtra("schedule");
	}

	class clicklisteners implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.editlistaddgrocerybut:
				dealWithAddButton();
				break;
			case R.id.editlistdonebut:
				if (addedList.getChildCount() == 0) {
					AppUtilities.prompt(context, "No Item added on list");
				} else {
					localdb.clearTable("ITEM",
							getIntent().getStringExtra("sentListName"));
					String createdat = getIntent().getExtras()
							.getString("time");
					for (int i = 0; i < itemArraylist.size(); i++) {
						String db_itemname = itemArraylist.get(i);
						String db_itemNote = itemNoteArraylist.get(i);
						localdb.addItem(db_itemname, db_itemNote, itemOfList);
						item.setItemName(db_itemname);
						item.setItemNote(db_itemNote);
						itemlist.add(item);
					}

					try {
						Class<?> togo = Class
								.forName("com.arkhm.freshlist.ContactInfoActivity");
						Intent intent = new Intent(EditListActivity.this, togo);
						intent.putExtra("createdat", createdat);
						intent.putExtra("builtList", itemArraylist);
						intent.putExtra("addedNotes", itemNoteArraylist);
						intent.putExtra("listname", finalListName);
						intent.putExtra("scheduletime", newScheduleTime);
						intent.putExtra("service",
								getIntent().getStringExtra("service"));

						startActivity(intent);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				break;
			case R.id.editlistdetailsbut:
				AlertDialog.Builder dialogBuilder = listDetailsDialog();

				dialogBuilder.show();
				break;
			case R.id.editlistdeletesbut:
				final String timestamp = getIntent().getExtras().getString(
						"time");

				AlertDialog.Builder attention = new AlertDialog.Builder(
						EditListActivity.this);
				attention.setTitle("Warning about to delete");
				attention
						.setMessage("You are about to delete this list forever, "
								+ "You will never use it again. \nDo you still need to delete it");
				attention.setPositiveButton("Yes: Delete It",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (localdb.deleteAList(finalListName,
										timestamp) == 1) {
									AppUtilities.prompt(context, "Deleted "
											+ finalListName);
									AppUtilities.goTo(context,
											"UserDashBoardActivity");
								}
							}
						});
				attention.setNegativeButton("No: Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				attention.create();
				attention.show();

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
	}

	private void refreshlists() {
		addedList.removeAllViewsInLayout();
		SimpleAdapter ad = AppUtilities.itemsArrayAdapter(
				EditListActivity.this, addedList, itemArraylist,
				itemNoteArraylist, "Note: ");
		addedList.setAdapter(ad);

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
			removeDatafromLists();
			refreshlists();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private AlertDialog.Builder buildDialog() {
		final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
				EditListActivity.this);
		itemNoteField = new EditText(EditListActivity.this);
		itemNoteField.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME);
		itemNoteField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		dialogBuilder.setView(itemNoteField);
		dialogBuilder.setTitle("add note to this item");
		dialogBuilder.setPositiveButton("Add Note",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						itemNote = itemNoteField.getText().toString();
						itemName = itemfield.getText().toString();
						itemArraylist.add(itemName);
						itemNoteArraylist.add(itemNote);
						dialog.dismiss();
						itemfield.setText("");
						refreshlists();
					}
				});
		dialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return dialogBuilder;
	}

	private AlertDialog.Builder listDetailsDialog() {
		Context con = EditListActivity.this;
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.HORIZONTAL);
		final EditText lstnm = new EditText(context);
		lstnm.setText(itemOfList);
		lstnm.setWidth(250);
		lstnm.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		lstnm.setImeOptions(EditorInfo.IME_ACTION_DONE);
		TextView tv = new TextView(context);
		tv.setText(" Listname: ");
		l.addView(tv);
		l.addView(lstnm);
		final TimePicker tm = new TimePicker(con);
		tm.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		tm.setVisibility(View.INVISIBLE);
		final DatePicker dt = new DatePicker(con);
		dt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		customizeDatePicker(dt);
		LinearLayout layout = new LinearLayout(EditListActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout finallayout = new LinearLayout(EditListActivity.this);
		finallayout.setOrientation(LinearLayout.VERTICAL);
		finallayout.addView(l);
		FrameLayout frame = new FrameLayout(context);
		frame.setId(android.R.id.tabcontent);
		frame.addView(dt);
		frame.addView(tm);
		layout.addView(frame);
		LinearLayout toplayout = new LinearLayout(EditListActivity.this);
		toplayout.setOrientation(LinearLayout.HORIZONTAL);
		toplayout.setGravity(Gravity.CENTER);
		toplayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		final Button b1 = new Button(context);
		b1.setText("Change Day");
		b1.setBackgroundColor(Color.parseColor("#00ddff"));
		b1.setTextColor(Color.WHITE);
		final Button b2 = new Button(context);
		b2.setText("Change Time");
		b2.setBackgroundColor(Color.WHITE);
		toplayout.addView(b1);
		toplayout.addView(b2);
		finallayout.addView(toplayout);
		finallayout.addView(layout);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				b1.setBackgroundColor(Color.CYAN);
				b1.setTextColor(Color.WHITE);
				b2.setBackgroundColor(Color.WHITE);
				b2.setTextColor(Color.BLACK);
				dt.setVisibility(View.VISIBLE);
				tm.setVisibility(View.INVISIBLE);
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				b2.setBackgroundColor(Color.CYAN);
				b2.setTextColor(Color.WHITE);
				b1.setBackgroundColor(Color.WHITE);
				b1.setTextColor(Color.BLACK);
				tm.setVisibility(View.VISIBLE);
				dt.setVisibility(View.INVISIBLE);
			}
		});
		builder.setView(finallayout);
		builder.setTitle("Edit List Details");
		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!(lstnm.getText().toString().equals(itemOfList) || localdb
								.getListFieldNumber(lstnm.getText().toString()) < 1)) {
							AppUtilities
									.prompt(context,
											"List Name Already Exists\nPick a new Listname");
						} else {
							newListName = lstnm.getText().toString();
							finalListName = lstnm.getText().toString();
							newScheduleTime = buildTimeToString(dt, tm);
							String oldTimeStamp = getIntent().getExtras()
									.getString("time");
							System.out.println("Time was :" + oldTimeStamp
									+ "\n New Time: " + newScheduleTime);

							localdb.updateList(itemOfList, newListName,
									oldTimeStamp, newScheduleTime);
							EditListActivity.this.setTitle(newListName);
						}
					}
				});
		
		return builder;

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void customizeDatePicker(DatePicker datePicker) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			datePicker.setCalendarViewShown(false);
			addedList.setAlpha(1);
		}
	}

	private String buildTimeToString(DatePicker datePicker,
			TimePicker timepicker) {
		String day = datePicker.getYear() + "/" + (datePicker.getMonth() + 1)
				+ "/" + datePicker.getDayOfMonth();
		String time = timepicker.getCurrentHour() + " : "
				+ timepicker.getCurrentMinute();
		return day + " at " + time;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			AppUtilities.goTo(context, "UserDashBoardActivity");
		}
		return true;
	}

	private void dealWithAddButton() {
		if (itemfield.getText().toString().equals("")) {
			AppUtilities.prompt(context,
					"No Item Described \n  \t example \n 5 packes of coffee");
		} else {
			AlertDialog.Builder dialogBuilder = buildDialog();
			dialogBuilder.show();
		}
	}
}
