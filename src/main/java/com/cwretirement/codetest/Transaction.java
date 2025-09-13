package com.cwretirement.codetest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.cwretirement.codetest.TradeSettlement.Type.SALE;

public class Transaction {

    public enum Type {
        CONTRIBUTION, TRADE_SETTLEMENT;

        private static final Map<String, Type> TRANSACTION_DESCRIPTION_TO_TRANSACTION_TYPE = new HashMap<>() {{
            put("Contribution", CONTRIBUTION);
            put("Purchase Cash Settlement", TRADE_SETTLEMENT);
            put("Sale Cash Settlement", TRADE_SETTLEMENT);
        }};

        public static Type fromString(String string) {
            return TRANSACTION_DESCRIPTION_TO_TRANSACTION_TYPE.get(string);
        }
    }

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yy");

    private final long acct;
    private final String acctNm;
    private final Date entered;
    private final Date posted;
    private final Date dateSettled;
    private final Date dateTraded;
    private final String transactionDescription;
    private final BigDecimal quantity;
    private final BigDecimal rate;
    private final String descr;
    private final BigDecimal amount;
    private final String symbolCusip;
    private final String explanation;
    private final long job;
    private final Type type;

    public Transaction(String[] fields) throws ParseException {
        acct = Long.parseLong(fields[0]);
        acctNm = fields[1];
        entered = DATE_FORMAT.parse(fields[2]);
        posted = DATE_FORMAT.parse(fields[3]);
        dateSettled = DATE_FORMAT.parse(fields[4]);
        dateTraded = DATE_FORMAT.parse(fields[5]);
        transactionDescription = fields[6];
        quantity = new BigDecimal(fields[7]);
        rate = new BigDecimal(fields[8]);
        descr = fields[9];
        amount = new BigDecimal(fields[10]);
        symbolCusip = fields[11];
        explanation = fields[12];
        job = Long.parseLong(fields[13]);
        type = Type.fromString(transactionDescription);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "acct=" + acct +
                ", acctNm='" + acctNm + '\'' +
                ", entered=" + entered +
                ", posted=" + posted +
                ", dateSettled=" + dateSettled +
                ", dateTraded=" + dateTraded +
                ", transactionDescription='" + transactionDescription + '\'' +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", descr='" + descr + '\'' +
                ", amount=" + amount +
                ", symbolCusip='" + symbolCusip + '\'' +
                ", explanation='" + explanation + '\'' +
                ", job=" + job +
                '}';
    }

    public Type getType() {
        return type;
    }

    public Contribution toContribution() {
        assert Type.CONTRIBUTION.equals(type);
        return new Contribution(
                Long.parseLong(explanation),
                descr,
                entered,
                posted,
                amount,
                job
        );
    }

    public TradeSettlement toTradeSettlement() {
        assert Type.TRADE_SETTLEMENT.equals(type);
        return new TradeSettlement(
                TradeSettlement.Type.fromString(transactionDescription),
                entered,
                posted,
                dateSettled,
                dateTraded,
                quantity,
                rate,
                descr,
                amount,
                symbolCusip,
                job
        );
    }
}
