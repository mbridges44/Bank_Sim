/**
	This file is brought to you by:
	Charles Corbett
	Michael Bridges
	
	Miscellaneous Information:
	Used by: 	Driver.java
	Requires: 	Teller.java;TellerResponse.java;Transaction.java
	Purpose:	Simulate a day at a bank with a certain number of tellers, a list of customers (called transactions here), and whether or not a drive thru is being used.
*/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;


public class Bank
{

																//STATE
																
	private ArrayList<Teller> 				tellers = new ArrayList<Teller>();
	private PriorityQueue<Transaction>		events = new PriorityQueue<Transaction>();
	private ArrayList<Queue<Transaction>>	queues;
	private ArrayList<Integer>				customerWaitTime;
	private ArrayList<Integer>				tellerIdleTime;
	private int								lastTime;
	private int								curTime;
	private boolean							hasDriveThrough;
	private int								tellerAtDriveThrough = -1;
	
																//CONSTRUCTOR
	
	public Bank(int tellerCount, ArrayList<String> transactions, boolean hasDriveThrough)
	{
		this.setEvents(transactions);								//fills events with transactions built from the ArrayList of Strings handed to the Bank
		this.hasDriveThrough = hasDriveThrough;
		this.customerWaitTime = new ArrayList<Integer>();
		this.tellerIdleTime  = new ArrayList<Integer>();

		initializeQueues(tellerCount);								//initializes the Queues for each teller, as well as a slot for their idleTimes in this.tellerIdleTime.
		run();														//We call the run method in the constructor.  The run method does all the heavy lifting.
	}//Constructor(int,ArrayList<String>,boolean)
	
	
																//BEHAVIOR

//MISCELLANEOUS METHODS
	
	private void assignLineToTrans(Transaction input)
	{
		if(this.hasDriveThrough && !input.isWalkIn())				//if the bank has a drive-thru line, and this customer wants to be in it...
		{
			this.queues.get(this.queues.size()-1).offer(input);		//...offer the transaction to the last queue, which has been assigned to be the driveThru line
		}//if(this.hasDriveThrough && !input.isWalkIn())
		else														//otherwise...
		{
			int spot=this.findShortLine();							//...find the shortest line in the building...
			this.queues.get(spot).offer(input);						//...offer this transaction to that line
		}//else
		
	}//assignLineToTrans(Transaction)
		
	private Integer assignTellerToQueue(int tellerId)
	{
		Integer wait = null;															//make Integer wait with value null
		Transaction hold = null;														//make Transaction hold with value null
		if(this.hasDriveThrough && needsTeller())										//if there is a customer waiting at the driverThru
		{
			hold = this.queues.get(this.queues.size()-1).peek();						//...hold gets the front of the driveThru queue, but we have not polled the transaction yet...
			this.tellerAtDriveThrough = tellerId;										//...set this.tellerAtDriveThrough to the id of the teller whom we are assigning to a queue...
			wait = this.tellers.get(tellerId).handleTransaction(hold, this.curTime);	//...handle the transaction, setting the wait to the waitTime of the customer...

			this.events.offer(hold);													//...offer the modified transaction to the events queue...
		}//if(this.hasDriveThrough && needsTeller())
		else if(!this.queues.get(tellerId).isEmpty())									//...otherwise, if the teller has a customer in its queue...
		{
			hold = this.queues.get(tellerId).peek();									//...hold gets the front of this teller's queue, but we have not polled the transaction yet...
			wait = this.tellers.get(tellerId).handleTransaction(hold, this.curTime);	//...handle the transaction, setting the wait to the waitTime of the customer...
			this.events.offer(hold);													//...offer the modified transaction to the events queue...
		}//else if(!this.queues.get(tellerId).isEmpty())
		return wait;																	//return wait, null or otherwise
	}//assignTellerToQueue(int tellerId)
	
	
	/**
	 * Averages an arraylist of Integers and returns a double
	 * @param in arraylist of Integers to be averaged
	 * @return double the average value of the integers
	 */
	private double average(ArrayList<Integer> in)
	{
		double avg = 0;									//make double avg=0
		for(int i = 0; i < in.size(); i++)				//for each number in the input...
		{
			avg = (double)avg + (double)in.get(i);		//...add this spot in the input to avg
		}//for(int i = 0; i < in.size(); i++)
		
		return avg / in.size();							//return avg divided by the number of numbers in the input
	}//average(ArrayList<Integer>)

