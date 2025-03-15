package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.models.UserInfo;
import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserInfoRepository implements IRepository<UserInfo> {

    private static final String DB_URL = DatabaseConfig.getDbUrl();
    private static final String DB_USERNAME = DatabaseConfig.getDbUsername();
    private static final String DB_PASSWORD = DatabaseConfig.getDbPassword();

    @Override
    public void create(UserInfo userInfo) {
        String sql = "INSERT INTO app.user_info (user_id, monthly_budget) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Начало транзакции
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, userInfo.getUserId());
                statement.setDouble(2, userInfo.getMonthlyBudget());

                statement.executeUpdate();

                // Получаем сгенерированный ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userInfo.setId(generatedKeys.getInt(1));
                    }
                }

                // Завершение транзакции
                connection.commit();
            } catch (SQLException e) {
                // Откат транзакции при ошибке
                connection.rollback();
                throw new RuntimeException("Failed to create user info", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user info", e);
        }
    }

    @Override
    public List<UserInfo> findAll() {
        List<UserInfo> userInfos = new ArrayList<>();
        String sql = "SELECT * FROM app.user_info";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                UserInfo userInfo = new UserInfo(
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("monthly_budget")
                );
                userInfo.setId(resultSet.getInt("id"));
                userInfos.add(userInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user infos", e);
        }
        return userInfos;
    }

    @Override
    public UserInfo findById(int id) {
        String sql = "SELECT * FROM app.user_info WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserInfo userInfo = new UserInfo(
                            resultSet.getInt("user_id"),
                            resultSet.getDouble("monthly_budget")
                    );
                    userInfo.setId(resultSet.getInt("id"));
                    return userInfo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user info by ID", e);
        }
        return null;
    }

    @Override
    public void update(UserInfo userInfo, int id) {
        String sql = "UPDATE app.user_info SET user_id = ?, monthly_budget = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Начало транзакции
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userInfo.getUserId());
                statement.setDouble(2, userInfo.getMonthlyBudget());
                statement.setInt(3, id);

                statement.executeUpdate();

                // Завершение транзакции
                connection.commit();
            } catch (SQLException e) {
                // Откат транзакции при ошибке
                connection.rollback();
                throw new RuntimeException("Failed to update user info", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user info", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM app.user_info WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Начало транзакции
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();

                // Завершение транзакции
                connection.commit();
            } catch (SQLException e) {
                // Откат транзакции при ошибке
                connection.rollback();
                throw new RuntimeException("Failed to delete user info", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete user info", e);
        }
    }

    public boolean existsByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM app.user_info WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check user info existence", e);
        }
        return false;
    }
}