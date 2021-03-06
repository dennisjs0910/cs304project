package row;

public class SelectionRow
{
	String lid;
	int courtid;
	String sin;
	String level;
	
	public SelectionRow(String lid, int courtid, String sin, String level)
	{
		this.lid = lid;
		this.courtid = courtid;
		this.sin = sin;
		this.level = level;
	}
	
	public String getLID()
	{
		return this.lid;
	}
	
	public int getCourtID()
	{
		return this.courtid;
	}
	
	public String getSin()
	{
		return this.sin;
	}
	
	public String getLevel()
	{
		return this.level;
	}
	
	
	@Override
	public String toString()
	{
		return "ROW lid: " + lid + "; courtid: " + courtid + "; sin: " + sin + "; level: " + level;
	}
}
