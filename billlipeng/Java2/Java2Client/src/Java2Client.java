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
    			/*Generate two random numbers*/
				int a = (int) (Math.random() * 50);
				int b = (int) (Math.random() * 50);
				System.out.print("Calculate: " + a + " + " + b + " = ");
				
				/*Get user input*/
				int c = 0;
				Scanner input = new Scanner(System.in);
				c = input.nextInt();
				
				/*Send a b to server and get sum back*/
				Socket client;  
				client = new Socket("127.0.0.1", 8887);
				OutputStream os = client.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				InputStream is = client.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				dos.writeUTF(a + " " + b);
				String str;
				str = dis.readUTF();
				int sum;
				sum = Integer.parseInt(str);
				
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
