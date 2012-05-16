import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Created by Scott Traver
//Created 05/11/2012

//This calss contains data for a kml object for a wifi Placemark
public class KmlObject
{
	String name;
	String security;
	String bssid;
	int secVal;
	LinkedList<Security> secvals;
	
	//takes parsed description string (some formatting assumed) and name
	public KmlObject (String nameIn, String description)
	{
		processBSSID(description);
		if(nameIn == null)
		{
			nameIn = "";
		}
		this.name = nameIn;
		if(KmlStat.names.containsKey(nameIn))	//add name and count if not already present
		{
			int old = KmlStat.names.get(nameIn);
			old++;
			KmlStat.names.put(nameIn, old);
		}else
		{
			KmlStat.names.put(nameIn, 1);
		}
		processSecurity(description);
		KmlStat.count++;						//increase total count
	}
	
	//regular expression to parse out SSID
	public void processBSSID(String input)
	{
		this.bssid = "";
		Pattern p = Pattern.compile("[^I][^D]:..:..:..:..:..");//to escape wiggle.net formatting
		Matcher matcher = p.matcher(input);
		if(matcher.find())
		{
			this.bssid = input.substring(matcher.start(), 17);
		}
		
	}
	
	//iterative process to check for security in description string
	public void processSecurity(String input)
	{
		this.security = "";
		this.secVal = 0;
		secvals = new LinkedList<Security>();
		for(int i = 0; i < Security.values().length; i++)
		{
			if(input.contains(Security.values()[i].name()))
			{
				secvals.add(Security.values()[i]);
				this.security += " " + Security.values()[i].name();
				int old = KmlStat.security.get(Security.values()[i].name());
				old++;
				KmlStat.security.put(Security.values()[i].name(), old);
				this.secVal++;
			}
		}
		if(this.security == "")			//log insecure network
			KmlStat.unsecure++;
	}
	
	public String toString()
	{
		return "Name: " + this.name;
		//return "Name: " + this.name;
	}
}
