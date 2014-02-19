package com.example.android3;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.Android3.MESSAGE";
	
	public int num1 = 0;
	public int num2 = 0;
	public int sum  = 0;
	
	public void sendMessage(View view) {
	    Intent intent = new Intent(this, DisplayMessageActivity.class);
	    EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    
	    try{
	    	int answer = Integer.parseInt(message);
	    	
	    	if (answer == sum)
		    	intent.putExtra(EXTRA_MESSAGE, "Correct!");
		    else
		    	intent.putExtra(EXTRA_MESSAGE, "Incorrect!");
	    	
	    }catch(NumberFormatException e){
	    	intent.putExtra(EXTRA_MESSAGE, "Your answer is garbage!");
	    }
	    
	    
	    startActivity(intent);
	}
	
	public void generateQuestion(View view) {
	    Intent intent = new Intent(this, DisplayMessageActivity.class);
	    
	    Random rand = new Random();
		num1 = rand.nextInt(10);
		num2 = rand.nextInt(10);
		sum = num1 + num2;
		
		String message2 = "What is " + num1 + "+" + num2 + "?";
	    
	    intent.putExtra(EXTRA_MESSAGE, message2);
	    startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
