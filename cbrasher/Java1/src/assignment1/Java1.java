package assignment1;

//Assignment 1 part 3_2
//Sum-Checking program.
import java.util.Scanner;

public class Java1
{
	// main method begins execution of java application
	public static void main( String[] args )
	{
		System.out.printf( "%s\n%s\n","5+2","5+9" ); 
		Scanner input = new Scanner( System.in ); // takes input
		
		int sum1; // user input of sum1
		int sum2; // user input of sum2
		int number1; // sum of first eq
		int number2; // sum of second eq
		
		number1 = 7; //true sum
		number2 = 14; //true sum
		
	Case1:System.out.print( "Enter first sum: " );
		sum1 = input.nextInt();
		if ( number1 == sum1 )
			System.out.printf("%d == %d\n", number1, sum1 );
		else {
			System.out.print( "Incorrect, please try again" );
		}
	Case2:System.out.print( "Enter second sum: " );
		sum2 = input.nextInt();
		if ( number2 == sum2 )
			System.out.printf("%d == %d\n", number2, sum2 );
		else {
			System.out.print( "Incorrect, please try again" );
		}
		}// end method main
	} // end class Java1