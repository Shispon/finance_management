package org.finance_manegement.server.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Purpose {
    private int id;
    private String name;
    private double targetAmount; // Целевая сумма
    private double currentAmount; // Текущая сумма
    private int userInfoId;
    public Purpose(String name, double targetAmount, double currentAmount, int userInfoId) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.userInfoId = userInfoId;
    }
}
