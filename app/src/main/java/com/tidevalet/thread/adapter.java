package com.tidevalet.thread;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tidevalet.helpers.Post;
import com.tidevalet.helpers.Attributes;
import com.tidevalet.helpers.Violation;

import java.util.ArrayList;

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
			db.execSQL(constants.SQL_PUPIL);
			db.execSQL(constants.SQL_POSTS);
			db.execSQL(constants.SQL_PSERV);
			db.execSQL(constants.SQL_SERVICES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public long addPupil(String pupilName) {
		ContentValues values = new ContentValues();
		values.put(constants.PUPIL_NAME, pupilName);
		return sqlDB.insert(constants.TABLE_PUPIL, null, values);
	}

	/*public ArrayList<Pupil> getAllPupils() {
		ArrayList<Pupil> pupils = new ArrayList<Pupil>();
		Cursor cursor = sqlDB.query(constants.TABLE_PUPIL, null, null, null,
				null, null, null);
		Pupil pupil = null;
		while (cursor.moveToNext()) {
			pupil = new Pupil();
			pupil.setId(cursor.getLong(cursor
					.getColumnIndex(constants.COL_KEY_ROW)));
			pupil.setName(cursor.getString(cursor
					.getColumnIndex(constants.PUPIL_NAME)));
			if(!pupil.getName().equals("")){
				pupils.add(pupil);
			}else{
				trashPupil(pupil.getId());
			}
		}
		cursor.close();
		return pupils;
	}*/

	private void trashPupil(long id){
		deletePupil(id);
	}
	public Post addPost(Post post) {
		ContentValues values = new ContentValues();
		values.put(constants.POST_PUPIL_ID, post.getPupilId());
		values.put(constants.POST_IS_POSTED, post.getIsPosted());
		values
				.put(constants.POST_LOCAL_IMAGE_PATH, post
						.getLocalImagePath());
		values.put(constants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(constants.POST_TIMESTAMP, post.getTimestamp());
		values.put(constants.POST_GRADE, post.getGrade());
		post.setId(sqlDB.insert(constants.TABLE_POSTS, null, values));
		return post;
	}
	
	public Post getPostById(long postId){
		Post post = new Post();
		Cursor cursor = sqlDB.query(constants.TABLE_POSTS, null, constants.COL_KEY_ROW+"="+postId, null, null, null, null);
		while (cursor.moveToNext()) {
			post.setId(postId);
			post.setPupilId(cursor.getLong(cursor.getColumnIndex(constants.POST_PUPIL_ID)));
			post.setIsPosted(cursor.getInt(cursor.getColumnIndex(constants.POST_IS_POSTED)));
			post.setLocalImagePath(cursor.getString(cursor.getColumnIndex(constants.POST_LOCAL_IMAGE_PATH)));
			post.setReturnedString(cursor.getString(cursor.getColumnIndex(constants.POST_RETURNED_STRING)));
			post.setTimestamp(cursor.getString(cursor.getColumnIndex(constants.POST_TIMESTAMP)));
			post.setGrade(cursor.getString(cursor.getColumnIndex(constants.POST_GRADE)));
		}
		cursor.close();
		return post;
	}

	public Pupil getPupilById(long pupilId) {
		Pupil pupil = null;
		Cursor cursor = sqlDB.query(constants.TABLE_PUPIL, null,
				constants.COL_KEY_ROW + "=" + pupilId, null, null, null,
				null);
		while (cursor.moveToNext()) {
			pupil = new Pupil();
			pupil.setId(pupilId);
			pupil.setName(cursor.getString(cursor
					.getColumnIndex(constants.PUPIL_NAME)));
		}
		cursor.close();
		return pupil;
	}

	public void updatePupil(long pupilId, String name) {
		ContentValues values = new ContentValues();
		values.put(constants.PUPIL_NAME, name);
		sqlDB.update(constants.TABLE_PUPIL, values, constants.COL_KEY_ROW
				+ "=" + pupilId, null);
	}

	public void deletePupil(long pupilId) {
		sqlDB.delete(constants.TABLE_PUPIL, constants.COL_KEY_ROW + "="
				+ pupilId, null);
	}

	public void updatePupilService(Attributes service) {
		ContentValues values = new ContentValues();
		values.put(constants.PSERV_PUPIL_ID, service.getPupilId());
		values.put(constants.PSERV_SERV_ID, service.getServiceId());
		values.put(constants.PSERV_ENABLED, service.getIsEnabled());
		values.put(constants.PSERV_NICK, service.getNickname());
		values.put(constants.PSERV_URL, service.getUrl());
		values.put(constants.PSERV_USERNAME, service.getUsername());
		values.put(constants.PSERV_PASSWORD, service.getPassword());
		values.put(constants.PSERV_USEDEFAULT, service.getUseDefault());
		sqlDB.update(constants.TABLE_PUPIL_SERVICES, values,
				constants.COL_KEY_ROW + "=" + service.getId(),
				null);
	}

	public Attributes addPupilService(Attributes service) {
		ContentValues values = new ContentValues();
		values.put(constants.PSERV_PUPIL_ID, service.getPupilId());
		values.put(constants.PSERV_SERV_ID, service.getServiceId());
		values.put(constants.PSERV_ENABLED, service.getIsEnabled());
		values.put(constants.PSERV_NICK, service.getNickname());
		values.put(constants.PSERV_URL, service.getUrl());
		values.put(constants.PSERV_USERNAME, service.getUsername());
		values.put(constants.PSERV_PASSWORD, service.getPassword());
		values.put(constants.PSERV_USEDEFAULT, service.getUseDefault());
		service.setId(sqlDB.insert(constants.TABLE_PUPIL_SERVICES, null,
				values));
		return service;
	}

	public void deletePupilService(long id) {
		sqlDB.delete(constants.TABLE_PUPIL_SERVICES,
				constants.COL_KEY_ROW + "=" + id, null);
	}

	public void deletePupilServiceByPupilId(long pupilId) {
		sqlDB.delete(constants.TABLE_PUPIL_SERVICES,
				constants.PSERV_PUPIL_ID + "=" + pupilId, null);
	}

	public Violation getPupilServicesById(long id) {
		Attributes service = new Attributes();
		Cursor cursor = sqlDB.query(constants.TABLE_PUPIL_SERVICES, null,
				constants.COL_KEY_ROW + "=" + id, null, null, null, null);
		if (cursor.moveToNext()) {
			service.setId(id);
			service.setPupilId(cursor.getLong(cursor
					.getColumnIndex(constants.PSERV_PUPIL_ID)));
			service.setServiceId(cursor.getLong(cursor
					.getColumnIndex(constants.PSERV_SERV_ID)));
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
	}

	public Attributes getPupilServiceByPupilId(long pupilId, int serviceId) {
		Attributes service = new PupilServices();
		Cursor cursor = sqlDB.query(constants.TABLE_PUPIL_SERVICES, null,
				constants.PSERV_PUPIL_ID + "=" + pupilId + " AND "
						+ constants.PSERV_SERV_ID + "=" + serviceId, null,
				null, null, null);
		if (cursor.moveToNext()) {
			service.setId(cursor.getLong(cursor
					.getColumnIndex(constants.COL_KEY_ROW)));
			service.setPupilId(cursor.getLong(cursor
					.getColumnIndex(constants.PSERV_PUPIL_ID)));
			service.setServiceId(cursor.getLong(cursor
					.getColumnIndex(constants.PSERV_SERV_ID)));
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
	}

	public void updatePost(Post post) {
		ContentValues values = new ContentValues();
		values.put(constants.POST_IS_POSTED, post.getIsPosted());
		values.put(constants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(constants.POST_TIMESTAMP, post.getTimestamp());
		values.put(constants.POST_GRADE, post.getGrade());
		sqlDB.update(constants.TABLE_POSTS, values, constants.COL_KEY_ROW
				+ "=" + post.getId(), null);
	}
}
