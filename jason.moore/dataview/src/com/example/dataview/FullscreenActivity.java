package com.example.dataview;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	public String[] SA = new String[]{
			"test", "test","TEST"
	};
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_fullscreen);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    ArrayAdapter<String> AA = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, SA);
	    gridview.setAdapter(AA);
	   
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(FullscreenActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	public void onDisplay(View view){
		
	}
}
