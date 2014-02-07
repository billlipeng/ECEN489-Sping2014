package com.example.assignment3;

import java.util.ArrayList;
import java.util.List;

import com.example.assignment3.util.SystemUiHider;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity implements SensorEventListener {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	 private SensorManager mSensorManager;
	  private Sensor mProximity;
	private GridView gridView;
	public double d;
	public static ArrayList<String> ArrayofEntry = new ArrayList<String>();
	public DatabaseHandler db = new DatabaseHandler(this);
	public String senseevent;
	TextView mTapTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fullscreen);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		mSensorManager.registerListener(this, mProximity, mSensorManager.SENSOR_DELAY_NORMAL);
		mTapTextView = (TextView) findViewById(R.id.tap_text);
}

	public void onDisplayDB(View view){
		 Log.d("Reading: ", "Reading all contacts..");
	        List<Entry> entries = db.getAllEntrys();       

	        for (Entry cn : entries) {
	            String log = "Id: "+cn.getID()+" ,Entry: " + cn.getEntry();
	                // Writing Contacts to log
	        Log.d("Entry: ", log);

	        }
	        db.getAllEntrys();
		gridView=(GridView) findViewById(R.id.Grid);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ArrayofEntry);
		gridView.setAdapter(adapter);
		
		}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensor(View view) {
		// TODO Auto-generated method stub
		onPause();
		double distance = d;
		String st = String.valueOf(distance);
	Entry s = new Entry(st);
		db.addEntry(s);
		onResume();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		 d =(double) event.values[0];
		 senseevent = String.valueOf(d);
		 mTapTextView.setText(senseevent);
	}
	 @Override
	  protected void onResume() {
	    // Register a listener for the sensor.
	    super.onResume();
	    mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
	    // Be sure to unregister the sensor when the activity pauses.
	    super.onPause();
	    mSensorManager.unregisterListener(this);
	  }
}