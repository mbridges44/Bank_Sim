/**
	This file is brought to you by:
	Charles Corbett
	Michael Bridges
	
	Miscellaneous Information:
	Used by: 	Bank.java;Teller.java
	Requires: 	N/A
	Purpose:	This class models the behavior of customers at a bank.
				We have called this class "Transaction" instead of "Customer" because our bank is more interested in the money than the people who have it.
				This class is comparable in a hierarchy on three different pieces of State, the next only called to break ties:
					1) Event Time		(with the lowest number getting highest priority)
					2) isWalkIn			(with false getting higher priority than true)
					3) id				(even with identical eventTimes, there are no true ties)
*/


public class Transaction implements Comparable<Object>
{
													//STATE
													
	private int 			entranceTime;
	private int 			runTime;
	private boolean 		isWalkIn;
	private long 			id;
	private static long 	counter; 
	private int 			waitTime;
	private int 			eventTime;
	
			
													//CONSTRUCTOR
	
	public Transaction(int entranceTime, int runTime, boolean isWalkIn)
	{
		this.entranceTime = entranceTime;
		this.eventTime = entranceTime;
		this.runTime = runTime;
		this.isWalkIn = isWalkIn;
		this.id = counter;
		counter++;
	}//Constructor(int,int,boolean)

	
													//BEHAVIOR
													
//OVERRIDDEN METHODS
	
	/**
		overrides the compareTo method from the Comparable Interface
		Compares with these three criterion:
		In order of eventTime.  Ties broken by isWalkin, false getting priority.  Ties broken by id.
		@param Object	must be case before usage, but is the other Transaction being compared
	*/
	@Override
	public int compareTo(Object arg0)
	{
		int ret = this.getEventTime() - ((Transaction) arg0).getEventTime();		//sets the int ret (what we will eventually return) to the difference between the two transaction's eventTimes.
		if(ret == 0)																//if ret is 0 (i.e. the eventTimes are equivalent...
		{
			if(this.isWalkIn() != ((Transaction) arg0).isWalkIn())					//...furthermore, if one is a WalkIn, but the other is not...
			{
				if(this.isWalkIn())													//...furthermore, if this.isWalkIn()...
				{
					ret =  1;														//...set ret to a positive number, indicating that the arg0 has higher priority
				}//(this.isWalkIn())
				else
				{
					ret = -1;														//...otherwise, set ret to a negative number, indicating that this has a higher priority
				}//else
				
			}//if(this.isWalkIn() != ((Transaction) arg0).isWalkIn())
			else																	//...otherwise (i.e. either they are both driveThru transactions or both walkIn transactions)...
			{
				ret = (int) (this.id -  ((Transaction) arg0).getId());				//...set ret to the difference in ids, the lower id getting priority
			}//else
			 
		}//if(ret == 0)
		return ret;
	}//compareTo(Object)

													
//GETTERS
	
	/** 
	* returns the counter of transactions at the bank (used for assigning id)
	* @return this.entranceTime
	*/
	public static long getCounter()
	{
		return counter;
	}//getCounter()

	/** 
	* returns the time this customer "entered the building".  AKA, the time this transaction will have been first taken off the events Queue
	* @return this.entranceTime
	*/
	public int getEntranceTime()
	{
		return this.entranceTime;
	}//getEntranceTime()
	
	/** 
	* returns the time at which this transaction will next do something
	* @return this.eventTime
	*/
	public int getEventTime()
	{
		return this.eventTime;
	}//getEventTime()

	/** 
	* returns the id of this transaction
	* @return this.id
	*/
	public long getId()
	{
		return this.id;
	}//getId()

	/** 
	* returns a boolean as to whether or not the transaction will be completed "inside"
	* @return this.isWalkIn
	*/
	public boolean isWalkIn()
	{
		return this.isWalkIn;
	}

	/** 
	* returns the time this transaction will take to complete
	* @return this.runTime
	*/
	public int getRunTime()
	{
		return this.runTime;
	}//getRunTime()

	/** 
	* returns the time this transaction has waited since it walked in the building
	* @return this.waitTime
	*/
	public int getWaitTime()
	{
		return this.waitTime;
	}//getWaitTime()

	
//SETTERS
	
	/** 
	* sets the counter to a given long
	* @param long
	*/
	public static void setCounter(long counter)
	{
		Transaction.counter = counter;
	}//setCounter(long)


	/** 
	* sets the event time to a given int
	* @param int
	*/
	public void setEventTime(int eventTime)
	{
		this.eventTime = eventTime;	
	}//setEventTime(int)
	
	
	/** 
	* sets this.isWalkIn to a given boolean
	* @param boolean
	*/
	public void setWalkIn(boolean isWalkIn)
	{
		this.isWalkIn = isWalkIn;
	}//setWalkIn(boolean)


	/** 
	* sets the wait time to a given int
	* @param int		
	*/
	public void setWaitTime(int time)
	{
		this.waitTime = time;
	}//setWaitTime(int)

	
//OTHER METHODS
	
	/** 
	* returns this transaction as a string with the basic information
	* @return String
	*/
	public String toString()
	{
		return "ID: " + this.id +  " entrance Time: " + this.entranceTime + "\tRun Time: " + this.runTime + "\tIsWalkIn: " + this.isWalkIn+"\n";
	}//toString()

}//class Transaction implements Comparable<Object>