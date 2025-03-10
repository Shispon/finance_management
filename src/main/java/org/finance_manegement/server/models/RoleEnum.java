package org.finance_manegement.server.models;

import lombok.Getter;


@Getter
public enum RoleEnum {
    USER("Пользователь"),
    ADMIN("Администратор");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

}
