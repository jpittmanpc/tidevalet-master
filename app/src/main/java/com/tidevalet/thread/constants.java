package com.tidevalet.thread;

public interface constants {
	public static final String DATABASE_NAME = "tidevalet.db";
	public static final int DATABASE_VERSION = 3;

	// Tables
	public static final String TABLE_PROPERTIES = "properties";
	public static final String TABLE_POSTS = "posts";

	// Key Row Column
	public static final String COL_KEY_ROW = "_id";
	
	// Columns for Table PROPERTIES
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_IMG = "image";
	public static final String PROPERTY_ADDRESS = "address";
	public static final String PROPERTY_CONTRACTOR = "contractor";
	public static final String PROPERTY_COMPLEXMGRS = "complex_mgr";
	
	// Columns for Table POSTS
	public static final String POST_VIOLATION_ID = "violation_id";
	public static final String POST_LOCAL_IMAGE_PATH = "image_path";
	public static final String POST_IS_POSTED = "is_posted";
	public static final String POST_RETURNED_STRING = "returned_string";
	public static final String POST_TIMESTAMP = "timestamp";
	public static final String POST_VIOLATION_TYPE = "violation_type";
	public static final String POST_BLDG = "bldg";
	public static final String POST_UNIT = "unit";
	public static final String POST_COMMENTS = "comments";
	public static final String POST_IMAGES = "images";
	public static final String PROPERTY_ID = "property_id";
	public static final String PICKEDUP = "picked_up";

	// Create Query for Properties Table
	public static final String SQL_PROPERTIES = "create table "
		+ constants.TABLE_PROPERTIES + "(" + constants.COL_KEY_ROW + " integer primary key,"
		+ constants.PROPERTY_NAME + " text," + constants.PROPERTY_ADDRESS + " text,"
		+ constants.PROPERTY_IMG + " text," + PROPERTY_CONTRACTOR + " text,"
		+ constants.PROPERTY_COMPLEXMGRS + " text);";
	// Create Query for Posts Table
	public static final String SQL_POSTS = "create table "
		+ constants.TABLE_POSTS + "(" + constants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ constants.POST_VIOLATION_ID + " integer," + constants.PICKEDUP + " integer," + constants.PROPERTY_ID + " integer," + constants.POST_LOCAL_IMAGE_PATH + " text,"
		+ constants.POST_RETURNED_STRING + " text," + constants.POST_IS_POSTED + " integer,"
		+ constants.POST_TIMESTAMP + " text,"+constants.POST_VIOLATION_TYPE +" text,"
		+ constants.POST_UNIT +" text,"+ constants.POST_BLDG + " text,"
		+ constants.POST_COMMENTS + " text," + constants.POST_IMAGES + " text);";

}
