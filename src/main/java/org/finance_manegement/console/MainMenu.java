package org.finance_manegement.console;

import org.finance_manegement.server.repository.IRepository;
import org.finance_manegement.server.repository.impl.UserRepository;
import org.finance_manegement.server.service.impl.*;
import org.finance_manegement.server.models.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final UserInfoService userInfoService = new UserInfoService();

    public MainMenu(UserService userService) {
        this.userService = userService;
    }


    public void start() {
        while (true) {
            System.out.println("\nГлавное меню:");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            System.out.println("0. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 0 -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор");
            }
        }
    }

    private void registerUser() {
        System.out.print("Имя: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        System.out.print("Роль (ADMIN/USER): ");
        RoleEnum role = RoleEnum.valueOf(scanner.nextLine().toUpperCase());

        User user = new User(name, email, password, role);
        userService.register(user);

        // Создаем UserInfo для нового пользователя
        UserInfo userInfo = new UserInfo(
                user.getId(),
                0.0
        );
        userInfoService.createUserInfo(userInfo);

        System.out.println("Регистрация успешна!");
    }

    private void loginUser() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        try {
            User user = userService.login(email, password);
            System.out.println("Вход выполнен! Добро пожаловать, " + user.getName());
            showUserMenu(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showUserMenu(User user) {
        while (Session.getCurrentUser() != null) {
            System.out.println("\nМеню пользователя:");
            System.out.println("1. Управление целями");
            System.out.println("2. Управление транзакциями");
            System.out.println("3. Просмотр баланса");
            System.out.println("4. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> new PurposeConsole(userInfoService, scanner, user).showMenu();
                case 2 -> new TransactionConsole(userInfoService, scanner, user).showMenu();
                case 3 -> showBalance(user);
                case 4 -> userService.logout();
                default -> System.out.println("Неверный выбор");
            }
        }
    }

    private void showBalance(User user) {
        UserInfo info = userInfoService.getUserInfoByUserId(user.getId());
        System.out.printf("Ваш баланс: %.2f%n", info.getMonthlyBudget());
    }
}