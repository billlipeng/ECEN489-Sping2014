package com.zpartal.android3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	
	private SensorManager mSensorManager;
	private Sensor mLight;
	float lux;
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		db = new DatabaseHandler(this);

	}
	
	@Override
	public final void onSensorChanged(SensorEvent event) {
		lux = event.values[0];
		TextView textView = (TextView) findViewById(R.id.lux_value);
		textView.setText("Lux: " + String.valueOf(lux));	   
	}
	
	public void storeLux (View view) {
		long actualTime = System.currentTimeMillis();
		db.addLuxVal(new LuxValue(String.valueOf(lux), String.valueOf(actualTime)));
	}
	
	public void clearTable(View view) {
		db.clearTable();
	}
	
	public void viewDB (View view) {
		Intent intent = new Intent(this, ViewDB.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    mSensorManager.registerListener((SensorEventListener) this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
	 }
	
	@Override
	protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener((SensorEventListener) this);
	 }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
