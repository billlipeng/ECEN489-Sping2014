package com.example.android3;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity {
	
	EditText num1;
	EditText num2;
	EditText sum;
	EditText message;
	Button generate;
	Button compute;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		num1 = (EditText) findViewById(R.id.editText1);
		num2 = (EditText) findViewById(R.id.editText2);
		sum  = (EditText) findViewById(R.id.editText3);
		message = (EditText) findViewById(R.id.editText4);
		generate = (Button) findViewById(R.id.button2);
		compute = (Button) findViewById(R.id.button1);
		
		generate.setOnClickListener(new ClickButton());
		compute.setOnClickListener(new ClickButton1());
	}
	Random random = new Random();
	int a =random.nextInt(100);
	int b =random.nextInt(100);
	int total1 = a + b;
	
private class ClickButton implements Button.OnClickListener{
		
		public void onClick(View v){
			
		num1.setText(Integer.toString(a));		
		num2.setText(Integer.toString(b));
		
		}
	}
	private class ClickButton1 implements Button.OnClickListener{
		
		public void onClick(View v){
		
		int x = Integer.parseInt(sum.getText().toString());
		try {
			if (x == total1)
				message.setText("Correct");
			else
				message.setText("Incorrect");

		}
		finally{
			
				}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
