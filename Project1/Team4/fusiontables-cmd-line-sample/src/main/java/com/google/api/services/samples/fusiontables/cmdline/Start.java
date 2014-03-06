package com.google.api.services.samples.fusiontables.cmdline;


import javax.swing.JFrame;



//THIS IS THE MAIN JUST INSTANTIATE THE JAVA3SERVER.

public class Start {

	  //-------------------------------------------------------------------------------------
	  //Main function
	  public static void main(String[] args) {
	   
		  Java3server application = new Java3server(); // create server
			application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			try{
				application.runServer(); // run server application
			}
			catch (Exception Erro){
			}
		  
		  	  }
	
	
}
