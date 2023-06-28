package ru.mrcrross.vphotoalbum.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.services.AuthService;
import ru.mrcrross.vphotoalbum.modules.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest{
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_CreateUser_ReturnIDCreateUser() {

        User user = new User();
        user.setAvatar("/img/avatar.svg");
        String password = AuthService.passwordHashing("qweqwe123");
        user.setPassword(password);
        user.setFio("Test Fio User");
        user.setFio("testLogin");
        user.setDateAdd(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int id = userRepository.registration(user);
        User newUser = userRepository.getByID(id);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(user.getFio(), newUser.getFio());
        Assertions.assertEquals(user.getPassword(), newUser.getPassword());
        Assertions.assertEquals(user.getDateAdd(), newUser.getDateAdd());
        Assertions.assertEquals(user.getAvatar(), newUser.getAvatar());
    }
}
