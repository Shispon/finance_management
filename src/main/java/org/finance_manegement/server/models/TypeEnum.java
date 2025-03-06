package org.finance_manegement.server.models;

import lombok.Getter;

@Getter
public enum TypeEnum {
    PROFIT("Доход"),
    EXPENSES("Расход");

    private final String TypeName;

    TypeEnum(String TypeName) {
        this.TypeName = TypeName;
    }

}
