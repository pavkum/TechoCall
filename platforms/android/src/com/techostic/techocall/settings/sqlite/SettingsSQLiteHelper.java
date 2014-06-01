package com.techostic.techocall.settings.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.modal.Settings;
import com.techostic.techocall.settings.SettingsAPI;

public class SettingsSQLiteHelper extends SQLiteOpenHelper implements
		SettingsAPI {

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "NextTime";

	private static final String CONTACT_ID = "ContactID";

	private static final String FULL_NAME = "FullName";

	private static final String PHOTO = "Photo";

	private static final String PHONE_NUMBER = "PhoneNumber";

	private static final String TABLE_NAME_CONTACT = "Contact";

	private static final String REMAINDER_ID = "RemainderID";

	private static final String REMAINDER_MESSAGE = "RemainderMessage";

	private static final String REMAINDER_TYPE = "RemainderType";

	private static final String IS_REMAINDED = "IsRemainded";

	private static final String REMAINDED_ON = "RemaindedOn";

	private static final String REMAINDED_USING = "RemaindedUsing";

	private static final String TABLE_NAME_REMAINDER = "Remainder";

	private static final String TABLE_NAME_SETTINGS = "Settings";

	private static final String SETTINGS_ID = "SETTINGS_ID";

	private static final String SETTINGS_NAME = "SETTINGS_NAME";

	private static final String SETTINGS_VALUE = "SETTINGS_VALUE";

	public SettingsSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_NAME_CONTACT
				+ " ( " + CONTACT_ID + " INTEGER PRIMARY KEY , " + FULL_NAME
				+ " TEXT," + PHOTO + " TEXT," + PHONE_NUMBER + " TEXT" + ")";

		String CREATE_REMAINDER_TABLE = "CREATE TABLE " + TABLE_NAME_REMAINDER
				+ " ( " + REMAINDER_ID + " INTEGER PRIMARY KEY , " + CONTACT_ID
				+ " INTEGER," + REMAINDER_MESSAGE + " TEXT," + REMAINDER_TYPE
				+ " INTEGER," + IS_REMAINDED + " INTEGER," + REMAINDED_ON
				+ " INTEGER," + REMAINDED_USING + " INTEGER," + "FOREIGN KEY ("
				+ CONTACT_ID + ") REFERENCES " + TABLE_NAME_CONTACT + "("
				+ CONTACT_ID + ") ON DELETE CASCADE " + ")";

		String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_NAME_SETTINGS
				+ " ( " + SETTINGS_ID + " INTEGER PRIMARY KEY , "
				+ SETTINGS_NAME + " TEXT," + SETTINGS_VALUE + " TEXT" + ")";

		Log.d(DATABASE_NAME, CREATE_CONTACT_TABLE);

		db.execSQL(CREATE_CONTACT_TABLE);

		Log.d(DATABASE_NAME, CREATE_REMAINDER_TABLE);

		db.execSQL(CREATE_REMAINDER_TABLE);

		Log.d(DATABASE_NAME, CREATE_SETTINGS_TABLE);

		db.execSQL(CREATE_SETTINGS_TABLE);
		
		Settings autoRemove = new Settings();
		autoRemove.setSettingsID(1l);
		autoRemove.setName("autoRemove");
		autoRemove.setValue("0");
		createSettings(autoRemove);
		
		Settings showCollapsed = new Settings();
		showCollapsed.setSettingsID(2l);
		showCollapsed.setName("showCollapsed");
		showCollapsed.setValue("0");
		createSettings(showCollapsed);
		
		Settings anonymousUsage = new Settings();
		anonymousUsage.setSettingsID(3l);
		anonymousUsage.setName("anonymousUsage");
		anonymousUsage.setValue("1");
		createSettings(anonymousUsage);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SETTINGS);

		this.onCreate(db);
	}
	
	private boolean createSettings(Settings settings){
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getWritableDatabase();
			
			db.beginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(SETTINGS_ID, settings.getSettingsID());
			values.put(SETTINGS_NAME, settings.getName());
			values.put(SETTINGS_VALUE, settings.getValue());
			
			db.insert(TABLE_NAME_SETTINGS, null, values);
			
			db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while creating settings "+settings + " : " + e.getMessage());
		}finally{
			if(db != null){
				db.endTransaction();
				if(db.isOpen()){
					db.close();
				}
			}
		}
		
		return false;
		
	}

	@Override
	public List<Settings> getAllSettings() {

		SQLiteDatabase db = null;

		try {

			db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_NAME_SETTINGS, null, null, null,
					null, null, null);

			List<Settings> settingsList = new ArrayList<Settings>();

			Settings settings = null;

			if (cursor.moveToFirst()) {
				do {
					settings = new Settings();

					settings.setSettingsID(cursor.getLong(0));
					settings.setName(cursor.getString(1));
					settings.setValue(cursor.getString(2));

					settingsList.add(settings);

				} while (cursor.moveToNext());

			} else {
				// empty
			}

			db.close();

			return settingsList;

		} catch (Exception e) {
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION,
					"An error occured while retriving settings list : "
							+ e.getMessage());
		} finally {
			if (db != null) {
				if (db.isOpen()) {
					db.close();
				}
			}
		}

		return null;

	}

	@Override
	public boolean updateSettings(Settings settings) {
		SQLiteDatabase db = null;

		try {
			db = this.getWritableDatabase();
			db.beginTransaction();

			ContentValues values = new ContentValues();

			values.put(SETTINGS_VALUE, settings.getValue());

			String updateArgs[] = { settings.getSettingsID() + "" };

			db.update(TABLE_NAME_SETTINGS, values, SETTINGS_ID + " = ? ",
					updateArgs);

			db.setTransactionSuccessful();
			return true;

		} catch (Exception e) {
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION,
					"Error occured while updating settings : " + settings
							+ " : " + e.getMessage());
		} finally {
			if (db != null) {
				db.endTransaction();
				if (db.isOpen()) {
					db.close();
				}
			}
		}
		return false;
	}
	
	@Override
	public Settings getSettingsBySettingsName(String name) {
		
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getReadableDatabase();
			
			String selectionArgs[] = {name + ""};
			
			Cursor cursor = db.query(TABLE_NAME_SETTINGS, null, SETTINGS_NAME + " = ? ", selectionArgs , null, null, null);
			
			Settings settings = new Settings();
			
			if(cursor.moveToFirst()){
				do {
					settings = new Settings();
					
					settings.setSettingsID(cursor.getLong(0));
					settings.setName(cursor.getString(1));
					settings.setValue(cursor.getString(2));
					
					break;
					
				}while(cursor.moveToNext());
				
			}else{
				// empty
			}
			
			db.close();
			
			return settings;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "An error occured while retriving settings by name : "+name + " : " + e.getMessage());
		}finally{
			if(db != null){
				if(db.isOpen()){
					db.close();
				}
			}
		}
		
		return null;
	}

}
