package com.techostic.techocall.modal;

public class Remainder {

	private long remainderID;
	
	private long contactID;
	
	private String remainderMessage;
	
	// only call or only message or both - 0 : all, 1 - only call, 2 - only message
	private byte remainderType;
	
	private boolean isRemainded;
	
	private long remaindedOn;
	
	// 0 - incoming call, 1- outgoing call, 2 - incoming msg, 3 - outgoing message
	private byte remaindedUsing;

	public long getRemainderID() {
		return remainderID;
	}

	public void setRemainderID(long remainderID) {
		this.remainderID = remainderID;
	}

	public long getContactID() {
		return contactID;
	}

	public void setContactID(long contactID) {
		this.contactID = contactID;
	}

	public String getRemainderMessage() {
		return remainderMessage;
	}

	public void setRemainderMessage(String remainderMessage) {
		this.remainderMessage = remainderMessage;
	}

	public byte getRemainderType() {
		return remainderType;
	}

	public void setRemainderType(byte remainderType) {
		this.remainderType = remainderType;
	}

	public boolean isRemainded() {
		return isRemainded;
	}

	public void setRemainded(boolean isRemainded) {
		this.isRemainded = isRemainded;
	}

	public long getRemaindedOn() {
		return remaindedOn;
	}

	public void setRemaindedOn(long remaindedOn) {
		this.remaindedOn = remaindedOn;
	}

	public byte getRemaindedUsing() {
		return remaindedUsing;
	}

	public void setRemaindedUsing(byte remaindedUsing) {
		this.remaindedUsing = remaindedUsing;
	}
	
	@Override
	public String toString() {
		String data = "remainderID : contactID : remainderMessage : remainderType : isRemainded : remaindedOn : remaindedUsing :: "+remainderID
				     + " : " + contactID + " : " + remainderMessage + " : " + remainderType + " : " + isRemainded + " : " + remaindedOn
				     + " : " + remaindedUsing;
		return data;
	}
	
}
