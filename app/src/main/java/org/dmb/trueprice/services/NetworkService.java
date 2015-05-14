package org.dmb.trueprice.services;

import android.app.IntentService;
import android.content.Intent;

public class NetworkService extends IntentService {
	
	

	public NetworkService() {
		super("NetworkService");
	}
	
	public NetworkService(String name) {
		super (name);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	
}
