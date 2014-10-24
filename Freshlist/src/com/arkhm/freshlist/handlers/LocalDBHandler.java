package com.arkhm.freshlist.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arkhm.freshlist.models.User;

public class LocalDBHandler extends SQLiteOpenHelper {

	private String USER_ID = "user_id";
	private String USER_NAME = "user_name";
	private String USER_MAIL = "user_mail";
	private String USER_PASSWORD = "user_password";
	private String USER_IMAGE = "user_mage";
	private String USER_ADDRESS = "user_address";

	private String ITEM_ID = "item_id";
	private String ITEM_NAME = "item_name";
	private String ITEM_NOTE = "item_note";
	private String ITEM_LIST = "item_list";

	private String LIST_ID = "list_id";
	private String LIST_NAME = "list_name";
	private String LIST_SCHEDULE = "list_schedule";
	private String LIST_TIMESTAMP = "list_timestamp";
	private String LIST_SERVICE = "list_service";
	private String ORDER_ID = "order_id";
	private String ORDER_CONTACTINFO = "order_contactinfo";

	private String ORDER_STATUS = "order_status";
	private String ORDER_SCHEDULE = "order_schedule";

	private String ORDER_TIMESTAMP = "order_timestamp";
	private String ORDER_LIST_LIST_NAME = "list_name";
	private String ORDER_LIST_ORDER_ID = "order_id";
	private static String db_name = "fresh_list_local_db";
	SQLiteDatabase freshlistDb;
	User user = new User();
	public static String createdAt[] = {};

