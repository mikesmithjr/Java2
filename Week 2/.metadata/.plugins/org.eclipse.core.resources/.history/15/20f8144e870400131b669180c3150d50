package com.example.multirotorcalculator;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	String[] optionsList;
	int rotors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	// Creating Array Adapter for spinner view
        Resources res = getResources();
        optionsList = res.getStringArray(R.array.rotor_number);
        
        Spinner typeSpinner = (Spinner) findViewById(R.id.spinner);
        
    	typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

    		@Override
    		public void onItemSelected(AdapterView<?> parent, View view,
    		int position, long id) {
    		// Saving the text from the spinner list to a string
    		String spinnerText = optionsList[position].toString();
    		rotors = Integer.parseInt(spinnerText);
    		}

    		@Override
    		public void onNothingSelected(AdapterView<?> arg0) {

    		}


    		});
        
        Button calculateButton = (Button) findViewById(R.id.button1);
        calculateButton.setOnClickListener(new OnClickListener() {
			
			private String INPUT_METHOD_SERVICE;

			@Override
			public void onClick(View v) {
				EditText weightField = (EditText) findViewById(R.id.editText1);
				TextView ozweightfieldLabel = (TextView) findViewById(R.id.ozweightlabel);
				TextView ozweightfield = (TextView) findViewById(R.id.ozweight);
				float weight = Float.valueOf(weightField.getText().toString());
				float weightOz = weight*16;
				ozweightfieldLabel.setText("Copter Weight in Ounces.");
				ozweightfield.setText(String.valueOf(weightOz));
				ozweightfield.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
				
				double dutyCycle = weightOz*1.6;
				double thrustPerMtr = dutyCycle/rotors;
				
				double thrustPerMtrAdj = roundTwoDecimals(thrustPerMtr);
				
				TextView thrustAmnt = (TextView) findViewById(R.id.finalThrust);
				thrustAmnt.setText(String.valueOf(thrustPerMtrAdj));
				thrustAmnt.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
    }

    double roundTwoDecimals(double d) {
    	  DecimalFormat twoDForm = new DecimalFormat("#.##");
    	  return Double.valueOf(twoDForm.format(d));
    	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
