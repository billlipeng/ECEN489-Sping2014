package ecen489.mark.speechrecognitionnopopup;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView resultList;
	Button speechButton;
	TextView textOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		speechButton = (Button) findViewById(R.id.speechButton);
		textOut = (TextView) findViewById(R.id.textOut);
		resultList = (ListView) findViewById(R.id.list);

		if (!SpeechRecognizer.isRecognitionAvailable(this)) {
			speechButton.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Recognizer Not Found",
			Toast.LENGTH_SHORT).show();
		}   
	 }

	 public void startVoiceRecognitionActivity(View view) {
		 System.out.println("Button push\n");
	  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      
	  SpeechRecognizer speechRec = SpeechRecognizer.createSpeechRecognizer(this);
	  MyRecognitionListener listener = new MyRecognitionListener();
	  speechRec.setRecognitionListener(listener);
	  speechRec.startListening(intent);
	 }
	 
	 class MyRecognitionListener implements RecognitionListener {

         @Override
         public void onBeginningOfSpeech() {
                 System.out.println("Beginning of speech.");
                 textOut.setText("Listening...");
         }

         @Override
         public void onBufferReceived(byte[] buffer) {
                 
         }

         @Override
         public void onEndOfSpeech() {
                 System.out.println("End of speech.");
                 textOut.setText("End of speech.");
         }

         @Override
         public void onError(int error) {
                 System.out.println("Error in speech.");
                 textOut.setText("Error in speech.");
         }

         @Override
         public void onEvent(int eventType, Bundle params) {

         }

         @Override
         public void onPartialResults(Bundle partialResults) {
            
         }

         @Override
         public void onReadyForSpeech(Bundle params) {
                 System.out.println("Ready for speech");
                 textOut.setText("Ready for speech");
         }
         

         @Override
         public void onResults(Bundle results) {             
                 ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                 resultList.setAdapter(new ArrayAdapter<String>(speechButton.getContext(), android.R.layout.simple_list_item_1, matches));
                 textOut.setText("Hit \"Start\" to begin");
         }

         @Override
         public void onRmsChanged(float rmsdB) {

         }
	 }
}
