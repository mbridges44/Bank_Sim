Bank Sim
========

This project simulates the interactions between customers and tellers in a bank office. The bank computes average customer wait times for some sample customer data.


Transactions are put into a queue which get polled off when it is time for a customer to have an interaction with a teller or
when a customer enters the bank.

The Bank class takes care of the Teller actions, which determine what the Transactions do. 