	/**
	 * Averages an arraylist of Doubles and returns a double
	 * @param in arraylist of Doubles to be averaged
	 * @return double the average value of the integers
	 */
	private double avg(ArrayList<Double> in)
	{
		double avg = 0;									//make double avg=0
		for(int i = 0; i < in.size(); i++)				//for each number in the input...
		{
			avg = (double)avg + (double)in.get(i);		//...add this spot in the input to avg
		}//for(int i = 0; i < in.size(); i++)
		
		return avg / in.size();							//return avg divided by the number of numbers in the input
	}//avg(ArrayList<Double>)

	
	/**
	 * Runs through each teller and looks at the status of that teller.
	 * If the teller is idle it will assign the teller to a queue if there is someone in their line.
	 * If the teller is Done it will get assigned to the Drive thru or its queue if there is someone else
	 * in line.
	 */
	private void checkTellers()
	{
		int firstIdle = -1;																//make an int firstIdle, set it to -1, for use later
		Integer wait = null;															//set wait (which we will be returning later) to null
		Queue<Integer> availableTellers = new LinkedList<Integer>();					//declare and create an empty queue of Integers
		for(int i = 0; i < tellers.size(); i++)											//for each teller...
		{
			if(firstIdle == -1 && tellers.get(i).getStatus() == TellerResponse.IDLE)	//...if we have not had an idle teller until now...
			{
				firstIdle = i;															//...change firstIdle to have the id of the current teller, that is, i...
				wait = assignTellerToQueue(i);											//...immediately assign this teller to a queue, so that this idle teller will have first crack at the drivethru, setting wait to this customer's wait time...
				if(wait != null)														//...furthermore, if there is a customer whose wait time has been set...
				{
					this.customerWaitTime.add(wait);									//...add wait to the ArrayList of customerWaitTimes...
					wait = null;														//...set wait to null again
				}//if(wait != null)

			}//if(firstIdle == -1 && tellers.get(i).getStatus() == TellerResponse.IDLE)
			else if(tellers.get(i).getStatus() == TellerResponse.DONE)					//...otherwise, if the current teller has just finished a transaction...
			{
				if(queues.get(i).isEmpty() && firstIdle < 0)							//...furthermore, if the curent teller does not have any more customers in its queue...
				{
					firstIdle = i;														//...change firstIdle to have the id of the current teller, that is, i...
					wait = assignTellerToQueue(i);										//...immediately assign this teller to a queue, so that this idle teller will have first crack at the drivethru, setting wait to this customer's wait time...
					if(wait != null)													//...furthermore, if there is a customer whose wait time has been set...
					{
						this.customerWaitTime.add(wait);								//...add wait to the ArrayList of customerWaitTimes...
						wait = null;													//...set wait to null again
					}//if(wait != null)
					
				}//if(queues.get(i).isEmpty() && firstIdle < 0)
				else																	//...otherwise...
				{
					if(i == this.tellerAtDriveThrough)									//...furthermore, if this teller just finished at the driveThru...
					{
						this.queues.get(this.queues.size()-1).poll();					//...poll the first transaction from the drive-thru queue...
						this.tellerAtDriveThrough = -1;									//...reset this.tellerAtDriveThrough to -1
					}//if(i == this.tellerAtDriveThrough)
					else																//...otherwise...
					{
						this.queues.get(i).poll();										//...poll the first transaction from the teller's queue...
					}//else
					availableTellers.offer(i);											//...add this teller to the queue of available tellers...
				}//else
				
			}//else if(tellers.get(i).getStatus() == TellerResponse.DONE)
			else if(tellers.get(i).getStatus() == TellerResponse.IDLE)					//...otherwise, if the teller is idle, but not the first idle teller...
			{
				availableTellers.offer(i);												//...add this teller to the queue of available tellers
			}//else if(tellers.get(i).getStatus() == TellerResponse.IDLE)
			
		}//for(int i = 0; i < tellers.size(); i++)
		
		while(!availableTellers.isEmpty())												//while there are still tellers available...
		{
			wait = assignTellerToQueue(availableTellers.poll());						//...take the first teller off the queue, assign him to a queue, setting wait to the customer's wait time...
			if(wait != null)															//...furthermore, if there actually was a customer whose wait time has been set...
			{
				this.customerWaitTime.add(wait);										//...add wait to the ArrayList of customerWaitTimes...
				wait = null;															//...set wait to null again
			}//if(wait != null)
			
		}//while(!availableTellers.isEmpty())
		
	}//checkTellers()
	
