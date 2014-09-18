package com.arkhm.freshlist.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.arkhm.freshlist.handlers.NetworkJsonHandler;

public class User {

	private String email;
	private String userAuthUrl2 = "http://10.0.2.2/freshlist/freshlist/freshlistAuth.php";
	private String username;
	private String first_name;
	private String last_name;
	private String password;
	private String repeatedPass;

	NetworkJsonHandler jsonParser = new NetworkJsonHandler();

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String email) {
		super();
		this.email = email;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
		System.out.println("Email set is " + email);
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the repeatedPass
	 */
	public String getRepeatedPass() {
		return repeatedPass;
	}

	/**
	 * @param repeatedPass
	 *            the repeatedPass to set
	 */
	public void setRepeatedPass(String repeatedPass) {
		this.repeatedPass = repeatedPass;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param first_name
	 *            the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name
	 *            the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * 
	 * @param email
	 * @param username
	 * @param password
	 * @return a json object tobe handler by asynk task in signup user
	 */
	public JSONObject signUpUser(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
		List<NameValuePair> values = new ArrayList<>();
		values.add(new BasicNameValuePair("email", email));
		values.add(new BasicNameValuePair("username", username));
		values.add(new BasicNameValuePair("password", password));
		values.add(new BasicNameValuePair("tag", "signup"));

		JSONObject json = jsonParser.getDataFromUrl(userAuthUrl2, values);
		return json;
	}
/**
 * @param email
 * @return a json object tobe handler by asynk task in logging up a user
 */
	public JSONObject loginUser(String email) {
		this.email = email;
		List<NameValuePair> values = new ArrayList<>();
		values.add(new BasicNameValuePair("email", email));
		values.add(new BasicNameValuePair("tag", "login"));

		JSONObject json = jsonParser.getDataFromUrl(userAuthUrl2, values);

		return json;
	}

}