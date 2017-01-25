
package com.tidevalet.thread;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tidevalet.App;
import com.tidevalet.SessionManager;
import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adapter {
	private SQLiteDatabase sqlDB;
	private DatabaseHelper dbHelper;
    private SessionManager sm = new SessionManager(App.getAppContext());


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
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public long addProperty(int propId, String propertyName, String address, String image, String contractors, String complexmgrs) {
		ContentValues values = new ContentValues();
		values.put(constants.COL_KEY_ROW, propId);
		values.put(constants.PROPERTY_NAME, propertyName);
		values.put(constants.PROPERTY_ADDRESS, address);
		values.put(constants.PROPERTY_IMG, image);
		values.put(constants.PROPERTY_CONTRACTOR, contractors);
		values.put(constants.PROPERTY_COMPLEXMGRS, complexmgrs);
		String strFilter = constants.COL_KEY_ROW + "=" + propId;
		if (getPropertyById(propId) == null) {
			return sqlDB.insert(constants.TABLE_PROPERTIES, null, values);
		}
		else {
			return sqlDB.update(constants.TABLE_PROPERTIES, values, strFilter, null);
		}
	}
	public List<Post> getPostsByPropertyId(long propId) throws Exception {
		List<Post> postList = new ArrayList<Post>();
		Cursor cursor = sqlDB.query(constants.TABLE_POSTS, null, constants.PROPERTY_ID+"="+propId, null, null, null, null);
		while (cursor.moveToNext()) {
			Post post = new Post();
			post.setId(cursor.getLong(cursor.getColumnIndex(constants.COL_KEY_ROW)));
			post.setViolationId(cursor.getLong(cursor.getColumnIndex(constants.POST_VIOLATION_ID)));
			post.setIsPosted(cursor.getInt(cursor.getColumnIndex(constants.POST_IS_POSTED)));
			post.setPropertyId(cursor.getInt(cursor.getColumnIndex(constants.PROPERTY_ID)));
			post.setLocalImagePath(cursor.getString(cursor.getColumnIndex(constants.POST_LOCAL_IMAGE_PATH)));
			post.setImagePath(cursor.getString(cursor.getColumnIndex(constants.POST_IMAGES)));
			post.setReturnedString(cursor.getString(cursor.getColumnIndex(constants.POST_RETURNED_STRING)));
			post.setTimestamp(cursor.getString(cursor.getColumnIndex(constants.POST_TIMESTAMP)));
			post.setViolationType(cursor.getString(cursor.getColumnIndex(constants.POST_VIOLATION_TYPE)));
			post.setContractorComments(cursor.getString(cursor.getColumnIndex(constants.POST_COMMENTS)));
			post.setBldg(cursor.getString(cursor.getColumnIndex(constants.POST_BLDG)));
			post.setUnit(cursor.getString(cursor.getColumnIndex(constants.POST_UNIT)));
			postList.add(post);
		}
		cursor.close();
		return postList;
	}

	public Map<String, Object> getAllProperties() throws NullPointerException, IllegalStateException {
		HashMap<String, Object> map = new HashMap<String, Object>();
			Cursor cursor = sqlDB.query(constants.TABLE_PROPERTIES, null, null, null,
					null, null, null, null);
			Properties property = null;
			while (cursor.moveToNext()) {
				property = new Properties();
				property.setId(cursor.getInt(cursor
						.getColumnIndex(constants.COL_KEY_ROW)));
				property.setName(cursor.getString(cursor
						.getColumnIndex(constants.PROPERTY_NAME)));
				property.setAddress(cursor.getString(cursor
						.getColumnIndex(constants.PROPERTY_ADDRESS)));
				property.setImage(cursor.getString(cursor
						.getColumnIndex(constants.PROPERTY_IMG)));
				property.setContractors(cursor.getString(cursor
						.getColumnIndex(constants.PROPERTY_CONTRACTOR)));
				property.setComplexMgrs(cursor.getString(cursor
						.getColumnIndex(constants.PROPERTY_COMPLEXMGRS)));
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
					if (!sm.getRole().equals("contractor")) {
                        map.put(name,objectHashMap);
                    }
					else if (property.isContractor()) {
                        map.put(name, objectHashMap);
                    }
					else {
                        trashProperty(property.getId());
                        Log.d("Trashed", name);
                    }
				}
                else {
					trashProperty(property.getId());
				}
			}
			cursor.close();
		return map;
	}
	private void trashProperty(long id){
		deleteProperty(id);
	}
	public Post addPost(Post post) {
		ContentValues values = new ContentValues();
		values.put(constants.POST_VIOLATION_ID, post.getViolationId());
		values.put(constants.POST_IS_POSTED, post.getIsPosted());
		values.put(constants.POST_LOCAL_IMAGE_PATH, post.getLocalImagePath());
		values.put(constants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(constants.PROPERTY_ID, post.getPropertyId());
		values.put(constants.POST_IMAGES, post.getImagePath());
		values.put(constants.POST_TIMESTAMP, post.getTimestamp());
		values.put(constants.POST_VIOLATION_TYPE, post.getViolationType());
		values.put(constants.POST_BLDG, post.getBldg());
		values.put(constants.POST_UNIT, post.getUnit());
		values.put(constants.PICKEDUP, post.getPU());
		values.put(constants.POST_COMMENTS, post.getContractorComments());
		post.setId(sqlDB.insert(constants.TABLE_POSTS, null, values));
		Log.d("addPost", post.getPropertyId() + " " + post.getViolationId() + " " + post.getId());
		return post;
	}
	
	public Post getPostById(long postId){
		Post post = new Post();
		Cursor cursor = sqlDB.query(constants.TABLE_POSTS, null, constants.COL_KEY_ROW+"="+postId, null, null, null, null);
		while (cursor.moveToNext()) {
			post.setId(postId);
			post.setViolationId(cursor.getLong(cursor.getColumnIndex(constants.POST_VIOLATION_ID)));
			post.setIsPosted(cursor.getInt(cursor.getColumnIndex(constants.POST_IS_POSTED)));
			post.setPropertyId(cursor.getInt(cursor.getColumnIndex(constants.PROPERTY_ID)));
			post.setLocalImagePath(cursor.getString(cursor.getColumnIndex(constants.POST_LOCAL_IMAGE_PATH)));
			post.setImagePath(cursor.getString(cursor.getColumnIndex(constants.POST_IMAGES)));
			post.setReturnedString(cursor.getString(cursor.getColumnIndex(constants.POST_RETURNED_STRING)));
			post.setTimestamp(cursor.getString(cursor.getColumnIndex(constants.POST_TIMESTAMP)));
			post.setViolationType(cursor.getString(cursor.getColumnIndex(constants.POST_VIOLATION_TYPE)));
			post.setContractorComments(cursor.getString(cursor.getColumnIndex(constants.POST_COMMENTS)));
			post.setBldg(cursor.getString(cursor.getColumnIndex(constants.POST_BLDG)));
			post.setUnit(cursor.getString(cursor.getColumnIndex(constants.POST_UNIT)));
			post.setPU(cursor.getInt(cursor.getColumnIndex(constants.PICKEDUP)));
		}
		cursor.close();
		return post;
	}
	public Properties getPropertyById(long propertyId) {
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
			properties.setContractors(cursor.getString(cursor.getColumnIndex(constants.PROPERTY_CONTRACTOR)));
			properties.setComplexMgrs(cursor.getString(cursor.getColumnIndex(constants.PROPERTY_COMPLEXMGRS)));
		}
		cursor.close();
		return properties;
	}

	public void updateProperty(long propertyId, String name, String contractorList, String address, String image, String complexMgrs) {
		ContentValues values = new ContentValues();
		values.put(constants.PROPERTY_NAME, name);
		values.put(constants.PROPERTY_CONTRACTOR, contractorList);
		values.put(constants.PROPERTY_ADDRESS, address);
		values.put(constants.PROPERTY_IMG, image);
		values.put(constants.PROPERTY_COMPLEXMGRS, complexMgrs);
		sqlDB.update(constants.TABLE_PROPERTIES, values, constants.COL_KEY_ROW
				+ "=" + propertyId, null);
	}

	public void deleteProperty(long propertyId) {
		sqlDB.delete(constants.TABLE_PROPERTIES, constants.COL_KEY_ROW + "="
				+ propertyId, null);
	}
	public boolean deletePost(long postId) {
		try { sqlDB.delete(constants.TABLE_POSTS, constants.COL_KEY_ROW + "=" + postId, null); }
		catch (SQLException E) { return false; }
		return true;
	}
	public void updatePost(Post post) {
		ContentValues values = new ContentValues();
		values.put(constants.POST_VIOLATION_ID, post.getViolationId());
		values.put(constants.POST_IS_POSTED, post.getIsPosted());
		values.put(constants.POST_LOCAL_IMAGE_PATH, post.getLocalImagePath());
		values.put(constants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(constants.PROPERTY_ID, post.getPropertyId());
		values.put(constants.POST_IMAGES, post.getImagePath());
		values.put(constants.POST_TIMESTAMP, post.getTimestamp());
		values.put(constants.POST_VIOLATION_TYPE, post.getViolationType());
		values.put(constants.POST_BLDG, post.getBldg());
		values.put(constants.POST_UNIT, post.getUnit());
		values.put(constants.PICKEDUP, post.getPU());
		values.put(constants.POST_COMMENTS, post.getContractorComments());
		sqlDB.update(constants.TABLE_POSTS, values, constants.COL_KEY_ROW
				+ "=" + post.getId(), null);
	}
}