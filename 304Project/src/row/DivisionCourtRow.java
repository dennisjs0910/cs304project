package row;

public class DivisionCourtRow {
	
	private String cid;
	private String cname;
	
	public DivisionCourtRow(String cid, String cname)
	{
		this.cid = cid;
		this.cname = cname;
	}
	
	public String getCID()
	{
		return cid;
	}
	
	public String getCName()
	{
		return cname;
	}

}
