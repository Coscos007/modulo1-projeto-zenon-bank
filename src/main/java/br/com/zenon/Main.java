package br.com.zenon;

public class Main {
    static void main() {
        TransactionIngestor ingestor = new TransactionIngestor();
        var transactions = ingestor.ingest("./data/PS_20174392719_1491204439457_log.csv", 10);
        transactions.forEach(IO::println);
    }
}
