package org.finance_manegement.console;

import org.finance_manegement.server.models.Purpose;
import org.finance_manegement.server.models.User;
import org.finance_manegement.server.models.UserInfo;
import org.finance_manegement.server.service.impl.UserInfoService;
import java.util.List;
import java.util.Scanner;

public class PurposeConsole {
    private final UserInfoService userInfoService;
    private final Scanner scanner;
    private final User currentUser;

    public PurposeConsole(UserInfoService userInfoService, Scanner scanner, User user) {
        this.userInfoService = userInfoService;
        this.scanner = scanner;
        this.currentUser = user;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\nУправление целями:");
            System.out.println("1. Создать цель");
            System.out.println("2. Просмотреть все цели");
            System.out.println("3. Добавить средства к цели");
            System.out.println("4. Отслеживание прогресса");
            System.out.println("5. Удалить цель");
            System.out.println("0. Назад");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createPurpose();
                case 2 -> viewAllPurposes();
                case 3 -> addMoneyToPurpose();
                case 4 -> trackProgress();
                case 5 -> deletePurpose();
                case 0 -> { return; }
                default -> System.out.println("Неверный выбор");
            }
        }
    }

    private void createPurpose() {
        System.out.print("Название цели: ");
        String name = scanner.nextLine();
        System.out.print("Целевая сумма: ");
        double target = scanner.nextDouble();

        Purpose purpose = new Purpose(name, target, 0.0);
        userInfoService.addPurposeToUser(currentUser.getId(), purpose);
        System.out.println("Цель создана!");
    }

    private void viewAllPurposes() {
        List<Purpose> purposes = userInfoService.getPurposesByUser(currentUser.getId());

        if (purposes.isEmpty()) {
            System.out.println("Целей не найдено");
            return;
        }

        System.out.println("Ваши цели:");
        purposes.forEach(p ->
                System.out.printf("%d: %s%nТекущая сумма: %.2f / %.2f%n",
                        p.getId(), p.getName(),
                        p.getCurrentAmount(), p.getTargetAmount()));
    }

    private void addMoneyToPurpose() {
        System.out.print("Введите ID цели: ");
        int id = scanner.nextInt();
        System.out.print("Сумма для добавления: ");
        double amount = scanner.nextDouble();

        List<Purpose> purposes = userInfoService.getPurposesByUser(currentUser.getId());
        Purpose purpose = purposes.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow();

        purpose.setCurrentAmount(purpose.getCurrentAmount() + amount);
        userInfoService.updateUserInfo(
                userInfoService.getUserInfoByUserId(currentUser.getId()),
                userInfoService.getUserInfoByUserId(currentUser.getId()).getId()
        );
        System.out.printf("Средства добавлены! Текущая сумма: %.2f%n", purpose.getCurrentAmount());
    }

    private void trackProgress() {
        System.out.print("Введите ID цели: ");
        int id = scanner.nextInt();

        Purpose purpose = userInfoService.getPurposesByUser(currentUser.getId()).stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow();

        double progress = (purpose.getCurrentAmount() / purpose.getTargetAmount()) * 100;
        System.out.printf("Прогресс цели '%s': %.2f%%%n", purpose.getName(), progress);
    }

    private void deletePurpose() {
        System.out.print("Введите ID цели для удаления: ");
        int id = scanner.nextInt();

        UserInfo userInfo = userInfoService.getUserInfoByUserId(currentUser.getId());
        userInfo.getPurposes().removeIf(p -> p.getId() == id);
        userInfoService.updateUserInfo(userInfo, userInfo.getId());
        System.out.println("Цель удалена!");
    }
}