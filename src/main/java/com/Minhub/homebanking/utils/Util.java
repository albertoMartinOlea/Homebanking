package com.Minhub.homebanking.utils;

import com.Minhub.homebanking.repositories.AccountRepository;
import com.Minhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.print.attribute.IntegerSyntax;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {


    public static boolean validationPassword(String password){

        // Regex to check valid password.
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        // Compile the ReGex
        Pattern pattern = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // La clase de patrón contiene el método matcher()
        // para encontrar coincidencias entre la contraseña dada
        // y expresión regular.
        Matcher matcher = pattern.matcher(password);

        // Return if the password
        // matched the ReGex
        return matcher.matches();
    }


    public static int getRandomNumber(int min, int max) {

        return (int) ((Math.random() * (max - min)) + min);

    }

    public static int cifras(int ramdom){
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
        if ( cifras(numberRamdon) == 3 ){
            number = "0"+ number;
        } else if (cifras(numberRamdon) == 2) {
            number = "00"+ number;
        } else if (cifras(numberRamdon) == 1) {
            number = "000" + number;
        }

        System.out.println("Numero con cifras agregadas---->"+number);

        return number;
    }



}
