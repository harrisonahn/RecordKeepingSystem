package com.cwretirement.codetest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Contribution {
    private final long memberAccountId;
    private final String transactionId;
    private final Date dateEntered;
    private final Date datePosted;
    private final BigDecimal amount;
    private final long custodianReference;

    public Contribution(long memberAccountId, String transactionId, Date dateEntered, Date datePosted, BigDecimal amount, long custodianReference) {
        this.memberAccountId = memberAccountId;
        this.transactionId = transactionId;
        this.dateEntered = dateEntered;
        this.datePosted = datePosted;
        this.amount = amount;
        this.custodianReference = custodianReference;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contribution that = (Contribution) o;
        return memberAccountId == that.memberAccountId &&
                custodianReference == that.custodianReference &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(dateEntered, that.dateEntered) &&
                Objects.equals(datePosted, that.datePosted) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberAccountId, transactionId, dateEntered, datePosted, amount, custodianReference);
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
