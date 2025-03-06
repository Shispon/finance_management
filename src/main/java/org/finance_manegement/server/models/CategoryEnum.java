package org.finance_manegement.server.models;

import lombok.Getter;

@Getter
public enum CategoryEnum {
    FOOD("Еда"),
    TRANSPORT("Транспорт"),
    ENTERTAINMENT("Развлечения"),
    UTILITY("Коммунальные услуги"),
    SHOPPING("Покупки"),
    HEALTH("Здоровье"),
    EDUCATION("Образование"),
    OTHER("Прочее");

    private final String displayName;

    CategoryEnum(String displayName) {
        this.displayName = displayName;
    }

}

