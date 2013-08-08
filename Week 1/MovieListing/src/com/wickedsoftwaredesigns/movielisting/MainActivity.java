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


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wickedsoftwaredesigns.libs.FileManagement;
import com.wickedsoftwaredesigns.libs.Network;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
public class MainActivity extends Activity {

	Context _context = this;
	String jsonMovieDataString;
	ListView movieList;
	Boolean connected = false;
	
	
	
	public void updateUI(){
		
		jsonMovieDataString = FileManagement.readStringFile(_context, "movieListFile", false);
		
		JSONObject job = null;
		JSONArray movies = null;
		ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
		
		try {
			job = new JSONObject(jsonMovieDataString);
			movies = job.getJSONArray("movies");
			int recordSize = movies.length();
			Log.i("JSONArray Size", "There are "+ String.valueOf(recordSize));
			
			
					for (int i = 0; i < recordSize; i++) {
						
						JSONObject movieObject = movies.getJSONObject(i);
						String title = movieObject.getString("title");
						String rating = movieObject.getString("mpaa_rating");
						String runtime = movieObject.getString("runtime");
						
						HashMap<String, String> displayMap = new HashMap<String, String>();
						displayMap.put("title", title);
						displayMap.put("rating", rating);
						displayMap.put("runtime", runtime);
						
						myList.add(displayMap);
					}
					SimpleAdapter adapter = new SimpleAdapter(_context, myList, R.layout.movielist_row, 
							new String[] { "title", "rating", "runtime"}, 
							new int[]{R.id.title, R.id.rating, R.id.runtime});
					movieList.setAdapter(adapter);
				
				
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
		
		movieList = (ListView) findViewById(R.id.movielist);
		View listHeader = getLayoutInflater().inflate(R.layout.movielist_header, null);
		movieList.addHeaderView(listHeader);
		
		//Building search button functionality
		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("HandlerLeak")
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
								updateUI();
								//updateUI(jsonMovieDataString);
								
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

	
	
	
	
	
	
}
