package com.techostic.techocall.storage;

import java.util.List;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Remainder;

public interface StorageAPI {
	
	public boolean addContact(Contact contact);

	public List<Contact> getAllContacts();
	
	public Long getContactIDByPhoneNumber(String phoneNumber);
	
	public Contact getContactById(long contactID);
	
	public List<Remainder> getAllRemaindersByContactID(long contactID);
	
	public List<Remainder> getAllPendingRemaindersByContactID(long contactID);
	
	public boolean addRemainder(Remainder remainder);
	
	public Remainder getRemainderByID(long remainderID);
	
	public boolean deleteRemainder(List<Long> remainderIDs);
	
	public boolean updateRemainder(Remainder remainder);
}
