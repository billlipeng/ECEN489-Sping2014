import java.util.Scanner;
import java.util.Random;

public class java1 {
	public static void display(int number1,int number2){
		System.out.print("what is the sum of ");
		System.out.println(number1 + "+" + number2 + "?");
	}
	public static void main(String[] args) {
		
		int CorrectAnswer;
		int ClientAnswer;
		Random random1=new Random();
		int No1=random1.nextInt(500)+1;
		Random random2=new Random();
		int No2=random2.nextInt(500)+1;
		CorrectAnswer = No1+No2;
		
		display(No1,No2);
		boolean status = true;
		while(status){
		System.out.print("Please Enter Your Answer:");
		Scanner input = new Scanner(System.in);
		ClientAnswer=input.nextInt();
		if(ClientAnswer == CorrectAnswer)
			{System.out.println("^_^ Your Answer is CORRECT!");
				status = false;
			}
		else
			System.out.println("T_T Your Answer is WRONG!");
		}
	}

}
