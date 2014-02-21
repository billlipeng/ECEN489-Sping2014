package com.mhardiman.gps_retrieval;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;

public class ChooseBluetooth extends Activity {
	String addressList[];
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        ListView addressListView = (ListView)findViewById(R.id.addressList);
        Intent intent = getIntent();
        addressList = intent.getStringArrayExtra("deviceList");
 
        System.out.println("inside activity\n");
        System.out.println(addressList[0] + "\n");
        /*if (addressList.length <= 1)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("deviceIndex", 0);
            setResult(RESULT_OK, returnIntent);            
            finish();
        }
        else
        {*/
        	
        	 /* Intent returnIntent = new Intent();
              returnIntent.putExtra("deviceIndex", 0);
              setResult(RESULT_OK, returnIntent);            
              finish();
              */
              
	        addressListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addressList));
	        

	
	 
	        // listening to single list item on click
	        addressListView.setOnItemClickListener(new OnItemClickListener() 
	        {
		          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		          {
		               
		
		               
		              // Launching new Activity on selecting single List Item
		              Intent returnIntent = new Intent();
		              // sending data to new activity
		              returnIntent.putExtra("deviceIndex", position);
		              setResult(RESULT_OK, returnIntent);            
		              finish();
		          }
	        });
       // }
    }

    
    
    
}
