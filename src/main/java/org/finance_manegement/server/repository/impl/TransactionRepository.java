package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.models.Transaction;
import org.finance_manegement.server.models.UserInfo;
import org.finance_manegement.server.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionRepository implements IRepository<Transaction> {
    private static AtomicInteger nextTransactionId = new AtomicInteger(1); // Потокобезопасный счетчик
    private List<Transaction> transactions = new ArrayList<>();
    @Override
    public void create(Transaction transaction) {
        transaction.setId(nextTransactionId.getAndIncrement());
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return transactions;
    }

    @Override
    public Transaction findById(int id) {
        return transactions.stream().filter(transaction -> transaction.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(Transaction transaction, int id) {
        Transaction updatedTransaction = findById(id);
        if (updatedTransaction != null) {
            updatedTransaction.setAmount(transaction.getAmount());
            updatedTransaction.setDate(transaction.getDate());
            updatedTransaction.setDescription(transaction.getDescription());
            updatedTransaction.setType(transaction.getType());
            updatedTransaction.setCategory(transaction.getCategory());
        }
    }

    @Override
    public void delete(int id) {
        transactions.removeIf(transaction -> transaction.getId() == id);
    }
}
