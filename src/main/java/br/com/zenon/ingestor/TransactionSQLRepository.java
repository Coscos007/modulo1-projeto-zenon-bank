package br.com.zenon.ingestor;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TransactionType;
import br.com.zenon.utils.IngestorUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    public Optional<Transaction> getByOriginName(String nameOrig) {
        Transaction transaction = null;
        
        long startTime = System.nanoTime();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transacoes", "root", "senha123")) {
            String selectSql = "SELECT step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud FROM TRANSACTIONS WHERE name_orig = ?";
            PreparedStatement statement = connection.prepareStatement(selectSql);
            statement.setString(1, nameOrig);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int step = resultSet.getInt("step");
                String type = resultSet.getString("type");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                String nameOrigFromDb = resultSet.getString("name_orig");
                BigDecimal oldBalanceOrig = resultSet.getBigDecimal("old_balance_orig");
                BigDecimal newBalanceOrig = resultSet.getBigDecimal("new_balance_orig");
                String nameDest = resultSet.getString("name_dest");
                BigDecimal oldBalanceDest = resultSet.getBigDecimal("old_balance_dest");
                BigDecimal newBalanceDest = resultSet.getBigDecimal("new_balance_dest");
                int isFraud = resultSet.getInt("is_fraud");
                int isFlaggedFraud = resultSet.getInt("is_flagged_fraud");
                transaction = new Transaction(step, TransactionType.valueOf(type), amount,
                        nameOrigFromDb, oldBalanceOrig, newBalanceOrig,
                        nameDest, oldBalanceDest, newBalanceDest,
                        IngestorUtils.parseIntToBoolean(isFraud), IngestorUtils.parseIntToBoolean(isFlaggedFraud));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.nanoTime();
        IO.println("Time to find transaction by nameOrig=[" + nameOrig + "] using MySQL: " + (endTime - startTime));
        return Optional.ofNullable(transaction);
    }

    @Override
    public void save(Transaction transaction) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transacoes", "root", "senha123")) {
            String insertSql = "INSERT INTO TRANSACTIONS (step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSql);
            prepareStatement(statement, transaction);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        IO.println("Saving " + transactions.size() + " transactions to MySQL");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/transacoes?rewriteBatchedStatements=true", "root", "senha123")) {
            connection.setAutoCommit(false);
            String insertSql = "INSERT INTO TRANSACTIONS (step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                for (Transaction transaction : transactions) {
                    prepareStatement(statement, transaction);
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareStatement(PreparedStatement statement, Transaction transaction) throws SQLException {
        statement.setInt(1, transaction.step());
        statement.setString(2, transaction.type().name());
        statement.setBigDecimal(3, transaction.amount());
        statement.setString(4, transaction.nameOrig());
        statement.setBigDecimal(5, transaction.oldBalanceOrig());
        statement.setBigDecimal(6, transaction.newBalanceOrig());
        statement.setString(7, transaction.nameDest());
        statement.setBigDecimal(8, transaction.oldBalanceDest());
        statement.setBigDecimal(9, transaction.newBalanceDest());
        statement.setInt(10, IngestorUtils.parseBooleanToInt(transaction.isFraud()));
        statement.setInt(11, IngestorUtils.parseBooleanToInt(transaction.isFlaggedFraud()));
    }
}
