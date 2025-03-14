package org.finance_manegement.server.service.impl;

import lombok.Getter;
import org.finance_manegement.server.models.User;

public class Session {
    // Получить текущего пользователя
    @Getter
    private static User currentUser; // Текущий авторизованный пользователь

    // Установить текущего пользователя (при входе)
    public static void login(User user) {
        currentUser = user;
    }

    // Выход из аккаунта
    public static void logout() {
        currentUser = null;
    }
}