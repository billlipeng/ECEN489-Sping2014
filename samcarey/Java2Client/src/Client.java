import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Client {
	public static void main(String []args){
		
		Random rand = new Random();
		int num1 = rand.nextInt(10);
		int num2 = rand.nextInt(10);
		int sum = 0;
		
		try{
			System.out.println("Client waiting...");
			Socket connection = new Socket("172.20.2.152", 2015);
			//Socket connection = new Socket(InetAddress.getByName("127.0.0.1"), 2015);
			System.out.println("Client connected!");
			
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			output.writeInt(num1);
			output.writeInt(num2);
			output.flush();
			
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			sum = input.readInt();
			
			input.close();
			output.close();
			connection.close();
		}catch(IOException e){
			System.out.println("IOException");
		}

		Scanner sc = new Scanner(System.in);
		
		while (true){
			try{
				System.out.println("What is " + num1 + "+" + num2 + "?");
				System.out.print("> ");
				
				if (sc.nextInt() == sum){
					System.out.println("Correct!");
					break;
				}else{
					System.out.println("Incorrect!");
				}
			}
			catch(InputMismatchException e){
				System.out.println("Your answer is garbage!");
				sc.next();
			}
		}
		sc.close();
	}	
}
