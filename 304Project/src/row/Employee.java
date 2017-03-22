package row;

public class Employee {
	private String sin;
	private String phone; 
	private String name;
	private String address;
	private String centreID;
	
	public Employee(String sin, String phone, String name, String address, String centreID){
		this.sin = sin;
		this.phone = phone;
		this.name = name.trim();
		this.address = address.trim();
		this.centreID = centreID;
	}
	
	public String getSin()
	{
		return sin;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getCentreID()
	{
		return centreID;
	}
}
