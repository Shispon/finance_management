    package org.finance_manegement.server.service.impl;

    import org.finance_manegement.server.models.User;
    import org.finance_manegement.server.service.IUserService;
    import org.finance_manegement.server.repository.impl.UserRepository;
    import org.finance_manegement.server.repository.IRepository;
    import org.finance_manegement.server.service.exception.UserException;

    import java.util.List;

    public class UserService implements IUserService  {
        private final IRepository<User> userRepository;

        // Новый конструктор для внедрения зависимости
        public UserService(IRepository<User> userRepository) {
            this.userRepository = userRepository;
        }
        @Override
        public User login(String email, String password) {
            User user = findUserByEmail(email);
            if (user == null) {
                throw new UserException("Пользователь с данным email не найден");
            }
            if (!user.getPassword().equals(password)) {
                throw new IllegalArgumentException("Неверный пароль");
            }
            Session.login(user);
            return user;
        }
        public User findUserByEmail(String email) {
           return userRepository.findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        }
        public void getCurrentuser () {
            Session.getCurrentUser();
        }
        public void logout() {
            Session.logout();
            System.out.println("Выход выполнен");
        }
        @Override
        public void updateUser(User user,int id) {
            userRepository.update(user,userRepository.findById(id).getId());
        }

        @Override
        public void register(User user) {
            userRepository.create(user);
        }

        @Override
        public void deleteUser(int id) {
            try {
                userRepository.delete(id);
            }catch (IllegalArgumentException e) {
                throw new UserException("Такого пользователя не существует");
            }
        }

        @Override
        public User getUserById(int id) {
            return userRepository.findAll().stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        }

        @Override
        public List<User> getAllUsers() {
            return userRepository.findAll();
        }
    }
