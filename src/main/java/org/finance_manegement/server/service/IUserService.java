package org.finance_manegement.server.service;

import org.finance_manegement.server.models.RoleEnum;
import org.finance_manegement.server.models.User;

import java.util.List;

public interface IUserService {
    void register(User user);
    User login(String email, String password);
    User findUserByEmail(String email);
    void updateUser(User user, int id);
    void deleteUser(int id);
    User getUserById(int id);
    List<User> getAllUsers();
}
