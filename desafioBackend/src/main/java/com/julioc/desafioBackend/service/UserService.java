package com.julioc.desafioBackend.service;

import com.julioc.desafioBackend.domain.user.User;
import com.julioc.desafioBackend.domain.user.UserType;
import com.julioc.desafioBackend.dtos.UserDto;
import com.julioc.desafioBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validationTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Merchant user not authorized");
        }
        if (sender.getBalance().compareTo(amount) <0){
            throw new Exception("insufficient funds");
        }
    }
    public User createUser(UserDto data ){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(() -> new Exception("User not found"));
    }
    public void saveUser(User user){
        this.userRepository.save(user);
    }
    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }
}
