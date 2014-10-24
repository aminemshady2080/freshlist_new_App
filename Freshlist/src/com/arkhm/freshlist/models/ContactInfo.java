package com.arkhm.freshlist.models;

public class ContactInfo {

	private String address;
	private String addressComments;
	private ItemList OrdersItemList = new ItemList();

	public ContactInfo() {
		// TODO Auto-generated constructor stub
	}

	public ContactInfo(String address, String addressComments,
			ItemList orderItemList) {
		super();
		this.address = address;
		this.addressComments = addressComments;
		this.OrdersItemList = orderItemList;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the addressComments
	 */
	public String getAddressComments() {
		return addressComments;
	}

	/**
	 * @param addressComments
	 *            the addressComments to set
	 */
	public void setAddressComments(String addressComments) {
		this.addressComments = addressComments;
	}

	/**
	 * @return the ordersItemList
	 */
	public ItemList getOrdersItemList() {
		return OrdersItemList;
	}

	/**
	 * @param ordersItemList
	 *            the ordersItemList to set
	 */
	public void setOrdersItemList(ItemList ordersItemList) {
		OrdersItemList = ordersItemList;
	}

}
