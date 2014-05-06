package com.techostic.techocall.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.techostic.techocall.DialerActivity;
import com.techostic.techocall.storage.StorageAPI;
import com.techostic.techocall.storage.StorageAPIImpl;

public class PhoneCallMonitorService extends Service {

	public TelephonyManager telephonyManager = null;
	
	private StorageAPI storageAPIImpl = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		super.onCreate();

		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
		Log.v(TELEPHONY_SERVICE, "registering");
		
		storageAPIImpl = StorageAPIImpl.getInstance(getApplicationContext());
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new TechoPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	public class TechoPhoneStateListener extends PhoneStateListener {
		
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch (state) {
			case TelephonyManager.CALL_STATE_OFFHOOK:
				showDialer(incomingNumber);
				break;

			case TelephonyManager.CALL_STATE_RINGING:
				showDialer(incomingNumber);
				break;
			}
		}
		
	}
	
	private void showDialer(String incomingNumber){
		
		Long contactID = storageAPIImpl.getContactIDByPhoneNumber(incomingNumber);
		
		
		if(contactID != null && contactID != -1){
			Intent dialogIntent = new Intent(getApplicationContext(), DialerActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);
		}
		
		
		
		/*Intent dialogIntent = new Intent(getBaseContext(), DialerActivity.class);
		
		getApplication().startActivity(dialogIntent);*/
	}

}
