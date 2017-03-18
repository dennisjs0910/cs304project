import java.sql.SQLException;
import java.util.List;

import query.QueryFacade;
import query.SelectionRow;

public class testQuery {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			QueryFacade q = new QueryFacade(false, "");
			q.connect();
			System.out.println("Connection successful");
			List<SelectionRow> result = q.getSelectionQuery("001");
			System.out.println("Selection query successful");
			System.out.println(result);
		}
		catch (SQLException e)
		{
			System.out.println("Connection Failed");
			e.printStackTrace();
		}
	}

}
