package row;

public class NestedAggregationRow {
	
	private String cid;
	private String cname;
	private int numReservations;
	
	public NestedAggregationRow(String cid, String cname, int numRes)
	{
		this.cid = cid;
		this.cname = cname;
		this.numReservations = numRes;
	}
	
	public String getCID()
	{
		return this.cid;
	}
	
	public String getCName()
	{
		return this.cname;
	}
	
	public int getMaxNumReservations()
	{
		return this.numReservations;
	}

}
