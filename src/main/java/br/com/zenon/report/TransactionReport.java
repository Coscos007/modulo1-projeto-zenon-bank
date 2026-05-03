package br.com.zenon.report;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.utils.IngestorUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class TransactionReport {

    public void report(String filePath, Language language) {
        Path path = Paths.get(filePath);

        Locale locale = language.getLocale();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        ResourceBundle mensagens = ResourceBundle.getBundle("mensagens", locale);
        if (mensagens == null) {
            IO.println("ERROR: Failed to load resource bundle for language [" + language + "]");
            return;
        }

        IO.println(mensagens.getString("report.name"));
        try (Stream<String> allLines = Files.lines(path);
             Stream<String> frauds = Files.lines(path);
             Stream<String> totalValue = Files.lines(path)) {
            IO.println(mensagens.getString("total.of.lines") + ": " + allLines.skip(1).count());
            IO.println(mensagens.getString("total.of.frauds") + ": " + frauds.skip(1).map(IngestorUtils::buildTransaction).filter(Transaction::isFraud).count());
            IO.println(mensagens.getString("total.transacted.value") + ": " + currencyFormatter.format(totalValue.skip(1).map(IngestorUtils::buildTransaction).map(Transaction::amount).reduce(BigDecimal.ZERO, BigDecimal::add)));
        } catch (IOException e) {
            IO.println("ERROR: Failed to read file [" + filePath + "] with error: " + e.getMessage());
        }
    }
}
