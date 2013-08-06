/*
 * project     MovieListing
 * 
 * package		com.wickedsoftwaredesigns.libs
 * 
 * @author     Michael R. Smith Jr
 * 
 * date			Jul 25, 2013
 */
package com.wickedsoftwaredesigns.libs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class FileManagement.
 */
public class FileManagement {

	/**
	 * Store stringfile.
	 *
	 * @param context the context
	 * @param filename the filename
	 * @param content the content
	 * @param external the external
	 * @return the boolean
	 */
	@SuppressWarnings("resource")
	public static Boolean storeStringfile(Context context, String filename, String content, Boolean external){
		
		try {
			//creates a new file object and file output stream
			File file;
			FileOutputStream fos;
			//checks to see if the external flag is true
			if(external){
				//if is external grabs the external file directory and creates it for this app
				//and builds the file
				file = new File(context.getExternalFilesDir(null), filename);
				//puts the file into the output stream
				fos = new FileOutputStream(file);
			}else{
				//if internal this builds the output stream with the file and places it into the private internal location
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			//writes the file from the output stream
			fos.write(content.getBytes());
			//closes the output stream
			fos.close();
		} catch (IOException e) {
			Log.e("WRITE ERROR", filename);
		}
		
		return true;
	}
	
	/**
	 * Store object file.
	 *
	 * @param context the context
	 * @param filename the filename
	 * @param content the content
	 * @param external the external
	 * @return the boolean
	 */
	@SuppressWarnings("resource")
	public static Boolean storeObjectFile(Context context, String filename, Object content, Boolean external){
		
		try {
			//creates a new file object and file output stream and object output stream
			File file;
			FileOutputStream fos;
			ObjectOutputStream oos;
			//checks to see if the external flag is true
			if(external){
				//if is external grabs the external file directory and creates it for this app
				//and builds the file
				file = new File(context.getExternalFilesDir(null), filename);
				//puts the file into the output stream
				fos = new FileOutputStream(file);
			}else{
				//if internal this builds the output stream with the file and places it into the private internal location
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			//adds the file output stream to the object output stream
			oos = new ObjectOutputStream(fos);
			//writes the object
			oos.writeObject(content);
			//closes the object stream and file stream
			oos.close();
			fos.close();
		} catch (IOException e) {
			Log.e("WRITE ERROR", filename);
		}
		
		return true;
	}
	
	
	/**
	 * Read string file.
	 *
	 * @param context the context
	 * @param filename the filename
	 * @param external the external
	 * @return the string
	 */
	@SuppressWarnings("resource")
	public static String readStringFile(Context context, String filename, Boolean external){
		
		String content = "";
		try{
			//creating the file and file input stream
			File file;
			FileInputStream fin;
			//checking if the file location is set to external
			if(external){
				//pulling the  file from the external location and saving it into the input stream
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
				
			}else{
				//pulling the file from internal memory and adding it to the input stream
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			//creating the buffer for the incoming data from the file input stream
			BufferedInputStream bin = new BufferedInputStream(fin);
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			//waiting for the buffer to finish loading the data and then appending to the string buffer
			while((bytesRead = bin.read(contentBytes)) != -1){
				content = new String(contentBytes,0,bytesRead);
				contentBuffer.append(content);
			}
			//converting the buffer to a string
			content = contentBuffer.toString();
			//closing the input buffer
			fin.close();
		}catch (FileNotFoundException e){
			Log.e("READ ERROR", "FILE NOT FOUND " + filename);
		}catch (IOException e) {
			Log.e("READ ERROR", "I/O ERROR");
		}
		//returning the content
		return content;
	}
	
/**
 * Read object file.
 *
 * @param context the context
 * @param filename the filename
 * @param external the external
 * @return the object
 */
@SuppressWarnings("resource")
public static Object readObjectFile(Context context, String filename, Boolean external){
		//creating the input object
		Object content = new Object();
		try{
			//creating the file and file input stream
			File file;
			FileInputStream fin;
			//checking if the file location is set to external
			if(external){
				//pulling the  file from the external location and saving it into the input stream
				file = new File(context.getExternalFilesDir(null), filename);
				fin = new FileInputStream(file);
				
			}else{
				//pulling the file from internal memory and adding it to the input stream
				file = new File(filename);
				fin = context.openFileInput(filename);
			}
			//creating the object input stream from the file input stream
			ObjectInputStream ois = new ObjectInputStream(fin);
			try{
				//adding the input stream data to the content object
				content = (Object) ois.readObject();
			}catch(ClassNotFoundException e){
				Log.e("READ ERROR", "INVALID JAVA OBJECT FILE");
			}
			//closing the object input stream and file input stream
			ois.close();
			fin.close();
		}catch (FileNotFoundException e){
			Log.e("READ ERROR", "FILE NOT FOUND " + filename);
			return null;
		}catch (IOException e) {
			Log.e("READ ERROR", "I/O ERROR");
		}
		return content;
	}
}
