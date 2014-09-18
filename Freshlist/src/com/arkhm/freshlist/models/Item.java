package com.arkhm.freshlist.models;

public class Item {

	String itemName;
	Double itemQuantity;
	String itemUnity;

	public Item() {
		// TODO Auto-generated constructor stub
	}

	
	public Item(String itemName, double itemQuantity, String itemUnity) {
		super();
		this.itemName = itemName;
		this.itemQuantity = itemQuantity;
		this.itemUnity = itemUnity;
	}


	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemQuantity
	 */
	public double getItemQuantity() {
		return itemQuantity;
	}

	/**
	 * @param itemQuantity the itemQuantity to set
	 */
	public void setItemQuantity(double itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	/**
	 * @return the itemUnity
	 */
	public String getItemUnity() {
		return itemUnity;
	}

	/**
	 * @param itemUnity the itemUnity to set
	 */
	public void setItemUnity(String itemUnity) {
		this.itemUnity = itemUnity;
	}

}
