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


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wickedsoftwaredesigns.libs.FileManagement;
import com.wickedsoftwaredesigns.libs.Network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
@SuppressWarnings("unchecked")
public class MainActivity extends Activity {

	Context _context = this;
	RadioGroup movieOptions;
	TextView resultView;
	String[] optionsList;
	Boolean connected = false;
	HashMap<String, String> _history;
	
	
	public void updateData(JSONArray jsonData){
		
		
		
		
		TextView title = (TextView) findViewById(R.id.data_title);
		TextView rating = (TextView) findViewById(R.id.data_rating);
		TextView runtime = (TextView) findViewById(R.id.data_runtime);
		// Creates local JSON Object from passed data
				JSONObject object = JSON.buildJSON(jsonData);
		
		try {
			title.setText(object.getJSONObject("query").getJSONObject("movie").getString("title"));
			rating.setText(object.getJSONObject("query").getJSONObject("movie").getString("rating"));
			runtime.setText(object.getJSONObject("query").getJSONObject("movie").getString("runtime"));
			((WebView) findViewById(R.id.data_thumbnail)).loadUrl(object.getJSONObject("query").getJSONObject("movie").getString("thumbnail"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		setContentView(R.layout.form);
		_history = getHistory();
		
		//Detect Network Connection
		connected = Network.getConnectionStatus(_context);
		if(connected){
			Log.i("Network Connection", Network.getConnectionType(_context));
			Toast toast = Toast.makeText(_context, "You are connected via " + Network.getConnectionType(_context)+ " network", Toast.LENGTH_SHORT);
			toast.show();
		}else{
			Toast toast = Toast.makeText(_context, "No Connection Found", Toast.LENGTH_SHORT);
			toast.show();
		}
		
		//Building search button functionality
		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText movie = (EditText) findViewById(R.id.searchField);
				String movieTitle = movie.getText().toString();
				//getMovieSearch(movieTitle);
				
				if(movie.getText().toString().length() == 0){
					Toast.makeText(_context, "Please Enter a Movie Title", Toast.LENGTH_LONG).show();
					return;
				}
				
				Handler moviedatahandler = new Handler(){

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						
						
						
						if(msg.arg1 == RESULT_OK){
							
						
							try {
								Toast.makeText(_context, "Loading Movie Info Please Wait", Toast.LENGTH_LONG).show();
								
							} catch (Exception e) {
								
								Log.e("Handler Response", e.getMessage().toString());
								
								e.printStackTrace();
							}
							
							
						}
					}
					
				};
				
				Messenger moviedatamessanger = new Messenger(moviedatahandler);
				
				Intent startMoviedataIntent = new Intent(_context, MovieDataService.class);
				
				startMoviedataIntent.putExtra(MovieDataService.MESSENGER_KEY, moviedatamessanger);
				
				startMoviedataIntent.putExtra(MovieDataService.MOVIE_KEY, movieTitle);
				startService(startMoviedataIntent);
			}
		});
		
		
		
		
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

	
	/**
	 * Gets the history.
	 *
	 * @return the history
	 */
	private HashMap<String, String> getHistory(){
		//sets object to pull the history file from internal storage
		Object stored = FileManagement.readObjectFile(_context, "history", false);
		//calls an instance of the history hashmap
		HashMap<String, String> history;
		if(stored == null){
			//if no file exists create it
			Log.i("HISTORY", "NO HISTORY FILE FOUND");
			history = new HashMap<String, String>();
		}else{
			//if file exists pull it and read it with the read object file method in the file management class
			history = (HashMap<String, String>) stored;
		}
		//return the history object
		return history;
	}
	
	
	
	/**
	 * The Class SearchRequest.
	 */
	private class SearchRequest extends AsyncTask<URL, Void, String>{
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		//pulls the async data in the background
		protected String doInBackground(URL... urls){
			String response = "";
			for (URL url: urls){
				//calls the url string response from the network library
				response = Network.getURLStringResponse(url);
			}
			//returns the response data
			return response;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result){
			Log.i("URL Response", result);
			
			//Pull the Array of Movies out of JSON Data
			try {
				JSONObject object = new JSONObject(result);
				//checks to see if the call from the api returns data if not it sends a toast
				if(object.getString("total").compareTo("0")==0){
					Toast toast = Toast.makeText(_context, "No Movie Found", Toast.LENGTH_SHORT);
					toast.show();
				}else{
				JSONArray movies = object.getJSONArray("movies");
				updateData(movies);
				JSONObject results = JSON.buildJSON(movies);
				//resultView.setText(JSON.readJSON(movies));
				
				//saves the data into a history file on internal memory and a temp file on the external memory
				_history.put(results.getJSONObject("query").getJSONObject("movie").getString("title"), results.toString());
				//FileManagement.storeObjectFile(_context, "history", _history, false);
				FileManagement.storeStringfile(_context, "temp", results.toString(), false);
				
				}
				
			} catch (JSONException e) {
				Log.e("JSON EXCEPTION", "JSON ARRAY ERROR");
				
			}
			
		}
	}
}
