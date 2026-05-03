package br.com.zenon;

import br.com.zenon.fraud.FraudAnalyzer;
import br.com.zenon.fraud.model.Transaction;

import java.util.List;

public class Main {
    static void main() {
        var ingestor = new TransactionIngestor();
        var analyzer = new FraudAnalyzer();

        List<Transaction> transactions = ingestor.ingest("./data/PS_20174392719_1491204439457_log.csv", 50000d);
        //transactions.forEach(IO::println);
        analyzer.analyze(transactions);

        //List<Transaction> transactionsError = ingestor.ingest("./data/paysim_with_bad_data.csv", null);
        //transactionsError.forEach(IO::println);
    }
}
