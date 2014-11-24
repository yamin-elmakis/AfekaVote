package com.afekavote.memory;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 5;

	// Database Name
	private static final String DATABASE_NAME = "afeka_vote_db";

	// Login User table name
	private static final String TABLE_USER_LOGIN = "login";

	// Login User Table Columns names
	public static final String KEY_USER_ID = "id";
	public static final String KEY_USER_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_USER_MAIL = "mail";
	public static final String KEY_UID = "uid";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_IS_FAC = "face";

	private static DB instance;
	
	private DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DB getInstance(Context context){
		if (instance == null)
			instance = new DB(context);
		return instance;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creating User Table
//		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER_LOGIN + "("
//				+ KEY_USER_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"
//				+ KEY_EMAIL + " TEXT UNIQUE," +KEY_USER_MAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_PHONE
//				+ " TEXT," + KEY_IS_FAC + " INTEGER," + KEY_CREATED_AT
//				+ " TEXT" + ")";
//		db.execSQL(CREATE_LOGIN_TABLE);
	}
		

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOGIN);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid, String phone,
			String isFac, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_USER_MAIL, email); // Email
		values.put(KEY_UID, uid); // uid
		values.put(KEY_PHONE, phone); // phone
		values.put(KEY_IS_FAC, isFac); // isFace
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		db.insert(TABLE_USER_LOGIN, null, values);
		db.close(); // Closing database connection
	}


	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void resetTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER_LOGIN, null, null);
		db.close();
	}

	

}
