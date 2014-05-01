package com.techostic.techocall.storage.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PhoneNumberSQLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "TechoCall";
	
	public PhoneNumberSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PHONENUMBER_TABLE = "CREATE TABLE PhoneNumber ( " +
                "contactID INTEGER, " + 
                "phoneNumber TEXT" +
                ")";
		
		db.execSQL(CREATE_PHONENUMBER_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS PhoneNumber");
		
		this.onCreate(db);

	}

}
