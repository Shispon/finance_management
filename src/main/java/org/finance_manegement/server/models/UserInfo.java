package org.finance_manegement.server.models;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UserInfo {
    private int id;
    private int userId;
    private List<Transaction> transactions; // Список операций
    private Double monthlyBudget; // Месячный бюджет
    private List<Purpose> purposes; // Список целей пользователя

    public UserInfo(int userId, List<Transaction> transactions, Double monthlyBudget, List<Purpose> purposes) {
        this.userId = userId;
        this.transactions = transactions;
        this.monthlyBudget = monthlyBudget;
        this.purposes = purposes;
    }
}
