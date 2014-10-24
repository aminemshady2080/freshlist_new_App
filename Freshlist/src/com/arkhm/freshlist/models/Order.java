package com.arkhm.freshlist.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.arkhm.freshlist.handlers.NetworkJsonHandler;

public class Order {

	private String theItemList;
	private String contactsInfo;
	private String Status;
	private String orderTimeStamp;
	private String orderSchedule;
	// private String orderUrl =
	// "http://10.0.2.2/freshlist/freshlist/freshlist_user_includes/freshlist_order.php";
	private String orderUrl = "http://freshbunch.heroku.com/api/v1/order/?format=json";
	NetworkJsonHandler jsonParser = new NetworkJsonHandler();

	public Order() {
		// TODO Auto-generated constructor stub
	}

	public Order(String theItemList, String contactsInfo, String status,
			String orderTimeStamp, String orderSchedule) {
		super();
		this.theItemList = theItemList;
		this.contactsInfo = contactsInfo;
		this.Status = status;
		this.orderTimeStamp = orderTimeStamp;
		this.orderSchedule = orderSchedule;
	}

	/**
	 * @return the theItemList
	 */
	public String getTheItemList() {
		return theItemList;
	}

	/**
	 * @param theItemList
	 *            the theItemList to set
	 */
	public void setTheItemList(String theItemList) {
		this.theItemList = theItemList;
	}

	/**
	 * @return the contactsInfo
	 */
	public String getContactsInfo() {
		return contactsInfo;
	}

	/**
	 * @param contactsInfo
	 *            the contactsInfo to set
	 */
	public void setContactsInfo(String contactsInfo) {
		this.contactsInfo = contactsInfo;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.Status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.Status = status;
	}

	/**
	 * @return the orderTimeStamp
	 */
	public String getOrderTimeStamp() {
		return orderTimeStamp;
	}

	/**
	 * @param orderTimeStamp
	 *            the orderTimeStamp to set
	 */
	public void setOrderTimeStamp(String orderTimeStamp) {
		this.orderTimeStamp = orderTimeStamp;
	}

	/**
	 * @return the orderSchedule
	 */
	public String getOrderSchedule() {
		return orderSchedule;
	}

	/**
	 * @param orderSchedule
	 *            the orderSchedule to set
	 */
	public void setOrderSchedule(String orderSchedule) {
		this.orderSchedule = orderSchedule;
	}

	public JSONObject sendOrder(String mobile, String contactsInfo,
			String status, String orderTimeStamp, String comment) {
		//this.theItemList = theItemList;
		this.contactsInfo = contactsInfo;
		this.Status = status;
		this.orderTimeStamp = orderTimeStamp;
		// this.orderSchedule = orderSchedule;
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("stage", "S"));
		values.add(new BasicNameValuePair("address", contactsInfo));
		values.add(new BasicNameValuePair("comment", comment));
		values.add(new BasicNameValuePair("mobile", mobile));

		// values.add(new BasicNameValuePair("order_timestamp",
		// orderTimeStamp));
		// values.add(new BasicNameValuePair("order_listname", theItemList));
		// values.add(new BasicNameValuePair("order_schedule", orderSchedule));

		return jsonParser.getDataFromUrl(orderUrl, values);
	}
}
