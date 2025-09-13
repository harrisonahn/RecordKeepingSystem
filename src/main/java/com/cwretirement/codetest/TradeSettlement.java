package com.cwretirement.codetest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TradeSettlement {

    private final Type type;
    private final Date dateEntered;
    private final Date datePosted;
    private final Date dateSettled;
    private final Date dateTraded;
    private final BigDecimal units;
    private final BigDecimal unitPrice;
    private final String fundName;
    private final BigDecimal amount;
    private final String symbolCusip;
    private final long custodianReference;

    public TradeSettlement(Type type, Date dateEntered, Date datePosted, Date dateSettled,
                           Date dateTraded, BigDecimal units, BigDecimal unitPrice, String fundName,
                           BigDecimal amount, String symbolCusip, long custodianReference) {
        this.type = type;
        this.dateEntered = dateEntered;
        this.datePosted = datePosted;
        this.dateSettled = dateSettled;
        this.dateTraded = dateTraded;
        this.units = units;
        this.unitPrice = unitPrice;
        this.fundName = fundName;
        this.amount = amount;
        this.symbolCusip = symbolCusip;
        this.custodianReference = custodianReference;
    }

    public enum Type {
        PURCHASE,
        SALE;

        private static final Map<String, Type> TRANSACTION_DESCRIPTION_TO_SETTLEMENT_TYPE = new HashMap<String, Type>() {{
            put("Purchase Cash Settlement", PURCHASE);
            put("Sale Cash Settlement", SALE);
        }};

        public static Type fromString(String string) {
            return TRANSACTION_DESCRIPTION_TO_SETTLEMENT_TYPE.get(string);
        }
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TradeSettlement that = (TradeSettlement) obj;
        return type == that.type &&
                custodianReference == that.custodianReference &&
                Objects.equals(dateEntered, that.dateEntered) &&
                Objects.equals(datePosted, that.datePosted) &&
                Objects.equals(dateSettled, that.dateSettled) &&
                Objects.equals(dateTraded, that.dateTraded) &&
                Objects.equals(units, that.units) &&
                Objects.equals(unitPrice, that.unitPrice) &&
                Objects.equals(fundName, that.fundName) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(symbolCusip, that.symbolCusip);

    }

    @Override
    public int hashCode() {
        return Objects.hash(type, custodianReference, dateEntered, datePosted, dateSettled,
                dateTraded, units, unitPrice, fundName, amount, symbolCusip);
    }

    public BigDecimal getUnits() {
        return units;
    }
}
