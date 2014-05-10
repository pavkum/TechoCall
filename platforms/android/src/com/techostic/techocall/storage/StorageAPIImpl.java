package com.techostic.techocall.storage;

import java.util.List;

import android.content.Context;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.storage.sqllite.ContactSQLiteHelper;
import com.techostic.techocall.storage.sqllite.RemainderSQLiteHelper;

public class StorageAPIImpl implements StorageAPI {

	private static StorageAPIImpl storageAPIImpl = null;
	
	private static ContactSQLiteHelper contactSQLiteHelper = null;
	
	private static RemainderSQLiteHelper remainderSQLiteHelper = null;
	
	private StorageAPIImpl(Context context){
		
		contactSQLiteHelper = new ContactSQLiteHelper(context);
		remainderSQLiteHelper = new RemainderSQLiteHelper(context);
	}
	
	public static StorageAPI getInstance(Context context){
		if(storageAPIImpl == null)
			storageAPIImpl = new StorageAPIImpl(context);
		
		return storageAPIImpl;
	}
	
	@Override
	public boolean addContact(Contact contact) {
		return contactSQLiteHelper.addContact(contact);
	}
	
	@Override
	public List<Contact> getAllContacts() {
		return contactSQLiteHelper.getAllContacts();
	}

	@Override
	public Long getContactIDByPhoneNumber(String phoneNumber) {
		return contactSQLiteHelper.getContactIDByPhoneNumber(phoneNumber);
	}
	
	@Override
	public List<Remainder> getAllRemaindersByContactID(long contactID) {
		return remainderSQLiteHelper.getAllRemaindersByID(contactID);
	}

	@Override
	public boolean addRemainder(Remainder remainder) {
		
		return remainderSQLiteHelper.addRemainder(remainder);
	}

	@Override
	public boolean updateRemainder(Remainder remainder) {
		return remainderSQLiteHelper.updateRemainder(remainder);
	}
	
	@Override
	public Remainder getRemainderByID(long remainderID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteRemainder(List<Long> remainderIDs) {
		return remainderSQLiteHelper.deleteRemainder(remainderIDs);
	}

	@Override
	public Contact getContactById(long contactID) {
		return contactSQLiteHelper.getContactById(contactID);
	}

	@Override
	public List<Remainder> getAllPendingRemaindersByContactID(long contactID) {
		return remainderSQLiteHelper.getAllPendingRemaindersByContactID(contactID);
	}

	

}
