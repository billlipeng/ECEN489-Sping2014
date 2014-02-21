package com.google.api.services.samples.fusiontables.cmdline;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.mhardiman.gps_retrieval.*;

public class DataThreader extends Thread{
		protected Socket socket;
		dataRow row;
		//declare secondary obj here for send if desired. 
		public DataThreader(Socket client){
			this.socket = client;
		}
		@SuppressWarnings("unchecked")
		public void run(){	
			//ObjectOutputStream sender = null; //Unneeded right now
			ObjectInputStream receiver = null;
			
			//Declaring data
			
			try{
				//sender = new ObjectOutputStream(socket.getOutputStream()); //Unneeded right now
				receiver = new ObjectInputStream(socket.getInputStream());					
			}
			catch(IOException e){
				System.err.println("Could not create I/O Streams..!");
			}
			
				try{

					while(true){
						//listening for packets
						ArrayList<dataRow> row = new ArrayList<dataRow>();
						row = (ArrayList<dataRow>) receiver.readObject();				
						System.out.println(row.get(0).sensorType);				
						System.out.println(row.get(0).latitude);
						System.out.println("Attempting connection to SQL server.");	
						new FusionTablesSample(row).start();

					}
					//Now has data read into row[].
				}

				catch(IOException e){
					
					System.out.println("A client disconnected!");
					System.err.println(e);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				try{
					socket.close();
					System.out.println("Socket terminated.");
					//connection.close();
					
				}
				catch(IOException e){
					System.err.println("Couldn't close the socket!");
				}	
				
			}		
	}