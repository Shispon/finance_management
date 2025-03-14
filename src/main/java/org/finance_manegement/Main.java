package org.finance_manegement;

import org.finance_manegement.console.MainMenu;
import org.finance_manegement.server.models.User;
import org.finance_manegement.server.service.impl.UserService;
import org.finance_manegement.server.repository.impl.UserRepository;
import org.finance_manegement.server.repository.IRepository;

public class Main {
    public static void main(String[] args) {
        IRepository<User> userRepository = new UserRepository(); // Создайте объект userRepository
        UserService userService = new UserService(userRepository); // Передайте его в UserService
        MainMenu mainMenu = new MainMenu(userService); // Передаем userService в MainMenu
        mainMenu.start(); // Запускаем меню
    }
}
