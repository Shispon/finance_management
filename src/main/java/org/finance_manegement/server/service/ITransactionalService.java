package org.finance_manegement.server.service;

import org.finance_manegement.server.models.CategoryEnum;
import org.finance_manegement.server.models.Transaction;

import java.util.List;

public interface ITransactionalService {
    void addTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    void updateTransaction(Transaction transaction, int id);
    void deleteTransaction(int id);
    List<Transaction> filterByCategory(CategoryEnum category);
}
