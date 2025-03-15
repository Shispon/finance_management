package org.finance_manegement.server.models;

import lombok.Data;
import lombok.ToString;



@Data
@ToString
public class UserInfo {
    private int id;
    private int userId;
    private Double monthlyBudget; // Месячный бюджет

    public UserInfo(int userId, Double monthlyBudget) {
        this.userId = userId;
        this.monthlyBudget = monthlyBudget;
    }
}
