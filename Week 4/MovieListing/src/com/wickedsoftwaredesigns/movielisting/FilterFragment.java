/*
 * project     MovieListing
 * 
 * package		com.wickedsoftwaredesigns.movielisting
 * 
 * @author     Michael R. Smith Jr
 * 
 * date			Aug 29, 2013
 */
package com.wickedsoftwaredesigns.movielisting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class FilterFragment.
 */
public class FilterFragment extends Fragment {
	
	Button filterButton;
	Button backToMain;
	EditText filterText;
	Cursor cursor;
	ListView filterList;
	
	
	public ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
	
	private FilterListener listener;
	
	/**
	 * The listener interface for receiving filter events.
	 * The class that is interested in processing a filter
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addFilterListener<code> method. When
	 * the filter event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see FilterEvent
	 */
	public interface FilterListener{
		
		/**
		 * On filter selected.
		 *
		 * @param intent the intent
		 */
		public void onFilterSelected(Intent intent);
	};
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (FilterListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement FilterListener");
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		 super.onCreateView(inflater, container, savedInstanceState);
	
		 LinearLayout view = (LinearLayout) inflater.inflate(R.layout.filter, container, false);
		 
		//finding the movie list listview and inflating the movielist header layout then adding it to the listview
			filterList = (ListView) view.findViewById(R.id.filterlist);
			View listHeader = getActivity().getLayoutInflater().inflate(R.layout.movielist_header, null);
			filterList.addHeaderView(listHeader);
		 
		
		 
		 	
		 	//pulls the movie title from the extras and adds it to the userSearched textview
			TextView userSearched = (TextView) view.findViewById(R.id.userSearched);
			Intent intent = getActivity().getIntent();
			userSearched.setText(intent.getExtras().getString("movieName"));
			//checks to make sure the data in the saved key is not null
			if(intent.getExtras().getStringArrayList("saved") !=null){
	    		Log.i("onFilterActivity saved string", "String has data");
	    		Log.i("Saved String", intent.getExtras().getStringArrayList("saved").toString());
	    		//pulls the data for the listview from the extras and serializes it into the listview
	    		myList = (ArrayList<HashMap<String, String>>) intent.getExtras().getSerializable("saved");
	    		//building a simple adapter to process the info into a listview
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), myList, R.layout.movielist_row, 
						new String[] { "title", "rating", "runtime"}, 
						new int[]{R.id.title, R.id.rating, R.id.runtime});
				filterList.setAdapter(adapter);
				
			}
			//setting up filter fields
			filterText = (EditText) view.findViewById(R.id.filterField);
			filterText.setText(MovieProvider.MovieData.CONTENT_URI.toString());
			
			filterButton = (Button) view.findViewById(R.id.filterButton);
			filterButton.setOnClickListener(new View.OnClickListener() {
				
				
				@Override
				//building the onclick function for the button
				public void onClick(View v) {
					
					//Hide Keyboard
					 InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					 
					 
					 
					 Uri filterUri = Uri.parse(filterText.getText().toString());
					 
					 cursor = getActivity().getContentResolver().query(filterUri, MovieProvider.MovieData.PROJECTION, null, null, null);
					
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
						SimpleAdapter adapter = new SimpleAdapter(getActivity(), myList, R.layout.movielist_row, 
								new String[] { "title", "rating", "runtime"}, 
								new int[]{R.id.title, R.id.rating, R.id.runtime});
						
						filterList.setAdapter(adapter);
					}
					
					Log.i("Button", "button has been pressed");
				}
			});
		 
			backToMain = (Button) view.findViewById(R.id.backtomain);
			backToMain.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent data = new Intent();
					//puts the contents of the listview into a serilaizable key in the extras of the intent
					data.putExtra("saved", (Serializable)myList);
					
					listener.onFilterSelected(data);
					
				}
			});
		 return view;
	}
}
