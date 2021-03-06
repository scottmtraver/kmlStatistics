//Created by Scott Traver
//Created 05/21/2012

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//This file contains an object containing metadata about a kml file's wifi contents
public class kmlStatMeta
{
	public HashMap<String, Integer> names;
	public HashMap<String, Integer> security;
	public int count;
	public int unsecureCount;
	public String unsecureKml;
	
	//constructor
	public kmlStatMeta(String filepath)
	{
		//initialize fields
		this.count = 0;
		this.unsecureKml = "";
		this.security = new HashMap<String, Integer>();
		this.names = new HashMap<String, Integer>();
		for(int i = 0; i < Security.values().length; i++)		//fill security array
		{
			this.security.put(Security.values()[i].name(), 0);
		}
		//process file
		try
		{
			processKMLFile(filepath);
		}catch (Exception e)
		{
			System.out.println("File Exception");
			System.out.println(e.getMessage());
		}
		
	}
	public String unsecureKml()//create new kml placemarks based on criteria (such as no security)
	{
		return this.unsecureKml;
	}
	
	public String metadata()//simple metadata
	{
		String ret = "";
		ret += "Count:" + this.count;
		double percentage = ((double)this.unsecureCount / (double)this.count) * 100;
		ret += "\nUnsecure: " + this.unsecureCount + " : " + percentage;
		return ret;
	}
	
	
	public String securityStats()//get the security statistics
	{
		String ret = "";
		for(int i = 0; i < Security.values().length; i++)		//fill security array
		{
			double percentage = ((double)this.security.get(Security.values()[i].name()) / this.count) * 100;
			ret += "\n" + Security.values()[i].name() +
					" : " + this.security.get(Security.values()[i].name()) + 
					" : " + percentage;
		}
		return ret;
	}
	public String ssidStats()//get the name statistics
	{
		String ret = "";
		ret += "Unique Name Count : " + this.names.keySet().size();
		ret += "\nDuplicate Names:";
		for(int i = 0; i < this.names.keySet().size(); i++)
		{
			String temp = (String)this.names.keySet().toArray()[i];
			if(this.names.get(temp) > 1)
			{
				ret += "\n" + temp + " : " + this.names.get(temp);
			}
		}
		return ret;
	}
	//file processing
	void processKMLFile(String path) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException
	{
		File fXmlFile = new File(path);									//create file, throws exception on unopened
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
 
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("Placemark");
 
		
		//set up kml build for new file
		//Modeled off wiggle.net's kml file
		this.unsecureKml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		this.unsecureKml = this.unsecureKml + "<kml xmlns=\"http://earth.google.com/kml/2.2\"><Document>\n";
		//kml list
		
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			  //System.out.println(temp);
		      Element eElement = (Element) nNode;
				String tempName = getTextValue(eElement, "name");
		      if(this.names.containsKey(tempName))	//add name and count if not already present
				{
					int old = this.names.get(tempName);
					old++;
					this.names.put(tempName, old);
				}else
				{
					this.names.put(tempName, 1);
				}
				//process security
		      String secStr = "";
		      String tempDesc = getTextValue(eElement, "description");
				for(int i = 0; i < Security.values().length; i++)
				{
					if(tempDesc.contains(Security.values()[i].name()))
					{
						int old = this.security.get(Security.values()[i].name());
						old++;
						this.security.put(Security.values()[i].name(), old);
						secStr += Security.values()[i].name();
					}
				}
				if(secStr == "")			//log insecure network
				{
					saveUnsecureKml(eElement);
				}
		      
				this.count++;						//increase total count
		   }
		   
		   
		}
		//close kml build file
		   this.unsecureKml = this.unsecureKml + "</Document></kml>";
	}
	public void saveUnsecureKml(Element ele)
	{
		String name = getTextValue(ele, "name");
		String desc = getTextValue(ele, "description");
		String coor = getTextValue(ele, "coordinates");
		String making = "<Placemark><name><![CDATA[" + name + "]]></name>";
		making = making + "<description><![CDATA[" + desc + "]]></description>";
		making = making + "<Point><coordinates>" + coor + "</coordinates></Point></Placemark>\n";
		this.unsecureKml = this.unsecureKml + making;
		this.unsecureCount++;
	}
	
	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			if(el.getFirstChild() != null)
			{
				textVal = el.getFirstChild().getNodeValue();
			}
		}

		return textVal;
	}
}
