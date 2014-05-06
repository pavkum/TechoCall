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
import com.techostic.techocall.storage.ContactAPI;

public class ContactSQLiteHelper extends SQLiteOpenHelper implements ContactAPI {

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "TechoCall";
	
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
	
	public ContactSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_NAME_CONTACT + " ( " +
                CONTACT_ID + " INTEGER PRIMARY KEY , " + 
                FULL_NAME + " TEXT," +
                PHOTO + " TEXT," +
                PHONE_NUMBER + " TEXT" +
                ")";
		
		
		String CREATE_REMAINDER_TABLE = "CREATE TABLE " + TABLE_NAME_REMAINDER + " ( " +
                REMAINDER_ID  + " INTEGER PRIMARY KEY , " + 
                CONTACT_ID + " INTEGER," +
                REMAINDER_MESSAGE + " TEXT," +
                REMAINDER_TYPE + " INTEGER," +
                IS_REMAINDED + " INTEGER," +
                REMAINDED_ON + " INTEGER," +
                REMAINDED_USING + " INTEGER" +
                ")";
		
		Log.d(DATABASE_NAME, CREATE_CONTACT_TABLE);
		
		db.execSQL(CREATE_CONTACT_TABLE);
		
		Log.d(DATABASE_NAME, CREATE_REMAINDER_TABLE);
		
		db.execSQL(CREATE_REMAINDER_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Contact");
		
		this.onCreate(db);

	}

	@Override
	public boolean addContact(Contact contact) {
		SQLiteDatabase db = null;
		
		try{
			
			if(getContactById(contact.getContactID()) != null){
				return true;
			}
			
			db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			
			values.put(CONTACT_ID, contact.getContactID());
			values.put(FULL_NAME, contact.getFullName());
			values.put(PHOTO, contact.getPhotoURL());
			values.put(PHONE_NUMBER, contact.getPhoneNumber());
			
			db.insert(TABLE_NAME_CONTACT, null, values);
			
			db.close();
			
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while creating contact "+contact + " : " + e.getMessage());
		}finally{
			if(db != null){
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

}
