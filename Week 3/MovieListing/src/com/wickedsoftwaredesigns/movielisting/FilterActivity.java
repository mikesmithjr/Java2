package com.wickedsoftwaredesigns.movielisting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FilterActivity extends Activity {

	Context _context = this;
	EditText filterText;
	Button filterButton;
	Cursor cursor;
	ListView filterList;
	
	public ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter);
		
		
		//finding the movie list listview and inflating the movielist header layout then adding it to the listview
		filterList = (ListView) findViewById(R.id.filterlist);
		View listHeader = getLayoutInflater().inflate(R.layout.movielist_header, null);
		filterList.addHeaderView(listHeader);
		
		
		TextView userSearched = (TextView) findViewById(R.id.userSearched);
		userSearched.setText(getIntent().getExtras().getString("movieName"));
		
		if(getIntent().getExtras().getStringArrayList("saved") !=null){
    		Log.i("onFilterActivity saved string", "String has data");
    		Log.i("Saved String", getIntent().getExtras().getStringArrayList("saved").toString());
    		myList = (ArrayList<HashMap<String, String>>) getIntent().getExtras().getSerializable("saved");
    		//building a simple adapter to process the info into a listview
			SimpleAdapter adapter = new SimpleAdapter(_context, myList, R.layout.movielist_row, 
					new String[] { "title", "rating", "runtime"}, 
					new int[]{R.id.title, R.id.rating, R.id.runtime});
			filterList.setAdapter(adapter);
			
		}
		
		
		
		//setting up filter fields
				filterText = (EditText) findViewById(R.id.filterField);
				filterText.setText(MovieProvider.MovieData.CONTENT_URI.toString());
				
				filterButton = (Button) findViewById(R.id.filterButton);
				filterButton.setOnClickListener(new View.OnClickListener() {
					
					
					@Override
					//building the onclick function for the button
					public void onClick(View v) {
						
						//Hide Keyboard
						 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						 
						 
						 
						 Uri filterUri = Uri.parse(filterText.getText().toString());
						 
						 cursor = getContentResolver().query(filterUri, MovieProvider.MovieData.PROJECTION, null, null, null);
						
						if (cursor.moveToFirst() == true){
							
							//Clearing any data from the list
							myList.clear();
							
							//looping over the data to build the list for the list view
							for (int i = 0; i < cursor.getCount(); i++) {
								//storing the data into a hashmap into key value pairs
								HashMap<String, String> displayMap = new HashMap<String, String>();
								displayMap.put("title", cursor.getString(1));
								displayMap.put("rating", cursor.getString(2));
								displayMap.put("runtime", cursor.getString(3));
								
								cursor.moveToNext();
								
								myList.add(displayMap);
							}
							//building a simple adapter to process the info into a listview
							SimpleAdapter adapter = new SimpleAdapter(_context, myList, R.layout.movielist_row, 
									new String[] { "title", "rating", "runtime"}, 
									new int[]{R.id.title, R.id.rating, R.id.runtime});
							
							filterList.setAdapter(adapter);
						}
						
						Log.i("Button", "button has been pressed");
					}
				});
				
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("onDestroy", "activity destroyed");
		Intent intent = new Intent();
		intent.putExtra("saved", (Serializable)myList);
		setResult(Activity.RESULT_OK, intent);
	}
}
