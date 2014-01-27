package com.example.java3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FunMath extends Activity {
	
	public EditText mainInput;
	public TextView mainOutput;
	public Button mainButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fun_math);
		
		mainInput = (EditText) findViewById(R.id.main_input);
		mainOutput = (TextView) findViewById(R.id.text_display);
		mainButton = (Button) findViewById(R.id.main_button);
		
		mainButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String input = mainInput.getText().toString();
				
				if(!input.isEmpty()){ //check to see that input is covered
					
					//if it is, we want to parse input for ints
					String value = add(input);
					mainOutput.setText("Program Output: "+value);
					
				}else{
					mainOutput.setText("No numbers entered");
				}
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fun_math, menu);
		return true;
	}
	
	public String add(String addVals){
		int returnVal;
		String[] tempVals = addVals.split(",");
		if(tempVals.length >= 2){
			//cheap conversion from string to int
			returnVal = Integer.valueOf(tempVals[0]) + Integer.valueOf(tempVals[1]);
			return String.valueOf(returnVal);
		}else{
			//if we didn't get correct input, let's return this to dislpay
			return "Input not Valid";
		}
	}

}
