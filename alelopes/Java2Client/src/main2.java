import javax.swing.JFrame;


public class main2 {

	public static void main( String[] args )
	{
		
		Java2Client app;
		app=new Java2Client("127.0.0.1");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.runClient();
	} // end main
} // end class ServerTest
	
	