	public LocalDBHandler(Context context) {
		super(context, db_name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String createUserSql = "CREATE TABLE USER(" + USER_ID
				+ " INTEGER  PRIMARY KEY ," + USER_NAME + " TEXT," + USER_MAIL
				+ " TEXT," + USER_PASSWORD + " TEXT" + ")";
		String createUserSettingsSql = "CREATE TABLE USER_SETTINGS(" + USER_ID
				+ " INTEGER  PRIMARY KEY AUTOINCREMENT," + USER_MAIL + " TEXT,"
				+ USER_IMAGE + " BLOB," + USER_ADDRESS + " TEXT" + ")";

		String createItemsSql = "CREATE TABLE ITEM(" + ITEM_ID
				+ " INTEGER  PRIMARY KEY AUTOINCREMENT," + ITEM_NAME + " TEXT,"
				+ ITEM_NOTE + " TEXT," + ITEM_LIST + " TEXT" + ")";
		String createListSql = "CREATE TABLE LIST(" + LIST_ID
				+ " INTEGER PRIMARY KEY  AUTOINCREMENT," + LIST_NAME + " TEXT,"
				+ LIST_SCHEDULE + " TEXT," + LIST_TIMESTAMP + " TEXT,"
				+ LIST_SERVICE + " TEXT" + ")";
		String createOrderSql = "CREATE TABLE USER_ORDERS(" + ORDER_ID
				+ " INTEGER PRIMARY KEY  AUTOINCREMENT," + ORDER_CONTACTINFO
				+ " TEXT," + ORDER_TIMESTAMP + " TEXT," + ORDER_LIST_LIST_NAME
				+ " TEXT," + ORDER_STATUS + " TEXT," + ORDER_SCHEDULE + " TEXT"
				+ ")";
		String createOrderListSql = "CREATE TABLE ORDER_LIST("
				+ ORDER_LIST_ORDER_ID + " INTEGER," + ORDER_LIST_LIST_NAME
				+ " TEXT" + ")";
		db.execSQL(createUserSql);
		db.execSQL(createUserSettingsSql);
		db.execSQL(createItemsSql);
		db.execSQL(createListSql);
		db.execSQL(createOrderSql);
		db.execSQL(createOrderListSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE USER");
		db.execSQL("DROP TABLE ITEM");
		db.execSQL("DROP TABLE LIST");
		db.execSQL("DROP TABLE USER_ORDERS");
		db.execSQL("DROP TABLE ORDER_LIST");
		this.onCreate(db);
	}

	public void createUser(String user_id, String username, String usermail,
			String userpassword) {
		freshlistDb = this.getWritableDatabase();
		freshlistDb.delete("USER", null, null);
		ContentValues userDetails = new ContentValues();
		userDetails.put(USER_ID, user_id);
		userDetails.put(USER_NAME, username);
		userDetails.put(USER_MAIL, usermail);
		userDetails.put(USER_PASSWORD, userpassword);
		freshlistDb.insert("USER", null, userDetails);
		freshlistDb.close();
	}

	public byte[] getUserPic() {
		freshlistDb = this.getWritableDatabase();
		byte[] img = null;
		Cursor c = freshlistDb.query("USER_SETTINGS",
				new String[] { USER_IMAGE }, null, null, null, null, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				img = c.getBlob(c.getColumnIndex(USER_IMAGE));
			}

		}
		c.close();
		freshlistDb.close();
		return img;
	}

	public void createUserSettings(String usermail, byte[] userimage,
			String useraddress) {
		freshlistDb = this.getWritableDatabase();
		freshlistDb.delete("USER_SETTINGS", null, null);
		ContentValues userDetails = new ContentValues();
		userDetails.put(USER_MAIL, usermail);
		userDetails.put(USER_IMAGE, userimage);
		userDetails.put(USER_ADDRESS, useraddress);

		freshlistDb.insert("USER_SETTINGS", null, userDetails);
		freshlistDb.close();
	}

	public User userFromLocal(String usermail) {

		String selectAllItemSql = "SELECT * FROM USER WHERE " + USER_MAIL
				+ " = ?";
		Cursor cursor = freshlistDb.rawQuery(selectAllItemSql,
				new String[] { usermail });
		cursor.moveToFirst();
		if (cursor.getCount() == 1) {
			user.setUsername(cursor.getString(cursor.getColumnIndex(USER_NAME)));
			user.setEmail(cursor.getString(cursor.getColumnIndex(USER_MAIL)));
			user.setPassword(cursor.getString(cursor
					.getColumnIndex(USER_PASSWORD)));
		}
		cursor.close();
		freshlistDb.close();

		return user;

	}

	public void addItem(String itemName, String itemNote, String itemListName) {
		freshlistDb = this.getWritableDatabase();
		ContentValues itemDetails = new ContentValues();
		itemDetails.put(ITEM_NAME, itemName);
		itemDetails.put(ITEM_NOTE, itemNote);
		itemDetails.put(ITEM_LIST, itemListName);
		freshlistDb.insert("ITEM", null, itemDetails);
		freshlistDb.close();
	}

	public void addList(String listName, String listSchedule,
			String listCreatedAt, String listService) {
		freshlistDb = this.getWritableDatabase();
		ContentValues listDetails = new ContentValues();
		listDetails.put(LIST_NAME, listName);
		listDetails.put(LIST_SCHEDULE, listSchedule);
		listDetails.put(LIST_TIMESTAMP, listCreatedAt);
		listDetails.put(LIST_SERVICE, listService);
		freshlistDb.insert("LIST", null, listDetails);
		freshlistDb.close();
	}

	public void addOrder(String orderContactInfo, String orderTimeStamp,
			String orderItemsList, String orderStatus, String orderSchedule) {
		freshlistDb = this.getWritableDatabase();
		ContentValues orderDetail = new ContentValues();
		orderDetail.put(ORDER_CONTACTINFO, orderContactInfo);
		orderDetail.put(ORDER_TIMESTAMP, orderTimeStamp);
		orderDetail.put(ORDER_LIST_LIST_NAME, orderItemsList);
		orderDetail.put(ORDER_STATUS, orderStatus);
		orderDetail.put(ORDER_SCHEDULE, orderSchedule);
		freshlistDb.insert("USER_ORDERS", null, orderDetail);
		freshlistDb.close();
	}

	public void addListToOrder(String listName) {

	}

	public String getCurrentUser(String column) {

		freshlistDb = this.getWritableDatabase();

		String selectUserSql = "SELECT * FROM USER";
		Cursor cursor = freshlistDb.rawQuery(selectUserSql, null);

		String user = new String();
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				user = cursor.getString(cursor.getColumnIndex(column));
			}
		}
		cursor.close();
		freshlistDb.close();
		return user;

	}

	public String getAllUserNames(String email, String column) {
		freshlistDb = this.getWritableDatabase();

		String selectAllUserSql = "SELECT * FROM USER WHERE " + USER_MAIL
				+ " = ?";
		Cursor cursor = freshlistDb.rawQuery(selectAllUserSql,
				new String[] { email });
		String user = new String();
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				user = cursor.getString(cursor.getColumnIndex(column));
			}
		}
		cursor.close();
		freshlistDb.close();
		return user;
	}

	public int logUserOut() {
		freshlistDb = this.getWritableDatabase();
		return freshlistDb.delete("USER", null, null);
	}

	public String[] getAllItemsNames(String listName, String column) {
		freshlistDb = this.getWritableDatabase();

		String selectAllItemSql = "SELECT * FROM ITEM WHERE " + ITEM_LIST
				+ " = ?";
		Cursor cursor = freshlistDb.rawQuery(selectAllItemSql,
				new String[] { listName });
		String[] items = new String[cursor.getCount()];
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				items[cursor.getPosition()] = cursor.getString(cursor
						.getColumnIndex(column));
			}
		}
		cursor.close();
		freshlistDb.close();
		return items;
	}

	public String[] getAllListsNames(String servicename, String column) {
		freshlistDb = this.getWritableDatabase();
		String selectListsql = "SELECT * FROM LIST WHERE " + LIST_SERVICE
				+ " = ? ";
		Cursor cursor = freshlistDb.rawQuery(selectListsql,
				new String[] { servicename });

		String[] lists = new String[cursor.getCount()];
		createdAt = new String[cursor.getCount()];
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				lists[cursor.getPosition()] = cursor.getString(cursor
						.getColumnIndex(column));
			}
		}

		cursor.close();
		freshlistDb.close();
		return lists;
	}

	public int getListFieldNumber(String listname) {
		int howmany = 0;
		freshlistDb = this.getWritableDatabase();
		String selectListsql = "SELECT * FROM LIST WHERE " + LIST_NAME
				+ " = ? ";
		Cursor cursor = freshlistDb.rawQuery(selectListsql,
				new String[] { listname });
		cursor.moveToFirst();
		howmany = cursor.getCount();
		return howmany;
	}

	public int updateUser(String id, String mail, String username,
			String password) {
		freshlistDb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_MAIL, mail);
		values.put(USER_NAME, username);
		values.put(USER_PASSWORD, password);
		int i = freshlistDb.update("USER", values, USER_ID + " = ?",
				new String[] { id });
		freshlistDb.close();
		return i;
	}

	public int updateList(String listName, String newlistName,
			String timeStamp, String newListSchedule) {
		freshlistDb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(LIST_NAME, newlistName);
		values.put(LIST_SCHEDULE, newListSchedule);
		int update = freshlistDb
				.update("LIST", values, LIST_NAME + " = ? AND "
						+ LIST_TIMESTAMP + " = ?", new String[] { listName,
						timeStamp });
		ContentValues itemvalues = new ContentValues();
		itemvalues.put(ITEM_LIST, newlistName);
		freshlistDb.update("ITEM", itemvalues, ITEM_LIST + " = ?",
				new String[] { listName });
		freshlistDb.close();
		return update;
	}

	public void printDbData(String table) {
		freshlistDb = this.getWritableDatabase();
		Cursor c = freshlistDb.query(table, null, null, null, null, null, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				System.out.println(c.getCount() + " columns found no "
						+ c.getPosition() + "\n" + " "
						+ c.getString(c.getColumnIndex(LIST_NAME)) + " "
						+ c.getString(c.getColumnIndex(LIST_SCHEDULE)) + " "
						+ c.getString(c.getColumnIndex(LIST_SERVICE)));
			}
		}
		c.close();
		freshlistDb.close();

	}

	public int clearTable(String tableName, String listName) {
		freshlistDb = this.getWritableDatabase();
		int i = freshlistDb.delete(tableName, ITEM_LIST + " = ?",
				new String[] { listName });
		freshlistDb.close();
		return i;
	}

	public int deleteAList(String listName, String createdAt) {
		freshlistDb = this.getWritableDatabase();
		int i = freshlistDb.delete("LIST", LIST_NAME + " = ? AND "
				+ LIST_TIMESTAMP + " = ? ",
				new String[] { listName, createdAt });
		freshlistDb.close();
		return i;
	}

	public void updateSettings(String whichColumn, String address, byte[] imgae) {
		freshlistDb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_ADDRESS, address);

	}

}
