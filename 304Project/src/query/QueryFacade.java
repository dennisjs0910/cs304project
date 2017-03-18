package query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import row.AggregationLessonAvgAgeRow;
import row.AggregationLessonCountRow;
import row.DivisionCoachRow;
import row.DivisionCourtRow;
import row.LessonReportRow;
import row.SelectionRow;

public class QueryFacade
{
	private Connection conn;
	private boolean isAdmin;
	private String username;
	
	public QueryFacade(boolean isAdmin, String username)
	{
		this.isAdmin = isAdmin;
		this.username = username;
		this.conn = null;
	}

	
	public boolean getIsAdmin()
	{
		return this.isAdmin;
	}
	
	public String getUsername()
	{
		return this.username;
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
		s.close();
		return rows;
	}
	
	// Join query
	public List<LessonReportRow> generateReport() throws SQLException
	{
		List<LessonReportRow> rows = new ArrayList<LessonReportRow>();
		
		String query = "SELECT l.lid, l.courtid, l.l_level, e.name, tc.centreid "
				+ "FROM Lesson l, Coach c, EmployeesWorkAt e, TennisCourt t, TennisCentre tc "
				+ "WHERE l.sin = c.sin AND c.sin = e.sin AND l.courtid = t.courtid AND t.centreid = tc.centreid";
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			String lid = rs.getString(1);
			String CourtID = rs.getString(2);
			String Level = rs.getString(3);
			String CoachName = rs.getString(4);
			String CentreID = rs.getString(5);
			
			LessonReportRow row = new LessonReportRow(lid, CourtID, Level, CoachName, CentreID);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	// Division query: courts
	public List<DivisionCourtRow> getDivisionCourts() throws SQLException
	{
		String query = "SELECT DISTINCT c.cid, c.name "
				+ "FROM Customer c, Reserve r, ReservableCourt rc "
				+ "WHERE c.cid = r.cid AND r.courtid = rc.courtid "
				+ "AND NOT EXISTS ((Select rc2.courtid FROM ReservableCourt rc2) MINUS (SELECT r2.courtid FROM Reserve r2 WHERE r2.cid = c.cid))";
		
		List<DivisionCourtRow> rows = new ArrayList<DivisionCourtRow>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			String cid = rs.getString(1);
			String cname = rs.getString(2);
			
			DivisionCourtRow row = new DivisionCourtRow(cid, cname);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	// Division query: coaches who teach all lessons of a certain level
	public List<DivisionCoachRow> getDivisionCoaches(String level) throws SQLException
	{
		String query = "SELECT DISTINCT e.name, e.phone "
				+ " FROM Lesson l, Coach c, EmployeesWorkAt e "
				+ "WHERE l.SIN = c.SIN AND c.SIN = e.SIN AND l.l_level = " + level
				+ " AND NOT EXISTS ((SELECT l2.lid FROM Lesson l2 WHERE l2.l_level = l.l_level) MINUS (SELECT l3.lid FROM Lesson l3, Coach c2 where l3.SIN=c2.SIN AND c2.SIN=c.SIN))";
		
		List<DivisionCoachRow> rows = new ArrayList<DivisionCoachRow>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			String name = rs.getString(1);
			String phone = rs.getString(2);
			
			DivisionCoachRow row = new DivisionCoachRow(name, phone);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	//Aggregation: Find number of students registered in each lesson. (count)
	public List<AggregationLessonCountRow> getAggregationLessonCount() throws SQLException
	{
		String query = "SELECT count(*), l.lid, l.l_level "
				+ "FROM Lesson l, Enrolled_In e "
				+ "WHERE l.lid = e.lid "
				+ "GROUP BY l.lid, l.l_level";

		List<AggregationLessonCountRow> rows = new ArrayList<AggregationLessonCountRow>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int numStudents = rs.getInt(1);
			String lid = rs.getString(2);
			String level = rs.getString(3);
			
			AggregationLessonCountRow row = new AggregationLessonCountRow(numStudents, lid, level);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	// Aggregation: Find the average age of students registered in each lesson.
	public List<AggregationLessonAvgAgeRow> getAggregationAvgAgeLesson() throws SQLException
	{
		String query = "SELECT avg(c.age), l.lid, l.l_level "
				+ "FROM Lesson l, Enrolled_In e, Customer c "
				+ "WHERE l.lid = e.lid AND e.cid = c.cid "
				+ "GROUP BY l.lid, l.l_level";

		List<AggregationLessonAvgAgeRow> rows = new ArrayList<AggregationLessonAvgAgeRow>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int avgAge = rs.getInt(1);
			String lid = rs.getString(2);
			String level = rs.getString(3);
			
			AggregationLessonAvgAgeRow row = new AggregationLessonAvgAgeRow(avgAge, lid, level);
			rows.add(row);
		}
		s.close();
		return rows;
	}
}
