package com.Minhub.homebanking.services;

import com.Minhub.homebanking.models.Card;

public interface CardService {

    Card getCardByNumber(String number);

    Card getCarById(long id);

    void saveCard(Card card);

    String cardValidate();




}
