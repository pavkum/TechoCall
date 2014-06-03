package com.techostic.techocall.webinterface;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techostic.techocall.modal.Remainder;
import com.techostic.techocall.storage.StorageAPI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class DialerInterface {

	private Context context;

	private StorageAPI storageAPIImpl = null;

	private String autoRemove;

	private byte remaindedUsing;

	public DialerInterface(Context context, StorageAPI storageAPIImpl,
			String autoRemove, byte remaindedUsing) {
		this.context = context;
		this.storageAPIImpl = storageAPIImpl;
		this.autoRemove = autoRemove;
		this.remaindedUsing = remaindedUsing;
	}

	@JavascriptInterface
	public void getUserProfile(String contactID) {

	}

	@JavascriptInterface
	public void markSelectedRead(String remainderIds) {

		if (remainderIds != null) {
			try {
				JSONArray array = new JSONArray(remainderIds);
				
				for(int i=0; i<array.length(); i++){
					JSONObject jsonRemainder = array.getJSONObject(i);
					
					Remainder remainder = new Remainder();
					
					remainder.setRemainderID(jsonRemainder.getLong("id"));
					remainder.setRemainderMessage(jsonRemainder.getString("message"));
					
					remainder.setRemainded(true);
					remainder.setRemaindedOn(new Date().getTime());
					remainder.setRemaindedUsing(this.remaindedUsing);
					
					storageAPIImpl.updateRemainder(remainder);
				}
				
			} catch (JSONException e) {
				Log.d("Dialer Interface : markSelectedRead",
						"An error occured while parsing json string of remainderIDs : "
								+ e.getMessage());
			}
		}

		finish();
	}

	@JavascriptInterface
	public void finish() {
		((Activity) this.context).finish();
	}

}
