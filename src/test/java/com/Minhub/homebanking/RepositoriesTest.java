package com.Minhub.homebanking;

import com.Minhub.homebanking.models.Account;
import com.Minhub.homebanking.models.Client;
import com.Minhub.homebanking.models.Loan;
import com.Minhub.homebanking.models.Transaction;
import com.Minhub.homebanking.repositories.AccountRepository;
import com.Minhub.homebanking.repositories.ClientRepository;
import com.Minhub.homebanking.repositories.LoanRepository;
import com.Minhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static com.Minhub.homebanking.models.TransactionType.DEBIT;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


    @SpringBootTest //son transaccionales y retroceden al final de cada prueba.
    @AutoConfigureTestDatabase(replace = NONE)

    public class RepositoriesTest {

        @Autowired
        private LoanRepository loanRepository;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private TransactionRepository transactionRepository;

        @Test
        public void existLoans(){

            List<Loan> loans = loanRepository.findAll();

            assertThat(loans,is(not(empty())));

        }


        @Test

        public void existPersonalLoan(){

            List<Loan> loans = loanRepository.findAll();

            assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

        }


        @Test
        public void existClients(){

            List<Client> clients = clientRepository.findAll();

            assertThat(clients,is(not(empty())));

        }

        @Test
        public void existClientMelba(){

            List<Client> clients = clientRepository.findAll();

            assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));

        }


        @Test
        public void existAccounts(){

            List<Account> accounts = accountRepository.findAll();

            assertThat(accounts,is(not(empty())));

        }

        @Test
        public void existAccountVIN001(){

            List<Account> accounts = accountRepository.findAll();

            assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));

        }

        @Test
        public void existTransaction(){

            List<Transaction> transactions = transactionRepository.findAll();

            assertThat(transactions,is(not(empty())));

        }

        @Test
        public void existTransactionDEBITO(){

            List<Transaction> transactions = transactionRepository.findAll();


            assertThat(transactions, hasItem(hasProperty("type", is(DEBIT))));

        }

    }

