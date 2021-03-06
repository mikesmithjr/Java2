/*
 * project     MovieListing
 * 
 * package		com.wickedsoftwaredesigns.movielisting
 * 
 * @author     Michael R. Smith Jr
 * 
 * date			Aug 8, 2013
 */
package com.wickedsoftwaredesigns.movielisting;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.wickedsoftwaredesigns.libs.FileManagement;
import com.wickedsoftwaredesigns.libs.Network;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class MovieDataService.
 */
public class MovieDataService extends IntentService{

	
	public static final String MESSENGER_KEY = "messenger";
	public static final String MOVIE_KEY = "movie";
	
	/**
	 * Instantiates a new movie data service.
	 */
	public MovieDataService() {
		super("MovieDataService");
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
		Log.i("onHandleIntent", "started");
		//pulling the extras from the intent
		Bundle extras = intent.getExtras();
		//getting the messenger key
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		//getting the movie title
		String movieName = extras.getString(MOVIE_KEY);
				
		//api key for rotten tomatoes api
		String apiKey = "t2m7kt6ccg644jte4fvfsaf7";
		//base url for rottentomatoes api call
		String baseURL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json";
		//search specific string for api call
		String movieURL = "?apikey=" + apiKey + "&q=" + movieName + "&page_limit=3";

		
		URL finalURL;
			
		try {
			//building url for call
			finalURL = new URL(baseURL + movieURL);
			Log.i("fullURL", finalURL.toString());
			//creating a new search request and calling it with the url
		} catch (MalformedURLException e) {
			Log.e("BAD URL", "malformed URL");
			finalURL = null;
		}
		
		String response = "";
		Log.i("Response string", "sending url to network library");
		response = Network.getURLStringResponse(finalURL);
		
		//Pull the Array of Movies out of JSON Data
		try {
			JSONObject object = new JSONObject(response);
			//checks to see if the call from the api returns data if not it sends a toast
			if(object.getString("total").compareTo("0")==0){
				Log.i("get JSONData", "no movies found");
			}else{
			//saves the json data to string in file
			Log.i("filemanagement", "saving file");
			FileManagement.storeStringfile(getBaseContext(), "movieListFile", response, false);
			
			}
			
		} catch (JSONException e) {
			Log.e("JSON EXCEPTION", "JSON ARRAY ERROR");
			
		}
		//obtains the message and checks the arg1
		Message message = Message.obtain();
		message.arg1 = Activity.RESULT_OK;
		try {
			//sends the ok message
			messenger.send(message);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Log.e("onHandleIntent", e.getMessage().toString());
			e.printStackTrace();
		}
	}

}
