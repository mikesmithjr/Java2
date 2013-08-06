package com.wickedsoftwaredesigns.libs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wickedsoftwaredesigns.movielisting.JSON;

import android.content.Context;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

public class MovieDisplay extends GridLayout{
	
	TextView title;
	TextView runtime;
	TextView rating;
	TextView thumbnail;
	Context _context;
	
	public MovieDisplay(Context context){
		super(context);
		
		_context = context;
		
		this.setColumnCount(2);
		
		TextView titleLabel = new TextView(_context);
		titleLabel.setText("Movie Title: ");
		title = new TextView(_context);
		
		TextView runtimeLabel = new TextView(_context);
		runtimeLabel.setText("Movie Length: ");
		runtime = new TextView(_context);
		
		TextView ratingLabel = new TextView(_context);
		ratingLabel.setText("MPAA Rating: ");
		rating = new TextView(_context);
		
		TextView thumbnailLabel = new TextView(_context);
		thumbnailLabel.setText("Thumbnail Image: ");
		thumbnail = new TextView(_context);
		
		this.addView(titleLabel);
		this.addView(title);
		
		this.addView(runtimeLabel);
		this.addView(runtime);
		
		this.addView(ratingLabel);
		this.addView(rating);
		
		this.addView(thumbnailLabel);
		this.addView(thumbnail);
	}
	
	public void updateData(JSONArray jsonData){
		
		
		
		
		// Creates local JSON Object from passed data
		JSONObject object = JSON.buildJSON(jsonData);
		//Log.i("Read Json Data", jsonData.toString());
		
		
		// Pulls and parses the data into a string
		try {
			title.setText(object.getJSONObject("query").getJSONObject("movie").getString("title"));
			rating.setText(object.getJSONObject("query").getJSONObject("movie").getString("rating"));
			runtime.setText(object.getJSONObject("query").getJSONObject("movie").getString("runtime"));
			thumbnail.setText(object.getJSONObject("query").getJSONObject("movie").getString("thumbnail"));
			

		} catch (JSONException e) {
			
			
			Log.e("error tag", "error", e);
			
		}
		
	}
}
