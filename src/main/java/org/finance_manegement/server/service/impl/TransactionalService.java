package org.finance_manegement.server.service.impl;

import org.finance_manegement.server.models.CategoryEnum;
import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.repository.impl.TransactionRepository;
import org.finance_manegement.server.service.ITransactionalService;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionalService implements ITransactionalService {
    private final TransactionRepository transactionRepository;

    public TransactionalService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @Override
    public void addTransaction(Transaction transaction) {
        transactionRepository.create(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public void updateTransaction(Transaction transaction, int id) {
        transactionRepository.update(transaction, transactionRepository.findById(id).getId());
    }

    @Override
    public void deleteTransaction(int id) {
        transactionRepository.delete(id);
    }

    @Override
    public List<Transaction> filterByCategory(CategoryEnum category) {
        if(category == null) {
            throw new IllegalArgumentException("Категория не может быть null");
        }
        return transactionRepository.findAll().stream().filter(transaction -> category.equals(transaction.getCategory())).collect(Collectors.toList());
    }
}
