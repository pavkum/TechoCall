package com.techostic.techocall.storage;

import java.util.List;

import com.techostic.techocall.modal.Contact;

public interface ContactAPI {

	public boolean addContact(Contact contact);

	public List<Contact> getAllContacts();

	public Long getContactIDByPhoneNumber(String phoneNumber);
}
