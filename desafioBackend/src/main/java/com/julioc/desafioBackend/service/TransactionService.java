package com.julioc.desafioBackend.service;

import com.julioc.desafioBackend.domain.transaction.Transaction;
import com.julioc.desafioBackend.domain.user.User;
import com.julioc.desafioBackend.dtos.TransactionDto;
import com.julioc.desafioBackend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionDto transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validationTransaction(sender,transaction.value());
        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized){
            throw new Exception("Transaction not authorized");
        }
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse= restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        if (authorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) authorizationResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }
        return false;
    }
}
