package com.techostic.techocall.webinterface;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;


public class DialerInterface {

	Context context;
	
	
	public DialerInterface(Context context) {
		this.context = context;
	}
	
	@JavascriptInterface
	public void getUserProfile(String contactID){
		
	}
	
	public void finish(){
		System.out.println("context finish");
		((Activity) this.context).finish();
	}
	
}
