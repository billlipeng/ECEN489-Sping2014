import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class SQLiteDBtest {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		int menuChoice = 9;
		boolean quit = false;
		
		Class.forName("org.sqlite.JDBC");
		Connection connection = null;
		try {
			
			while (!quit) {
				connection = DriverManager.getConnection("jdbc:sqlite:Java2TestDB.db");
				menuChoice = printMenu(menuChoice);
				switch(menuChoice) {
					case 1: printTable(connection); connection.close(); break;
					case 2: insertInTable(connection); connection.close(); break;
					case 3: deleteFromTable(connection); connection.close(); break;
					case 0: System.out.println("Exiting Program..."); quit = true; break;
				}
			}
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e);
			}
		}

	}
	
	public static void printTable(Connection conn) throws ClassNotFoundException {
		String Make = "";
		String Model = "";
		int Year = 0;
		int Amount = 0;
		
		try {
			Statement statement = conn.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			System.out.println("Make\t\tModel\t\tYear\t\tAmount");
			System.out.println("------------------------------------------------------");
			ResultSet rs = statement.executeQuery("select * from Inventory");
			
			while (rs.next()) {
				Make = rs.getString("Make");
				Model = rs.getString("Model");
				Year = rs.getInt("Year");
				Amount = rs.getInt("Amount");
				
				System.out.print(Make);
				if (Make.length() < 8)
					System.out.print("\t\t");
				else System.out.print("\t");
				System.out.print(Model);
				if (Model.length() < 8)
					System.out.print("\t\t");
				else System.out.print("\t");
				System.out.print(Year + "\t\t" + Amount + "\n");
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static int printMenu(int menuChoice) throws IOException {
		System.out.println("\n####################\n# Database Program #\n####################\n"
						  + "1.Print Table\n"
						  + "2.Insert Data\n"
						  + "3.Delete Data\n"
						  + "0.Exit\n");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			menuChoice = Integer.parseInt(br.readLine());
		}
		catch (NumberFormatException nfe) {
			System.err.println("Invalid Choice!");
		}
		return menuChoice;
	}

	public static void insertInTable(Connection conn) throws ClassNotFoundException, IOException {
		String Make = "";
		String Model = "";
		int Year = 0;
		int Amount = 0;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter the Make: ");
			Make = br.readLine();
			System.out.println("Enter the Model: ");
			Model = br.readLine();
			System.out.println("Enter the Year: ");
			Year = Integer.parseInt(br.readLine());
			System.out.println("Enter the Amount: ");
			Amount = Integer.parseInt(br.readLine());
		}
		catch (NumberFormatException nfe) {
			System.err.println("Invalid Number Format!");
		}
		
		try {
			conn.setAutoCommit(false);
			String insert = "INSERT INTO Inventory(Make, Model, Year, Amount) VALUES(?,?,?,?)";
			PreparedStatement insertData = conn.prepareStatement(insert);
			insertData.setString(1,  Make);
			insertData.setString(2, Model);
			insertData.setInt(3, Year);
			insertData.setInt(4, Amount);
			insertData.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	public static void deleteFromTable(Connection conn) throws ClassNotFoundException {
		String Make = "";
		String Model = "";
		int Year = 0;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Enter the Make to Remove: ");
			Make = br.readLine();
			System.out.println("Enter the Model to Remove: ");
			Model = br.readLine();
			System.out.println("Enter the Year to Remove: ");
			Year = Integer.parseInt(br.readLine());
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		try {
			conn.setAutoCommit(false);
			String insert = "DELETE FROM Inventory WHERE Make = ? AND Model = ? AND Year = ?";
			PreparedStatement deleteData = conn.prepareStatement(insert);
			deleteData.setString(1,  Make);
			deleteData.setString(2, Model);
			deleteData.setInt(3, Year);
			if (deleteData.executeUpdate() != 0) {
				System.out.println("Record Deleted!");
				conn.commit();
				conn.setAutoCommit(true);
			}
			else System.out.println("Error: Record not Found!");
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

}
