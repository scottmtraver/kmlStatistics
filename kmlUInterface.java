//Created by Scott Traver
//Created 05/21/2012

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.*;

//This class contains the entrypoint and User Interface for .kml wifi file analysis and comparisons
//This GUI covers open file operations, security settings, analysis controls
//a kmlPanel is generated for each file processed for ease of comparison
public class kmlUInterface extends JFrame implements ActionListener
{
	public JMenuBar menu;												//menubar for utility options
	public JTextArea workspace;											//reference to Jtext area for text display
	public JFrame me;													//reference handle for top level referencing
	public kmlStatMeta data;											//data attached to this frame
	
	public kmlUInterface ()
	{
		super ("KmlAnalyser");											//construct main JFrame
		
		me = this;														//set top level reference
		
		setBackground(Color.white);										//set background color		
		setBounds(100,100,500,700);										//set frame bounds
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);					//initialize close window on close condition
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());								//add border layout
		
		workspace = new JTextArea();
		
		JScrollPane sp = new JScrollPane(workspace);
		
		
		cp.add(sp);
		//MenuBar
		JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
 
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the first menu.
        menu = new JMenu("Save");
        menuBar.add(menu);
 
        //JMenu items
        menuItem = new JMenuItem("Save Unsecure KML");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        this.setJMenuBar(menuBar);
		//EndMenuBar Creation
		
		
		
		//System.out.println("source:" + e.getSource().toString());//DIAGNOSTIC ONLY
		System.out.println("open Action");
		JFileChooser opener = new JFileChooser();				//create file chooser
		class TXTFileFilter extends javax.swing.filechooser.FileFilter
		{
		    public boolean accept(File f)
		    {
		        return f.isDirectory() || f.getName().toLowerCase().endsWith(".kml");
		    }
		    
		    public String getDescription()
		    {
		        return ".kml Files";
		    }
		}
		opener.setFileFilter(new TXTFileFilter());					//set to filter to .txt files
		//opener.setCurrentDirectory();//set current path
		opener.showOpenDialog(null);								//show open dialog
		File fin = opener.getSelectedFile();						//get selected file if applicable
		if(fin != null)												//if a file was selected
		{
			data = new kmlStatMeta(fin.getAbsolutePath());			//check visibility of elements in list
			//workspace.setText(data.metadata() + "\n" + data.securityStats() + "\n\n" + data.ssidStats());
			workspace.setText(data.unsecureCount + "\n" + data.unsecureKml());
		}		
	}
	//SelfActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser opener = new JFileChooser();				//create file chooser
		class TXTFileFilter extends javax.swing.filechooser.FileFilter
		{
		    public boolean accept(File f)
		    {
		        return f.isDirectory() || f.getName().toLowerCase().endsWith(".kml");
		    }
		    
		    public String getDescription()
		    {
		        return ".kml Files";
		    }
		}
		opener.setFileFilter(new TXTFileFilter());				//set to filter to .txt files
		//opener.setCurrentDirectory();//set current path
		opener.showOpenDialog(null);							//show open dialog
		File fout = opener.getSelectedFile();					//get selected file if applicable
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(fout);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(this.data.unsecureKml);
			  //Close the output stream
			  out.close();
		  }catch (Exception ex){//Catch exception if any
			  System.err.println("Error: " + ex.getMessage());
		  }
	}
	
	//***** MAIN *****
	public static void main (String [] args)
	{
		kmlUInterface show = new kmlUInterface();
		show.setVisible(true);
	}
}
