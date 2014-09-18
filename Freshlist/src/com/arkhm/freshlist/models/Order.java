package com.arkhm.freshlist.models;

import java.util.Date;

public class Order {

	private ItemList theItemList;
	private ContactInfo contactsInfo;
	private String Status;
	private Date orderTimeStamp;
	private String market;

	public Order() {
		// TODO Auto-generated constructor stub
	}

	public Order(ItemList theItemList, ContactInfo contactsInfo, String market) {
		super();
		this.theItemList = theItemList;
		this.contactsInfo = contactsInfo;
		this.market = market;
	}

	/**
	 * @return the theItemList
	 */
	public ItemList getTheItemList() {
		return theItemList;
	}

	/**
	 * @param theItemList
	 *            the theItemList to set
	 */
	public void setTheItemList(ItemList theItemList) {
		this.theItemList = theItemList;
	}

	/**
	 * @return the contactsInfo
	 */
	public ContactInfo getContactsInfo() {
		return contactsInfo;
	}

	/**
	 * @param contactsInfo
	 *            the contactsInfo to set
	 */
	public void setContactsInfo(ContactInfo contactsInfo) {
		this.contactsInfo = contactsInfo;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * @return the orderTimeStamp
	 */
	public Date getOrderTimeStamp() {
		return orderTimeStamp;
	}

	/**
	 * @param orderTimeStamp
	 *            the orderTimeStamp to set
	 */
	public void setOrderTimeStamp(Date orderTimeStamp) {
		this.orderTimeStamp = orderTimeStamp;
	}

	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}

	/**
	 * @param market
	 *            the market to set
	 */
	public void setMarket(String market) {
		this.market = market;
	}

}
