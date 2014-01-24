import javax.swing.JOptionPane;
import java.util.Random;

public class Java1Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random numRandom = new Random();
		int num1 = numRandom.nextInt(10);
		int num2 = numRandom.nextInt(10);
		boolean cont=true;
		String ans;
		while(cont){
        ans = JOptionPane.showInputDialog(null, num1 + " + " + num2);
        int total=num1+num2;
        
        if(total==Integer.parseInt(ans)){
        	
        	int again= JOptionPane.showConfirmDialog(null, "Correct! Play again?","choose one", JOptionPane.YES_NO_OPTION);
        	if(again==JOptionPane.NO_OPTION){
    			cont=false;
    		}
        	else{
        		 num1 = numRandom.nextInt(10);
        		 num2 = numRandom.nextInt(10);
        	}
        }
        
        else{
        	int again= JOptionPane.showConfirmDialog(null, "Incorrect! Try Again?","choose one", JOptionPane.YES_NO_OPTION);
        	if(again==JOptionPane.NO_OPTION){
    			cont=false;
    		}
        	
        }
        
        }
        
        
		System.exit(0);
		}
		
		
	}


