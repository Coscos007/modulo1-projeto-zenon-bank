package br.com.zenon;

import br.com.zenon.fraud.FraudAnalyzer;
import br.com.zenon.fraud.model.Transaction;

import java.util.List;

public class Main {
    static void main() {
        var ingestor = new TransactionIngestor();
        var analyzer = new FraudAnalyzer();
        var report = new TransactionReport();

        String filePath = "./data/PS_20174392719_1491204439457_log.csv";
        //List<Transaction> transactions = ingestor.ingest(filePath, null);
        //transactions.forEach(IO::println);

        //List<Transaction> transactionsError = ingestor.ingest("./data/paysim_with_bad_data.csv", null);
        //transactionsError.forEach(IO::println);

        //analyzer.analyze(transactions);

        /*
        TransactionRepository repositoryList = new TransactionListRepository(transactions);
        findAndPrintOptionalTransaction("C12345", repositoryList);
        findAndPrintOptionalTransaction("C1231006815", repositoryList);
        findAndPrintOptionalTransaction("C1868032458", repositoryList);
        TransactionRepository repositoryMap = new TransactionMapRepository(transactions);
        findAndPrintOptionalTransaction("C12345", repositoryMap);
        findAndPrintOptionalTransaction("C1231006815", repositoryMap);
        findAndPrintOptionalTransaction("C1868032458", repositoryMap);
        */

        report.report(filePath);
    }

    private static void findAndPrintOptionalTransaction(String nameOrig, TransactionRepository repository) {
        repository.getByNameOrig(nameOrig)
                .ifPresentOrElse(
                        t -> IO.println(t.toString()),
                        () -> IO.println("Transação não encontrada para o cliente " + nameOrig)
                );
    }
}
