package com.Minhub.homebanking.services;

import com.Minhub.homebanking.dtos.LoanDTO;
import com.Minhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getLoansDTO();
    Loan getLoanById(long id);

    void saveLoan(Loan loan);
}
