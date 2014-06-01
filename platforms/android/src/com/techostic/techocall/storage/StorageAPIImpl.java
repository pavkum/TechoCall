package com.techostic.techocall.storage;

import java.util.List;

import android.content.Context;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.modal.Settings;
import com.techostic.techocall.settings.sqlite.SettingsSQLiteHelper;
import com.techostic.techocall.storage.sqllite.ContactSQLiteHelper;
import com.techostic.techocall.storage.sqllite.RemainderSQLiteHelper;

public class StorageAPIImpl implements StorageAPI {

	private static StorageAPIImpl storageAPIImpl = null;
	
	private static ContactSQLiteHelper contactSQLiteHelper = null;
	
	private static RemainderSQLiteHelper remainderSQLiteHelper = null;
	
	private static SettingsSQLiteHelper settingsSQLiteHelper = null;
	
	private StorageAPIImpl(Context context){
		
		settingsSQLiteHelper = new SettingsSQLiteHelper(context);
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

	@Override
	public boolean deleteAllRecordsOfContactById(List<Long> contactIDs) {
		return contactSQLiteHelper.deleteContactById(contactIDs);
		/*boolean remainderRemovalStatus = remainderSQLiteHelper.deleteRemainderByContactId(contactID);
		
		boolean contactRemovalStatus = false;
		
		if(remainderRemovalStatus){
			contactRemovalStatus = contactSQLiteHelper.deleteContactById(contactID);
		}
		
		return contactRemovalStatus;*/
	}

	@Override
	public List<Settings> getAllSettings() {
		return settingsSQLiteHelper.getAllSettings();
	}

	@Override
	public boolean updateSettings(Settings settings) {
		return settingsSQLiteHelper.updateSettings(settings);
	}
	
	@Override
	public Settings getSettingsBySettingsName(String name) {
		return settingsSQLiteHelper.getSettingsBySettingsName(name);
	}

}
