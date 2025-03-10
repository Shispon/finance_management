package org.finance_manegement.server.repository.impl;

import org.finance_manegement.server.models.User;
import org.finance_manegement.server.repository.IRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository implements IRepository<User> {
    private static AtomicInteger nextUserId = new AtomicInteger(1); // Потокобезопасный счетчик
    private List<User> users = new ArrayList<>();
    @Override
    public void create(User user) {
        int id = nextUserId.getAndIncrement();
        user.setId(id);
        users.add(user);
    }

    @Override
    public List findAll() {
        return users;
    }

    @Override
    public User findById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void update(User user,int id) {
        User existingUser = findById(id);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
        }
    }

    @Override
    public void delete(int id) {
        User userToDelete = findById(id);
        if (userToDelete != null) {
            users.remove(userToDelete);
        }
    }

    public boolean findByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().isPresent();
    }
}
