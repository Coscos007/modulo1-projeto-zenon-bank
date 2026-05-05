package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.utils.IngestorUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    public void readAsStream(String filePath, Consumer<Transaction> consumer, Long amountThreshold) {
        if (amountThreshold == null) {
            amountThreshold = Long.MAX_VALUE;
        }

        Path path = Paths.get(filePath);
        try (Stream<String> lines = Files.lines(path)) {
            lines.skip(1).limit(amountThreshold).map(IngestorUtils::buildTransaction).forEach(consumer);
        } catch (IOException e) {
            IO.println("ERROR: Failed to read file [" + filePath + "] with error: " + e.getMessage());
        }
    }

    public void readBatch(String filePath, Consumer<List<Transaction>> consumer, Long amountThreshold) {
        if (amountThreshold == null) {
            amountThreshold = Long.MAX_VALUE;
        }

        Path path = Paths.get(filePath);
        try (ExecutorService executorService = Executors.newFixedThreadPool(10);
             Stream<String> lines = Files.lines(path).skip(1).limit(amountThreshold)) {

            Spliterator<String> spliterator = lines.spliterator();

            while (true) {
                List<Transaction> batch = new ArrayList<>(10000);
                boolean noMoreElements = false;
                for (int i = 0; i < 10000; i++) {
                    boolean elementAdded = spliterator.tryAdvance(line -> batch.add(IngestorUtils.buildTransaction(line)));
                    if (!elementAdded) {
                        noMoreElements = true;
                        break;
                    }
                }
                //consumer.accept(batch);
                executorService.execute(() -> consumer.accept(batch));
                if (noMoreElements) {
                    break;
                }
            }
        } catch (IOException e) {
            IO.println("ERROR: Failed to read file [" + filePath + "] with error: " + e.getMessage());
        }
    }
}
