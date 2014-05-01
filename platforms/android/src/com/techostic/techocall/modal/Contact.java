package com.techostic.techocall.modal;

import java.util.List;

public class Contact {

	private long contactID;

	private String fullName;

	private String photoURL;
	
	private String phoneNumber;

	private List<Remainder> remainders;

	public long getContactID() {
		return contactID;
	}

	public void setContactID(long contactID) {
		this.contactID = contactID;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Remainder> getRemainders() {
		return remainders;
	}

	public void setRemainders(List<Remainder> remainders) {
		this.remainders = remainders;
	}

	@Override
	public String toString() {
		String data = "contactID : fullName : phoneNumber : remainders :: "
				+ this.contactID + ":" + this.fullName + ":" + this.phoneNumber
				+ ":" + this.remainders;
		return data;
	}

}
