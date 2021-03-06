/*
 * project     MovieListing
 * 
 * package		com.wickedsoftwaredesigns.movielisting
 * 
 * @author     Michael R. Smith Jr
 * 
 * date			Aug 22, 2013
 */
package com.wickedsoftwaredesigns.movielisting;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class FilterActivity.
 */
public class FilterActivity extends Activity implements FilterFragment.FilterListener{

	Context _context = this;
	EditText filterText;
	Button filterButton;
	Cursor cursor;
	ListView filterList;
	Intent data;
	
	public ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String,String>>();
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filterfrag);
		
				
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 * sends the filtered list data back to the MainActivity upon completion of the filter activity
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
		Log.i("onFinish", "activity finished");
		
		
		
		//sends the activity result code as ok and sends the intent
		setResult(Activity.RESULT_OK, data);
		super.finish();
	}

	
	@Override
	public void onFilterSelected(Intent intent) {
		data = intent;
		finish();
		
	}
}
