package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> getByNameOrig(String nameOrig);
}
