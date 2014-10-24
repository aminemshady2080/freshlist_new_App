package com.arkhm.freshlist.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.arkhm.freshlist.handlers.NetworkJsonHandler;

public class Item {

	String itemName;
	String itemNote;
	String itemList;

	//private String itemUrl = "http://10.0.2.2/freshlist/freshlist/freshlist_user_includes/freshlist_item.php";
	private String itemUrl = "http://freshbunch.heroku.com/api/v1/list_item/?format=json";
	NetworkJsonHandler jsonParser = new NetworkJsonHandler();

	public Item() {
		// TODO Auto-generated constructor stub
	}

	public Item(String itemName, String itemNote, String itemList) {
		super();
		this.itemName = itemName;
		this.itemNote = itemNote;
		this.itemList = itemList;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName
	 *            the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemNote
	 */
	public String getItemNote() {
		return itemNote;
	}

	/**
	 * @param itemNote
	 *            the itemNote to set
	 */
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}

	/**
	 * @return the itemList
	 */
	public String getItemList() {
		return itemList;
	}

	/**
	 * @param itemList
	 *            the itemList to set
	 */
	public void setItemList(String itemList) {
		this.itemList = itemList;
	}

//	public JSONObject addItem(String itemname, String itemnote, String itemlist) {
//		this.itemName = itemname;
//		this.itemNote = itemnote;
//		this.itemList = itemlist;
//		List<NameValuePair> values = new ArrayList<NameValuePair>();
//		values.add(new BasicNameValuePair("item_name", itemname));
//		values.add(new BasicNameValuePair("item_note", itemnote));
//		values.add(new BasicNameValuePair("item_list", itemlist));
//
//		return jsonParser.getDataFromUrl(itemUrl, values);
//	}

	public JSONObject addItem(String itemname, String itemnote, String itemlist) {
		this.itemName = itemname;
		this.itemNote = itemnote;
		this.itemList = itemlist;
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("item_description", itemname));
		values.add(new BasicNameValuePair("note", itemnote));
		values.add(new BasicNameValuePair("orderlist", itemlist));

		return jsonParser.getDataFromUrl(itemUrl, values);
	}

}
