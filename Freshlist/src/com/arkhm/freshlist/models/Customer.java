package com.arkhm.freshlist.models;


public class Customer extends User {

	private String phoneNumber;
	private boolean loggedIn;
	private boolean signedIn;

	public Customer() {
		//
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn
	 *            the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * @return the signedIn
	 */
	public boolean isSignedIn() {
		return signedIn;
	}

	/**
	 * @param signedIn
	 *            the signedIn to set
	 */
	public void setSignedIn(boolean signedIn) {
		this.signedIn = signedIn;
	}

	// Here below there are all methods according to customer behavior and
	// relationship with other objects

	/**
	 * @param itemName
	 *            item name of current item
	 * @param itemQuantity
	 *            itemQuantity of current item
	 * @param itemUnity
	 *            itemUnity of current item
	 * 
	 * @param this method is called when a customer has provided the data which
	 *        makeup an item
	 * */
	public void creatItem(String itemName, int itemNote) {
		// here we create an item based on values or data entered by customer
		Item item = new Item();
		item.setItemName(itemName);

		// here we add the current item to the item list and set created time to
		// the current time
		ItemList itemlist = new ItemList();
		itemlist.add(item);
	}

	/**
	 * @param itemPositionInList
	 *            this is the position of the item to be deleted in a list
	 * 
	 * */
	public void deleteItem(int itemPositionInList) {
		ItemList itemlist = new ItemList();
		itemlist.remove(itemPositionInList);
	}

	public void createItemList(String title, String time,String timeStamp) {
		ItemList itemlist = new ItemList();
		itemlist.setTimeStamp(timeStamp);
		itemlist.setTitle(title);
	}

	public void createContactInfo(String address, String addressComments) {
		ContactInfo contacts = new ContactInfo();
		contacts.setAddress(address);
		contacts.setAddressComments(addressComments);
	}

	public void buildOrder(ItemList theItemList, ContactInfo contactsInfo,
			String market) {
		Order order = new Order();
		order.setStatus(market);

	}

	public void submitOrder() {
		// codes to submit order will go here
	}
}
