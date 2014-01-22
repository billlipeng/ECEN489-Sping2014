package Java2Server;
import javax.swing.JFrame;


public class main {

	public static void main( String[] args )
	{
		Java2Server application = new Java2Server(); // create server
		application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		application.runServer(); // run server application
		
//		Java2Client app;
//		app=new Java2Client("127.0.0.1");
//		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		app.runClient();
	} // end main
} // end class ServerTest
	
	

