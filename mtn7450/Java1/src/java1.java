//Prompt user for answer to arithmetic
/*Pseudocode:
output “3+4=?” //prompt user with sum 

read in next integer and store to an answer variable

if answer variable equals 7, then output “Correct.”
else, output “Incorrect.”

 */


import java.util.Scanner;


public class java1 {

	public static void main( String[] args )
	{
		Scanner input = new Scanner(System.in );
		System.out.print("3+4=?\n");
		int answer = input.nextInt();
		if (answer == 7)
			System.out.print("Correct.");
		else
			System.out.print("Incorrect.");
		
		input.close();
	}
	
}
