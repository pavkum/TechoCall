package com.techostic.techocall.storage.sqllite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.storage.RemainderAPI;

public class RemainderSQLiteHelper extends SQLiteOpenHelper implements RemainderAPI{

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "NextTime";
	
	private static final String TABLE_NAME = "Remainder";
	
	private static final String REMAINDER_ID = "RemainderID";
	
	private static final String CONTACT_ID = "ContactID";
	
	private static final String REMAINDER_MESSAGE = "RemainderMessage";
	
	private static final String REMAINDER_TYPE = "RemainderType";
	
	private static final String IS_REMAINDED = "IsRemainded";
	
	private static final String REMAINDED_ON = "RemaindedOn";
	
	private static final String REMAINDED_USING = "RemaindedUsing";
	
	public RemainderSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_REMAINDER_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
                REMAINDER_ID  + " INTEGER PRIMARY KEY , " + 
                CONTACT_ID + " INTEGER," +
                REMAINDER_MESSAGE + " TEXT," +
                REMAINDER_TYPE + " INTEGER," +
                IS_REMAINDED + " INTEGER," +
                REMAINDED_ON + " INTEGER," +
                REMAINDED_USING + " INTEGER" +
                ")";
		
		Log.d(DATABASE_NAME, CREATE_REMAINDER_TABLE);
		
		db.execSQL(CREATE_REMAINDER_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		
		this.onCreate(db);
	}

	@Override
	public List<Remainder> getAllRemaindersByID(long userID) {
		
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getReadableDatabase();
			
			String selectionArgs[] = {userID + ""};
			
			Cursor cursor = db.query(TABLE_NAME, null, CONTACT_ID + " = ? ", selectionArgs , null, null, null);
			
			List<Remainder> remainderList = new ArrayList<Remainder>();
			
			Remainder remainder = null;
			
			if(cursor.moveToFirst()){
				do {
					remainder = new Remainder();
					
					remainder.setRemainderID(cursor.getLong(0));
					remainder.setContactID(cursor.getLong(1));
					remainder.setRemainderMessage(cursor.getString(2));
					remainder.setRemainderType(Byte.parseByte(cursor.getInt(3) + ""));
					
					int isRemainded = cursor.getInt(4);
					if(isRemainded == 1){
						remainder.setRemainded(true);
					}else{
						remainder.setRemainded(false);
					}
					
					remainder.setRemaindedOn(cursor.getLong(5));
					remainder.setRemaindedUsing(Byte.parseByte(cursor.getInt(6) + ""));
					
					remainderList.add(remainder);
					
				}while(cursor.moveToNext());
				
			}else{
				// empty
			}
			
			db.close();
			
			return remainderList;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "An error occured while retriving remainder list for contact ID : "+userID + " : " + e.getMessage());
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
	public boolean addRemainder(Remainder remainder) {
		SQLiteDatabase db = null;
		
		try{
			db = this.getWritableDatabase();
			db.beginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(REMAINDER_ID, remainder.getRemainderID());
			values.put(CONTACT_ID, remainder.getContactID());
			values.put(REMAINDER_MESSAGE, remainder.getRemainderMessage());
			values.put(REMAINDER_TYPE, remainder.getRemainderType());
			values.put(IS_REMAINDED, remainder.isRemainded());
			values.put(REMAINDED_ON, remainder.getRemaindedOn());
			values.put(REMAINDED_USING, remainder.getRemaindedUsing());
			
			db.insert(TABLE_NAME, null, values);
			
			db.setTransactionSuccessful();
			
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while creating Remainder "+remainder + " : " + e.getMessage());
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
	public boolean updateRemainder(Remainder remainder) {
		SQLiteDatabase db = null;
		
		try{
			db = this.getWritableDatabase();
			db.beginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(REMAINDER_MESSAGE, remainder.getRemainderMessage());
			values.put(IS_REMAINDED, remainder.isRemainded());
			values.put(REMAINDED_ON, remainder.getRemaindedOn());
			values.put(REMAINDED_USING, remainder.getRemaindedUsing());
			
			String updateArgs[] = {remainder.getRemainderID() + ""};
			
			db.update(TABLE_NAME, values, REMAINDER_ID + " = ? ", updateArgs);
			
			db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while updating Remainder "+remainder + " : " + e.getMessage());
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
	public boolean deleteRemainder(List<Long> remainderIDs) {
		SQLiteDatabase db = null;
		
		try{
			db = this.getWritableDatabase();
			db.beginTransaction();
			
			for(int i=0; i<remainderIDs.size(); i++){
				Long remainderID = remainderIDs.get(i);
				
				String deleteArgs[] = {remainderID + ""};
				
				db.delete(TABLE_NAME, REMAINDER_ID + " = ? ", deleteArgs);
			}
			
			db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while deleting Remainder "+remainderIDs + " : " + e.getMessage());
			
		
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
	public List<Remainder> getAllPendingRemaindersByContactID(long contactID) {
		SQLiteDatabase db = null;
		
		try{
			
			db = this.getReadableDatabase();
			
			String columns[] = {REMAINDER_ID , CONTACT_ID , REMAINDER_MESSAGE};
			String selectionArgs[] = {contactID + "" };
			
			
			
			Cursor cursor = db.query(TABLE_NAME, columns, CONTACT_ID + " = ? and " + IS_REMAINDED + " = 0", selectionArgs, null, null, null);
			
			List<Remainder> remainderList = new ArrayList<Remainder>();
			Remainder remainder = null;
			
			if(cursor.moveToFirst()){
				do {
					remainder = new Remainder();
					
					remainder.setRemainderID(cursor.getLong(0));
					remainder.setContactID(cursor.getLong(1));
					remainder.setRemainderMessage(cursor.getString(2));
					
					remainderList.add(remainder);
					
				}while(cursor.moveToNext());
			}
			
			db.close();
			
			return remainderList;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while getting pending remainders for contact ID "+contactID + " : " + e.getMessage());
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
	public boolean deleteRemainderByContactId(long contactID) {
		SQLiteDatabase db = null;
		
		try{
			db = this.getWritableDatabase();
			db.beginTransaction();
			
			String deleteArgs[] = {contactID + ""};
			
			db.delete(TABLE_NAME, CONTACT_ID + " = ? ", deleteArgs);
			
			db.setTransactionSuccessful();
			return true;
			
		}catch(Exception e){
			Log.d(DATABASE_NAME + " : " + DATABASE_VERSION, "Error occured while deleting Remainder by contactId "+contactID + " : " + e.getMessage());
			
		
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
