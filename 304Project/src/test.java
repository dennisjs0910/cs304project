
import java.sql.*;
public class test {

	public static void main(String[] args) {
		System.out.println("BOOP");
		Connection conn = null;
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_v4e9", "a33356130");
			System.out.println("Connection Successful!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Failed\n" + e);
			e.printStackTrace();
		}
		
		try
		{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * from publishers");
			
			while (rs.next())
			{
				System.out.println(rs.getString("pub_id"));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

}
