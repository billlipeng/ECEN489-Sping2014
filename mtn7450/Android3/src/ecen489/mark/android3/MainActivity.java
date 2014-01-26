package ecen489.mark.android3;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void calculate(View view)
	{
		EditText text1 = (EditText) findViewById(R.id.editText1);
		EditText text2 = (EditText) findViewById(R.id.editText2);
		EditText answerText = (EditText) findViewById(R.id.editText4);
		
		int int1 = Integer.parseInt(text1.getText().toString());
		int int2 = Integer.parseInt(text2.getText().toString());
		String stringAnswer = Integer.toString(int1+int2);
		answerText.setText(stringAnswer);
	}
	
	public void clear(View view)
	{
		EditText text1 = (EditText) findViewById(R.id.editText1);
		EditText text2 = (EditText) findViewById(R.id.editText2);
		EditText answerText = (EditText) findViewById(R.id.editText4);
		
		text1.setText("");
		text2.setText("");
		answerText.setText("");
	}

}
