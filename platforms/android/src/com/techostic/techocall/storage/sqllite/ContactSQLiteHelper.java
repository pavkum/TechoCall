package com.techostic.techocall.storage.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Settings;
import com.techostic.techocall.storage.ContactAPI;

public class ContactSQLiteHelper extends SQLiteOpenHelper implements ContactAPI {

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
	
	public ContactSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_NAME_CONTACT
				+ " ( " + CONTACT_ID + " INTEGER PRIMARY KEY , " + FULL_NAME
				+ " TEXT," + PHOTO + " TEXT," + PHONE_NUMBER + " TEXT" + ");";

		String CREATE_REMAINDER_TABLE = "CREATE TABLE " + TABLE_NAME_REMAINDER
				+ " ( " + REMAINDER_ID + " INTEGER PRIMARY KEY , " + CONTACT_ID
				+ " INTEGER," + REMAINDER_MESSAGE + " TEXT," + REMAINDER_TYPE
				+ " INTEGER," + IS_REMAINDED + " INTEGER," + REMAINDED_ON
				+ " INTEGER," + REMAINDED_USING + " INTEGER," + " FOREIGN KEY ("
				+ CONTACT_ID + ") REFERENCES " + TABLE_NAME_CONTACT + "("
				+ CONTACT_ID + ") ON DELETE CASCADE" + ");";

		String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_NAME_SETTINGS
				+ " ( " + SETTINGS_ID + " INTEGER PRIMARY KEY , "
				+ SETTINGS_NAME + " TEXT," + SETTINGS_VALUE + " TEXT" + ");";

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
		createSettings(autoRemove , db);
		
		Settings showCollapsed = new Settings();
		showCollapsed.setSettingsID(2l);
		showCollapsed.setName("showCollapsed");
		showCollapsed.setValue("0");
		createSettings(showCollapsed , db);
		
		Settings anonymousUsage = new Settings();
		anonymousUsage.setSettingsID(3l);
		anonymousUsage.setName("anonymousUsage");
		anonymousUsage.setValue("1");
		createSettings(anonymousUsage , db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Contact");
		
		this.onCreate(db);

	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	private boolean createSettings(Settings settings , SQLiteDatabase db){
		
		try{
			
			//db = this.getWritableDatabase();
			
			//db.beginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(SETTINGS_ID, settings.getSettingsID());
			values.put(SETTINGS_NAME, settings.getName());
			values.put(SETTINGS_VALUE, settings.getValue());
			
			db.insert(TABLE_NAME_SETTINGS, null, values);
			
			//db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while creating settings "+settings + " : " + e.getMessage());
		}finally{
			if(db != null){
				//db.endTransaction();
				if(db.isOpen()){
					//db.close();
				}
			}
		}
		
		return false;
		
	}


	@Override
	public boolean addContact(Contact contact) {
		SQLiteDatabase db = null;
		
		try{
			
			if(getContactById(contact.getContactID()) != null){
				return true;
			}
			
			db = this.getWritableDatabase();
			
			db.beginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(CONTACT_ID, contact.getContactID());
			values.put(FULL_NAME, contact.getFullName());
			values.put(PHOTO, contact.getPhotoURL());
			values.put(PHONE_NUMBER, contact.getPhoneNumber());
			
			db.insert(TABLE_NAME_CONTACT, null, values);
			
			db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while creating contact "+contact + " : " + e.getMessage());
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
	public List<Contact> getAllContacts() {
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME_CONTACT, null, null, null, null, null, FULL_NAME + " ASC");

			List<Contact> contactList = new ArrayList<Contact>();
			
			Contact contact = null;
			if(cursor.moveToFirst()){
				
				do{
					contact = new Contact();
					contact.setContactID(cursor.getLong(0));
					contact.setFullName(cursor.getString(1));
					contact.setPhotoURL(cursor.getString(2));
					contact.setPhoneNumber(cursor.getString(3));
					
					contactList.add(contact);
					
				}while(cursor.moveToNext());
				
			}else{
				//empty
			}
			
			db.close();
			
			return contactList;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while getting all contacts : " + e.getMessage());
		}finally{
			if(db != null){
				if(db.isOpen()){
					db.close();
				}
			}
		}
		
		return null;
	}
	
	@Override
	public Long getContactIDByPhoneNumber(String phoneNumber) {
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getReadableDatabase();
			
			String columns[] = {CONTACT_ID + ""};
			String like[] = {"%" + phoneNumber + "%"};
			 
			Cursor cursor = db.query(TABLE_NAME_CONTACT, columns, PHONE_NUMBER + " LIKE ? ", like, null, null, null);
			
			Long contactID = -1l;
			
			if(cursor.moveToFirst()){
				contactID = cursor.getLong(0);
			}
			
			db.close();
			
			return contactID;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME	+ " : " + DATABASE_VERSION, "Error occured while getting contactID by phonenumber : "+phoneNumber + " : "+e.getMessage());
		}finally{
			if(db != null){
				if(db.isOpen()){
					db.close();
				}
			}
		}
		
		return null;
	}

	@Override
	public Contact getContactById(long contactID) {
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getReadableDatabase();
			
			String where[] = {contactID + ""};
			 
			Cursor cursor = db.query(TABLE_NAME_CONTACT, null, CONTACT_ID + " = ? ", where, null, null, null);
			
			Contact contact = null;
			
			if(cursor.moveToFirst()){
				
				contact = new Contact();
				
				contact.setContactID(cursor.getLong(0));
				contact.setFullName(cursor.getString(1));
				contact.setPhotoURL(cursor.getString(2));
				contact.setPhoneNumber(cursor.getString(3));
				
			}
			
			db.close();
			
			return contact;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME	+ " : " + DATABASE_VERSION, "Error occured while getting contact by contactID : "+contactID + " : "+e.getMessage());
		}finally{
			if(db != null){
				if(db.isOpen()){
					db.close();
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean deleteContactById(List<Long> contactIDs) {
		SQLiteDatabase db = null;
		// Foreign key mapping should delete all remainder ids automatically
		try{
			db = this.getWritableDatabase();
			db.beginTransaction();
			
			for(int i=0; i<contactIDs.size(); i++){
				String deleteArgs[] = {contactIDs.get(i) + ""};
				
				db.delete(TABLE_NAME_CONTACT, CONTACT_ID + " = ? ", deleteArgs);
			}
			
			
			db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while deleting Contact by contactId "+contactIDs + " : " + e.getMessage());
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

}
