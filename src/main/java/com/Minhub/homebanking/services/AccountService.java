package com.Minhub.homebanking.services;

import com.Minhub.homebanking.dtos.AccountDTO;
import com.Minhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();

    Account getAccountById(long id);
    AccountDTO getAccountDTO(long id, Authentication authentication);

    void saveAccount(Account account);

    Account getAccountByNumber(String number);

    String validateAccount();


}
