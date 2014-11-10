Michael Bridges
Charles Corbett

**Note we named our what most groups called customer class "Transactions", we did this because we felt each line 
was really a Transaction.


Our test files:
backwardsOrderedEntrance.txt
bigDayAtBank.txt
driveThruSlammedWWalkIns.txt
oneWalkIn.txt
Slammed.txt
Slammed2.txt
slammedInWaves.txt
SlammedShortLine.txt
SlammedWDriveThru.txt
SlammedWDriveThru1.txt
slowDay.txt

backwardsOrderedEntrance.txt
-We figured that you would end up testing with an out of order file so that would make this a good test.
Along with a good test to make sure our PriorityQueue is initally loaded correctly.

bigDayAtBank.txt
-This is good test of how our bank handles getting a lot of customers throughout the
day. This contains many customers and random times so it is a stress test for this bank.


driveThruSlammedWWalkIns.txt
-This is a good test to see how the tellers react when the drive thru
is slammed. A couple walk ins come in as well which will test how the tellers react
when people are in their line and at the drive thru.


oneWalkIn.txt
-This is a good test to make sure tellers are able to handle a very light day with only one
customer coming in at time 20.


Slammed.txt
-We thought this was a good test to see how our tellers
traffic when many customers come in at once with no drive thru


Slammed2.txt
-This is similar to slammed.txt, but instead of each transaction being short we decided to 
throw in some longer transaction times to test how tellers and customers handle longer transaction and wait
times


slammedInWaves.txt
-This file is a good test to see how tellers handle waves of multiple customers coming in at once,
making sure that the tellers properly handle a wave after they've already dealt with one wave. This
file does include some drive thru action as well to make sure tellers are properly handling the drive thru with
customers in their line.


SlammedShortLine.txt
-This file will create a senario where one line has short transaction time and the rest are long. This is a good test
of looking at customers behavior and making sure they will pick the shortest line and stay in that line


SlammedWDriveThru.txt
-This file will test what happens when everybody comes in at the same time and there are some at the drive thru.
This is a good test to see what happens to the tellers when their line is long and there are people in the drive thru, which
should involve most of the tellers in helping the drive thru.


SlammedWDriveThru1.txt
-Similar to SlammedWDriveThru but adds a customer after the initial rush, which is a good test to see how a customer reacts
to many queues being long.


slowDay.txt
-This simulates a slow day at the bank where customers come in spread apart and no lines are formed. A good test to make sure
customers and tellers are behaving correctly when a customer is able to walk right to the front of the queue.



