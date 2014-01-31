import java.io.*;
import java.net.*;
import java.util.*;

public class mysocketclient {
	
	
	
	
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Please Input Server IP:");
		//System.out.println("");
		String IP = input.nextLine();
		System.out.print("Please input Port Number:");
		//System.out.println("");
		//String host="127.0.0.1";
		int port = input.nextInt();
		byte[] accept= new byte[4];
		
	try{
		Socket socket = new Socket(IP,port);
		
		try{
			DataInputStream is = new DataInputStream(socket.getInputStream());
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			
			Scanner scanner = new Scanner(System.in);
			boolean status = true;
			while (true){
				System.out.print("Type the 1st number:");
				int num1 = scanner.nextInt();
				System.out.print("Type the 1st number:");
				int num2 = scanner.nextInt();
				int correctAnswer = num1+num2;
				
//				{
//				    byte[] itob = new byte[4];
//				    itob[3] = (byte) (a & 0xFF);   
//				    itob[2] = (byte) ((a >> 8) & 0xFF);   
//				    itob[1] = (byte) ((a >> 16) & 0xFF);   
//				    itob[0] = (byte) ((a >> 24) & 0xFF);
//				    return itob;
//				}
				
				
				
				byte[] buff = new byte[8];
				buff[7] = (byte) (num2);
				buff[6] = (byte) (num2>>8);
				buff[5] = (byte) (num2>>16);
				buff[4] = (byte) (num2>>24);
				buff[3] = (byte) (num1);
				buff[2] = (byte) (num1>>8);
				buff[1] = (byte) (num1>>16);
				buff[0] = (byte) (num1>>24);
				os.write(buff);
				
				
//				int num = is.read(accept);
//				int sum = ((accept[0] & 0xff)<<24)+((accept[1] & 0xff)<<16)
//						+((accept[2] & 0xff)<<8)+(accept[3] & 0xff);
				
				int sum = is.readInt();
				
				System.out.println("Waiting for Answer.....");
				System.out.println("The sum is: "+sum);
				if (sum == correctAnswer)
					System.out.println("The Answer is Correct!");
					else
					System.out.println("The Answer is Wrong!");
				
				System.out.println("Calculate other two numbers?(Yes or No)");
				String con = scanner.next();
				if (con.equals("No") || con.equals("NO") || con.equals("no"))
					status = false;
				else if (con.equals("yes") || con.equals("YES") || con.equals("Yes"))
					status = true;
				else {
					System.out.println("Your Input is invalid, Please enter again!");
					status = true;
				}
				
				
				
				//String receive = is.read(null);
				//os.writeUTF("Client:"+ send);
				//String accept = is.readUTF();
				//System.out.println(accept);
			}
		}finally {
			socket.close();
		}
	}catch (IOException e){
		e.printStackTrace();
	}
		
	}
}