	/**
	 * Returns an int of the index of the shortest line
	 * @return int the index to the shortest line, not counting drive thru
	 */
	private int findShortLine()
	{
		int length=this.queues.size();							//make int length holding the number of queues at this bank
		int sizeOfShortLine=999999999;							//set int sizeOfShortLine to an unimaginably long line
		int res=-1;												//set int res (which will later be returned as the position in the ArrayList of Queues) to -1
		if(this.hasDriveThrough)								//if there is a driveThru at this bank...
		{
			length--;											//...don't count it in the search for the shortest line
		}//if(this.hasDriveThrough)
		for(int i=0;i<length;i++)								//for each queue inside the bank...
		{
			if(this.queues.get(i).size()<sizeOfShortLine)		//...if this queue is shorter than the last one...
			{
				sizeOfShortLine=this.queues.get(i).size();		//...set sizeOfShortLine to the size of this line...
				res=i;											//...set res to the id of this line, that is, i
			}//if(this.queues.get(i).size()<sizeOfShortLine)
			
		}//for(int i=0;i<length;i++)
		return res;												//return the shortestline's spot in the ArrayList of Queues
	}//findShortLine()

	
//GETTERS

	/** 
	* returns the queue of transactions events (used as clock)
	* @return this.events
	*/
	public PriorityQueue<Transaction> getEvents()
	{
		return this.events;
	}//getEvents()
	
	/** 
	* returns the previous clock stoppage
	* @return this.lastTime
	*/
	public int getLastTime()
	{
		return this.lastTime;
	}//getLastTime()

	/** 
	* returns the ArrayList of the tellers' Queues of Transactions
	* @return this.queues
	*/
	public ArrayList<Queue<Transaction>> getQueues()
	{
		return this.queues;
	}//getQueues()

	/** 
	* returns whether or not there is a drive thru at this bank
	* @return boolean this.hasDriveThrough
	*/
	public boolean isHasDriveThrough()
	{
		return this.hasDriveThrough;
	}//isHasDriveThrough()

	
//OTHER METHODS
		
	/** 
	* initializes this.tellers, this.tellerIdleTime, and this.queues
	* @param int 			tells how many tellers to "hire"
	*/
	private void initializeQueues(int tellerCount)
	{
		for(int i = 0; i < tellerCount; i++)					//for each teller we "hired" for this bank...
		{
			this.tellers.add(new Teller(i));					//...add a new teller to the ArrayList of tellers...
			this.tellerIdleTime.add(0);							//...add an idleTime of 0 to the ArrayList of tellerIdleTimes for this teller 
		}//for(int i = 0; i < tellerCount; i++)
		if(this.hasDriveThrough)								//if this bank has a drive through line...
		{
			tellerCount++;										//...increment the tellerCount fed into this method
		}//if(this.hasDriveThrough)
		this.queues = new ArrayList<Queue<Transaction>>();		//declare a new ArrayList<Queue<Transaction>> queues
		for(int i = 0; i < tellerCount; i++)					//for each teller we "hired" for this bank (maybe including a driveThru)...
		{
			this.queues.add(new LinkedList<Transaction>());		//...give them an empty queue of transactions to handle later
		}//for(int i = 0; i < tellerCount; i++)
		
	}//initializeQueues(int)
	
	/** 
	* determines if any teller is idle
	* @return boolean 
	*/
	private boolean haveIdleTeller()
	{
		for(int i = 0; i < queues.size(); i++)		//for each teller...
		{
			if(queues.get(i).isEmpty())				//...if their queue is empty...
			{
				return true;						//...stop method, returning that there is an empty queue/idle teller
			}//if(queues.get(i).isEmpty())
			
		}//for(int i = 0; i < queues.size(); i++)
		return false;								//...otherwise, i.e. none of the queues were found to be empty, return false				
	}//haveIdleTeller()
	
