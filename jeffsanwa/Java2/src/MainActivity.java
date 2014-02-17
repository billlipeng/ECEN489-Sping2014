
public class MainActivity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultiThreadedServer server = new MultiThreadedServer(3333);
		new Thread(server).start();
		while(true){
		try {
		    Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		}
		//System.out.println("Stopping Server");
		//server.stop();
	}

}
