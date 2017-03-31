package row;

import java.sql.Date;

public class ReservableTennisCourt {
	private String courtId;
	private String surface;
	private String centreId;
	private String reserveDate;
	private String startTime;
	private String endTime;
	
	public ReservableTennisCourt(String courtId, String surface, String centreId) {
		this.courtId = courtId;
		this.surface = surface;
		this.centreId = centreId;
	}
	
	public String getCourtId() {
		return courtId;
	}
	public String getSurface(){
		return surface;
	}
	
	public String getCentreId(){
		return centreId;
	}
	public void setDate(String reserveDate){
		this.reserveDate = reserveDate;
	}
	
	public void setStart(String startTime){
		this.startTime = startTime;
	}
	
	public void setEnd(String endTime){
		this.endTime = endTime;
	}
	public String getDate(){
		return reserveDate.substring(0,10);
	}
	
	public String getStart(){
		return startTime.substring(10,16);
	}
	public String getEnd(){
		return endTime.substring(10,16);
	}
	
}












