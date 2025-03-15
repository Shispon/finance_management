package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.config.DatabaseConfig;
import org.finance_manegement.server.models.CategoryEnum;
import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.models.TypeEnum;
import org.finance_manegement.server.repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TransactionRepository implements IRepository<Transaction> {
    private static final String DB_URL = DatabaseConfig.getDbUrl();
    private static final String DB_USERNAME = DatabaseConfig.getDbUsername();
    private static final String DB_PASSWORD = DatabaseConfig.getDbPassword();
    @Override
    public void create(Transaction transaction) {
        String sql = "INSERT INTO app.transactions (date, amount, category, type, description, user_info_id) VALUES (?, ?, ?, ?, ?, ?)";;
        try(Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD)) {
            connection.setAutoCommit(false);
            try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setTimestamp(1, transaction.getDate());
                statement.setDouble(2,transaction.getAmount());
                statement.setString(3, String.valueOf(transaction.getCategory()));
                statement.setString(4, String.valueOf(transaction.getType()));
                statement.setString(5, transaction.getDescription());
                statement.setInt(6, transaction.getUserInfoId());

                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if(generatedKeys.next()) {
                        transaction.setId(generatedKeys.getInt(1));
                    }
                }

                connection.commit();
            }catch (SQLException e){
                connection.rollback();
                throw new RuntimeException("Failed to create transactional",e);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Failed to create transactional",e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM app.transactions";
        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while(resultSet.next()) {
                Transaction transaction = new Transaction(
                resultSet.getTimestamp("date"),
                resultSet.getDouble("amount"),
                CategoryEnum.valueOf(resultSet.getString("category")),
                TypeEnum.valueOf(resultSet.getString("type")),
                resultSet.getString("description"),
                resultSet.getInt("user_info_id")
                );
                transaction.setId(resultSet.getInt("id"));
                transactions.add(transaction);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Failed to fetch transactions",e);
        }
        return transactions;
    }

    @Override
    public Transaction findById(int id) {
        String sql = "SELECT * FROM app.transactions WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    Transaction transaction = new Transaction(
                            resultSet.getTimestamp("date"),
                            resultSet.getDouble("amount"),
                            CategoryEnum.valueOf(resultSet.getString("category")),
                            TypeEnum.valueOf(resultSet.getString("type")),
                            resultSet.getString("description"),
                            resultSet.getInt("user_info_id")
                    );
                    transaction.setId(resultSet.getInt("id"));
                    return transaction;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to fetch transaction",e);
        }
        return null;
    }

    @Override
    public void update(Transaction transaction, int id) {
        String sql = "UPDATE app.transactions SET date = ?, amount = ?, category = ?, type = ?, description = ?, user_info_id = ? WHERE id = ? ";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setTimestamp(1, transaction.getDate());
                statement.setDouble(2, transaction.getAmount());
                statement.setString(3, String.valueOf(transaction.getCategory()));
                statement.setString(4, String.valueOf(transaction.getType()));
                statement.setString(5, transaction.getDescription());
                statement.setInt(6, transaction.getUserInfoId());
                statement.setInt(7, id);

                statement.executeUpdate();

                connection.commit();
            }catch (SQLException e) {
                throw new RuntimeException("Failed to update transaction",e);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Failed to update transaction",e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM app.transactions WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete transactional", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete transactional", e);
        }
    }
}
