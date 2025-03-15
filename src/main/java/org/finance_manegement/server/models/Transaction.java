package org.finance_manegement.server.models;

import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
public class Transaction {
    private int id;
    private Timestamp date;
    private Double amount; //Сумма
    private CategoryEnum category; // Категория транзакции
    private TypeEnum type; // тип транзакции(доход/расход)
    private String description; // описание операции
    private int userInfoId;
    public Transaction(Timestamp date, Double amount, CategoryEnum category, TypeEnum type, String description, int user_info_id) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.description = description;
        this.userInfoId = user_info_id;
    }
}
