package query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueryFacade
{
	private Connection conn;
	private boolean isAdmin;
	
	public QueryFacade(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
		this.conn = null;
	}
	
	public void connect() throws SQLException
	{
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_v4e9", "a33356130");
	}
	
	public List<SelectionRow> getSelectionQuery(String specifiedLevel) throws SQLException
	{
		List<SelectionRow> rows = new ArrayList<SelectionRow>();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM Lesson WHERE l_level = " + specifiedLevel);
		while (rs.next())
		{
			String lid = rs.getString("lid");
			String courtid = rs.getString("courtid");
			String sin = rs.getString("sin");
			String level = rs.getString("l_level");
			SelectionRow selectionRow = new SelectionRow(lid, courtid, sin, level);
			rows.add(selectionRow);
		}
		return rows;
	}
	
}
