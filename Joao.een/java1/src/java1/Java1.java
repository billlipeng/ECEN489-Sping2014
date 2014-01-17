package java1;
import java.util.Scanner;

public class Java1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a;
		Scanner input = new Scanner(System.in);
		System.out.printf("5 + 3 = ?\n");
		a = input.nextInt();
		if (a == 8)
				System.out.printf("The answer is correct.");
		else
				System.out.printf("The answer is wrong.");
		input.close();
	}

}
