package com.techostic.techocall;

import java.util.Date;
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
	
	private Intent dialerIntent = null;
	
	private List<Remainder> remainderList = null;
	
	private byte remaindedUsing = -1; // 0 incoming, 1 outgoing
	
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
                System.out.println("state : " + state);
                switch (state) {
    			case TelephonyManager.CALL_STATE_OFFHOOK:
    				remaindedUsing = 1;
    				showDialer(context , incomingNumber);
    				break;

    			case TelephonyManager.CALL_STATE_RINGING:
    				remaindedUsing = 0;
    				showDialer(context , incomingNumber);
    				break;
    				
    			case TelephonyManager.CALL_STATE_IDLE :
    				System.out.println("idle");
    				hideDialer(context);
    			}
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	private void hideDialer(Context context) {
		System.out.println("dialer intent : " + dialerIntent);
		if(this.dialerIntent != null){
			this.dialerIntent.putExtra("finish", "finish");
			
			this.dialerIntent = null;
			
			for(int i=0; i<this.remainderList.size(); i++){
				Remainder remainder = remainderList.get(i);
				
				remainder.setRemainded(true);
				remainder.setRemaindedOn(new Date().getTime());
				remainder.setRemaindedUsing(this.remaindedUsing);
				
				storageAPIImpl.updateRemainder(remainder);
				
			}
			
			this.remainderList = null;
			this.remaindedUsing = -1;
			
			
		}
	}
	
	private void showDialer (Context context , String incomingNumber){
		
		Long contactID = storageAPIImpl.getContactIDByPhoneNumber(incomingNumber);
		// check only for ID - performance as we expect 99% calls wouldn't be having any remainders
		
		
		if(contactID != null && contactID != -1){
			
	        
			Contact contact = storageAPIImpl.getContactById(contactID);
			
			remainderList = storageAPIImpl.getAllPendingRemaindersByContactID(contact.getContactID());
			
			if(remainderList == null || (remainderList != null && remainderList.size() == 0)){ // no pending remainders
				return;
			}
			
			contact.setRemainders(remainderList);
			
			JSONObject jsonObject = new JSONObject();
			
			try {
				jsonObject.put("contactID", contact.getContactID());
				jsonObject.put("name", contact.getFullName());
				
				JSONArray jsonMessageArray = new JSONArray();
				
				for(int i=0; i<remainderList.size(); i++){
					jsonMessageArray.put(remainderList.get(i).getRemainderMessage());
				}
				
				jsonObject.put("message", jsonMessageArray);
				
			} catch (JSONException e) {
				Log.d("Dialer Activity", "An error occured while displaying remainder for contact ID : "+contactID + " : " + e.getMessage());
				return;
			}
			
			dialerIntent = new Intent(context, DialerActivity.class); 
			
			dialerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			dialerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			dialerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			dialerIntent.putExtra("contactID", contactID);
			dialerIntent.putExtra("json", jsonObject.toString());
			
	        context.startActivity(dialerIntent);
		}
		
        
	}

}
