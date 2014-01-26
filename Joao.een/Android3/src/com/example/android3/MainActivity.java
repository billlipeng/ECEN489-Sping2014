package com.example.android3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

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
    
	public void sendNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_number);
        String message = editText.getText().toString();
        String answer;
        int number = Integer.parseInt(message);
        if (number == 8)
        	answer = "Correct answer!";
        else
        	answer = "Wrong answer!";
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(answer);
        setContentView(textView);
    }

}
