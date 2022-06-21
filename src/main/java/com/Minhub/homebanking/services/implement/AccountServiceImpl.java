package com.Minhub.homebanking.services.implement;

import com.Minhub.homebanking.dtos.AccountDTO;
import com.Minhub.homebanking.models.Account;
import com.Minhub.homebanking.repositories.AccountRepository;
import com.Minhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.Minhub.homebanking.utils.Util.cuatroCifras;
import static com.Minhub.homebanking.utils.Util.getRandomNumber;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @Override
    public Account getAccountById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public AccountDTO getAccountDTO(long id, Authentication authentication) {
        if(!authentication.getName().equals("admin@admin.com")){
            Account account = accountRepository.findById(id).orElse(null);
            if(account != null) {
                account.setTransactions(account.getTransactions().stream().filter(transaction -> transaction.isVisibility() == true).collect(Collectors.toSet()));
                return new AccountDTO(account);
            }
        }
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }


    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public String validateAccount(){
        String account = "VIN-" + cuatroCifras(getRandomNumber(1,10000)) + cuatroCifras(getRandomNumber(1,10000));
        while(accountRepository.findByNumber(account) != null){
            account = "VIN-" +  cuatroCifras(getRandomNumber(1,10000)) + cuatroCifras(getRandomNumber(1,10000));
        }
        return account;
    }
}
