package com.techostic.techocall.storage;

import java.util.List;

import com.techostic.techocall.modal.Remainder;

public interface RemainderAPI {

	public List<Remainder> getAllRemaindersByID(long userID);
	
	public boolean addRemainder(Remainder remainder);
	
	public boolean updateRemainder(Remainder remainder);
	
	public boolean deleteRemainder(List<Long> remainderIDs);
	
	public List<Remainder> getAllPendingRemaindersByContactID(long contactID);
} 
