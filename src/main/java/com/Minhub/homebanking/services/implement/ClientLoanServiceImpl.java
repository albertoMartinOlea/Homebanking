package com.Minhub.homebanking.services.implement;

import com.Minhub.homebanking.models.Card;
import com.Minhub.homebanking.models.ClientLoan;
import com.Minhub.homebanking.repositories.ClientLoanRepository;
import com.Minhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanServiceImpl implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;


    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }


}
