package com.zyc.accedemo;

import android.app.Activity;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.hardware.*;
import android.os.*;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.*;

public class RecorderActivity extends Activity implements SensorEventListener {

	private TextView txtDataX;
	private TextView txtDataY;
	private TextView txtDataZ;
	private ImageView img;
	private Bitmap[] bmBuff = new Bitmap[2];
	private int bmPtr = 0;
	private Sensor sensor;
	private SensorManager sensorManager;
	private Float preX = null;
	private Float preY = null;
	private Float preZ = null;
	private int imgW;
	private int imgH;
	private String unit;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.recorder);
		
		unit = "m/s2";

		txtDataX = (TextView) findViewById(R.id.txtDataX);
		txtDataY = (TextView) findViewById(R.id.txtDataY);
		txtDataZ = (TextView) findViewById(R.id.txtDataZ);
		img = (ImageView) findViewById(R.id.img);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		// a way to get the correct width and height, if too early, you get 0
		img.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
								
				imgW = img.getWidth();
				imgH = img.getHeight();
				
				bmBuff[0] = Bitmap.createBitmap(imgW, imgH, Config.ARGB_8888);
				bmBuff[1] = Bitmap.createBitmap(imgW, imgH, Config.ARGB_8888);
				
				img.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		
	}
	
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	private void updateGraph(float[] values) {
		
		float x = values[0];
		float y = values[1];
		float z = values[2];
		String strX = String.format("%.5f", x);
		String strY = String.format("%.5f", y);
		String strZ = String.format("%.5f", z);
		txtDataX.setText(String.format("X: %s %s", strX, unit));
		txtDataY.setText(String.format("Y: %s %s", strY, unit));
		txtDataZ.setText(String.format("Z: %s %s", strZ, unit));
		
		x *= 5;
		y *= 5;
		z *= 5;
		
		if(bmBuff[0] == null || bmBuff[1] == null) return;
		
		Canvas canvas = new Canvas(bmBuff[bmPtr]);
		canvas.drawARGB(255, 0, 0, 0);
		Paint paint = new Paint();
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawBitmap(bmBuff[(bmPtr+1)%2], -1, 0, paint);
		
		if(preX == null) preX = x;
		if(preY == null) preY = y;
		if(preZ == null) preZ = z;
		
		paint.setColor(Color.RED);
		canvas.drawLine(imgW-1, preX+imgH/6, imgW, x+imgH/6, paint);
		
		paint.setColor(Color.GREEN);
		canvas.drawLine(imgW-1, preY+imgH/2, imgW, y+imgH/2, paint);
		
		paint.setColor(Color.BLUE);
		canvas.drawLine(imgW-1, preZ+imgH/6*5, imgW, z+imgH/6*5, paint);
		
		preX = x;
		preY = y;
		preZ = z;
		
		img.setImageBitmap(bmBuff[bmPtr]);
		bmPtr = (bmPtr+1)%2;
		
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	@Override
	public void onSensorChanged(SensorEvent e) {
		updateGraph(e.values);
	}
	
}