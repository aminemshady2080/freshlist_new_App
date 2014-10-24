package com.arkhm.freshlist.handlers;


public class LocalAuthentication {

	public LocalAuthentication() {
		// TODO Auto-generated constructor stub
	}

	public boolean cleanFields(String email, String username, String password) {

		if (!(email.length() == 0 && username.length() == 0
				&& password.length() == 0)) {
			return true;

		} else {
			return false;
		}
	}

	public boolean cleanUserName(String username) {
		boolean isclean = false;
		if (username.length() <= 4) {
			isclean = false;
		} else if (username.matches("[0-9]+")) {
			isclean = false;
		} else if (username.substring(0, 1).matches("[0-9]+")) {
			isclean = false;
		} else {
			isclean = true;
		}

		return isclean;
	}

	public boolean isshort = true;
	public boolean isfair = false;
	public boolean isstrong = false;

	public boolean cleanPassword(String password) {

		boolean isclean = false;

		if (password.length() <= 5) {
			isclean = false;
			isshort = true;
			isfair = false;
		} else if (password.length() >= 6 && password.length() < 8) {
			isclean = true;
			isshort = false;
			isfair = true;
		} else if (password.length() >= 8) {
			isstrong = true;
			isclean = true;
			isshort = false;
			isfair = false;
		}

		return isclean;
	}

	public boolean isSamePassword(String pass1, String pass2) {
		boolean isSame = false;

		if (pass1.equals(pass2)) {
			isSame = true;
		} else {
			isSame = false;
		}
		return isSame;
	}

	boolean isEmailSpaced = true;

	public boolean cleanEmail(String email) {
		boolean isclean = false;
		int atIndex = email.indexOf("@");
		int dotIndex = email.indexOf(".");
		if (!email.contains("@")) {
			isclean = false;
		} else if (!email.substring(atIndex, email.length()).contains(".")) {
			isclean = false;
		} else if (email.substring(dotIndex,email.length()).length() == 0) {
			isclean = false;
		} else {
			if (email.contains(" ")) {
				isclean = false;
				isEmailSpaced = true;
			} else {
				isEmailSpaced = false;
				isclean = true;
			}
		}
		return isclean;
	}
	
}
