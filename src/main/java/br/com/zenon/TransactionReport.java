package br.com.zenon;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.utils.IngestorUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TransactionReport {

    public void report(String filePath) {
        Path path = Paths.get(filePath);

        try (Stream<String> allLines = Files.lines(path);
             Stream<String> frauds = Files.lines(path);
             Stream<String> totalValue = Files.lines(path)) {
            IO.println("Total de linhas: " + allLines.skip(1).count());
            IO.println("Total de fraudes: " + frauds.skip(1).map(IngestorUtils::buildTransaction).filter(Transaction::isFraud).count());
            IO.println("Valor total transacionado: " + totalValue.skip(1).map(IngestorUtils::buildTransaction).map(Transaction::amount).reduce(BigDecimal.ZERO, BigDecimal::add));
        } catch (IOException e) {
            IO.println("ERROR: Failed to read file [" + filePath + "] with error: " + e.getMessage());
        }
    }
}
