package br.com.zenon.fraud;

import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    public void analyze(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return;
        }
        NumberFormat decimalFormatter = new DecimalFormat("0.00");
        List<Transaction> fraudulentTransactions = transactions.stream()
                .filter(Transaction::isFraud)
                .toList();

        IO.println("1. Total de Fraudes: " + fraudulentTransactions.size());

        IO.println("2. Top 3 Fraudes de Maior Valor:");
        fraudulentTransactions.stream()
                .map(Transaction::amount)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .forEach(amount -> IO.println(decimalFormatter.format(amount)));

        IO.println("3. Clientes Suspeitos:");
        fraudulentTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::nameOrig, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> IO.println(entry.getKey()));

        fraudulentTransactions.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal::add)
                .ifPresent(total -> IO.println("4. Prejuízo Total: " + decimalFormatter.format(total)));

        IO.println("5. Fraudes por Tipo:");
        fraudulentTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()))
                .forEach((transactionType, count) -> IO.println(" - " + transactionType + ": " + count));
    }
}
