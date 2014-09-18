package com.arkhm.freshlist.models;

import java.util.ArrayList;
import java.util.Date;

public class ItemList extends ArrayList<Item> {
	private static final long serialVersionUID = 1L;
	
	private Date timeStamp;
	private String title;

	public ItemList() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		if (title.equals("")) {
			title = "list made on " + timeStamp.toString();
			return title;
		} else

			return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String listToString(){
		String toReturn="";
		for(int i=0;i<this.size();i++){
			toReturn=toReturn+this.get(i)+" , ";
		}
		return toReturn;
	}
}
