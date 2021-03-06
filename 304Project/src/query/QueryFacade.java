package query;
import java.util.Date;
import java.lang.reflect.Array;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import row.AggregationLessonAgeRow;
import row.AggregationLessonCountRow;
import row.Coach;
import row.Customer;
import row.DivisionCoachRow;
import row.DivisionCourtRow;
import row.Employee;
import row.LessonReportRow;
import row.NestedAggregationRow;
import row.SelectionRow;
import row.ReservableTennisCourt;

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
			int courtid = rs.getInt("courtid");
			String sin = rs.getString("sin");
			String level = rs.getString("l_level");
			SelectionRow selectionRow = new SelectionRow(lid, courtid, sin, level);
			rows.add(selectionRow);
		}
		s.close();
		return rows;
	}
	
	public List<String> getSelectionCustomer(String attr, String age, String condition) throws SQLException
	{
		String query = "SELECT " + attr + " FROM Customer WHERE age " + condition + age;

		List<String> rows = new ArrayList<String>();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		
		while (rs.next())
		{
			rows.add(rs.getString(1));
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
				int CourtID = rs.getInt(2);
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
	
	// Aggregation: Find the average/max/min age of students registered in each lesson.
	public List<AggregationLessonAgeRow> getAggregationAgeLesson(String agg) throws SQLException
	{
		String query = "SELECT " +  agg + "(c.age), l.lid, l.l_level "
				+ "FROM Lesson l, Enrolled_In e, Customer c "
				+ "WHERE l.lid = e.lid AND e.cid = c.cid "
				+ "GROUP BY l.lid, l.l_level";

		List<AggregationLessonAgeRow> rows = new ArrayList<AggregationLessonAgeRow>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int avgAge = rs.getInt(1);
			String lid = rs.getString(2);
			String level = rs.getString(3);
			
			AggregationLessonAgeRow row = new AggregationLessonAgeRow(avgAge, lid, level);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	public List<AggregationLessonAgeRow> getNestedAggregationAgeLesson(String agg, String nestedAgg) throws SQLException
	{
		String view = "create view nested (aggAge, lid, l_level) as SELECT " +  agg + "(c.age) as aggAge, l.lid, l.l_level "
				+ "FROM Lesson l, Enrolled_In e, Customer c "
				+ "WHERE l.lid = e.lid AND e.cid = c.cid "
				+ "GROUP BY l.lid, l.l_level";

		List<AggregationLessonAgeRow> rows = new ArrayList<AggregationLessonAgeRow>();
		
		Statement s = conn.createStatement();
		try
		{
			s.executeQuery("drop view nested");
		}
		catch (SQLException e)
		{
			System.out.println("view already dropped");
		}
		s.executeQuery(view);
		
		
		String query = "SELECT aggAge, lid, l_level FROM nested t WHERE t.aggAge = (SELECT " + nestedAgg + "(aggAge) FROM nested)";
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int avgAge = rs.getInt(1);
			String lid = rs.getString(2);
			String level = rs.getString(3);
			
			AggregationLessonAgeRow row = new AggregationLessonAgeRow(avgAge, lid, level);
			rows.add(row);
		}
		try
		{
			s.executeQuery("drop view nested");
		}
		catch (SQLException e)
		{
			System.out.println("view already dropped");
		}
		
		s.close();
		return rows;
	}
	
	// Nested aggregation: Find the customer(s) with the max/min number of court reservations of any customer
	public List<NestedAggregationRow> getNestedAggregation(String minMax) throws SQLException
	{
		String view = "create view temp(cid, name, numRes) as select c.cid, c.name, count(*) as numRes FROM Reserve r, Customer c WHERE r.cid=c.cid group by c.cid, c.name";

		Statement s = conn.createStatement();
		try
		{
			s.executeQuery("drop view temp");
		}
		catch (SQLException e)
		{
			System.out.println("view already dropped");
		}
		s.executeQuery(view);
		
		String query = "select Temp.cid, Temp.name, Temp.numRes FROM Temp WHERE Temp.numRes = (SELECT " + minMax + "(Temp.numRes) FROM Temp)";

		System.out.println("Executing temp query");
		
		List<NestedAggregationRow> rows = new ArrayList<NestedAggregationRow>();
		
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			String cid = rs.getString(1);
			String cname = rs.getString(2);
			int numReservations = rs.getInt(3);
			
			NestedAggregationRow row = new NestedAggregationRow(cid, cname, numReservations);
			rows.add(row);
		}
		System.out.println("Dropping view");
		s.executeQuery("Drop view temp");
		
		s.close();
		return rows;
	}
	
	/*
	 * Deletes an employee with the associated sin.
	 * Return true if delete was successful; if there was no record to delete that
	 * matched the specified sin, return false.
	 */
	public boolean deleteEmployee(String sin) throws SQLException
	{
		String query = "DELETE FROM EmployeesWorkAt e "
				+ "WHERE e.sin = " + sin;
		
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	/*
	 * Deletes a reservation for the given cid and courtid.
	 * Returns true if the delete was successful. Returns false if no row matched
	 * the given info.
	 */
	public boolean createReservation(String cid, String courtid, String rDate, String start, String end) throws SQLException
	{
		
		String query = "INSERT INTO Reserve values ('"+cid+"','"+courtid+"','"+rDate+"',TO_DATE('"+start+"','hh24:mi:ss'),TO_DATE('"+end+"','hh24:mi:ss'))";
		System.out.println(query);
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	/*
	 * Deletes a reservation for the given cid and courtid.
	 * Returns true if the delete was successful. Returns false if no row matched
	 * the given info.
	 */
	public boolean deleteReservation(String cid, String cancelCourtId, String date, String start) throws SQLException
	{
		String query = "DELETE FROM Reserve r "
				+ "WHERE r.cid = " + cid + " AND r.courtid = " + cancelCourtId+
				" AND  r.r_date = '" + date + 
				"' AND r.starttime = TO_DATE('" + start +"','hh24:mi:ss')";
		System.out.println(query);
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	/*
	 * Adds a Customer to a lesson. If insertion was successful returns true
	 * else if customer was already enrolled it will return false.
	 */
	public boolean enrollCustomerLesson(String cid, String lid) throws SQLException {
		String query = "INSERT INTO Enrolled_In values ('"+ lid +"','"+cid+ "')";
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}
		return true;
	}
	
	/*
	 * Deletes a tuple in Enrolled_In table for the given cid and lid
	 * Returns true if the delete was successful. Returns false if no row matched
	 * the given info.
	 */
	public boolean unenrollCustomerLessons(String cid, String lid) throws SQLException {
		String query = "DELETE FROM Enrolled_In e "
				+ "WHERE e.cid = " + cid + " AND e.lid = " + lid;
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}
		return true;
	}
	
	
	/*
	 * Returns true if customer is already enrolled in lesson
	 */
	public boolean customerEnrollsIn(String cid, String lid) throws SQLException {
		String query = "SELECT COUNT(*) " +
					   "FROM Enrolled_In e " +
					   "WHERE e.cid = " + cid +
					   "AND e.lid = " + lid;
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int count = Integer.parseInt(rs.getString(1));
			if (count <= 0) {
				return false;
			}
			
		}
		s.close();
		return true;
	}
	
	/*
	 * Update the name of the customer with the given cid. Returns
	 * true if successful. Returns false if no customer with cid exists.
	 */
	public boolean updateCustomerName(String cid, String name) throws SQLException
	{
		String query = "UPDATE Customer "
				+ "SET Name = '" + name
				+ "' WHERE cid = " + cid;
		
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	public boolean updateCustomerCard(String cid, String ccnumber) throws SQLException
	{
		String query = "UPDATE Customer "
				+ "SET CCNumber = '" + ccnumber
				+ "' WHERE cid = " + cid;
		
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	/*
	 * Update the phone of the customer with the given cid. Returns
	 * true if successful. Returns false if no customer with cid exists.
	 */
	public boolean updateCustomerPhone(String cid, String phone) throws SQLException
	{
		String query = "UPDATE Customer "
				+ "SET Phone = '" + phone
				+ "' WHERE cid = " + cid;
		
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	/*
	 * Update the address of the customer with the given cid. Returns
	 * true if successful. Returns false if no customer with cid exists.
	 */
	public boolean updateCustomerAddress(String cid, String address) throws SQLException
	{
		String query = "UPDATE Customer "
				+ "SET Address = '" + address
				+ "' WHERE cid = '" + cid +"'";
		
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	/*
	 * Update the age of the customer with the given cid. Returns
	 * true if successful. Returns false if no customer with cid exists.
	 * 
	 * Exception thrown if the value of age violates the check constraint
	 * (age is not between 0 and 120).
	 */
	public boolean updateCustomerAge(String cid, int age) throws SQLException
	{
		String query = "UPDATE Customer "
				+ "SET Age = '" + age
				+ "' WHERE cid = " + cid;
		
		Statement s = conn.createStatement();
		int rowsChanged = s.executeUpdate(query);
		
		if (rowsChanged <= 0)
		{
			return false;
		}

		return true;
	}
	
	public Customer getCustomer(String custid, String custPhone) throws SQLException {
		String query = "SELECT * "
				+ "FROM Customer c "
				+ "WHERE c.cid = " + custid
				+ " AND c.phone = " + custPhone;
				
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		
		Customer cus = null;
		
		while (rs.next())
		{
			String cid = rs.getString(1);
			String ccnumber = rs.getString(2);
			String phone = rs.getString(3);
			String cname = rs.getString(4);
			String address = rs.getString(5);
			int age = Integer.parseInt(rs.getString(6));
			cus = new Customer(cid,ccnumber,phone,cname,address,age);
		}
		s.close();
		
		return cus;
	}
	
	public Employee getAdmin(String adminSin, String ephone) throws SQLException {
		String query = "SELECT * "
				+ "FROM EmployeesWorkAt e "
				+ "WHERE e.sin = " + adminSin
				+ " AND e.phone = " + ephone;
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		
		Employee admin = null;
		
		while (rs.next())
		{
			
			String sin = rs.getString(1);
			String phone = rs.getString(2);
			String name = rs.getString(3);
			String address = rs.getString(4);
			String centreID = rs.getString(5);
			
			admin = new Employee(sin, phone, name, address ,centreID);
		}
		s.close();
		
		return admin;
	}

	public List<Employee> getEmployees() throws SQLException
	{
		String query = "SELECT * FROM EmployeesWorkAt";
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		
		List<Employee> employees = new ArrayList<Employee>();
		
		while (rs.next())
		{
			
			String sin = rs.getString(1);
			String phone = rs.getString(2);
			String name = rs.getString(3);
			String address = rs.getString(4);
			String centreID = rs.getString(5);
			
			Employee emp = new Employee(sin, phone, name, address ,centreID);
			employees.add(emp);
		}
		s.close();
		
		return employees;
	}
	
	public List<Coach> getCoaches() throws SQLException
	{
		String query = "SELECT * FROM Coach";
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		
		List<Coach> rows = new ArrayList<Coach>();
		while(rs.next())
		{
			String sin = rs.getString(1);
			String certID = rs.getString(2);
			
			Coach coach = new Coach(sin, certID);
			rows.add(coach);
		}
		
		s.close();
		
		return rows;
	}

	/*
	 * gets all the TennisCourts there is a typo for surface type
	 */
	public List<ReservableTennisCourt> getReservableTennisCourts() throws SQLException {
		String query = "SELECT rc.courtid, t.suface_type, t.centreId"+
						" FROM TennisCourt t, ReservableCourt rc" +
						" WHERE t.courtid = rc.courtid";

		List<ReservableTennisCourt> rows = new ArrayList<ReservableTennisCourt>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int courtId = rs.getInt(1);
			String surfaceT = rs.getString(2);
			String centreId = rs.getString(3);
			
			ReservableTennisCourt row = new ReservableTennisCourt(Integer.toString(courtId),surfaceT,centreId);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	public List<ReservableTennisCourt> getReservedTennisCourts() throws SQLException {
		String query = "SELECT rc.courtid, t.suface_type, t.centreId, r.r_date, r.starttime, r.endtime"+
						" FROM TennisCourt t, ReservableCourt rc, Reserve r" +
						" WHERE t.courtid = rc.courtid AND r.courtid = rc.courtid";

		List<ReservableTennisCourt> rows = new ArrayList<ReservableTennisCourt>();
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int courtId = rs.getInt(1);
			String surfaceT = rs.getString(2);
			String centreId = rs.getString(3);
			String rdate = rs.getString(4);
			String rstart = rs.getString(5);
			String rend = rs.getString(6);
			
			ReservableTennisCourt row = new ReservableTennisCourt(Integer.toString(courtId),surfaceT,centreId);
			row.setDate(rdate);
			row.setStart(rstart);
			row.setEnd(rend);
			rows.add(row);
		}
		s.close();
		return rows;
	}
	
	/*
	 * returns the start and end time of reservation for the specified court
	 * the sql table start and end date seem to be mixed up
	 */
	public List<Date[]> getCourtReservation(String courtId) throws SQLException{
		String query = "SELECT r.r_date, r.starttime, r.endtime"+
				" FROM ReservableCourt rc, Reserve r" +
				" WHERE r.courtid = rc.courtid AND r.courtid = " + courtId;
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		List<Date[]> reservationDates = new ArrayList<>();
		while (rs.next())
		{
			Date reserveDate = rs.getDate(1);
			Date endTime = rs.getTime(2);
			Date startTime = rs.getTime(3);
			Date[] reservationDate = new Date[]{reserveDate, startTime,endTime};
			reservationDates.add(reservationDate);
			
		}
		s.close();
		
		return reservationDates;
	}
	
	
	public boolean customerReserved(String cid, String courtId, String date) throws SQLException {
		String query = "SELECT COUNT(*) " +
					   "FROM Reserve r " +
					   "WHERE r.cid = " + cid +
					   "AND r.courtid = " + courtId +
					   "AND r.r_date = '" + date +"'";
		
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next())
		{
			int count = Integer.parseInt(rs.getString(1));
			if (count <= 0) {
				return false;
			}
			
		}
		s.close();
		return true;
	}
	
}
