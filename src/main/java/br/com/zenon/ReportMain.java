package br.com.zenon;

import br.com.zenon.report.Language;
import br.com.zenon.report.TransactionReport;

public class ReportMain {
    static void main() {
        var report = new TransactionReport();

        String filePath = "./data/PS_20174392719_1491204439457_log.csv";
        report.report(filePath, Language.PORTUGUESE);
        report.report(filePath, Language.ENGLISH);
    }
}
