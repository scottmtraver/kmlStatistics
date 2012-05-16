//Created by Scott Traver
//Created 05/11/2012

import java.util.*;
import java.beans.XMLDecoder;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//This program reads in a file and analyzes kml geodata of wifi networks
//Versions
//	05/11/2012	-	Input features (input file parse and read)
//					Provides statistics

public class kmlstats
{
	public static void main (String [] args)
	{
		KmlStat.initialize();
		System.out.println("Program Begin");
		//get/read file
		Scanner kb = new Scanner(System.in);
		
		//Adapted from <http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/>
		try {			
			File fXmlFile = new File("");
			boolean done = false;
			while(!done)
			{
				System.out.print("Enter Filename:");
				kb.nextLine();
				fXmlFile = new File(kb.nextLine());
				if(fXmlFile.canRead())
				{
					done = true;
				}
			}
			
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
	 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("Placemark");
	 
			//kml list
			LinkedList<KmlObject> list = new LinkedList<KmlObject>();
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
			   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				  //System.out.println(temp);
			      Element eElement = (Element) nNode;
			      //System.out.println("Name: " + getTextValue(eElement, "name"));
			      list.add(new KmlObject(getTextValue(eElement, "name"), getTextValue(eElement, "description")));
	 
			   }
			}
						
			Stats(list);
		  } catch (Exception e) {
			e.printStackTrace();
		  }		
		
		System.out.println("Program Complete");
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
	 
	 private static void Stats(LinkedList<KmlObject> list)
	 {
		//print stats
//		for(KmlObject obj : list)
//		{
//			System.out.println(obj);
//		} 
		KmlStat.metadata();
		KmlStat.securityStats();		
		KmlStat.ssidStats();
	 }
}

