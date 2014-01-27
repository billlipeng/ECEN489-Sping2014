package com.example.android3;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	/** Called when the activity is first created. */
	private Button new_question,check_answer;
	private TextView text_qestion,text_result;
	private EditText user_answer;
	public 	String result;
	public 	int a,b;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new_question = (Button)findViewById(R.id.new_question);
		check_answer = (Button)findViewById(R.id.check_answer);
		
		user_answer = (EditText)findViewById(R.id.user_answer);
		
		text_qestion = (TextView)findViewById(R.id.t1);
		text_result = (TextView)findViewById(R.id.t2);
		
		a = (int)(Math.random()*50);
		b = (int)(Math.random()*50);
		text_qestion.setText("Calculate: "+ a + "+" + b + " = ");
		
		new_question.setOnClickListener(this);
		check_answer.setOnClickListener(this);
	}
	public void onClick(View v){
		switch (v.getId()){
			case R.id.new_question:
				a = (int)(Math.random()*50);
				b = (int)(Math.random()*50);
				text_qestion.setText("Calculate: "+ a + "+" + b + " = ");
				break;
			
			case R.id.check_answer:
				if (isNumeric(user_answer.getText().toString())){
					result = (Integer.parseInt(user_answer.getText().toString()) == (a + b)) ? "correct!": "incorrect.";
					text_result.setText("Your answer is "+ result);
				}
				else
					text_result.setText("Please input legal format!");
				break;
		}
	}
	
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	} 
}