package com.wickedsoftwaredesigns.movielisting;

import com.wickedsoftwaredesigns.libs.Network;

import android.app.IntentService;
import android.content.Intent;

public class MovieDataService extends IntentService{

	public MovieDataService() {
		super("MovieDataService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String response = "";
		
		//response = Network.getURLStringResponse(url);
	
	}

}
