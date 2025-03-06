package org.finance_manegement.server.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private RoleEnum role;

    public User(String name, String email, String password, RoleEnum role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
