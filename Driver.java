/**
	This file is brought to you by:
	Charles Corbett
	Michael Bridges
	
	Miscellaneous Information:
	Used by: 	Bank.java;Teller.java
	Requires: 	N/A
	Purpose:	set everything in motion.  Please note that there is functionality to this driver!
*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Driver
{
	/**
	 * Main method, gets all input from user and creates a bank which will run the simulator
	 * and prints out the bank output.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		ArrayList<String> input = FileEcho.getLinesFromFile();
		int numTellers = getNumTellers();
		boolean hasDriveThru = getDriveThru();
		
		Bank bank = new Bank(numTellers, input, hasDriveThru);
		System.out.println(bank);

		
	}
	
	/**
	 * Returns a boolean, true if the user entered yes, false if the user entered no
	 * @return boolean true if input is yes, false if input is no
	 * @throws IOException
	 */
	private static boolean getDriveThru() throws IOException {
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		
		System.out.println("Enter yes if you want a drive-thru, enter no if you don't want a drive thru. ");
		String in = br.readLine();
		boolean ret;
		
		try
		{
			if(in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y"))
			{
				ret = true;
			}
			else if(in.equalsIgnoreCase("no") || in.equalsIgnoreCase("n"))
			{
				ret = false;
			}
			else
			{
				throw new IllegalArgumentException();
			}
		}
		catch(IllegalArgumentException iae)
		{
			System.out.println("Please enter yes or no.");
			ret = getDriveThru();
		}
		
		return ret;
	}


	/**
	 * Returns the user input as an int
	 * @return the user input as an int
	 * @throws IOException
	 */
	public static int getNumTellers() throws IOException
	{
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		
		System.out.println("How many tellers are working today? ");
		//get user input for a
		String in = br.readLine();
		int ret;
		
		try
		{
			ret = Integer.parseInt(in);
		}
		catch(NumberFormatException nfe)
		{
			System.out.println("Please enter a number.");
			ret = getNumTellers();
		}
		return ret;
	}

}
