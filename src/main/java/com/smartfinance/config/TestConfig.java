package com.smartfinance.config;

import com.smartfinance.entities.Account;
import com.smartfinance.entities.Movement;
import com.smartfinance.entities.User;
import com.smartfinance.entities.enums.AccountType;
import com.smartfinance.repositories.AccountRepository;
import com.smartfinance.repositories.MovementRepository;
import com.smartfinance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementRepository movementRepository;

    @Override
    public void run(String... args) throws Exception {

        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com","123456");

        Account a1 = new Account(null, "Nubank", 2500.00, AccountType.CHECKING, u1);
        Account a2 = new Account(null, "Bradesco", 8500.00, AccountType.SAVINGS, u1);
        Account a3 = new Account(null, "Carteira", 350.00, AccountType.CASH, u2);

        Movement m1 = new Movement(null, "Supermercado", 800.00, Instant.now(), a1);
        Movement m2 = new Movement(null, "Farmácia", 100.00, Instant.now(), a3);

        userRepository.saveAll(Arrays.asList(u1, u2));

        accountRepository.saveAll(Arrays.asList(a1, a2, a3));

        movementRepository.saveAll(Arrays.asList(m1, m2));


    }
}
