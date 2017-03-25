package row;

public class AggregationLessonAgeRow {
	
	private int avgAge;
	private String lid;
	private String level;
	
	public AggregationLessonAgeRow(int avgAge, String lid, String level)
	{
		this.avgAge = avgAge;
		this.lid = lid;
		this.level = level;
	}
	
	public int getAverageAge()
	{
		return this.avgAge;
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
