package row;

import java.sql.Date;

public class ReservableTennisCourt {
	private String courtId;
	private String surface;
	private String centreId;
	private Date reserveDate;
	private Date startTime;
	private Date endTime;
	
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
}