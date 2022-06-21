package com.Minhub.homebanking.controllers;

import com.Minhub.homebanking.dtos.AccountDTO;
import com.Minhub.homebanking.models.Account;
import com.Minhub.homebanking.models.AccountType;
import com.Minhub.homebanking.models.Client;

import com.Minhub.homebanking.models.Transaction;
import com.Minhub.homebanking.repositories.AccountRepository;
import com.Minhub.homebanking.services.AccountService;
import com.Minhub.homebanking.services.ClientService;
import com.Minhub.homebanking.services.TransactionService;
import com.Minhub.homebanking.utils.Util;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")//Asocia la peticion con la ruta especificada-mapeando una peticion con la ruta
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        return accountService.getAccountDTO(id,authentication);
    }

    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType type, Authentication authentication) {

        Client client = clientService.getClientCurrent(authentication);

            if (client.getAccounts().size() >= 3){
                return new ResponseEntity<>("You cannot have more than 3 accounts", HttpStatus.FORBIDDEN);
            }else {


                accountService.saveAccount(new Account(accountService.validateAccount(), LocalDateTime.now(), 0.0, client, true, type));

                return new ResponseEntity<>("Successful account creation", HttpStatus.CREATED);
            }
    }

    @PatchMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> hideAccount(@PathVariable long id){
        Account account =  accountService.getAccountById(id);

        if (account.getBalance() > 0 ){
            return new ResponseEntity<>("Balance greater than 0", HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = account.getTransactions();
        transactions.forEach(transaction -> transaction.setVisibility(false));
        transactions.forEach(transaction -> transactionService.saveTransaction(transaction));

        account.setVisibility(false);
        accountService.saveAccount(account);

        return new ResponseEntity<>("hidden Account", HttpStatus.ACCEPTED);
    }

}


