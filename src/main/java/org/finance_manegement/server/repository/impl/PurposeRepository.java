package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.config.DatabaseConfig;
import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.repository.IRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PurposeRepository implements IRepository<Purpose> {
    private static final String DB_URL = DatabaseConfig.getDbUrl();
    private static final String DB_USERNAME = DatabaseConfig.getDbUsername();
    private static final String DB_PASSWORD = DatabaseConfig.getDbPassword();
    @Override
    public void create(Purpose purpose) {
      String sql = "INSERT INTO app.purposes (name, target_amount, current_amount, user_info_id) VALUES (?, ?, ?, ?)";
      try(Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD)) {
          connection.setAutoCommit(false);
          try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
              statement.setString(1, purpose.getName());
              statement.setDouble(2, purpose.getTargetAmount());
              statement.setDouble(3, purpose.getCurrentAmount());
              statement.setInt(4, purpose.getUserInfoId());

              statement.executeUpdate();

              try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                  if (generatedKeys.next()) {
                      purpose.setId(generatedKeys.getInt(1));
                  }
              }

              connection.commit();
          }catch (SQLException e) {
              connection.rollback();
              throw new RuntimeException("Failed to create purpose",e);
          }
      }catch (SQLException e) {
          throw new RuntimeException("Failed to create purpose",e);
      }
    }

    @Override
    public List<Purpose> findAll() {
        List<Purpose> purposes = new ArrayList<>();
        String sql = "SELECT * FROM app.purposes";

        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            while(resultSet.next()) {
                Purpose purpose = new Purpose(
                        resultSet.getString("name"),
                        resultSet.getDouble("target_amount"),
                        resultSet.getDouble("current_amount"),
                        resultSet.getInt("user_info_id")
                );
                purpose.setId(resultSet.getInt("id"));
                purposes.add(purpose);
            }
        }catch (SQLException e) {
            throw new RuntimeException("Failed to fetch purposes",e);
        }
        return purposes;
    }

    @Override
    public Purpose findById(int id) {
        String sql = "SELECT * FROM app.purposes WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    Purpose purpose = new Purpose(
                            resultSet.getString("name"),
                            resultSet.getDouble("target_amount"),
                            resultSet.getDouble("current_amount"),
                            resultSet.getInt("user_info_id")
                    );
                    purpose.setId(resultSet.getInt("id"));
                    return purpose;
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Failed to fetch purpose",e);
        }
        return null;
    }

    @Override
    public void update(Purpose purpose, int id) {
       String sql = "UPDATE app.purposes SET name = ?, target_amount = ?, current_amount = ?, user_info_id = ? WHERE id = ?";
       try (Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD)){
           connection.setAutoCommit(false);

           try (PreparedStatement statement = connection.prepareStatement(sql)) {
               statement.setString(1, purpose.getName());
               statement.setDouble(2, purpose.getTargetAmount());
               statement.setDouble(3, purpose.getCurrentAmount());
               statement.setInt(4, purpose.getUserInfoId());
               statement.setInt(5,id);

               statement.executeUpdate();

               connection.commit();
           }catch (SQLException e) {
               connection.rollback();
               throw new RuntimeException("Failed to update purpose",e);
           }
       }catch (SQLException e) {
           throw new RuntimeException("Failed to update purpose", e);
       }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM app.purposes WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to delete purpose", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete purpose", e);
        }
    }
}
