package row;

public class Customer {
	private String cid;
	private String ccnumber;
	private String phone;
	private String name;
	private String address;
	private int age;
	
	public Customer(String cid, String ccnumber, String phone, String name, String address, int age) {
		this.cid = cid;
		this.ccnumber = ccnumber.trim();
		this.phone = phone.trim();
		this.name = name.trim();
		this.address = address.trim();
		this.age = age;
	}
	public String getCcnumber(){
		return ccnumber;
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getAge(){
		return Integer.toString(age);
	}
	
	public String getName(){
		return name;
	}
	
	public String getCid(){
		return cid;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
}
