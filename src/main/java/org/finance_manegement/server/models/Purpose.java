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

    public Purpose(String name, double targetAmount, double currentAmount) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }
}
