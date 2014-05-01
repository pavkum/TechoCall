package com.techostic.techocall.plugin;

import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.storage.StorageAPI;
import com.techostic.techocall.storage.StorageAPIImpl;

public class StoragePluginOld extends CordovaPlugin {

	private static StorageAPI storageAPIImpl = null;
	
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		Context context = cordova.getActivity().getApplicationContext();
		storageAPIImpl = StorageAPIImpl.getInstance(context);
		super.initialize(cordova, webView);
	}
	
	private boolean addContact( JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		
		final JSONObject jsonContact = args.getJSONObject(0);
		
		final Contact contact = new Contact();
		
		contact.setContactID(jsonContact.getLong("id"));
		contact.setFullName(jsonContact.getString("displayName"));
		
		this.cordova.getThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				boolean result = storageAPIImpl.addContact(contact);
				if(result){
					callbackContext.success(jsonContact);
				}else{
					callbackContext.error("An error occured");
				}
			}
		});
		
		return true;
	}
	
	private boolean getAllContact( JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
				
		
		this.cordova.getThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				List<Contact> contacts = storageAPIImpl.getAllContacts();
				
				if(contacts != null){
					JSONArray jsonArray = new JSONArray(contacts);
					callbackContext.success(jsonArray);
				}else{
					callbackContext.error("An error occured");
				}
			}
		});
		
		return true;
	}
	
	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		System.out.println("here");
		if(action.equalsIgnoreCase("addContact")){
			return addContact(args, callbackContext);
		}else if(action.equalsIgnoreCase("getAllContacts")){
			return getAllContact(args, callbackContext);
		}else{
			return false;
		}
		
	}
	
}
