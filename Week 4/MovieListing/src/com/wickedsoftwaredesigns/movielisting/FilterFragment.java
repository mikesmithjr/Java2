package com.wickedsoftwaredesigns.movielisting;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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

public class FilterFragment extends Fragment {
	
	Button filterButton;
	EditText filterText;
	Cursor cursor;
	ListView filterList;
	
	
	public ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
	
	private FilterListener listener;
	
	public interface FilterListener{
		public void onFilterSelected(String movieName);
	};
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (FilterListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement FilterListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		 super.onCreateView(inflater, container, savedInstanceState);
	
		 LinearLayout view = (LinearLayout) inflater.inflate(R.layout.filter, container, false);
		 
		//setting up filter fields
		 
		 	filterList = (ListView) view.findViewById(R.id.filterlist);
		 	
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
		 
		 return view;
	}
}
