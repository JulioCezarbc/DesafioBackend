package com.julioc.desafioBackend.service;

import com.julioc.desafioBackend.domain.user.User;
import com.julioc.desafioBackend.domain.user.UserType;
import com.julioc.desafioBackend.dtos.TransactionDto;
import com.julioc.desafioBackend.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TransactionServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private TransactionRepository repository;
    @Mock
    private NotificationService notification;
    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is ok")
    void createTransactionCase1() throws Exception {
        User sender = new User(1L, "teste", "soza", "99999999901", "souza@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "maria", "teste", "99999999902", "maria@gmail.com", "123456", new BigDecimal(10), UserType.COMMON);


        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        TransactionDto request = new TransactionDto(new BigDecimal(10), sender.getId(),receiver.getId());
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notification,times(1)).sendNotification(sender,"Successful transaction");
        verify(notification,times(1)).sendNotification(receiver,"Successful transaction");
    }
    @Test
    @DisplayName("Should throw Exception when transaction is not allowed ")
    void createTransactionCase2() throws Exception {
        User sender = new User(1L, "teste", "soza", "99999999901", "souza@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "maria", "teste", "99999999902", "maria@gmail.com", "123456", new BigDecimal(10), UserType.COMMON);


        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        
    }
}