package com.julioc.desafioBackend.repository;

import com.julioc.desafioBackend.domain.user.User;
import com.julioc.desafioBackend.domain.user.UserType;
import com.julioc.desafioBackend.dtos.UserDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DataBase")
    void findUserByDocumentSuccess() {
        String document = "99999999901";
        UserDto data = new UserDto("julio","teste",document,new BigDecimal(10), "teste@gmai.com", "12345", UserType.COMMON);
        this.createUser(data);

        Optional<User> result = this.repository.findUserByDocument(document);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get User from DataBase when user not exists")
    void findUserByDocumentFail() {
        String document = "99999999901";

        Optional<User> result = this.repository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();

    }

    private User createUser(UserDto data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}