
import org.finance_manegement.server.models.CategoryEnum;
import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.models.TypeEnum;
import org.finance_manegement.server.repository.impl.TransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.print.attribute.DateTimeSyntax;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.SAME_THREAD)
public class TransactionRepositoryTest {

    @Container
    private static final PostgresTestContainer postgresContainer = PostgresTestContainer.getInstance();

    private TransactionRepository transactionRepository;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USERNAME"),
                System.getProperty("DB_PASSWORD"));
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE SCHEMA IF NOT EXISTS app");
            statement.execute("CREATE TABLE app.transactions (" +
                    "id SERIAL PRIMARY KEY, " +
                    "date DATE, " +
                    "amount DOUBLE PRECISION, " +
                    "category VARCHAR(255), " +
                    "type VARCHAR(255), " +
                    "description TEXT, " +
                    "user_info_id INTEGER" +
                    ")");
        }
    }

    @BeforeEach
    public void setUp() {
        transactionRepository = new TransactionRepository();
        clearDatabase();
    }

    private void clearDatabase() {
        try (Connection connection = DriverManager.getConnection(
                System.getProperty("DB_URL"),
                System.getProperty("DB_USERNAME"),
                System.getProperty("DB_PASSWORD"));
             Statement statement = connection.createStatement()) {

            statement.execute("TRUNCATE TABLE app.transactions RESTART IDENTITY");
        } catch (Exception e) {
            throw new RuntimeException("Failed to truncate database", e);
        }
    }

    @Test
    public void testCreateAndFindById() {
        Transaction transaction = new Transaction(
                new Timestamp(System.currentTimeMillis()),
                100.0,
                CategoryEnum.FOOD,
                TypeEnum.EXPENSES,
                "Groceries",
                1
        );
        transactionRepository.create(transaction);

        Transaction foundTransaction = transactionRepository.findById(transaction.getId());
        assertNotNull(foundTransaction);
        assertEquals(100.0, foundTransaction.getAmount());
        assertEquals(CategoryEnum.FOOD, foundTransaction.getCategory());
        assertEquals(TypeEnum.EXPENSES, foundTransaction.getType());
        assertEquals("Groceries", foundTransaction.getDescription());
    }

    @Test
    public void testFindAll() {
        Transaction transaction1 = new Transaction(
                new Timestamp(System.currentTimeMillis()),
                100.0,
                CategoryEnum.FOOD,
                TypeEnum.EXPENSES,
                "Groceries",
                1
        );
        Transaction transaction2 = new Transaction(
                new Timestamp(System.currentTimeMillis()),
                200.0,
                CategoryEnum.ENTERTAINMENT,
                TypeEnum.EXPENSES,
                "Flight tickets",
                1
        );

        transactionRepository.create(transaction1);
        transactionRepository.create(transaction2);

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(2, transactions.size());
    }

    @Test
    public void testUpdate() {
        Transaction transaction = new Transaction(
                new Timestamp(System.currentTimeMillis()),
                100.0,
                CategoryEnum.FOOD,
                TypeEnum.EXPENSES,
                "Groceries",
                1
        );
        transactionRepository.create(transaction);

        Transaction updatedTransaction = new Transaction(
                new Timestamp(System.currentTimeMillis()),
                150.0,
                CategoryEnum.ENTERTAINMENT,
                TypeEnum.PROFIT,
                "Updated description",
                1
        );
        transactionRepository.update(updatedTransaction, transaction.getId());

        Transaction foundTransaction = transactionRepository.findById(transaction.getId());
        assertNotNull(foundTransaction);
        assertEquals(150.0, foundTransaction.getAmount());
        assertEquals(CategoryEnum.ENTERTAINMENT, foundTransaction.getCategory());
        assertEquals(TypeEnum.PROFIT, foundTransaction.getType());
        assertEquals("Updated description", foundTransaction.getDescription());
    }

    @Test
    public void testDelete() {
        Transaction transaction = new Transaction(
                new Timestamp(System.currentTimeMillis()),
                100.0,
                CategoryEnum.FOOD,
                TypeEnum.EXPENSES,
                "Groceries",
                1
        );
        transactionRepository.create(transaction);

        transactionRepository.delete(transaction.getId());

        Transaction foundTransaction = transactionRepository.findById(transaction.getId());
        assertNull(foundTransaction);
    }
}