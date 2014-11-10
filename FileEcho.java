/**
	This file is brought to you by:
	Charles Corbett
	Michael Bridges
	With help from:
	Jason Sawin
	
	Miscellaneous Information:
	Used by: 	Driver.java
	Requires: 	N/A
	Purpose:	This class is used to locate a file and parse the contents of said file into a usable format, in this case, an ArrayList<String>
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;


/**
 * This program demonstrates how to read from text files.  It brings
 * up a file-selection dialog box and, if the user selects a file,
 * copies the file to the screen, one line at a time.
 */
public class FileEcho
{
    public static ArrayList<String> getLinesFromFile()
	{
        // Create a file-selection dialog object
        JFileChooser chooser = new JFileChooser();
        ArrayList<String> lines = new ArrayList<String>();
        
        try
		{   
            // Display the dialog, and wait for return value.  If they cancel
            // out of the selection, throw an error -- no file to read
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                throw new Error("Input file not selected");
            
            // Grab the selected File info
            File inFile = chooser.getSelectedFile();
            
            // Create a scanner, and attach it to the file.  Loop through
            // line at a time and print the contents to the screen.
            Scanner fileScanner = new Scanner(inFile);
            while(fileScanner.hasNext())
			{
                String line = fileScanner.nextLine();
                lines.add(line);
            }//while(fileScanner.hasNext())
            fileScanner.close();
        }//try
		catch (FileNotFoundException e)
		{
            System.err.println("Data file not found."); 
        }//catch (FileNotFoundException e)
		catch (Exception e)
		{
            System.err.println("A mysterious error occurred.");
            e.printStackTrace(System.err);
        }//catch (Exception e)
        return lines;
    }//getLinesFromFile()
	
}//class FileEcho