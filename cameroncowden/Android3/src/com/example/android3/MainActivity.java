package com.example.android3;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity implements OnClickListener{

	//Declarations
	private Button newq, soln;
	private TextView output;
	private EditText useranswer;
	public int n1, n2, sum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		newq = (Button)findViewById(R.id.button1);
		soln = (Button)findViewById(R.id.button2);
		output = (TextView)findViewById(R.id.textView2);
		useranswer = (EditText)findViewById(R.id.editText1);
		
		Random r = new Random(100);
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.button1:
			Random r = new Random();
			n1 = r.nextInt(100);
			n2 = r.nextInt(100);
			sum = n1 + n2;
			output.setText("Sum: " + n1 + "+" + n2);
		case R.id.button2:
			if(Integer.parseInt(useranswer.getText().toString()) == sum)
				output.setText("You are correct!");
			else
				output.setText("You're wrong!");
			break;
			
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