	/** 
	* determines if the drive thru needs a teller
	* @return boolean 
	*/
	private boolean needsTeller()
	{
		boolean res = false;									//make a boolean res = false
		if(!this.queues.get(this.queues.size()-1).isEmpty())	//if there are customers at the drive thru...
		{
			if(this.tellerAtDriveThrough < 0)					//...furthermore, if there is not a teller at the drive thru...
			{
				res = true;										//...set res=true
			}//if(this.tellerAtDriveThrough < 0)
			
		}//if(!this.queues.get(this.queues.size()-1).isEmpty())
		return res;												//return res
	}//needsTeller()

	/** 
	* does all heavy lifting of this class.  Read in-line comments for more info.
	*/
	private void run()
	{
		Transaction hold = this.events.poll();							//take the first transaction off of the events queue
		
		this.assignLineToTrans(hold);									//offer it to the correct line
		this.setTime(hold.getEventTime());								//update the banks clock, which includes some other things to be mentioned there
		this.checkTellers();											//assign a teller to the first customer
		while(!this.events.isEmpty())									//while there are still events happening today...
		{
			hold = this.events.poll();									//...take the next transaction off of the events queue
			this.setTime(hold.getEventTime());							//...update the banks clock, which includes some other things to be mentioned there

			if(hold.getEntranceTime() == hold.getEventTime())			//...if this is an entrance to the bank...
			{
				if(this.haveIdleTeller())								//...furthermore, if there is an idle teller at this time...
				{
					this.assignLineToTrans(hold);						//...offer the transaction (hold) to the correct line
					this.setTime(hold.getEventTime());					//...update the banks clock, which includes some other things to be mentioned there
					this.checkTellers();								//...assign a teller to this customer
				}//if(this.haveIdleTeller())
				else													//...otherwise (i.e. there were no idle tellers)...
				{
					this.assignLineToTrans(hold);						//...offer the transaction (hold) to the correct line
					this.setTime(hold.getEventTime());					//...update the banks clock, which includes some other things to be mentioned there
				}//else
				
			}//if(hold.getEntranceTime() == hold.getEventTime())
			else														//...otherwise (i.e. this was an end of transaction event)...
			{
				checkTellers();											//...have the tellers check their queues
			}//else
			
		}//while(!this.events.isEmpty())
		
	}//run()


//SETTERS
	
	/** 
	* sets events from a given ArrayList<String>
	* @param ArrayList<String>
	*/
	private void setEvents(ArrayList<String> lines)
	{
		for(int i = 0; i < lines.size(); i++)									//for each String in the input...
		{
			Transaction transaction;											//...declare a new Transaction transaction...
			String[] lineVals = lines.get(i).split("\t");						//...make a String[] filled with the parts of the current line split by the tab character...
			int entranceTime = Integer.parseInt(lineVals[0]);					//...set entranceTime to the first part of the line, parsed as an Integer...
			int transTime = Integer.parseInt(lineVals[1]);						//...set transTime to the second part of the line, parsed as an Integer...
			boolean isWalkIn = Boolean.parseBoolean(lineVals[2]);				//...set isWalkIn to the lastpart of the line, parsed as a Boolean...
			
			transaction = new Transaction(entranceTime, transTime, isWalkIn);	//...set transaction to a new Transaction(entranceTime, transTime, isWalkIn)
			this.events.offer(transaction);										//...offer transaction to the events queue
		}//for(int i = 0; i < lines.size(); i++)
		
	}//setEvents(ArrayList<String>)

