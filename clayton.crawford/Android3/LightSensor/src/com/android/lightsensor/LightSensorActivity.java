package com.android.lightsensor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LightSensorActivity extends Activity implements SensorEventListener {
	
	TextView luxOutput;
	TextView DBerror;

	private SensorManager mSensorManager;
	private Sensor mLux;
	SQLiteDatabase db;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_sensor);

		// Get an instance of the sensor service, and use that to get an instance of
		// a particular sensor.
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mLux = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		luxOutput = (TextView) findViewById(R.id.textView1);
		DBerror = (TextView) findViewById(R.id.textView2);
		
		// SQLite Database Code Begins
		this.deleteDatabase("LuxReadings.db");
		db = openOrCreateDatabase("LuxReadings.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(3);
		db.setLocale(Locale.getDefault());
		
		final String CREATE_TABLE_LUX = "CREATE TABLE IF NOT EXISTS lux (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT, Time TEXT, Lux INTEGER);";
		db.execSQL(CREATE_TABLE_LUX);
	}
	
	public final void viewDB(View view) {
		Intent intent = new Intent(this, DatabaseActivity.class);
		startActivity(intent);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		// Do something with this sensor data.
		luxOutput.setText(Float.toString(event.values[0]) + " lx");

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date date = new Date();
			ContentValues initialValues = new ContentValues(); 
			initialValues.put("Time", dateFormat.format(date));
			initialValues.put("Lux", event.values[0]);
			db.insertOrThrow("lux", null, initialValues);
		} catch (Exception e) {
			DBerror.setText(e.getMessage());
		}
	}

	@Override
	protected void onResume() {
		// Register a listener for the sensor.
		super.onResume();
		mSensorManager.registerListener(this, mLux, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

}
