/**
	This file is brought to you by:
	Charles Corbett
	Michael Bridges
	
	Miscellaneous Information:
	Used by: 	Bank.java
	Requires: 	TellerResponse.java; Transaction.Java
	Purpose:	This class models the behaviour of tellers in a bank who interact with both the bank and customers.
				We do not have any public setters other than the updateWatch method.
*/
public class Teller
{
												//STATE
												
	private int				customersHelped;
	private int				id;
	private	TellerResponse	lastResponse;
	private	Integer			nextWatchCheck;
	private	TellerResponse	status;
	
												//CONSTRUCTOR
												
	public Teller(int id)
	{
		this.id=id;
		nextWatchCheck=null;
	}//Constructor(int)
	
												//BEHAVIOR
												
//GETTERS	
	
	/** 
	* returns the number of customers that this particular teller has helped so far this day
	* @return this.customersHelped
	*/
	public int getCustomersHelped()
	{
		return this.customersHelped;
	}//getCustomersHelped()
	
	/** 
	* returns the id of this particular teller, which (as defined elsewhere) corresponds to his spot in Bank's tellers as well as the queue assigned to him
	* @return this.id
	*/
	public int getId()
	{
		return this.id;
	}//getId()
	
	/** 
	* returns the status of this particular teller the last time we updated the watch
	* @return this.lastResponse
	*/
	public TellerResponse getLastResponse()
	{
		return this.lastResponse;
	}//getLastResponse()
	
	/** 
	* returns the current status of this particular teller
	* @return this.status
	*/
	public TellerResponse getStatus()
	{
		return this.status;
	}//getStatus()

	
//OTHER METHODS

	/** 
	* returns the wait time of the customer at the front of the line this teller is currently handling
	* @param transaction    this parameter is the transaction which has just been polled from the front of the queue that the bank sent this teller to handle
	* @param curTime		this parameter is the current time according to the bank  
	* @return transaction.waitTime
	*/
	public Integer handleTransaction(Transaction transaction, int curTime)
	{
		transaction.setWaitTime(curTime - transaction.getEntranceTime());		//set this transaction's wait time to the difference between when it arrived and now
		this.nextWatchCheck = curTime + transaction.getRunTime();				//tell this teller when we are done with this transaction
		transaction.setEventTime(this.nextWatchCheck);							//change the Event Time to the time when we will finish the transaction
		return transaction.getWaitTime();										//return the wait time for this customer
	}//handleTransaction(Transaction, int)
	
	/** 
	* sets the lastResponse to the status, then updates the status to reflect what is happening now
	* if the teller just finished with a customer, then it also increments the number of customers helped and resets the watch.
	*/
	public void updateWatch(int time, int curTime)
	{
		this.lastResponse = this.status;				//copies this.status into this.lastResponse to hold onto what the teller had been doing
		
		if(this.nextWatchCheck==null)					// if the teller does not care what time it is...
		{
			this.status= TellerResponse.IDLE;			//... set this.status to IDLE.
		}//if(this.nextWatchCheck==null)
		else if(curTime==this.nextWatchCheck)			// alternatively, if the time this teller was waiting for just happened...
		{
			this.status=TellerResponse.DONE;			//... the teller is now done with this transaction, ...
			this.customersHelped++;						//... has helped another customer, ...
			resetWatch();								//... and now shall consider himself idle
		}//else if(curTime==this.nextWatchCheck)
		else											// finally, if the teller is still waiting for the time to show up...
		{
			this.status=TellerResponse.SAME;			//... then this teller is still helping the same customer
		}//else
		
	}//updateWatch(int, int)	
	
	/** 
	* sets this.nextWatchCheck to null so that we verify that the teller is idle
	*/
	private void resetWatch()
	{
		this.nextWatchCheck = null;
	}//resetWatch()

}//class Teller
