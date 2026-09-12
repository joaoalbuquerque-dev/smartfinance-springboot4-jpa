package com.smartfinance.config;

import com.smartfinance.entities.*;
import com.smartfinance.entities.enums.AccountType;
import com.smartfinance.entities.enums.TransactionType;
import com.smartfinance.entities.pk.MovementCategoryPK;
import com.smartfinance.repositories.*;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MovementCategoryRepository movementCategoryRepository;

    @Override
    public void run(String... args) throws Exception {

        User u1 = new User(null, "Maria Brown", "maria@gmail.com", "123456");
        User u2 = new User(null, "Alex Green", "alex@gmail.com","123456");

        Account a1 = new Account(null, "Nubank", 2500.00, AccountType.CHECKING, u1);
        Account a2 = new Account(null, "Bradesco", 8500.00, AccountType.SAVINGS, u1);
        Account a3 = new Account(null, "Carteira", 350.00, AccountType.CASH, u2);

        Category c1 = new Category(null, "Alimentação");
        Category c2 = new Category(null, "Saúde");

        Movement m1 = new Movement(null, "Supermercado", 800.00, Instant.now(), TransactionType.EXPENSE, a1);
        Movement m2 = new Movement(null, "Farmácia", 100.00, Instant.now(), TransactionType.EXPENSE, a3);
        Movement m3 = new Movement(null, "Restaurante", 300.00, Instant.now(), TransactionType.EXPENSE, a1);
        Movement m4 = new Movement(null, "Médico", 400.00, Instant.now(), TransactionType.EXPENSE, a3);

        MovementCategory mc1 = new MovementCategory(m1, c1, 500.00);
        MovementCategory mc2 = new MovementCategory(m1, c2, 300.00);
        MovementCategory mc3 = new MovementCategory(m2, c2, 70.00);
        MovementCategory mc4 = new MovementCategory(m2, c1, 30.00);

        userRepository.saveAll(Arrays.asList(u1, u2));

        accountRepository.saveAll(Arrays.asList(a1, a2, a3));

        categoryRepository.saveAll(Arrays.asList(c1, c2));

        movementRepository.saveAll(Arrays.asList(m1, m2, m3, m4));

        movementCategoryRepository.saveAll(Arrays.asList(mc1, mc2, mc3, mc4));


    }

}
