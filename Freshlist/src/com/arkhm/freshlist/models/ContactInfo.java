package com.arkhm.freshlist.models;

public class ContactInfo {

	private String address;
	private String addressComments;
	
	public ContactInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	

	public ContactInfo(String address, String addressComments) {
		super();
		this.address = address;
		this.addressComments = addressComments;
	}

	/**
	 * @param address the address to set
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
	 * @param addressComments the addressComments to set
	 */
	public void setAddressComments(String addressComments) {
		this.addressComments = addressComments;
	}

	
}
