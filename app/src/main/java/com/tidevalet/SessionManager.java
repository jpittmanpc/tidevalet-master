package com.tidevalet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {

	Context context;
	SharedPreferences pref;
	Editor editor;
	String PREF_NAME = "pref_name";
	int PREF_MODE = 0;
	static final String POST_ID = "post_id";
	static final String LATITUDE = "latitude";
	static final String LONGITUDE = "longitude";
	static final String ADDRESS = "address";
	static final String DATE = "date";
	static final String TIME = "time";
	static final String DEF_LOGGED_IN = "logged_in";
	private static final String ROLE = "role";
	private static final String DEF_URL = "url";
	private static final String DEF_USR = "username";
	private static final String DEF_PWD = "password";
	private static final String PROPERTY_SELECTED = "property";
	private static String UPDATE_VIEW = "update_view";
	private static String WP_USER_ID = "contractor_id";

	public SessionManager(Context _context) {
		super();
		this.context = _context;
		pref = context.getSharedPreferences(PREF_NAME, PREF_MODE);
		editor = pref.edit();
	}

	public void ShareValues
			(long postId) {
		editor.putLong(POST_ID, postId);
		editor.commit();
	}

	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(LATITUDE, pref.getString(LATITUDE, null));
		user.put(LONGITUDE, pref.getString(LONGITUDE, null));
		user.put(ADDRESS, pref.getString(ADDRESS, null));
		user.put(DATE, pref.getString(DATE, null));
		user.put(TIME, pref.getString(TIME, null));
		return user;
	}
	public String resetUser() {
		editor.remove(DEF_USR);
		editor.remove(DEF_LOGGED_IN);
		editor.remove(DEF_PWD);
		editor.commit();
		System.exit(0);
		return "bye";
	}
	public void setDefUrl(String url) {
		url = App.getSiteUrl();
		if (!url.contains("http://") && !url.startsWith("http")) {
			url = "http://" + url;
		}
		if (url.lastIndexOf("/") < url.length() - 1) {
			url = url + "///";
		}
		editor.putString(DEF_URL, url);
		editor.commit();
	}
	public void setDefUsr(String username) {
		editor.putString(DEF_USR, username);
		editor.commit();
	}
	public void setDefPwd(String password) {
		editor.putString(DEF_PWD, password);
		editor.commit();
	}
	public long propertySelected() {
		return pref.getLong(PROPERTY_SELECTED, 0);
	}
	public void setPropertySelected(long value) {
		editor.putLong(PROPERTY_SELECTED, value);
		editor.commit();
	}
	public long getpostId() {
		return pref.getLong(POST_ID, -1);
	}
	public void setpostId(long postId) {
		editor.putLong(POST_ID, postId);
		editor.commit();
	}
	public void resetpostId() {
		editor.remove(POST_ID);
		editor.commit();
	}
	public void setLoggedIn(Boolean value) { editor.putBoolean(DEF_LOGGED_IN, value); editor.commit(); }
	public boolean isLoggedIn() { return pref.getBoolean(DEF_LOGGED_IN, false); }
	public String a() { return pref.getString(DEF_URL, null); }
	public String getUsername() { return pref.getString(DEF_USR, null); }
	public String getPassword() { return pref.getString(DEF_PWD, null); }
	public String getUserId() { return pref.getString(WP_USER_ID, "").toString(); }
	public void setUserId(String id) { editor.putString(WP_USER_ID, id); editor.commit(); }
    public void setNoProperties(boolean value) {
		editor.putBoolean(UPDATE_VIEW, value); editor.commit();
	}
	public boolean noProperties() {
		return pref.getBoolean(UPDATE_VIEW, false);
	}

	public void setRole(String role) {
		editor.putString(ROLE, role);
		editor.commit();
	}
	public String getRole() { return pref.getString(ROLE,""); }
}
