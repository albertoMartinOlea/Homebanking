package com.Minhub.homebanking.services;

import com.Minhub.homebanking.models.Account;
import com.lowagie.text.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public interface PDFGeneratorService {

    void export(HttpServletResponse response, Account account, LocalDate desde, LocalDate hasta) throws DocumentException, IOException;

}
