package br.com.zenon;

import br.com.zenon.fraud.Transaction;

import java.util.List;

public class Main {
    static void main() {
        TransactionIngestor ingestor = new TransactionIngestor();

        //List<Transaction> transactions = ingestor.ingest("./data/PS_20174392719_1491204439457_log.csv", 10d);
        //transactions.forEach(IO::println);

        List<Transaction> transactionsError = ingestor.ingest("./data/paysim_with_bad_data.csv", null);
        transactionsError.forEach(IO::println);
    }
}
