package row;

public class AggregationLessonCountRow {
	
	private int numStudents;
	private String lid;
	private String level;
	
	public AggregationLessonCountRow(int numStudents, String lid, String level)
	{
		this.numStudents = numStudents;
		this.lid = lid;
		this.level = level;
	}
	
	public int getNumStudents()
	{
		return this.numStudents;
	}
	
	public String getLID()
	{
		return this.lid;
	}
	
	public String getLevel()
	{
		return this.level;
	}

}
