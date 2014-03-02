package com.jdterrell.p2;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Worker implements Runnable {
	private Socket client;	
	static private ObjectInputStream ois;
    private Config config = new Config();
    ArrayList<AndroidPacket2> data = null;

	public Worker(Socket client) {
		super();
		this.client = client;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
        try {
            ois = new ObjectInputStream(client.getInputStream());	
            data = (ArrayList<AndroidPacket2>) ois.readObject();	//store data
            ois.close();										
            client.close();											//close the client connection

            // Local DB
            new Thread(new DatabaseHandler(data)).start();		//push data to database

            // Fusion Table
            new Thread(new FusionTableHandler(data)).start();	//push data to fusion table

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}