package row;

public class Coach
{
	private String SIN;
	private String cert_id;
	
	
	public Coach(String sin, String certificationID)
	{
		SIN = sin;
		cert_id = certificationID;
	}
	
	public String getSIN()
	{
		return SIN;
	}
	
	public String getCertificationID()
	{
		return cert_id;
	}
}
