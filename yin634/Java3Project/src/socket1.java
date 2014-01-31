import java.net.*;
import java.io.*;


public class socket1 {

	public static void main(String[] args) {
		try{
			URL readSource = new URL("");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					readSource.openStream()));
			String s ="";
			while ((s=in.readLine())!=null){
				System.out.println(s);
			}
			in.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
