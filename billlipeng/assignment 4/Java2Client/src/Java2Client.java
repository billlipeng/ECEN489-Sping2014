import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;  
import java.net.UnknownHostException;  
import java.util.Scanner;
public class Java2Client 
{
    public Java2Client() throws UnknownHostException, IOException 
    {  
    	try {
    			/*Connection Info*/
				System.out.print("Client Start...\n");
    			System.out.print("Please input IP address: ");
    			Scanner sc = new Scanner(System.in);
    			String ip_address = sc.nextLine();
    			//System.out.print("Please input port number: ");
    			//int port = sc.nextInt();
    			 			
    			/*Generate two random numbers*/
				int a = (int) (Math.random() * 100);
				int b = (int) (Math.random() * 100);
				System.out.print("Calculate: " + a + " + " + b + " = ");
				int c = sc.nextInt();//Get user input
				
				byte[] bs = new byte[8];
				bs[3] = (byte) (a & 0xFF);
				bs[7] = (byte) (b & 0xFF);

				/*Set connection to server*/
				Socket client = new Socket(ip_address, 5555);
				//client = new Socket("10.201.41.12", 8887);
				OutputStream os = client.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				InputStream is = client.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				dos.write(bs);	
				int sum = dis.readInt();
				
				/*Compare and print output*/
				if (c == sum)
					System.out.println("Correct!");
				else {
					System.out.println("Uncorrect!");
					System.out.println("Correct answer should be:\n" + a + " + "
							+ b + " = " + sum);
			}
		} catch (Exception e) {
			System.out.println("Error in Java2Client!");
            e.printStackTrace();
		}
		
    }  
    public static void main(String[] args) {  
        try {  
            new Java2Client();  
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();
        }
    }  
}
