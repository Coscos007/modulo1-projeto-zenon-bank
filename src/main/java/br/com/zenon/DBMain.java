package br.com.zenon;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.ingestor.*;

import java.util.List;

public class DBMain {
    static void main() {
        String filePath = "./data/PS_20174392719_1491204439457_log.csv";

        TransactionRepository repositorySQL = new TransactionSQLRepository();
        //insertTransactionsInDatabase(filePath, 10000d, repositorySQL);

        findAndPrintOptionalTransaction("C12345", repositorySQL);
        findAndPrintOptionalTransaction("C1231006815", repositorySQL);
        findAndPrintOptionalTransaction("C1868032458", repositorySQL);
    }

    private static void findAndPrintOptionalTransaction(String nameOrig, TransactionRepository repository) {
        repository.getByOriginName(nameOrig)
                .ifPresentOrElse(
                        t -> IO.println(t.toString()),
                        () -> IO.println("Transação não encontrada para o cliente " + nameOrig)
                );
    }

    private static void insertTransactionsInDatabase(String filePath, double amountThreshold, TransactionRepository repository) {
        var ingestor = new TransactionIngestor();
        List<Transaction> transactions = ingestor.ingest(filePath, amountThreshold);

        long startTime = System.nanoTime();
        transactions.forEach(repository::save);
        long endTime = System.nanoTime();
        IO.println("Time to insert " +  transactions.size() + " transactions using MySQL: " + (endTime - startTime));
    }
}
