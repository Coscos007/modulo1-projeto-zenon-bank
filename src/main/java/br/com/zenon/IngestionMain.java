package br.com.zenon;


import br.com.zenon.ingestor.EfficientTransactionIngestor;
import br.com.zenon.ingestor.TransactionRepository;
import br.com.zenon.ingestor.TransactionSQLRepository;

public class IngestionMain {
    static void main() {
        var ingestor = new EfficientTransactionIngestor();
        var repositorySQL = new TransactionSQLRepository();

        String filePath = "./data/PS_20174392719_1491204439457_log.csv";

        long startTime = System.nanoTime();
        //ingestor.readAsStream(filePath, repositorySQL::save, 10000L);
        ingestor.readBatch(filePath, repositorySQL::saveAll, null);
        long endTime = System.nanoTime();
        IO.println("Time to insert transactions using MySQL and EfficientIngestor: " + (endTime - startTime));

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
}
