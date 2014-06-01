package com.techostic.techocall.webinterface;

import org.json.JSONArray;

import com.techostic.techocall.storage.StorageAPI;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;


public class DialerInterface {

	private Context context;
	
	private StorageAPI storageAPIImpl = null;
	
	private String autoRemove;
	
	private int remaindedUsing;
	
	public DialerInterface(Context context , StorageAPI storageAPIImpl , String autoRemove , int remaindedUsing) {
		this.context = context;
		this.storageAPIImpl = storageAPIImpl;
		this.autoRemove = autoRemove;
		this.remaindedUsing = remaindedUsing;
	}
	
	@JavascriptInterface
	public void getUserProfile(String contactID){
		
	}
	
	@JavascriptInterface
	public void markSelectedRead(String remainderIds){
		System.out.println("remainder IDs : " + remainderIds );
		
		JSONArray array = new JSONArray();
		
		
		
		finish();
	}
	
	@JavascriptInterface
	public void finish(){
		((Activity) this.context).finish();
	}
	
}
