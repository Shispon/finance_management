package org.finance_manegement.console;

import org.finance_manegement.server.models.*;
import org.finance_manegement.server.service.impl.UserInfoService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TransactionConsole {
    private final UserInfoService userInfoService;
    private final Scanner scanner;
    private final User currentUser;

    public TransactionConsole(UserInfoService userInfoService, Scanner scanner, User user) {
        this.userInfoService = userInfoService;
        this.scanner = scanner;
        this.currentUser = user;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\nУправление транзакциями:");
            System.out.println("1. Новая транзакция");
            System.out.println("2. Просмотреть все транзакции");
            System.out.println("3. Фильтровать по категории");
            System.out.println("4. Удалить транзакцию");
            System.out.println("0. Назад");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createTransaction();
                case 2 -> viewAllTransactions();
                case 3 -> filterByCategory();
                case 4 -> deleteTransaction();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор");
            }
        }
    }

    private void createTransaction() {
        System.out.print("Сумма: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Категория (INCOME/EXPENSE): ");
        CategoryEnum category = CategoryEnum.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Тип (INCOME/EXPENSE): ");
        TypeEnum type = TypeEnum.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Описание: ");
        String description = scanner.nextLine();

        Transaction transaction = new Transaction(
                new Date(),
                amount,
                category,
                type,
                description
        );

        userInfoService.addTransactionToUser(currentUser.getId(), transaction);
        System.out.println("Транзакция добавлена!");
    }

    private void viewAllTransactions() {
        List<Transaction> transactions = userInfoService.getTransactionsByUser(currentUser.getId());

        if (transactions.isEmpty()) {
            System.out.println("Транзакций не найдено");
            return;
        }

        System.out.println("Ваши транзакции:");
        transactions.forEach(t ->
                System.out.printf("%d: %.2f | %s | %s | %s%n",
                        t.getId(), t.getAmount(),
                        t.getCategory(), t.getType(),
                        t.getDescription()));
    }

    private void filterByCategory() {
        System.out.print("Введите категорию (INCOME/EXPENSE): ");
        CategoryEnum category = CategoryEnum.valueOf(scanner.nextLine().toUpperCase());

        List<Transaction> filtered = userInfoService.getTransactionsByUser(currentUser.getId()).stream()
                .filter(t -> t.getCategory() == category)
                .toList();

        filtered.forEach(t ->
                System.out.printf("%.2f | %s | %s%n",
                        t.getAmount(), t.getType(), t.getDescription()));
    }

    private void deleteTransaction() {
        System.out.print("Введите ID транзакции: ");
        int id = scanner.nextInt();

        UserInfo userInfo = userInfoService.getUserInfoByUserId(currentUser.getId());
        userInfo.getTransactions().removeIf(t -> t.getId() == id);
        userInfoService.updateUserInfo(userInfo, userInfo.getId());
        System.out.println("Транзакция удалена!");
    }
}
