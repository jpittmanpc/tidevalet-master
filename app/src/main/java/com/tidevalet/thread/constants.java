package com.tidevalet.thread;

import java.util.List;

public interface constants {
	public static final String DATABASE_NAME = "tidevalet.db";
	public static final int DATABASE_VERSION = 2;

	// Tables
	public static final String TABLE_PROPERTIES = "properties";
	public static final String TABLE_POSTS = "posts";
	public static final String TABLE_VIOLATIONS = "pservices";
	public static final String TABLE_SERVICES = "services";

	
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

	// 0 - Not posted    1 - Posted
	public static final String POST_IS_POSTED = "is_posted";
	public static final String POST_RETURNED_STRING = "returned_string";
	public static final String POST_TIMESTAMP = "timestamp";
	public static final String POST_VIOLATION_TYPE = "violation_type";
	public static final String POST_BLDG = "bldg";
	public static final String POST_UNIT = "unit";
	public static final String POST_COMMENTS = "comments";
	
	// Columns for Table PUPIL_SERVICES
	public static final String PROP_VIOLATION_ID = "violation_id";
	public static final String PROPERTY_ID = "property_id";

	// Columns for Table SERVICES
	public static final String SERV_URL = "url";
	public static final String SERV_NAME = "service_name";
	public static final String SERV_POST_SYNTAX = "post_syntax";
	public static final String SERV_RETURNED_STRING = "returned_string";
	public static final String SERV_FIELDS_REQ = "fields_req";
	public static final String SERV_USERNAME = "username";
	public static final String SERV_PASSWORD = "password";
	// create table properties (_id [integer primary key autoincrement], name [text])
	// Create Query for Properties Table
	public static final String SQL_PROPERTIES = "create table "
		+ constants.TABLE_PROPERTIES + "(" + constants.COL_KEY_ROW + " integer primary key,"
		+ constants.PROPERTY_NAME + " text," + constants.PROPERTY_ADDRESS + " text," + constants.PROPERTY_IMG + " text," + PROPERTY_CONTRACTOR + " text);";
	//create table posts (_id integer primary key autoincrement,
	//						  violation_id integer,
	//	 					image_path text,
	//						returned_string text,
	//						is_posted integer,
	//						timestamp text,
	//						violation_type text)
	// Create Query for Posts Table
	public static final String SQL_POSTS = "create table "
		+ constants.TABLE_POSTS + "(" + constants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ constants.POST_VIOLATION_ID + " integer," + constants.PROPERTY_ID + " integer," + constants.POST_LOCAL_IMAGE_PATH + " text,"
		+ constants.POST_RETURNED_STRING + " text," + constants.POST_IS_POSTED + " integer,"
		+ constants.POST_TIMESTAMP + " text,"+constants.POST_VIOLATION_TYPE +" text,"+constants.POST_UNIT +" text," + constants.POST_BLDG + " text," + constants.POST_COMMENTS + " text);";
	// create table pservices (_id integer primary key autoincrement,
	//							pupil_id integer
	// 							serv_id integer,
	//							enabled integer,
	//							nick text,
	//							url text,
	//							username text,
	//							password text,
	//							use_default integer);
	// Create Query for Pupil Services Table
	//sqlDB.query(pservices, _id,
	//pupil_id=" + pupilId + " AND "
	//		serv_id=" + serviceId, null,
	//		null, null, null);
	public static final String SQL_PROPVIO = "create table "
		+ constants.TABLE_VIOLATIONS + "(" + constants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ constants.PROP_VIOLATION_ID + " integer," + constants.PROPERTY_ID + " integer)";
	
	// Create Query for Services table
	public static final String SQL_SERVICES = "create table "
		+ constants.TABLE_SERVICES + "(" + constants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ constants.SERV_URL + " text," + constants.SERV_NAME + " text,"
		+ constants.SERV_POST_SYNTAX + " text," + constants.SERV_RETURNED_STRING + " text,"
		+ constants.SERV_FIELDS_REQ + " text," + constants.SERV_USERNAME + " text,"
		+ constants.SERV_PASSWORD + " text);";
}
