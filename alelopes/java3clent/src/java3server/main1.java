package java3server;


import javax.swing.JFrame;


public class main1 {

	public static void main( String[] args )
	{
		java3server application = new java3server(); // create server
		application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		try{
			application.runServer(); // run server application
		}
		catch (Exception Erro){
		}
		
//		Java2Client app;
//		app=new Java2Client("127.0.0.1");
//		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		app.runClient();
	} // end main
} // end class ServerTest