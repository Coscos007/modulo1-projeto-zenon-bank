package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> getByOriginName(String nameOrig);

    void save(Transaction transaction);

    void saveAll(List<Transaction> transactions);
}
