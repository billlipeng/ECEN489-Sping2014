package ecen489.mark.speechrecognition;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int REQUEST_CODE = 1234;
	private ListView resultList;
	private ListView confList;
	Button speechButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		speechButton = (Button) findViewById(R.id.speechButton);
		resultList = (ListView) findViewById(R.id.list);
		confList = (ListView) findViewById(R.id.confList);
	 
		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
	    RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			speechButton.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Recognizer Not Found",
			Toast.LENGTH_SHORT).show();
		} 	  
	 }

	 public void startVoiceRecognitionActivity(View view) {
	  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	  intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
	    "Start speaking...");
	  startActivityForResult(intent, REQUEST_CODE);
	 }
	 
	 @SuppressLint("InlinedApi")
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode == REQUEST_CODE & resultCode == RESULT_OK) {
	   ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	   
	   //confidence is only returned for the first result for some reason
	   float confidences [] = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
	   System.out.println(Float.toString(confidences[2]));
	   String conf_strings [] = new String[confidences.length];
	   for (int i=0; i<confidences.length; i++)
	   {
		   conf_strings[i] = Float.toString(confidences[i]);
	   }
	   resultList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
	   confList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, conf_strings));
	  }
	  super.onActivityResult(requestCode, resultCode, data);
	 }
	

}
