package com.Minhub.homebanking.controllers;

import com.Minhub.homebanking.dtos.LoanApplicationDTO;
import com.Minhub.homebanking.dtos.LoanDTO;
import com.Minhub.homebanking.models.*;
import com.Minhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


import static com.Minhub.homebanking.models.TransactionType.CREDIT;
import static com.Minhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    LoanService loanService;

    @Autowired
    ClientLoanService clientLoanService;

    @Autowired
    TransactionService transactionService;

    @GetMapping(path = "/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoansDTO();
    }

    @PostMapping("/createLoans")
    public ResponseEntity<Object> addLoan(@RequestBody Loan loan){
        Loan loan1 = new Loan (loan.getName(), loan.getMaxAmount(), loan.getPayments(), loan.getInterest());
        loanService.saveLoan(loan1);

        return new ResponseEntity<>("Loan created successfully",HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping(path = "/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {


        Client client = clientService.getClientByEmail(authentication.getName());
        Account account = accountService.getAccountByNumber(loanApplicationDTO.getNumberAccountDestiny());
        Loan loan = loanService.getLoanById(loanApplicationDTO.getLoanId());


        if (client.getLoans().contains(loan)){
            return new ResponseEntity<>("You cannot have 2 loans of the same type", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getAmount().isInfinite() || loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if (loan == null) {
            return new ResponseEntity<>("The loan type does not exist", HttpStatus.FORBIDDEN);
        }

        if( loanApplicationDTO.getAmount() >loan.getMaxAmount()){
            return new ResponseEntity<>("The amount requested exceeds the maximum amount to request",HttpStatus.FORBIDDEN);
        }

        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Payment type does not exist", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("The destination account is non-existent", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The destination account does not belong to the client", HttpStatus.FORBIDDEN);
        }


        clientLoanService.saveClientLoan(new ClientLoan(loanApplicationDTO.getAmount()+(loanApplicationDTO.getAmount()*loan.getInterest()/100),loanApplicationDTO.getPayments(),client,loan));


        account.setBalance(account.getBalance()+ loanApplicationDTO.getAmount());
        accountService.saveAccount(account);

        transactionService.saveTransaction(new Transaction(CREDIT, loanApplicationDTO.getAmount(), LocalDateTime.now(), loan.getName() +" "+ "loan approved",account,true,account.getBalance()));


        return new ResponseEntity<>("Successful transaction creation",HttpStatus.CREATED);

    }

}
