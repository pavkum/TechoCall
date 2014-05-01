package com.techostic.techocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateChangeActivity extends BroadcastReceiver{

	@Override
	public void onReceive(final Context context, Intent intent) {
		TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                System.out.println("incomingNumber : "+incomingNumber);
                Intent i = new Intent(context, DialerActivity.class); 
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                context.startActivity(i);
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
	}

}
