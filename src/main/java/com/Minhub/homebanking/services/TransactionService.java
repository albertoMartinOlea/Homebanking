package com.Minhub.homebanking.services;

import com.Minhub.homebanking.models.Transaction;

public interface TransactionService {

    void saveTransaction(Transaction transaction);
}
