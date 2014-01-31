import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Client {
	public static void main(String []args){
		
		String ip = "";
		String id = "Client: ";
		Scanner sc = new Scanner(System.in);
		Random rand = new Random();
		int num1 = rand.nextInt(10);
		int num2 = rand.nextInt(10);
		int sum = 0;
		byte[] bytes = new byte[8];
		
		bytes[0] = (byte) (num1 >> 24);
		bytes[1] = (byte) (num1 >> 16);
		bytes[2] = (byte) (num1 >>  8);
		bytes[3] = (byte) (num1);
		
		bytes[4] = (byte) (num2 >> 24);
		bytes[5] = (byte) (num2 >> 16);
		bytes[6] = (byte) (num2 >>  8);
		bytes[7] = (byte) (num2);
		
		System.out.println(id + "data: " + Arrays.toString(bytes));
		
		System.out.println(id + "custom ip? Enter '0' if local");
		System.out.print("> ");
		ip = sc.nextLine();
		if (ip == "0") ip = "127.0.0.1";
		
		try{
			System.out.println(id + "waiting...");
			Socket connection = new Socket(InetAddress.getByName(ip), 5555);;
			System.out.println(id + "connected!");
			
			DataOutputStream output = new DataOutputStream(connection.getOutputStream());
			System.out.println(id + "sending numbers...");
			output.write(bytes);
			output.flush();
			
			DataInputStream input = new DataInputStream(connection.getInputStream());
			sum = input.readInt();
			//System.out.println(id + "sum = " + sum);
			
			input.close();
			output.close();
			connection.close();
		}catch(IOException e){
			System.out.println(id + "IOException");
		}
		
		while (true){
			try{
				System.out.println(id + "What is " + num1 + "+" + num2 + "?");
				System.out.print(id + "> ");
				
				if (sc.nextInt() == sum){
					System.out.println(id + "Correct!");
					break;
				}else{
					System.out.println(id + "Incorrect!");
				}
			}
			catch(InputMismatchException e){
				System.out.println(id + "Your answer is garbage!");
				sc.next();
			}
		}
		sc.close();
	}	
}
