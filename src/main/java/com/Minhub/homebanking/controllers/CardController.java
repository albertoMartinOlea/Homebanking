package com.Minhub.homebanking.controllers;

import com.Minhub.homebanking.dtos.PaymentApplicationDTO;
import com.Minhub.homebanking.models.*;

import com.Minhub.homebanking.services.AccountService;
import com.Minhub.homebanking.services.CardService;
import com.Minhub.homebanking.services.ClientService;
import com.Minhub.homebanking.services.TransactionService;
import com.Minhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.Minhub.homebanking.models.TransactionType.CREDIT;
import static com.Minhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    ClientService clientService;

    @Autowired
    CardService cardService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @PatchMapping(path = "/cards/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable long id){
        Card card = cardService.getCarById(id);
        if(card == null){
            return new ResponseEntity<>("The id does not exist",HttpStatus.FORBIDDEN);
        }
        card.setState(false);
        cardService.saveCard(card);
        return new ResponseEntity<>("Eliminada",HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/cards/expired/{id}")
    public ResponseEntity<Object> expiredCard(@PathVariable long id){
        Card card = cardService.getCarById(id);
        if(card == null){
            return new ResponseEntity<>("Deleted",HttpStatus.FORBIDDEN);
        }
        card.setExpired(true);
        cardService.saveCard(card);
        return new ResponseEntity<>("The card is expired",HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/clients/current/cards")

    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication) {


        Client client = clientService.getClientCurrent(authentication);


        Set<Card> card = client.getCards();

       card= card.stream().filter(card1 -> card1.getType().equals(cardType)).collect(Collectors.toSet());

       if (card.size() >=3){
           return new ResponseEntity<>("You cannot have more than 3 cards of the same type", HttpStatus.FORBIDDEN);
       }


        cardService.saveCard(new Card(client.getFirstName()+" "+client.getLastName(), cardType ,cardColor, cardService.cardValidate(),
                CardUtils.getRandomNumber(100,1000), LocalDate.now(),LocalDate.now().plusYears(5),client,true,false));

        return new ResponseEntity<>("Successful card creation",HttpStatus.CREATED);

    }

    @CrossOrigin
    @Transactional
    @PostMapping (path = "cards/payments")
    public ResponseEntity<Object> paymentsPostman(@RequestBody PaymentApplicationDTO payment, Authentication authentication){




        if(payment.getNumberCard().isEmpty() || payment.getDescription().isEmpty()){
            return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
        }

        Card card = cardService.getCardByNumber(payment.getNumberCard());
        if(card == null){
            return new ResponseEntity<>("The card is invalid",HttpStatus.FORBIDDEN);
        }
        Client client = card.getClient();
        Account account = client.getAccounts().stream().filter(account1 -> account1.getBalance()> payment.getAmount()).findFirst().orElse(null);

        if(payment.getCvv() <= 0){
            return new ResponseEntity<>("The cvv is wrong",HttpStatus.FORBIDDEN);
        }

        if(payment.getAmount() <= 0){
            return new ResponseEntity<>("Invalid amount",HttpStatus.FORBIDDEN);
        }

        if (payment.getCvv() != card.getCvv()){
            return new ResponseEntity<>("The cvv does not match the cvv of the card",HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("The card is expired",HttpStatus.FORBIDDEN);
        }

        if (account == null){
            return new ResponseEntity<>("The account associated with the card is not available",HttpStatus.FORBIDDEN);
        }

        if (account.getBalance()<=0 ){
            return new ResponseEntity<>("Account cannot have 0 balance",HttpStatus.FORBIDDEN);

        }

        if (account.getBalance() < payment.getAmount()){
            return new ResponseEntity<>("Insufficient balance to carry out this operation",HttpStatus.FORBIDDEN);
        }


        account.setBalance(account.getBalance()- payment.getAmount());
        accountService.saveAccount(account);

        transactionService.saveTransaction(new Transaction(DEBIT, -payment.getAmount(), LocalDateTime.now(), payment.getDescription() +" "+payment.getNumberCard(), account,true,account.getBalance()));


        return new ResponseEntity<>("Payment accepted",HttpStatus.ACCEPTED);
    }
}
