package com.Minhub.homebanking;

import com.Minhub.homebanking.models.*;
import com.Minhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.Minhub.homebanking.models.TransactionType.CREDIT;
import static com.Minhub.homebanking.models.TransactionType.DEBIT;


@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository repositoryAccount,
                                      TransactionRepository repositoryTransaction, LoanRepository repositoryLoan,
                                      ClientLoanRepository repositoryClientLoan, CardRepository repositoryCard) {
        return (args) -> {
            // save a couple of customers
            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
            repositoryClient.save(client1);
            LocalDateTime fechaCreacion = LocalDateTime.now();
            Account account1 = new Account("VIN001",fechaCreacion,5000.00,client1,true,AccountType.CURRENT);
            Account account2 = new Account("VIN002",fechaCreacion.plusDays(1),7500.00,client1,true,AccountType.SAVING);

            repositoryAccount.save(account1);
            repositoryAccount.save(account2);

            Client client2 = new Client("Martin", "Olea", "martinolea71@gmail.com", passwordEncoder.encode("martin123"));
            repositoryClient.save(client2);

            Client client3 = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin123"));
            repositoryClient.save(client3);

            Transaction transaction1 = new Transaction(DEBIT,-50,LocalDateTime.now().plusDays(5),"pago de boleta",account1,true, 4950);
            Transaction transaction2 = new Transaction(CREDIT,1500,LocalDateTime.now(),"varios",account1,true,6450);
            Transaction transaction4 = new Transaction(DEBIT,-1000,LocalDateTime.now(),"pago luz",account1,true,5450);
            Transaction transaction3 = new Transaction(CREDIT,4000,LocalDateTime.now(),"deposito",account2,true,0);
            Transaction transaction5 = new Transaction(DEBIT,-100,LocalDateTime.now(),"carga virtual",account2,true,0);
            Transaction transaction6 = new Transaction(CREDIT,3500,LocalDateTime.now(),"varios",account2,true,0);


            repositoryTransaction.save(transaction1);
            repositoryTransaction.save(transaction2);
            repositoryTransaction.save(transaction3);
            repositoryTransaction.save(transaction4);
            repositoryTransaction.save(transaction5);
            repositoryTransaction.save(transaction6);

            Loan loan1 = new Loan("Mortgage",500000, List.of(12,24,36,48,60),10);
            Loan loan2 = new Loan("Personal",100000, List.of(6,12,24),15);
            Loan loan3 = new Loan("Car",300000, List.of(6,12,24,36),20);

            repositoryLoan.save(loan1);
            repositoryLoan.save(loan2);
            repositoryLoan.save(loan3);

            ClientLoan clientLoan1 = new ClientLoan(400000,60,client1,loan1);
            repositoryClientLoan.save(clientLoan1);

            ClientLoan clientLoan2 = new ClientLoan(50000,12,client1,loan2);
            repositoryClientLoan.save(clientLoan2);

            ClientLoan clientLoan3 = new ClientLoan(100000,24,client2,loan2);
            repositoryClientLoan.save(clientLoan3);

            ClientLoan clientLoan4 = new ClientLoan(200000,36,client2,loan3);
            repositoryClientLoan.save(clientLoan4);
            Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.DEBIT, CardColor.GOLD,
                    "4444-5555-6666-7777",111, LocalDate.now(),LocalDate.now().plusYears(5),client1,true,false);

            Card card2 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.DEBIT, CardColor.TITANIUM,
                    "1111-2222-3333-4444",222, LocalDate.now(),LocalDate.now().plusYears(5),client1,true,false);

            Card card3 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.CREDIT, CardColor.SILVER,
                    "1111-2222-5555-4444",333, LocalDate.now(),LocalDate.now().plusYears(-5),client1,true,false);

            repositoryCard.save(card1);
            repositoryCard.save(card2);
            repositoryCard.save(card3);







        };
    }
}


