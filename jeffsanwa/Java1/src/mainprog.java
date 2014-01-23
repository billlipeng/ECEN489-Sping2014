import java.util.Scanner;



public class mainprog {
	
	 public static Scanner scan = new Scanner(System.in);

  public static void main(String[] args) {
	  System.out.println("The super insano calculator");
	    System.out.println("enter the corrosponding number:");
	    System.out.println("1. Add | 2. Subtract | 3. Divide | 4.Multiply");
	    String input = null;
	    //get user input for string

	    int a = 0, b = 0;
	    int c = 0;
	    System.out.println("Enter the first number");
	    //get user input for a
	    a = scan.nextInt();
	    System.out.println("Enter the second number");
	    b = scan.nextInt();
	    
	    
	    c = a+b;
	    
	    System.out.println(String.valueOf(c));
	    
  }
  
}