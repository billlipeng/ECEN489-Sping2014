package assignment1;

import java.util.*;

public class Java1 {

	public static void main(String[] args) 
	{
		// main method begins execution of java application
			System.out.printf( "%s\n%s\n","5+2","5+9" ); 
			Scanner input = new Scanner( System.in ); // takes input
			
			int sum1; // user input of sum1
			int sum2; // user input of sum2
			int number1; // sum of first eq
			int number2; // sum of second eq
			
			number1 = 7; //true sum
			number2 = 14; //true sum
			
		Case1:
			System.out.print( "Enter first sum: " );
			sum1 = input.nextInt();
			if ( number1 == sum1 )
				System.out.printf("That is correct\n"+"%d == %d\n", number1, sum1 );
			else {
				System.out.print( "Incorrect, please try again\n" );
			}
		Case2:
			System.out.print( "Enter second sum: " );
			sum2 = input.nextInt();
			if ( number2 == sum2 )
				System.out.printf("That is correct\n"+"%d == %d\n", number2, sum2 );
			else {
				System.out.print( "Incorrect, please try again\n" );
			}
			}// end method main
		} // end class Sumchecker1