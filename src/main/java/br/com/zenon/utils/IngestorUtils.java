package br.com.zenon.utils;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;

import java.math.BigDecimal;

public class IngestorUtils {

    public static Transaction buildTransaction(String line) {
        return buildTransaction(line.split(","));
    }

    public static Transaction buildTransaction(String[] columns) {
        if (columns == null || columns.length != 11) {
            throw new IllegalArgumentException("not enough columns in the line");
        }

        var step = Integer.parseInt(columns[0]);
        if (step < 1) {
            throw new IllegalArgumentException("step should be positive: " + step);
        }

        var amount = new BigDecimal(columns[2]);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount should be positive: " + amount);
        }

        var nameOrig = columns[3];
        if (nameOrig == null || nameOrig.isBlank()) {
            throw new IllegalArgumentException("nameOrig should not be empty");
        }

        var oldBalanceOrig = new BigDecimal(columns[4]);
        if (oldBalanceOrig.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("oldBalanceOrig should be positive: " + oldBalanceOrig);
        }

        var newBalanceOrig = new BigDecimal(columns[5]);
        if (newBalanceOrig.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("newBalanceOrig should be positive: " + newBalanceOrig);
        }

        var nameDest = columns[6];
        if (nameDest == null || nameDest.isBlank()) {
            throw new IllegalArgumentException("nameDest should not be empty");
        }

        var oldBalanceDest = new BigDecimal(columns[7]);
        if (oldBalanceDest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("oldBalanceDest should be positive: " + oldBalanceDest);
        }

        var newBalanceDest = new BigDecimal(columns[8]);
        if (newBalanceDest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("newBalanceDest should be positive: " + newBalanceDest);
        }

        int isFraud = Integer.parseInt(columns[9]);
        if (isFraud != 0 && isFraud != 1) {
            throw new IllegalArgumentException("isFraud should be 0 or 1");
        }

        int isFlaggedFraud = Integer.parseInt(columns[10]);
        if (isFlaggedFraud != 0 && isFlaggedFraud != 1) {
            throw new IllegalArgumentException("isFlaggedFraud should be 0 or 1");
        }

        return new Transaction(step, TransactionType.valueOf(columns[1]), amount,
                nameOrig, oldBalanceOrig, newBalanceOrig,
                nameDest, oldBalanceDest, newBalanceDest,
                parseBoolean(isFraud), parseBoolean(isFlaggedFraud));
    }

    private static boolean parseBoolean(int value) {
        return value == 1;
    }
}
