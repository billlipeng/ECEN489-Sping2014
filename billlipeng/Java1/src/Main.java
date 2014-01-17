import java.util.*;


public class Main {

	public static void main(String[] args){
		try{
				/*Generate a question*/
				int a = (int)(Math.random()*50);
				int b = (int)(Math.random()*50);
			 	System.out.print("Calculate: "+ a +" + "+ b + " = ");
			    
			 	/*Get user input*/
			 	int c =0;
			 	Scanner input = new Scanner(System.in);
			 	c = input.nextInt();	
			 	if(c == (a + b))
			 		System.out.println("Correct!");
			 	else{
			 		System.out.println("Uncorrect!");
		 			System.out.println("Correct answer should be:\n"+ a + " + " + b +" = " + (a+b) );
			 	}
		}
		catch(Exception e)
		{
			System.out.println("Answer format illegal!");
		}
	}
}
