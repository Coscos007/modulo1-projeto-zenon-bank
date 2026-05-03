package br.com.zenon;

import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionType;
import br.com.zenon.utils.IngestorUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionIngestor {

    public List<Transaction> ingest(String filePath, double amountThreshold) {
        Path path = Paths.get(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            br.readLine(); // skip csv headers

            var transactions = new ArrayList<Transaction>();
            String line;
            while ((line = br.readLine()) != null && transactions.size() < amountThreshold) {
                String[] columns = line.split(",");
                var transaction = new Transaction(Integer.parseInt(columns[0]), TransactionType.valueOf(columns[1]), new BigDecimal(columns[2]),
                        columns[3], new BigDecimal(columns[4]), new BigDecimal(columns[5]),
                        columns[6], new BigDecimal(columns[7]), new BigDecimal(columns[8]),
                        IngestorUtils.parseBoolean(columns[9]), IngestorUtils.parseBoolean(columns[10]));
                transactions.add(transaction);
            }
            return transactions;
        } catch (IOException e) {
            IO.println("ERROR: Failed to read file [" + filePath + "] with the following error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
