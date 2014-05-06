package com.techostic.techocall.plugin;

import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.storage.StorageAPI;
import com.techostic.techocall.storage.StorageAPIImpl;

public class DialerPlugin extends CordovaPlugin {

	private static StorageAPI storageAPIImpl = null;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		Context context = cordova.getActivity().getApplicationContext();
		storageAPIImpl = StorageAPIImpl.getInstance(context);
		super.initialize(cordova, webView);
	}

	private boolean getCompleteUserData(JSONArray array, CallbackContext callbackContext) throws JSONException {
		
		Long userID = null;
		
		try{
			userID = array.getLong(0);
			
			Contact contact = storageAPIImpl.getContactById(userID);
			List<Remainder> pendingRemainders = storageAPIImpl.getAllPendingRemaindersByContactID(userID);
			
		}catch(Exception e){
			
		}
		
		return true;
	}
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		// TODO Auto-generated method stub
		return super.execute(action, args, callbackContext);
	}
}
