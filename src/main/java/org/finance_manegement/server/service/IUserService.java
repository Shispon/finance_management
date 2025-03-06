package org.finance_manegement.server.service;

import org.finance_manegement.server.models.User;

import java.util.List;

public interface IUserService {
    void register(User user);
    User login(String email, String password);
    void updateUser(User user);
    void deleteUser(User user);
    User getUserById(int id);
    List<User> getAllUsers(String role);
}
