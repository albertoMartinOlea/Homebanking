package com.Minhub.homebanking.services.implement;

import com.Minhub.homebanking.models.Card;
import com.Minhub.homebanking.repositories.CardRepository;
import com.Minhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.Minhub.homebanking.utils.CardUtils.cuatroCifras;
import static com.Minhub.homebanking.utils.CardUtils.getRandomNumber;

@Service
public class CardServiceImpl implements CardService {


    @Autowired
    CardRepository cardRepository;

    @Override
    public Card getCardByNumber(String number) {
      return cardRepository.findByNumber(number);
    }

    public Card getCarById(long id){
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public  String cardValidate(){
        String card = 4509+"-"+ cuatroCifras(getRandomNumber(1,10000))+"-"+cuatroCifras(getRandomNumber(1,10000))+"-"+cuatroCifras(getRandomNumber(1,10000));
        while (cardRepository.findByNumber(card) != null){
            card = 4509+"-"+ cuatroCifras(getRandomNumber(1,10000))+"-"+cuatroCifras(getRandomNumber(1,10000))+"-"+cuatroCifras(getRandomNumber(1,10000));
        }
        return card;
    }
}
