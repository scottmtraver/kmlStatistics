import java.util.HashMap;

//Created by Scott Traver
//Created 05/15/2012

//this class contains dictionarys and overall statistics about a kml file's placemark contents
public class KmlStat
{
	public static HashMap<String, Integer> names;
	public static HashMap<String, Integer> security;
	public static int count;
	public static int unsecure;
	
	//constructor
	public static void initialize()
	{
		KmlStat.count = 0;
		KmlStat.security = new HashMap<String, Integer>();
		KmlStat.names = new HashMap<String, Integer>();
		for(int i = 0; i < Security.values().length; i++)		//fill security array
		{
			KmlStat.security.put(Security.values()[i].name(), 0);
		}
	}
	public static void metadata()
	{
		System.out.println("Count:" + KmlStat.count);
		double percentage = ((double)KmlStat.unsecure / (double)KmlStat.count) * 100;
		System.out.println("Unsecure: " + KmlStat.unsecure + " : " + percentage);
	}
	
	
	public static void securityStats()//get the security statistics
	{
		for(int i = 0; i < Security.values().length; i++)		//fill security array
		{
			double percentage = ((double)KmlStat.security.get(Security.values()[i].name()) / KmlStat.count) * 100;
			System.out.println(Security.values()[i].name() +
					" : " + KmlStat.security.get(Security.values()[i].name()) + 
					" : " + percentage);
		}
	}
	public static void ssidStats()//get the security statistics
	{
		System.out.println("Unique Name Count : " + KmlStat.names.keySet().size());
		System.out.println();
		System.out.println("Duplicate Names:");
		for(int i = 0; i < KmlStat.names.keySet().size(); i++)
		{
			String temp = (String)KmlStat.names.keySet().toArray()[i];
			if(KmlStat.names.get(temp) > 1)
			System.out.println(temp + " : " + KmlStat.names.get(temp));
		}
	}
}
