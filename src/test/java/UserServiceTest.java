import org.finance_manegement.server.models.RoleEnum;
import org.finance_manegement.server.models.User;
import org.finance_manegement.server.repository.impl.UserRepository;
import org.finance_manegement.server.service.exception.UserException;
import org.finance_manegement.server.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Мокируем репозиторий
        userRepository = mock(UserRepository.class);
        // Передаем мок в конструктор
        userService = new UserService(userRepository);
    }

    @Test
    void testRegisterUser() {
        User user = new User("John Doe", "john.doe@example.com", "password123", RoleEnum.USER);

        // Сохраняем пользователя
        userService.register(user);

        // Проверяем, что метод create был вызван с правильным аргументом
        verify(userRepository, times(1)).create(user);
    }

    @Test
    void testLoginSuccessful() {
        User user = new User("John Doe", "john.doe@example.com", "password123", RoleEnum.USER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        // Пытаемся войти
        User loggedInUser = userService.login("john.doe@example.com", "password123");

        // Проверяем, что вход прошел успешно
        assertEquals(user, loggedInUser);
    }

    @Test
    void testLoginWithWrongPassword() {
        User user = new User("John Doe", "john.doe@example.com", "password123", RoleEnum.USER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        // Проверяем неправильный пароль
        assertThrows(IllegalArgumentException.class, () -> userService.login("john.doe@example.com", "wrongpassword"));
    }

    @Test
    void testLoginWithNonExistingEmail() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Проверяем, что будет выброшено исключение при неправильном email
        assertThrows(UserException.class, () -> userService.login("non.existing@example.com", "password123"));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User("John Doe", "john.doe@example.com", "password123", RoleEnum.USER);
        User user2 = new User("Jane Doe", "jane.doe@example.com", "password456", RoleEnum.USER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Проверяем, что метод возвращает всех пользователей
        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testUpdateUser() {
        // Создаем пользователя, который будет обновляться
        User user = new User("John Doe", "john.doe@example.com", "password123", RoleEnum.USER);

        // Мокируем поведение репозитория: метод findById должен вернуть созданного пользователя
        when(userRepository.findById(user.getId())).thenReturn(user);

        // Новый объект пользователя, с которым мы будем обновлять старого
        User updatedUser = new User("John Updated", "john.doe@example.com", "newpassword123", RoleEnum.ADMIN);

        // Выполняем обновление
        userService.updateUser(updatedUser, user.getId());

        // Проверяем, что метод update был вызван один раз с правильными параметрами
        verify(userRepository, times(1)).update(updatedUser, user.getId());
    }


    @Test
    void testDeleteUser() {
        User user = new User("John Doe", "john.doe@example.com", "password123", RoleEnum.USER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        // Удаляем пользователя
        userService.deleteUser(user.getId());

        // Проверяем, что метод delete был вызван
        verify(userRepository, times(1)).delete(user.getId());
    }



}
