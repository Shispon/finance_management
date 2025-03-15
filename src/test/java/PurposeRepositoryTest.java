import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.repository.impl.PurposeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.SAME_THREAD)
public class PurposeRepositoryTest {

    @Container
    private static final PostgresTestContainer postgresContainer = PostgresTestContainer.getInstance();

    private PurposeRepository purposeRepository;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USERNAME"),
                System.getProperty("DB_PASSWORD"));
             Statement statement = connection.createStatement()) {

            // Создаем схему app
            statement.execute("CREATE SCHEMA IF NOT EXISTS app");

            // Создаем таблицу app.purposes
            statement.execute("CREATE TABLE app.purposes (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "target_amount DOUBLE PRECISION NOT NULL, " +
                    "current_amount DOUBLE PRECISION NOT NULL, " +
                    "user_info_id INTEGER NOT NULL" +
                    ")");
        }
    }

    @BeforeEach
    public void setUp() {
        purposeRepository = new PurposeRepository();
        clearDatabase();
    }

    private void clearDatabase() {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USERNAME"),
                System.getProperty("DB_PASSWORD"));
             Statement statement = connection.createStatement()) {

            statement.execute("TRUNCATE TABLE app.purposes RESTART IDENTITY");
        } catch (Exception e) {
            throw new RuntimeException("Failed to truncate database", e);
        }
    }

    @Test
    public void testCreateAndFindById() {
        Purpose purpose = new Purpose(
                "Vacation",
                5000.0,
                1000.0,
                1
        );
        purposeRepository.create(purpose);

        Purpose foundPurpose = purposeRepository.findById(purpose.getId());
        assertNotNull(foundPurpose);
        assertEquals("Vacation", foundPurpose.getName());
        assertEquals(5000.0, foundPurpose.getTargetAmount());
        assertEquals(1000.0, foundPurpose.getCurrentAmount());
        assertEquals(1, foundPurpose.getUserInfoId());
    }

    @Test
    public void testFindAll() {
        Purpose purpose1 = new Purpose(
                "Vacation",
                5000.0,
                1000.0,
                1
        );
        Purpose purpose2 = new Purpose(
                "Car",
                10000.0,
                2000.0,
                1
        );

        purposeRepository.create(purpose1);
        purposeRepository.create(purpose2);

        List<Purpose> purposes = purposeRepository.findAll();
        assertEquals(2, purposes.size());
    }

    @Test
    public void testUpdate() {
        Purpose purpose = new Purpose(
                "Vacation",
                5000.0,
                1000.0,
                1
        );
        purposeRepository.create(purpose);

        Purpose updatedPurpose = new Purpose(
                "Updated Vacation",
                6000.0,
                1500.0,
                2
        );
        purposeRepository.update(updatedPurpose, purpose.getId());

        Purpose foundPurpose = purposeRepository.findById(purpose.getId());
        assertNotNull(foundPurpose);
        assertEquals("Updated Vacation", foundPurpose.getName());
        assertEquals(6000.0, foundPurpose.getTargetAmount());
        assertEquals(1500.0, foundPurpose.getCurrentAmount());
        assertEquals(2, foundPurpose.getUserInfoId());
    }

    @Test
    public void testDelete() {
        Purpose purpose = new Purpose(
                "Vacation",
                5000.0,
                1000.0,
                1
        );
        purposeRepository.create(purpose);

        purposeRepository.delete(purpose.getId());

        Purpose foundPurpose = purposeRepository.findById(purpose.getId());
        assertNull(foundPurpose);
    }
}