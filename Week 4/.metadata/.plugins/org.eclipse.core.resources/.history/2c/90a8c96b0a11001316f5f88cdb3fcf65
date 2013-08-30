/*
 * project     MovieListing
 * 
 * package		com.wickedsoftwaredesigns.movielisting
 * 
 * @author     Michael R. Smith Jr
 * 
 * date			Jul 17, 2013
 */
package com.wickedsoftwaredesigns.movielisting;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wickedsoftwaredesigns.libs.FileManagement;
import com.wickedsoftwaredesigns.libs.Network;
import com.wickedsoftwaredesigns.libs.ToastFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;


/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity implements FormFragment.FormListener{

	protected static final int FilterActivityRequestCode = 0;
	public static String JSON_MOVIES = "movies";
	public static String JSON_TITLE = "title";
	public static String JSON_RATING = "mpaa_rating";
	public static String JSON_RUNTIME = "runtime";
	
	
	Context _context = this;
	String jsonMovieDataString;
	ListView movieList;
	Boolean connected = false;
	Button filterActivityButton;
	
	
	public ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
	/**
	 * Update ui.
	 * this is what is called to read the file parse the JSON and build the listview
	 */
	public void updateUI(){
		//reading JSON string from local file
		jsonMovieDataString = FileManagement.readStringFile(_context, "movieListFile", false);
		
		JSONObject job = null;
		JSONArray movies = null;
		
		movieList = (ListView) findViewById(R.id.movielist);
		
		try {
			//creating the JSON Object
			job = new JSONObject(jsonMovieDataString);
			//pulling the movies array out of the object
			movies = job.getJSONArray(JSON_MOVIES);
			//recording the length of the array
			int recordSize = movies.length();
			Log.i("JSONArray Size", "There are "+ String.valueOf(recordSize)+ " records in the file.");
			
					//looping over the array and pulling the data for each movie out
					for (int i = 0; i < recordSize; i++) {
						
						JSONObject movieObject = movies.getJSONObject(i);
						String title = movieObject.getString(JSON_TITLE);
						String rating = movieObject.getString(JSON_RATING);
						String runtime = movieObject.getString(JSON_RUNTIME);
						//storing the data into a hashmap into key value pairs
						HashMap<String, String> displayMap = new HashMap<String, String>();
						displayMap.put("title", title);
						displayMap.put("rating", rating);
						displayMap.put("runtime", runtime);
						
						myList.add(displayMap);
						Log.i("myList Hashmap", myList.toString());
					}
					
					Log.i("mylist", myList.toString());
					//building a simple adapter to process the info into a listview
					SimpleAdapter adapter = new SimpleAdapter(_context, myList, R.layout.movielist_row, 
							new String[] { "title", "rating", "runtime"}, 
							new int[]{R.id.title, R.id.rating, R.id.runtime});
					movieList.setAdapter(adapter);
					
				
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	
	}
	
	//custom save instance state
	protected void onSaveInstanceState(Bundle savedInstanceState){
		super.onSaveInstanceState(savedInstanceState);
		Log.i("onSaveInstanceState", "Save Started");
		//Saving the movie list to the bundle
		if(myList != null && !myList.isEmpty()){
			savedInstanceState.putSerializable("saved", (Serializable)myList);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formfrag);
		
		
		//Detect Network Connection
		connected = Network.getConnectionStatus(_context);
		if(connected){
			Log.i("Network Connection", Network.getConnectionType(_context));
			
			ToastFactory.shortToast(_context, "You are connected via " + Network.getConnectionType(_context)+ " network");
		}else{
			
			ToastFactory.shortToast(_context, "No Connection Found");
		}
		
		
		
	}
	//Restores data after the orientation change
	@SuppressWarnings("unchecked")
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	    Log.i("onRestoreInstanceState", "Restore Started");
	    // Restore state members from saved instance
	 // Restore state members from saved instance
	    if(savedInstanceState != null){
	    	Log.i("onRestore savedInstanceState", "Bundle has data");
	    	//Log.i("savedInstanceState", savedInstanceState.JSONArray("saved"));
	    	if(savedInstanceState.getStringArrayList("saved") !=null){
	    		Log.i("onRestore saved string", "String has data");
	    		Log.i("Saved String", savedInstanceState.getStringArrayList("saved").toString());
	    		myList = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("saved");
	    		//building a simple adapter to process the info into a listview
				SimpleAdapter adapter = new SimpleAdapter(_context, myList, R.layout.movielist_row, 
						new String[] { "title", "rating", "runtime"}, 
						new int[]{R.id.title, R.id.rating, R.id.runtime});
				movieList.setAdapter(adapter);
	    	
	    	    }
	    	
	    }
	}
	//accepts data from FilterActivity upon finish
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		//checks the Result code, request code, and checks if data is not null
		if( resultCode == RESULT_OK && requestCode == 0 && data != null){
			Log.i("onResume saved string", "String has data");
    		Log.i("Saved String", data.getExtras().getStringArrayList("saved").toString());
    		//pulls the filtered list from the data and serializes it to myList
    		myList = (ArrayList<HashMap<String, String>>) data.getExtras().getSerializable("saved");
    		//building a simple adapter to process the info into a listview
			SimpleAdapter adapter = new SimpleAdapter(_context, myList, R.layout.movielist_row, 
					new String[] { "title", "rating", "runtime"}, 
					new int[]{R.id.title, R.id.rating, R.id.runtime});
			movieList.setAdapter(adapter);
		}
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("HandlerLeak")
	@Override
	public void onMovieSearch(String movie) {
		
		//checking to see if the search field is blank then notifiying the user
		if(movie.length() == 0){
			Log.i("Movie Search", "no movie name entered");
			
			ToastFactory.longToast(_context, "Please Enter a Movie Title");
			return;
		}
		//setting up the handler to process the user's search 
		Handler moviedatahandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				
				
				
				if(msg.arg1 == RESULT_OK){
					
				
					
						Log.i("handleMessage", "pulling movie info");
						
						ToastFactory.shortToast(_context, "Loading Movie Info Please Wait");
						updateUI();
						
						
					
					
					
				}
			}
			
		};
		//starting the messenger for the service
		Messenger moviedatamessanger = new Messenger(moviedatahandler);
		//creating the intent
		Intent startMoviedataIntent = new Intent(_context, MovieDataService.class);
		//putting the messenger into the extras
		startMoviedataIntent.putExtra(MovieDataService.MESSENGER_KEY, moviedatamessanger);
		//passing the movie title from the user search box to the moviedataservice
		startMoviedataIntent.putExtra(MovieDataService.MOVIE_KEY, movie);
		//starting the service
		startService(startMoviedataIntent);
		
	}

	@Override
	public void onFilterList(String filterMovieTitle) {
		//creating new intent to launch FilterActivity
		Intent intent = new Intent(_context, FilterActivity.class);
		//putting movie title as extra
		intent.putExtra("movieName", filterMovieTitle);
		//sending mylist contents as data
		intent.putExtra("saved", (Serializable)myList);
		//launching the activity prepairing for results
		startActivityForResult(intent, 0);
		
	}

	
	
	
	
	
	
}
