package com.wickedsoftwaredesigns.movielisting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wickedsoftwaredesigns.libs.FileManagement;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class MovieProvider extends ContentProvider{

	public static final String AUTHORITY = "com.wickedsoftwaredesigns.movielisting.movieprovider";
	
	public static class MovieData implements BaseColumns{
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movies");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.wickedsoftwaredesigns.movielisting.movie";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.movie/vnd.wickedsoftwaredesigns.movielisting.movie";
		
		//Define Columns
		public static final String TITLE_COLUMN = "title";
		public static final String RATNG_COLUMN = "rating";
		public static final String RUNTIME_COLUMN = "runtime";
		
		public static final String[] PROJECTION = {"_Id", TITLE_COLUMN, RATNG_COLUMN, RUNTIME_COLUMN};
		
		private MovieData() {};
	}
	
	public static final int MOVIES = 1;
	public static final int MOVIES_ID = 2;
	
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static{
		uriMatcher.addURI(AUTHORITY, "movies/", MOVIES);
		uriMatcher.addURI(AUTHORITY, "movies/#", MOVIES_ID);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		switch (uriMatcher.match(uri)){
		case MOVIES:
			return MovieData.CONTENT_TYPE;
		
		case MOVIES_ID:
			return MovieData.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		MatrixCursor result = new MatrixCursor(MovieData.PROJECTION);
		
		String JSONString = FileManagement.readStringFile(getContext(), "movieListFile", false);
		JSONObject job = null;
		JSONArray recordArray = null;
		JSONObject movie = null;
		
		try {
			job = new JSONObject(JSONString);
			recordArray = job.getJSONArray(MainActivity.JSON_MOVIES);
			Log.i("Record Array", recordArray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (recordArray == null)
		{
			return result;
		}
		switch (uriMatcher.match(uri)){
		case MOVIES:
			
			for (int i = 0; i < recordArray.length(); i++) {
				
				try {
					movie = recordArray.getJSONObject(i);
					result.addRow(new Object[] {i + 1, 
							movie.get(MainActivity.JSON_TITLE),
							movie.get(MainActivity.JSON_RATING),
							movie.get(MainActivity.JSON_RUNTIME)});
					}
				 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		case MOVIES_ID:
			
		}
		Log.i("filter Result", result.toString());
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
