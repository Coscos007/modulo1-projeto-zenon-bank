package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    private final Map<String, Transaction> transactions;

    public TransactionMapRepository(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            this.transactions = Map.of();
        } else {
            this.transactions = transactions.stream().collect(Collectors.toMap(Transaction::nameOrig, t -> t));
        }
    }

    public Optional<Transaction> getByNameOrig(String nameOrig) {
        long startTime = System.nanoTime();
        Transaction transaction = transactions.get(nameOrig);
        long endTime = System.nanoTime();
        IO.println("Time to find transaction by nameOrig=[" + nameOrig + "] using MAP: " + (endTime - startTime));
        return Optional.ofNullable(transaction);
    }
}
