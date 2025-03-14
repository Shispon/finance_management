import org.finance_manegement.server.models.CategoryEnum;
import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.models.TypeEnum;
import org.finance_manegement.server.repository.impl.TransactionRepository;
import org.finance_manegement.server.service.impl.TransactionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionalServiceTest {

    private TransactionalService transactionalService;
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        // Мокируем репозиторий
        transactionRepository = mock(TransactionRepository.class);
        // Передаем мок в конструктор сервиса
        transactionalService = new TransactionalService( transactionRepository);
    }

    @Test
    void testAddTransaction() {
        // Создаем транзакцию с указанным типом и категорией
        Transaction transaction = new Transaction(new Date(), 100.0, CategoryEnum.FOOD, TypeEnum.EXPENSES, "Groceries Purchase");

        // Сохраняем транзакцию
        transactionalService.addTransaction(transaction);

        // Проверяем, что метод create был вызван
        verify(transactionRepository, times(1)).create(transaction);
    }

    @Test
    void testGetAllTransactions() {
        // Создаем несколько транзакций
        Transaction transaction1 = new Transaction(new Date(), 100.0, CategoryEnum.FOOD, TypeEnum.EXPENSES, "Groceries Purchase");
        Transaction transaction2 = new Transaction(new Date(), 200.0, CategoryEnum.TRANSPORT, TypeEnum.EXPENSES, "Bus Ticket");
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        // Получаем все транзакции
        List<Transaction> transactions = transactionalService.getAllTransactions();

        // Проверяем, что транзакции возвращены корректно
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    void testDeleteTransaction() {
        // Создаем транзакцию для удаления
        Transaction transaction = new Transaction(new Date(), 100.0, CategoryEnum.FOOD, TypeEnum.EXPENSES, "Groceries Purchase");
        when(transactionRepository.findById(1)).thenReturn(transaction);

        // Удаляем транзакцию
        transactionalService.deleteTransaction(1);

        // Проверяем, что метод delete был вызван
        verify(transactionRepository, times(1)).delete(1);
    }

    @Test
    void testFilterByCategory() {
        // Создаем несколько транзакций с разными категориями
        Transaction transaction1 = new Transaction(new Date(), 100.0, CategoryEnum.FOOD, TypeEnum.EXPENSES, "Groceries Purchase");
        Transaction transaction2 = new Transaction(new Date(), 200.0, CategoryEnum.TRANSPORT, TypeEnum.EXPENSES, "Bus Ticket");
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        // Фильтруем транзакции по категории "Еда"
        List<Transaction> filteredTransactions = transactionalService.filterByCategory(CategoryEnum.FOOD);

        // Проверяем, что фильтрация работает правильно
        assertEquals(1, filteredTransactions.size());
        assertTrue(filteredTransactions.contains(transaction1));
        assertFalse(filteredTransactions.contains(transaction2));
    }

    @Test
    void testFilterByCategoryWithNullCategory() {
        // Проверяем, что при null категории выбрасывается IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> transactionalService.filterByCategory(null));
    }

    @Test
    void testFilterByType() {
        // Создаем несколько транзакций с разными типами
        Transaction transaction1 = new Transaction(new Date(), 100.0, CategoryEnum.FOOD, TypeEnum.EXPENSES, "Groceries Purchase");
        Transaction transaction2 = new Transaction(new Date(), 200.0, CategoryEnum.TRANSPORT, TypeEnum.PROFIT, "Transport Payment");
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        // Фильтруем транзакции по типу "Доход"
        List<Transaction> filteredTransactions = transactionalService.filterByCategory(CategoryEnum.FOOD);

        // Проверяем, что фильтрация работает правильно
        assertEquals(1, filteredTransactions.size());
        assertTrue(filteredTransactions.contains(transaction1));
    }
}
