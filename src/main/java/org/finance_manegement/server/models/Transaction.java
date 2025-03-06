package org.finance_manegement.server.models;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Transaction {
    private int id;
    private Date date;
    private Double amount; //Сумма
    private CategoryEnum category; // Категория транзакции
    private TypeEnum type; // тип транзакции(доход/расход)
    private String description; // описание операции

    public Transaction(Date date, Double amount, CategoryEnum category, TypeEnum type, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.description = description;
    }
}
