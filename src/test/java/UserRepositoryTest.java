import org.finance_manegement.server.models.RoleEnum;
import org.finance_manegement.server.models.User;
import org.finance_manegement.server.repository.impl.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.SAME_THREAD) // Запрет параллельного выполнения тестов
public class UserRepositoryTest {

    @Container
    private static final PostgresTestContainer postgresContainer = PostgresTestContainer.getInstance();

    private UserRepository userRepository;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USERNAME"),
                System.getProperty("DB_PASSWORD"));
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE SCHEMA IF NOT EXISTS app");
            statement.execute("CREATE TABLE app.user (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255) UNIQUE, " +
                    "password VARCHAR(255), " +
                    "role VARCHAR(50)" +
                    ")");
        }
    }

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
        clearDatabase(); // Очищаем базу данных перед каждым тестом
    }

    private void clearDatabase() {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USERNAME"),
                System.getProperty("DB_PASSWORD"));
             Statement statement = connection.createStatement()) {

            statement.execute("TRUNCATE TABLE app.user RESTART IDENTITY");
            System.out.println("Database truncated successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to truncate database", e);
        }
    }

    @Test
    public void testCreateAndFindById() {
        User user = new User("John Doe", "john@example.com", "password123", RoleEnum.USER);
        userRepository.create(user);

        User foundUser = userRepository.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
        assertEquals("john@example.com", foundUser.getEmail());
    }

    @Test
    public void testFindAll() {
        User user1 = new User("John Doe", "john@example.com", "password123", RoleEnum.USER);
        User user2 = new User("Jane Doe", "jane@example.com", "password456", RoleEnum.ADMIN);

        userRepository.create(user1);
        userRepository.create(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void testUpdate() {
        User user = new User("John Doe", "john@example.com", "password123", RoleEnum.USER);
        userRepository.create(user);

        User updatedUser = new User("John Updated", "john@example.com", "newpassword", RoleEnum.ADMIN);
        userRepository.update(updatedUser, user.getId());

        User foundUser = userRepository.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals("John Updated", foundUser.getName());
        assertEquals(RoleEnum.ADMIN, foundUser.getRole());
    }

    @Test
    public void testDelete() {
        User user = new User("John Doe", "john@example.com", "password123", RoleEnum.USER);
        userRepository.create(user);

        userRepository.delete(user.getId());

        User foundUser = userRepository.findById(user.getId());
        assertNull(foundUser);
    }

    @Test
    public void testFindByEmail() {
        User user = new User("John Doe", "john@example.com", "password123", RoleEnum.USER);
        userRepository.create(user);

        boolean exists = userRepository.findByEmail("john@example.com");
        assertTrue(exists);

        boolean notExists = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(notExists);
    }
}