package com.techostic.techocall;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.techostic.techocall.modal.Contact;
import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.storage.StorageAPI;
import com.techostic.techocall.storage.StorageAPIImpl;

public class PhoneStateChangeActivity extends BroadcastReceiver{

	private static StorageAPI storageAPIImpl = null;
	
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		if(storageAPIImpl == null){
			storageAPIImpl = StorageAPIImpl.getInstance(context);
		}
		
		TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                
                switch (state) {
    			case TelephonyManager.CALL_STATE_OFFHOOK:
    				showDialer(context , incomingNumber);
    				break;

    			case TelephonyManager.CALL_STATE_RINGING:
    				showDialer(context , incomingNumber);
    				break;
    			}
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private void showDialer (Context context , String incomingNumber){
		System.out.println("incomingNumber : "+incomingNumber);
		
		Long contactID = storageAPIImpl.getContactIDByPhoneNumber(incomingNumber);
		// check only for ID - performance as we expect 99% calls wouldn't be having any remainders
		
		
		if(contactID != null && contactID != -1){
			
	        
			Contact contact = storageAPIImpl.getContactById(contactID);
			
			List<Remainder> remainderList = storageAPIImpl.getAllPendingRemaindersByContactID(contact.getContactID());
			
			contact.setRemainders(remainderList);
			
			JSONObject jsonObject = new JSONObject();
			
			try {
				jsonObject.put("contactID", contact.getContactID());
				jsonObject.put("name", contact.getFullName());
				
				JSONArray jsonArray = new JSONArray();
				
				for(int i=0; i<remainderList.size(); i++){
					jsonArray.put(remainderList.get(i).getRemainderMessage());
				}
				
				jsonObject.put("message", jsonArray);
				
			} catch (JSONException e) {
				Log.d("Dialer Activity", "An error occured while displaying remainder for contact ID : "+contactID + " : " + e.getMessage());
				return;
			}
			
			Intent intent = new Intent(context, DialerActivity.class); 
			
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("contactID", contactID);
			intent.putExtra("json", jsonObject.toString());
	        context.startActivity(intent);
		}
		
        
	}

}
