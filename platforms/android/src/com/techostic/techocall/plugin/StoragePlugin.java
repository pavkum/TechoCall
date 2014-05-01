package com.techostic.techocall.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.storage.StorageAPI;
import com.techostic.techocall.storage.StorageAPIImpl;

public class StoragePlugin extends CordovaPlugin {

	private static StorageAPI storageAPIImpl = null;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		Context context = cordova.getActivity().getApplicationContext();
		storageAPIImpl = StorageAPIImpl.getInstance(context);
		super.initialize(cordova, webView);
	}

	private boolean addContact(JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		final JSONObject jsonContact = args.getJSONObject(0);

		final Contact contact = new Contact();

		contact.setContactID(jsonContact.getLong("id"));
		contact.setFullName(jsonContact.getString("displayName"));
		contact.setPhotoURL(jsonContact.getString("photo"));
		contact.setPhoneNumber(jsonContact.getString("phoneNumber"));
		
		this.cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				boolean result = storageAPIImpl.addContact(contact);
				if (result) {
					callbackContext.success(jsonContact);
				} else {
					callbackContext.error("An error occured");
				}
			}
		});

		return true;
	}

	private boolean getAllContact(JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		this.cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				List<Contact> contacts = storageAPIImpl.getAllContacts();
				
				if (contacts != null) {

					JSONArray jsonArray = new JSONArray();

					for (int i = 0; i < contacts.size(); i++) {
						JSONObject jsonObject = new JSONObject();

						try {
							jsonObject
									.put("id", contacts.get(i).getContactID());
							jsonObject.put("displayName", contacts.get(i)
									.getFullName());
							jsonObject.put("photo", contacts.get(i).getPhotoURL());
							jsonObject.put("phoneNumber", contacts.get(i).getPhoneNumber());
						} catch (JSONException e) {
							Log.d("getAllContact ",contacts + " :: " + e.getMessage() );
							callbackContext.error("Please try Later");
						}

						jsonArray.put(jsonObject);
					}
					
					JSONObject responseObject = new JSONObject();
					try {
						responseObject.put("contacts", jsonArray);
					} catch (JSONException e) {
						Log.d("getAllContact ",contacts + " :: " + e.getMessage() );
						callbackContext.error("Please try Later");
					}
					
					callbackContext.success(responseObject);
				}else{
					callbackContext.error("Please try Later");
				}
			}
		});

		return true;
	}
	
	private boolean addRemainder(JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		
		final JSONObject jsonRemainder = args.getJSONObject(0);

		final Remainder remainder = new Remainder();
		
		remainder.setRemainderID(jsonRemainder.getLong("remainderId"));
		remainder.setContactID(jsonRemainder.getLong("contactId"));
		remainder.setRemainderMessage(jsonRemainder.getString("remainderMessage"));
		remainder.setRemainderType(Byte.parseByte(jsonRemainder.getString("remainderType")));
		remainder.setRemainded(false);
		remainder.setRemaindedOn(0);
		remainder.setRemaindedUsing(new Byte("0"));

		
		this.cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				boolean result = storageAPIImpl.addRemainder(remainder);
				
				if (result) {
					callbackContext.success(jsonRemainder);
				} else {
					callbackContext.error("An error occured");
				}
			}
		});

		return true;
	}

	private boolean updateRemainder(JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		
		final JSONObject jsonRemainder = args.getJSONObject(0);
		
		final Remainder remainder = new Remainder();
		
		remainder.setRemainderID(jsonRemainder.getLong("remainderId"));
		//remainder.setContactID(jsonRemainder.getLong("contactId"));
		remainder.setRemainderMessage(jsonRemainder.getString("remainderMessage"));
		//remainder.setRemainderType(Byte.parseByte(jsonRemainder.getString("remainderType")));
		remainder.setRemainded(false);
		remainder.setRemaindedOn(0);
		remainder.setRemaindedUsing(new Byte("0"));
		
		this.cordova.getThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				boolean result = storageAPIImpl.updateRemainder(remainder);
				if (result) {
					callbackContext.success(jsonRemainder);
				} else {
					callbackContext.error("An error occured");
				}
			}
		});

		return true;
	}
	
	private boolean getAllRemaindersByID(final JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		
		this.cordova.getThreadPool().execute(new Runnable() {
			
			@Override
			public void run() {
				List<Remainder> remainderList = null;
				try {
					
					remainderList = storageAPIImpl.getAllRemaindersByID(Long.parseLong(args.getString(0)));
					
					if(remainderList == null){
						callbackContext.error("An error occured");
						return;
					}
					
					JSONArray jsonRemainderArray = new JSONArray();
					
					for(int i=0; i<remainderList.size(); i++){
						
						Remainder remainder = remainderList.get(i);
						JSONObject jsonRemainder = new JSONObject();
						
						jsonRemainder.put("remainderId", remainder.getRemainderID());
						jsonRemainder.put("contactID" , remainder.getContactID());
						jsonRemainder.put("remainderMessage" , remainder.getRemainderMessage());
						jsonRemainder.put("remainderType", remainder.getRemainderType());
						jsonRemainder.put("isRemainded", remainder.isRemainded());
						jsonRemainder.put("remaindedOn", remainder.getRemaindedOn());
						jsonRemainder.put("remaindedUsing" , remainder.getRemaindedUsing());
						
						jsonRemainderArray.put(jsonRemainder);
					}
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("userRemainders", jsonRemainderArray);
					
					callbackContext.success(jsonObject);
					
				} catch (JSONException e) {
					Log.d("getAllRemaindersByID", remainderList + " : "+e.getMessage());
					callbackContext.error("An error occured");
				}
				
			}
		});
		
		return true;
	}
	
	public boolean deleteRemainder (final JSONArray args,
			final CallbackContext callbackContext) throws JSONException {

		
		
		this.cordova.getThreadPool().execute( new Runnable() {
			
			@Override
			public void run() {
				try {
		
					JSONObject array = args.getJSONObject(0);
					List<Long> remainderIDs = new ArrayList<Long>();
					
					for(int i=0; i<array.length(); i++){
						remainderIDs.add(array.getLong("remainderId"));
					}
					
					boolean result = storageAPIImpl.deleteRemainder(remainderIDs);
					
					if(result){
						callbackContext.success(array);
					}else{
						callbackContext.error("An error occured");
					}
					
				} catch (JSONException e) {
					Log.d("deleteRemainder", "Error while deleting remainders : " + args + " : "+ e.getMessage());
					callbackContext.error("An error occured");
				}
			}
		});
		
		return true;
	}

	@Override
	public boolean execute(String action, JSONArray args,
			final CallbackContext callbackContext) throws JSONException {
		
		if (action.equalsIgnoreCase("addContact")) {
			return addContact(args, callbackContext);
		} else if (action.equalsIgnoreCase("getAllContacts")) {
			return getAllContact(args, callbackContext);
		}else if(action.equalsIgnoreCase("getAllRemaindersByID")){
			return getAllRemaindersByID(args, callbackContext);
		}else if(action.equalsIgnoreCase("addRemainder")){
			return addRemainder(args, callbackContext);
		}else if(action.equalsIgnoreCase("deleteRemainder")){
			return deleteRemainder(args, callbackContext);
		}else if(action.equalsIgnoreCase("updateRemainder")){
			return updateRemainder(args, callbackContext);
		} else {
			return false;
		}

	}

}
