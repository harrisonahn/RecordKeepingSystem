package com.cwretirement.codetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.function.BooleanSupplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CodeTestApplicationE2ETest {

    @Mock
    private ReconciliationService reconciliationService;

    @BeforeEach
    void setup() {
        Mockito.reset(reconciliationService);
    }

    @Test
    void runReconciliationWithContributions() throws ParseException {
        final Date jan31 = Transaction.DATE_FORMAT.parse("31-Jan-20");
        Contribution contribution1 = new Contribution(321095, "cf4f9942-3c52-41dd-978d-ba90a3675f9c", jan31, jan31, new BigDecimal("10000.00"), 4459254);
        Contribution contribution2 = new Contribution(123005, "a2ef9542-3c52-b16a-9f8f-345aa387520c", jan31, jan31, new BigDecimal("200.00"), 4459687);

        reconciliationService.reconcileContribution(contribution1);
        reconciliationService.reconcileContribution(contribution2);

        ArgumentCaptor<Contribution> contributionArgumentCaptor = ArgumentCaptor.forClass(Contribution.class);
        verify(reconciliationService, times(2)).reconcileContribution(contributionArgumentCaptor.capture());
        final List<Contribution> contributionList = contributionArgumentCaptor.getAllValues();

        assertThat(contributionList).hasSize(2).containsExactly(contribution1, contribution2);
    }

    @Test
    void runReconciliationWithTradeSettlements() throws ParseException {
        final Date apr25 = Transaction.DATE_FORMAT.parse("25-Apr-20");
        TradeSettlement tradeSettlementPurchase = new TradeSettlement(TradeSettlement.Type.PURCHASE,
                apr25, apr25, apr25, apr25, new BigDecimal("200.00"), new BigDecimal("135.00"),
                "VANGUARD RETIREMENT 2020 POOLED FUND", new BigDecimal("25.00"), "VC2Y", 2353643);
        TradeSettlement tradeSettlementSale = new TradeSettlement(TradeSettlement.Type.SALE,
                apr25, apr25, apr25, apr25, new BigDecimal("250.00"), new BigDecimal("175.00"),
                "VANGUARD RETIREMENT 2020 POOLED FUND", new BigDecimal("25.00"), "VC28", 5392057);

        reconciliationService.reconcileTradeSettlement(tradeSettlementPurchase);
        reconciliationService.reconcileTradeSettlement(tradeSettlementSale);

        ArgumentCaptor<TradeSettlement> tradeSettlementArgumentCaptor = ArgumentCaptor.forClass(TradeSettlement.class);
        verify(reconciliationService, times(2)).reconcileTradeSettlement(tradeSettlementArgumentCaptor.capture());
        final List<TradeSettlement> tradeSettlementList = tradeSettlementArgumentCaptor.getAllValues();

        assertThat(tradeSettlementList).hasSize(2).containsExactly(tradeSettlementPurchase, tradeSettlementSale);
    }

    @Test
    void contributionEqualsHandlesNullFields() {
        Contribution c1 = new Contribution(12345, null, null, null, null, 54321);
        Contribution c2 = new Contribution(12345, null, null, null, null, 54321);

        assertEquals(c1, c2);
    }

    @Test
    void transactionToContributionConvertsCorrectly() throws ParseException {
        Date date = Transaction.DATE_FORMAT.parse("01-Jan-20");
        Transaction transaction = new Transaction(new String[] { "123", "AcctName", "01-Jan-20", "01-Jan-20", "01-Jan-20", "01-Jan-20",
                "Contribution", "100.00", "0.00", "descr", "1000.00", "SYM", "12345", "789" });

        Contribution contribution = transaction.toContribution();

        assertThat(contribution.getAmount()).isEqualByComparingTo("1000.00");
    }

    @Test
    void transactionToTradeSettlementConvertsCorrectly() throws ParseException {
        Date date = Transaction.DATE_FORMAT.parse("01-Jan-20");
        Transaction transaction = new Transaction(new String[] { "123", "AcctName", "01-Jan-20", "01-Jan-20", "01-Jan-20", "01-Jan-20",
                "Purchase Cash Settlement", "100.00", "0.00", "descr", "1000.00", "SYM", "12345", "789" });

        TradeSettlement tradeSettlement = transaction.toTradeSettlement();

        assertThat(tradeSettlement.getUnits()).isEqualByComparingTo("100.00");
    }
}
