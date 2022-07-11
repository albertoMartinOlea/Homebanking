package com.Minhub.homebanking.controllers;

import com.Minhub.homebanking.models.*;

import com.Minhub.homebanking.services.AccountService;
import com.Minhub.homebanking.services.ClientService;
import com.Minhub.homebanking.services.TransactionService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


import static com.Minhub.homebanking.models.TransactionType.CREDIT;
import static com.Minhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam Double amount, @RequestParam String description,
                                                    @RequestParam String numberAccountOrigin,@RequestParam String numberAccountDestiny,
                                                    Authentication authentication) {

        Client client = clientService.getClientCurrent(authentication);
        Account originAccount = accountService.getAccountByNumber(numberAccountOrigin);
        Account destinyAccount = accountService.getAccountByNumber(numberAccountDestiny);

        if ( description.isEmpty() || numberAccountDestiny.isEmpty() || numberAccountOrigin.isEmpty() ) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (amount.isNaN() ||amount.isInfinite() || amount <= 0){
            return new ResponseEntity<>("The amount is not valid", HttpStatus.FORBIDDEN);
        }

        if (numberAccountDestiny.equals(numberAccountOrigin)) {
            return new ResponseEntity<>("The accounts cannot be the same", HttpStatus.FORBIDDEN);
        }


        if (accountService.getAccountByNumber(numberAccountOrigin) == null){
            return new ResponseEntity<>("Source account does not exist", HttpStatus.FORBIDDEN);
        }

        if (accountService.getAccountByNumber(numberAccountDestiny) == null){
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(originAccount)){
            return new ResponseEntity<>("Account is non-existent for this customer", HttpStatus.FORBIDDEN);
        }


        if(originAccount.getBalance() < amount){
            return new ResponseEntity<>("Balance not available to make the transfer", HttpStatus.FORBIDDEN);
        }

        originAccount.setBalance(originAccount.getBalance()-amount);
        destinyAccount.setBalance(destinyAccount.getBalance()+ amount);

        accountService.saveAccount(originAccount);
        accountService.saveAccount(destinyAccount);

        transactionService.saveTransaction(new Transaction(DEBIT, -amount, LocalDateTime.now(),description +" "+ destinyAccount.getNumber(),originAccount,true, originAccount.getBalance()));
        transactionService.saveTransaction(new Transaction(CREDIT, amount, LocalDateTime.now(),description +" "+ originAccount.getNumber(),destinyAccount,true,destinyAccount.getBalance()));





        return new ResponseEntity<>("Successful transaction creation",HttpStatus.CREATED);

    }

    /*   @PostMapping(path = "/pdf/{id}")
     public ResponseEntity<Object> export( @PathVariable long id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta)
             throws FileNotFoundException, DocumentException {

         Account account = accountService.getAccountById(id);
         if(account == null){
             return new ResponseEntity<>("The account does not exist",HttpStatus.FORBIDDEN);
         }

         Client client = account.getClient();
         Set <Transaction> transaction = account.getTransactions();
         Set <Transaction> transactionSet = transaction.stream().filter(transaction1 -> transaction1.getDate().toLocalDate().isBefore(hasta.plusDays(1))).collect(Collectors.toSet());
         transactionSet.stream().filter(transaction1 -> transaction1.getDate().toLocalDate().isAfter(desde));

        Document document = new Document();
       String ruta = System.getProperty("user.home");
         PdfWriter.getInstance(document,new FileOutputStream(ruta+"/Desktop/transactions.pdf"));
         document.open();

         Phrase p = new Phrase("ACCOUNT: "+account.getNumber());
         document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Date");
        table.addCell("Description");
        table.addCell("Type");
        table.addCell("Amount");
        table.addCell("Current balance");


        transactionSet.stream().forEach(transaction1 -> {
            PdfPCell c1 = new PdfPCell(new Phrase(transaction1.getDate().toLocalDate().toString()));
            PdfPCell c2 = new PdfPCell(new Phrase(transaction1.getDescription()));
            PdfPCell c3 = new PdfPCell(new Phrase(transaction1.getType().toString()));
            PdfPCell c4 = new PdfPCell(new Phrase(String.valueOf(transaction1.getAmount())));
            PdfPCell c5 = new PdfPCell(new Phrase(String.valueOf(transaction1.getBalanceTransaction())));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
        });

        document.add(table);
        document.close();
        return new ResponseEntity<>("Summary of successfully created transactions",HttpStatus.ACCEPTED);
    }*/
}
