package eclipseSumGuess;
// adding notes to show commit changes
//imports
import java.util.Random;
import java.util.Scanner;
// create a class for function
public class sumGuess {
// create a main
	public static void main(String[] args) {
// random number gen
		Random generator = new Random();
// define the two numbers to be added (between 1 and 10)
		int x = generator.nextInt(10) + 1;
		int y = generator.nextInt(10) + 1;
// print out the math problem
		System.out.println(x);
		System.out.println(" + ");
		System.out.println(y);
		System.out.println(" = ?");
// scan the input prompt
		Scanner input = new Scanner(System.in);
		int sum = input.nextInt();
// check to see if correct and print
		if (x+y == sum) {
			System.out.println("Correct");
		} else {
			System.out.println("Incorrect");
		}
	}

}
