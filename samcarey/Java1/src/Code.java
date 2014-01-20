import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


public class Code {
	public static void main(String []args){
		
		Random rand = new Random();
		int num1 = rand.nextInt(10);
		int num2 = rand.nextInt(10);
		Scanner sc = new Scanner(System.in);
		
		while(true){
			try{
				System.out.println("What is " + num1 + "+" + num2 + "?");
				System.out.print("> ");
				
				if (sc.nextInt() == (num1+num2)){
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
