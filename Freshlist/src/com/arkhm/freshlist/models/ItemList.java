package com.arkhm.freshlist.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.arkhm.freshlist.handlers.NetworkJsonHandler;

public class ItemList extends ArrayList<Item> {
	private static final long serialVersionUID = 1L;

	private String timeStamp;
	private String title;
	private String scheduledTime;
	private String listServices;
	//private String listUrl = "http://10.0.2.2/freshlist/freshlist/freshlist_user_includes/freshlist_lists.php";
	private String listUrl = "http://freshbunch.heroku.com/api/v1/orderlist/?format=json";
	NetworkJsonHandler jsonParser = new NetworkJsonHandler();

	public ItemList() {
		// TODO Auto-generated constructor stub
	}

	public ItemList(String timeStamp, String title, String scheduledTime,
			String listServices) {
		super();
		this.timeStamp = timeStamp;
		this.title = title;
		this.scheduledTime = scheduledTime;
		this.listServices = listServices;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the scheduledTime
	 */
	public String getScheduledTime() {
		return scheduledTime;
	}

	/**
	 * @param scheduledTime
	 *            the scheduledTime to set
	 */
	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	/**
	 * @return the listServices
	 */
	public String getListServices() {
		return listServices;
	}

	/**
	 * @param listServices
	 *            the listServices to set
	 */
	public void setListServices(String listServices) {
		this.listServices = listServices;
	}

//	public JSONObject addItemList(String timeStamp, String title,
//			String scheduledTime, String listServices) {
//		this.timeStamp = timeStamp;
//		this.title = title;
//		this.scheduledTime = scheduledTime;
//		this.listServices = listServices;
//		List<NameValuePair> values = new ArrayList<NameValuePair>();
//		values.add(new BasicNameValuePair("list_name", title));
//		values.add(new BasicNameValuePair("list_timestamp", timeStamp));
//		values.add(new BasicNameValuePair("list_service", listServices));
//		values.add(new BasicNameValuePair("list_schedule", scheduledTime));
//		return jsonParser.getDataFromUrl(listUrl, values);
//
//	}
	public JSONObject addItemList(String timeStamp, String title,
			String scheduledTime, String listServices) {
		this.timeStamp = timeStamp;
		this.title = title;
		this.scheduledTime = scheduledTime;
		//this.listServices = listServices;
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("title", title));
		values.add(new BasicNameValuePair("list_timestamp", timeStamp));
	//	values.add(new BasicNameValuePair("list_service", listServices));
		values.add(new BasicNameValuePair("scheduled_time", scheduledTime));
		return jsonParser.getDataFromUrl(listUrl, values);

	}
}