	/** 
	* sets this.time to a given int, among other things mentioned in in-line comments
	* @param int
	*/
	private void setTime(int time)
	{
		this.lastTime = this.curTime;													//set lastTime to the old curTime
		this.curTime = time;															//set curTime to the given time
		int diff = this.curTime-this.lastTime;											//int diff is the elapsed time
		TellerResponse response=null;													//TellerResponse response gets null, to be changed later
		
		for(int i=0; i<tellers.size(); i++)												//for each teller...
		{
			tellers.get(i).updateWatch(diff, curTime);									//...update the watch...
			response = tellers.get(i).getLastResponse();								//...look at the last status of the teller...
			if(response==TellerResponse.IDLE)											//...furthermore, if the teller was idle...
			{
				this.tellerIdleTime.set(i,(Integer)this.tellerIdleTime.get(i)+diff);	//...add diff to this teller's idle time
			}//if(response==TellerResponse.IDLE)
			
		}//for(int i=0; i<tellers.size(); i++)
		
	}//setTime(int)


//SOME OTHER METHODS
	
	/** 
	* returns the standard deviation of an ArrayList of Integers
	* @param ArrayList<Integer>
	* @return double
	*/
	private double standardDev(ArrayList<Integer> in)
	{
		ArrayList<Double> diff = new ArrayList<Double>();				//make a new ArrayList<Double> diff
		
		double avg = average(in);										//make a double avg to be the average of the Integers in the input
		
		for(int i = 0; i < in.size(); i++)								//for each number in the input...
		{
			diff.add(i, ((avg - in.get(i)) *  (avg - in.get(i))));		//...add the square of the difference between the average and this value in the input
		}
		
		double result = avg(diff);										//make double result to be variance, that is the average of diff
		return Math.sqrt(result);										//return the square root of result
	}//double standardDev(ArrayList<Integer>)
	
	/** 
	* returns the standard deviation of an ArrayList of Doubles
	* @param ArrayList<Double>
	* @return double
	*/
	private double standardDevDub(ArrayList<Double> in)
	{
		ArrayList<Double> diff = new ArrayList<Double>();				//make a new ArrayList<Double> diff
		
		double avg = avg(in);											//make a double avg to be the average of the Doubles in the input
		
		for(int i = 0; i < in.size(); i++)								//for each number in the input...
		{
			diff.add(i, ((avg - in.get(i)) *  (avg - in.get(i))));		//...add the square of the difference between the average and this value in the input
		}
		
		double result = avg(diff);										//make double result to be variance, that is the average of diff
		return Math.sqrt(result);										//return the square root of result
	}//standardDevDub(ArrayList<Double>)

	/** 
	* overrides Object.toString, gives necessary information for assignment
	* @return String
	*/
	public String toString()
	{
		
		int totCust = 0;																												//make int totCust=0, that is, the total number of customers helped today
		for(int i = 0; i < tellers.size(); i++)																							//for each teller...
		{
			totCust = totCust + this.tellers.get(i).getCustomersHelped();																//...add the number of tellers that specific teller helped to totCust
		}//for(int i = 0; i < tellers.size(); i++)
		double cwsd = standardDev(this.customerWaitTime);																				//set the standard deviation of customer wait times
		double cwAvg = average(this.customerWaitTime);																					//set the average wait time
		ArrayList<Double> percents = new ArrayList<Double>();																			//make an ArrayList<Double> percents
		
		String out =  "The bank has been open for " + this.curTime + " seconds.\n";
		out = out + "Today we helped " + totCust + " customers.\n";
		out = out + "The customers waited for an average of " + cwAvg + " seconds, with a standard deviation of " + cwsd + ".\n";
		
		for(int i = 0; i < tellers.size(); i++)																							//for each teller...
		{
			out = out + "Teller " + i + " helped " + this.tellers.get(i).getCustomersHelped() + " customers.\n";						//...add the number of customers this teller helped today to out...
			double percent = 100.0 * ((double)this.tellerIdleTime.get(i) / this.curTime);												//...make double percent to be the percent of idle time for this teller...
			out = out + "Teller " + i + " was idle " + percent + "% of the time.\n";													//...and add it to out...
			percents.add(percent);																										//...add percent to the ArrayList of percents
		}//for(int i = 0; i < tellers.size(); i++)
		
		double tsd = standardDevDub(percents);																							//set the standard deviation of the idle times
		double tAvg = avg(percents);																									//set the average of the idle times
		
		out = out + "The tellers were idle for an average of " + tAvg + "% of the time, with a standard deviation of " + tsd + ".\n";
		
		return out;
	}//toString()
	
}//class Bank
