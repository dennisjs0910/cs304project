import java.sql.SQLException;
import java.util.List;

import query.QueryFacade;
import row.AggregationLessonAgeRow;
import row.AggregationLessonCountRow;
import row.Customer;
import row.NestedAggregationRow;
import row.SelectionRow;
import row.ReservableTennisCourt;

public class testQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			QueryFacade q = new QueryFacade(false, "123456");
			q.connect();
			System.out.println("Connection successful");
			//List<SelectionRow> result = q.getSelectionQuery("001");
			//List<AggregationLessonCountRow> result = q.getAggregationLessonCount();
			//List<AggregationLessonAvgAgeRow> result = q.getAggregationAvgAgeLesson();
			//List<NestedAggregationRow> result = q.getNestedAggregation();
			//boolean result = q.deleteEmployee("202020202");
			//boolean result = q.deleteReservation("123456", 7);
			Customer a = q.getCustomer("123456", "6048221111");
			//q.getCourtReservation("000000007");
			System.out.println(a);
		
			
			
			
		}
		catch (SQLException e)
		{
			System.out.println("Connection Failed");
			e.printStackTrace();
		}
	}

}
