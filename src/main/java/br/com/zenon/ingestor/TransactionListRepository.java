package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Optional<Transaction> getByOriginName(String nameOrig) {
        long startTime = System.nanoTime();
        Optional<Transaction> transaction = transactions.stream().filter(t -> t.nameOrig().equals(nameOrig)).findFirst();
        long endTime = System.nanoTime();
        IO.println("Time to find transaction by nameOrig=[" + nameOrig + "] using LIST: " + (endTime - startTime));
        return transaction;
    }

    @Override
    public void save(Transaction transaction) {
        throw new UnsupportedOperationException("Saving not supported for in-memory repository.");
    }
}
