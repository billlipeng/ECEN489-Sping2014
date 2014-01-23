package primary;
import static java.lang.System.*;
import java.util.Scanner;
public class Primary {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
out.println("3+5=?");
Scanner reader = new Scanner(System.in);
int answer = reader.nextInt();
if (answer ==8) {
	out.println("Thats correct");
	
}
else {
	out.println("Input was incorrect");
}
	}

}
