package com.Minhub.homebanking.controllers;

import com.Minhub.homebanking.models.Account;
import com.Minhub.homebanking.services.AccountService;
import com.Minhub.homebanking.services.implement.PDFGeneratorServiceImpl;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class PDFExportController{

    @Autowired
    AccountService accountService;
    private final PDFGeneratorServiceImpl pdfGeneratorService;

    public PDFExportController(PDFGeneratorServiceImpl pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @PostMapping("/pdf/generate/{id}")
    public ResponseEntity<Object> generatePDF(HttpServletResponse response, @PathVariable long id,
                                              @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
                                              @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta)
                                              throws IOException, DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Account account = accountService.getAccountById(id);
        if(account == null){
            return new ResponseEntity<>("The account does not exist", HttpStatus.FORBIDDEN);
        }

        this.pdfGeneratorService.export(response,account,desde,hasta);

        return new ResponseEntity<>("Summary of successfully created transactions", HttpStatus.ACCEPTED);

    }
}
