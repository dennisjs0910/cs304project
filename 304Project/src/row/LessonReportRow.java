package row;

public class LessonReportRow {
	
	private String LID;
	private String CourtID;
	private String Level;
	private String CoachName;
	private String CentreID;
	
	public LessonReportRow(String lid, String courtid, String level, String coachname, String centreid)
	{
		LID = lid;
		CourtID = courtid;
		Level = level;
		CoachName = coachname.trim();
		CentreID = centreid;
	}
	
	public String getLID()
	{
		return LID;
	}
	
	public String getCourtID()
	{
		return CourtID;
	}
	
	public String getLevel()
	{
		return Level;
	}
	
	public String getCoachName()
	{
		return CoachName;
	}
	
	public String getCentreID()
	{
		return CentreID;
	}
	
	@Override
	public String toString()
	{
		return "ROW lid: " + LID + "; courtid: " + CourtID + "; level: " + Level + "; coachname: " + CoachName + "; CentreID: " + CentreID;
	}
}
