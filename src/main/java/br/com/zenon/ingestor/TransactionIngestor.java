package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.utils.IngestorUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> ingest(String filePath, Double amountThreshold) {
        if (amountThreshold == null) {
            amountThreshold = Double.MAX_VALUE;
        }
        Path path = Paths.get(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            br.readLine(); // skip csv headers

            var transactions = new ArrayList<Transaction>();
            int errorCount = 0;
            String line;
            while ((line = br.readLine()) != null && transactions.size() < amountThreshold) {
                String[] columns = line.split(",");
                try {
                    transactions.add(IngestorUtils.buildTransaction(columns));
                } catch (IllegalArgumentException e) {
                    IO.println("ERRO: " + line + " | " + e);
                    errorCount++;
                }
            }
            IO.println(errorCount + " lines with errors");
            return transactions;
        } catch (IOException e) {
            IO.println("ERROR: Failed to read file [" + filePath + "] with error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
