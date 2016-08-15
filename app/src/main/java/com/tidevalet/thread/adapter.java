
package com.tidevalet.thread;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tidevalet.MainActivity;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Properties;

import java.util.HashMap;
import java.util.Map;

public class adapter {
	private SQLiteDatabase sqlDB;
	private DatabaseHelper dbHelper;

	public adapter(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	// Opens the database
	public adapter open() throws SQLException {
		sqlDB = dbHelper.getWritableDatabase();
		return this;
	}

	// Closes the database
	public void close() {
		dbHelper.close();
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, constants.DATABASE_NAME, null,
					constants.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(constants.SQL_PROPERTIES);
			db.execSQL(constants.SQL_POSTS);
			db.execSQL(constants.SQL_PROPVIO);
			db.execSQL(constants.SQL_SERVICES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public long addProperty(int propId, String propertyName, String address, String image, String contractors) {
		ContentValues values = new ContentValues();
		values.put(constants.COL_KEY_ROW, propId);
		values.put(constants.PROPERTY_NAME, propertyName);
		values.put(constants.PROPERTY_ADDRESS, address);

		values.put(constants.PROPERTY_IMG, image);
		values.put(constants.PROPERTY_CONTRACTOR, contractors);
		String strFilter = constants.COL_KEY_ROW + "=" + propId;
		if (getPropertyById(propId) == null) {
			return sqlDB.insert(constants.TABLE_PROPERTIES, null, values);
		}
		else {
			return sqlDB.update(constants.TABLE_PROPERTIES, values, strFilter, null);
		}
	}

	public Map<String, Object> getAllProperties() throws NullPointerException, IllegalStateException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			Log.d("ADAPTER", "Get ALl Properties");

			Cursor cursor = sqlDB.query(constants.TABLE_PROPERTIES, null, null, null,
					null, null, null, null);
			Properties property = null;

			while (cursor.moveToNext()) {
				property = new Properties();
				property.setId(cursor.getInt(cursor
						.getColumnIndex(constants.COL_KEY_ROW)));
				property.setName(cursor.getString(cursor
						.getColumnIndex(constants.PROPERTY_NAME)));
				property.setAddress(cursor.getString(cursor.getColumnIndex(constants.PROPERTY_ADDRESS)));
				property.setImage(cursor.getString(cursor.getColumnIndex(constants.PROPERTY_IMG)));
				if (!property.getName().equals("")) {
					String name = cursor.getString(cursor
							.getColumnIndex(constants.PROPERTY_NAME));
					Object prop_id = property.getId();
					Object image = property.getImage();
					Object address = property.getAddress();
					HashMap<String, Object> objectHashMap = new HashMap<String, Object>();
					objectHashMap.put(constants.PROPERTY_ID, prop_id);
					objectHashMap.put(constants.PROPERTY_IMG, image);
					objectHashMap.put(constants.PROPERTY_ADDRESS, address);
					map.put(name, objectHashMap);
				} else {
					trashProperty(property.getId());
				}
			}
			cursor.close();
		}
		catch (IllegalStateException i) {

			i.printStackTrace();
		}
		return map;
	}


	private void trashProperty(long id){
		deleteProperty(id);
	}
	public Post addPost(Post post) {
		ContentValues values = new ContentValues();
		values.put(constants.POST_VIOLATION_ID, post.getViolationId());
		values.put(constants.POST_IS_POSTED, post.getIsPosted());
		values
				.put(constants.POST_LOCAL_IMAGE_PATH, post
						.getLocalImagePath());
		values.put(constants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(constants.POST_TIMESTAMP, post.getTimestamp());
		values.put(constants.POST_VIOLATION_TYPE, post.getViolationType());
		post.setId(sqlDB.insert(constants.TABLE_POSTS, null, values));
		return post;
	}
	
	public Post getPostById(long postId){
		Post post = new Post();
		Cursor cursor = sqlDB.query(constants.TABLE_POSTS, null, constants.COL_KEY_ROW+"="+postId, null, null, null, null);
		while (cursor.moveToNext()) {
			post.setId(postId);
			post.setViolationId(cursor.getLong(cursor.getColumnIndex(constants.POST_VIOLATION_ID)));
			post.setIsPosted(cursor.getInt(cursor.getColumnIndex(constants.POST_IS_POSTED)));
			post.setLocalImagePath(cursor.getString(cursor.getColumnIndex(constants.POST_LOCAL_IMAGE_PATH)));
			post.setReturnedString(cursor.getString(cursor.getColumnIndex(constants.POST_RETURNED_STRING)));
			post.setTimestamp(cursor.getString(cursor.getColumnIndex(constants.POST_TIMESTAMP)));
			post.setViolationType(cursor.getString(cursor.getColumnIndex(constants.POST_VIOLATION_TYPE)));
		}
		cursor.close();
		return post;
	}
    public Integer getPropertyByName(String propertyName) {
		Integer propId = null;
		Cursor cursor = sqlDB.query(constants.TABLE_PROPERTIES, null, constants.PROPERTY_NAME + "=" + propertyName, null, null, null, null);
		while (cursor.moveToNext()) {
			propId = cursor.getInt(cursor.getColumnIndex(constants.PROPERTY_ID));
		}
		return propId;
	}
	public Properties getPropertyById(long propertyId) {
		try {
			if (!sqlDB.isOpen()) {
				this.open();
			}
		}
		catch (NullPointerException e) {
			this.open();
		}
		Properties properties = null;
		Cursor cursor = sqlDB.query(constants.TABLE_PROPERTIES, null,
				constants.COL_KEY_ROW + "=" + propertyId, null, null, null,
				null);
		while (cursor.moveToNext()) {
			properties = new Properties();
			properties.setId(propertyId);
			properties.setName(cursor.getString(cursor
					.getColumnIndex(constants.PROPERTY_NAME)));
			properties.setAddress(cursor.getString(cursor.getColumnIndex(constants.PROPERTY_ADDRESS)));
			properties.setImage(cursor.getString(cursor.getColumnIndex(constants.PROPERTY_IMG)));
		}
		cursor.close();
		return properties;
	}

	public void updateProperty(long pupilId, String name) {
		ContentValues values = new ContentValues();
		values.put(constants.PROPERTY_NAME, name);
		sqlDB.update(constants.TABLE_PROPERTIES, values, constants.COL_KEY_ROW
				+ "=" + pupilId, null);
	}

	public void deleteProperty(long pupilId) {
		sqlDB.delete(constants.TABLE_PROPERTIES, constants.COL_KEY_ROW + "="
				+ pupilId, null);
	}

	/*public void updatePupilService(Attributes service) {
		ContentValues values = new ContentValues();
		values.put(constants.PROP_VIOLATION_ID, service.getPropertyId());
		values.put(constants.PROPERTY_ID, service.getServiceId());
		values.put(constants.PSERV_ENABLED, service.getIsEnabled());
		values.put(constants.PSERV_NICK, service.getNickname());
		values.put(constants.PSERV_URL, service.getUrl());
		values.put(constants.PSERV_USERNAME, service.getUsername());
		values.put(constants.PSERV_PASSWORD, service.getPassword());
		values.put(constants.PSERV_USEDEFAULT, service.getUseDefault());
		sqlDB.update(constants.TABLE_VIOLATIONS, values,
				constants.COL_KEY_ROW + "=" + service.getId(),
				null);
	}

	public Attributes addPupilService(Attributes service) {
		ContentValues values = new ContentValues();
		values.put(constants.PROP_VIOLATION_ID, service.getPropertyId());
		values.put(constants.PROPERTY_ID, service.getServiceId());
		values.put(constants.PSERV_ENABLED, service.getIsEnabled());
		values.put(constants.PSERV_NICK, service.getNickname());
		values.put(constants.PSERV_URL, service.getUrl());
		values.put(constants.PSERV_USERNAME, service.getUsername());
		values.put(constants.PSERV_PASSWORD, service.getPassword());
		values.put(constants.PSERV_USEDEFAULT, service.getUseDefault());
		service.setId(sqlDB.insert(constants.TABLE_VIOLATIONS, null,
				values));
		return service;
	}

	public void deletePupilService(long id) {
		sqlDB.delete(constants.TABLE_VIOLATIONS,
				constants.COL_KEY_ROW + "=" + id, null);
	}

	public void deletePupilServiceByPupilId(long pupilId) {
		sqlDB.delete(constants.TABLE_VIOLATIONS,
				constants.PROP_VIOLATION_ID + "=" + pupilId, null);
	}

	public Attributes getPupilServicesById(long id) {
		Attributes service = new Attributes();
		Cursor cursor = sqlDB.query(constants.TABLE_VIOLATIONS, null,
				constants.COL_KEY_ROW + "=" + id, null, null, null, null);
		if (cursor.moveToNext()) {
			service.setId(id);
			service.setPropertyId(cursor.getLong(cursor
					.getColumnIndex(constants.PROP_VIOLATION_ID)));
			service.setServiceId(cursor.getLong(cursor
					.getColumnIndex(constants.PROPERTY_ID)));
			service.setIsEnabled(cursor.getInt(cursor
					.getColumnIndex(constants.PSERV_ENABLED)));
			service.setNickname(cursor.getString(cursor
					.getColumnIndex(constants.PSERV_NICK)));
			service.setUrl(cursor.getString(cursor
					.getColumnIndex(constants.PSERV_URL)));
			service.setUsername(cursor.getString(cursor
					.getColumnIndex(constants.PSERV_USERNAME)));
			service.setPassword(cursor.getString(cursor
					.getColumnIndex(constants.PSERV_PASSWORD)));
			service.setUseDefault(cursor.getInt(cursor
					.getColumnIndex(constants.PSERV_USEDEFAULT)));
		}
		cursor.close();
		return service;
	}*/

	public Attributes getViolationByPropertyId(long pupilId, int serviceId) {
		Attributes service = new Attributes();
		Cursor cursor = sqlDB.query(constants.TABLE_VIOLATIONS, null,
				constants.PROP_VIOLATION_ID + "=" + pupilId + " AND "
						+ constants.PROPERTY_ID + "=" + serviceId, null,
				null, null, null);
		if (cursor.moveToNext()) {
			service.setId(cursor.getLong(cursor
					.getColumnIndex(constants.COL_KEY_ROW)));
			service.setPropertyId(cursor.getLong(cursor
					.getColumnIndex(constants.PROP_VIOLATION_ID)));
			service.setViolationId(cursor.getLong(cursor
					.getColumnIndex(constants.PROPERTY_ID)));
					}
		cursor.close();
		return service;
	}

	public void updatePost(Post post) {
		ContentValues values = new ContentValues();
		values.put(constants.POST_IS_POSTED, post.getIsPosted());
		values.put(constants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(constants.POST_TIMESTAMP, post.getTimestamp());
		values.put(constants.POST_VIOLATION_TYPE, post.getViolationType());
		sqlDB.update(constants.TABLE_POSTS, values, constants.COL_KEY_ROW
				+ "=" + post.getId(), null);
	}
}