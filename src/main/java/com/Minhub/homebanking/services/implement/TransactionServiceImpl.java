package com.Minhub.homebanking.services.implement;

import com.Minhub.homebanking.models.Transaction;
import com.Minhub.homebanking.repositories.TransactionRepository;
import com.Minhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
}
