
public class ServerThread implements Runnable {
		private MainServer server;
		private int mPort;
		
	
	public ServerThread(int port){
		mPort = port;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		server = new MainServer(mPort);
		
		while(!server.isConnected()){
			System.out.println("Made It");
		ServerThread next = new ServerThread(mPort);
		next.run();
		}
		}

	/**
	 * @param args
	 */
	

}
