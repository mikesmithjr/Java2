/*
 * project     MovieListing
 * 
 * package		com.wickedsoftwaredesigns.movielisting
 * 
 * @author     Michael R. Smith Jr
 * 
 * date			Aug 15, 2013
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class MovieProvider.
 */
public class MovieProvider extends ContentProvider{

	public static final String AUTHORITY = "com.wickedsoftwaredesigns.movielisting.movieprovider";
	
	/**
	 * The Class MovieData.
	 */
	public static class MovieData implements BaseColumns{
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movies");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.wickedsoftwaredesigns.movielisting.movie";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.movie/vnd.wickedsoftwaredesigns.movielisting.movie";
		
		//Define Columns
		public static final String TITLE_COLUMN = "title";
		public static final String RATNG_COLUMN = "rating";
		public static final String RUNTIME_COLUMN = "runtime";
		
		public static final String[] PROJECTION = {"_Id", TITLE_COLUMN, RATNG_COLUMN, RUNTIME_COLUMN};
		
		/**
		 * Instantiates a new movie data.
		 */
		private MovieData() {};
	}
	
	public static final int MOVIES = 1;
	public static final int MOVIES_ID = 2;
	public static final int MOVIES_TITLE = 3;
	public static final int MOVIES_RATING = 4;
	public static final int MOVIES_RUNTIME = 5;
	
	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static{
		uriMatcher.addURI(AUTHORITY, "movies/", MOVIES);
		uriMatcher.addURI(AUTHORITY, "movies/#", MOVIES_ID);
		uriMatcher.addURI(AUTHORITY, "movies/title/*", MOVIES_TITLE);
		uriMatcher.addURI(AUTHORITY, "movies/rating/*", MOVIES_RATING);
		uriMatcher.addURI(AUTHORITY, "movies/runtime/*", MOVIES_RUNTIME);
	}
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
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

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
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
			Log.i("URI Matcher", "matched MOVIES");
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
			break;
			
		case MOVIES_ID:
			Log.i("URI Matcher", "matched MOVIES_ID");
			String movieID = uri.getLastPathSegment();
			Log.i("queryId", movieID);
			
			int index;
			
			try {
				index = Integer.parseInt(movieID);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("query", "index format error");
				break;
			}
			
			if (index <= 0 || index > recordArray.length())
			{
				Log.e("query", "index out of range for " + uri.toString());
				break;
			}
			
			try {
				movie = recordArray.getJSONObject(index-1);
				result.addRow(new Object[] {index, 
						movie.get(MainActivity.JSON_TITLE),
						movie.get(MainActivity.JSON_RATING),
						movie.get(MainActivity.JSON_RUNTIME)});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
			
		case MOVIES_TITLE:
			Log.i("URI Matcher", "matched MOVIES_TITLE");
			String movieTitle = uri.getLastPathSegment();
			Log.i("queryId", movieTitle);
			for (int i = 0; i < recordArray.length(); i++) {
				
				try {
					movie = recordArray.getJSONObject(i);
					if (movie.getString(MainActivity.JSON_TITLE).contentEquals(movieTitle)) {
						result.addRow(new Object[] {i + 1, 
								movie.get(MainActivity.JSON_TITLE),
								movie.get(MainActivity.JSON_RATING),
								movie.get(MainActivity.JSON_RUNTIME)});
					}
					
					}
				 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		case MOVIES_RATING:
			Log.i("URI Matcher", "matched MOVIES_RATING");
			String movieRating = uri.getLastPathSegment();
			Log.i("queryId", movieRating);
			for (int i = 0; i < recordArray.length(); i++) {
				
				try {
					movie = recordArray.getJSONObject(i);
					if (movie.getString(MainActivity.JSON_RATING).contentEquals(movieRating)) {
						result.addRow(new Object[] {i + 1, 
								movie.get(MainActivity.JSON_TITLE),
								movie.get(MainActivity.JSON_RATING),
								movie.get(MainActivity.JSON_RUNTIME)});
					}
					
					}
				 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			
		case MOVIES_RUNTIME:
			Log.i("URI Matcher", "matched MOVIES_RUNTIME");
			String movieRuntime = uri.getLastPathSegment();
			Log.i("queryId", movieRuntime);
			for (int i = 0; i < recordArray.length(); i++) {
				
				try {
					movie = recordArray.getJSONObject(i);
					if (movie.getString(MainActivity.JSON_RUNTIME).contentEquals(movieRuntime)) {
						result.addRow(new Object[] {i + 1, 
								movie.get(MainActivity.JSON_TITLE),
								movie.get(MainActivity.JSON_RATING),
								movie.get(MainActivity.JSON_RUNTIME)});
					}
					
					}
				 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
			default:
				Log.e("query", "Invalid URI" + uri.toString());
		}
		Log.i("filter Result", result.toString());
		return result;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
