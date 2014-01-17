
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		int userSum;
		Boolean quit = false;
		char answer;
		Random rndNum = new Random();
		
		System.out.println("Let's see how well you can add!");
		
		while (!quit) {
			int num1 = rndNum.nextInt(101);
			int num2 = rndNum.nextInt(101);
			int sum = num1 + num2;
			
			System.out.printf("What is %d + %d = ?\n", num1, num2);
			
			Scanner input = new Scanner(System.in);
			userSum = input.nextInt();
			
			if (userSum != sum)
				System.out.printf("Incorrect! The answer is %d\n", sum);
			else System.out.println("Correct!");
			
			System.out.println("Would you like to play again? (y = yes | n = no)");
			input.nextLine();
			answer = input.next().trim().charAt(0);
			if (answer == 'n' || answer == 'N')
				break;
			else if (answer == 'y' || answer == 'Y')
				System.out.println("Getting next addition problem...");
			else if (answer != 'y' || answer != 'Y') System.out.println("Incorrect Option. Defaulting to Playing Again.");
		}
		System.out.println("Program has terminated!");
	}
}
