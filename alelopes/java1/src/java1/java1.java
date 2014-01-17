package java1;
//Fig. 2.1: Welcome1.java

import java.util.Scanner;




public class java1 {


	private static Scanner input;

	public static void main( String[] args )
	{
	
	input = new Scanner(System.in);
	int result;
	System.out.println( "What is the result for 3+5?" );
	result=input.nextInt();
	if ( result == 8 )
		System.out.print("Yes! It is right!");
	else 
		System.out.print("that is sad man, it is wrong");
		
	}
}

