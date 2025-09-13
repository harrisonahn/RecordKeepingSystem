Based off of the implementation of contributions, I was able to implement Trade Settlements in the rest of the classes.

I started by creating the constructor in TradeSettlement, while also setting the variables to final in this class as well as in Contribution.
I also created its own override methods such as toString, equals, and hashCode.

I used AI to create a return statement quicker in TradeSettlement.equals() instead of typing all the lines manually. This also showed that Objects.equals(obj1, obj2) should be used instead of obj1.equals(obj2) to prevent a NullPointerException, which allowed me to make the correction in Contribution.equals() as well.

Afterwards in CodeTestApplication, I added the if statement for when a transaction is of type trade settlement.

In Transaction, I added the TRADE_SETTLEMENT enum type as well as its high-level transaction type in the hashmap for a purchase cash settlement or sale cash settlement.
I also added the toTradeSettlement to convert a transaction to a trade settlement, similar to toContribution()

Next in ReconciliationService, I created a method reconcile used by both reconcileContribution and reconcileTradeSettlement since most of the original method would be repetitive besides the url and payload.

For the unit tests, @SpringBootTest and @MockitoBeans were not properly mocking the reconciliationService so I replaced them. I tested reconciliation with contributions and trade settlements of type purchase and sale. I also tested some edge cases such as a contribution with null fields and simple logic such as converting transactions to contributions and trade settlements.
I inquired AI to propose some unit test ideas, which it suggested to test the transactionToContribution and transactionToTradeSettlement logic.