package com.Minhub.homebanking.utils;

import com.Minhub.homebanking.repositories.CardRepository;

public final class CardUtils {


    private CardUtils() {
    }

    public static int getRandomNumber(int min, int max) {

        return (int) ((Math.random() * (max - min)) + min);

    }

    public static int contadorCifras(int ramdom){
        int count = 0;
        while(ramdom != 0){
            ramdom = ramdom / 10;
            count ++;
        }
        //System.out.println("Cantidad de cifras "+count);
        return count;
    }

    public static String cuatroCifras(Integer numberRamdon){

        String number = Integer.toString(numberRamdon);

        //System.out.println("Numero generado---->"+number);
        if ( contadorCifras(numberRamdon) == 3 ){
            number = "0"+ number;
        } else if (contadorCifras(numberRamdon) == 2) {
            number = "00"+ number;
        } else if (contadorCifras(numberRamdon) == 1) {
            number = "000" + number;
        }

        System.out.println("Numero con cifras agregadas---->"+number);

        return number;
    }



}
