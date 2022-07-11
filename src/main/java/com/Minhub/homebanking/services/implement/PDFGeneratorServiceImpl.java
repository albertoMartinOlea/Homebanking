package com.Minhub.homebanking.services.implement;

import com.Minhub.homebanking.models.Account;
import com.Minhub.homebanking.models.Client;
import com.Minhub.homebanking.models.Transaction;
import com.Minhub.homebanking.services.PDFGeneratorService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    public void export(HttpServletResponse response, Account account, LocalDate desde,LocalDate hasta) throws DocumentException, IOException {

        Client client = account.getClient();
        Set<Transaction> transaction = account.getTransactions();
        Set <Transaction> transactionSet = transaction.stream().filter(transaction1 -> transaction1.getDate().toLocalDate().isBefore(hasta.plusDays(1))).collect(Collectors.toSet());
        transactionSet.stream().filter(transaction1 -> transaction1.getDate().toLocalDate().isAfter(desde));

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
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


    }

}
