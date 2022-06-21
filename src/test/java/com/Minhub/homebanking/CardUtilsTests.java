package com.Minhub.homebanking;

import com.Minhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {


    @Test

    public void ramdomNumberIsCreated(){

        int randomNumber = CardUtils.getRandomNumber(1,10);

        assertThat( randomNumber, isA(Integer.class));

    }

    @Test

    public void randomNumberGreaterThan0(){

        int randomNumber = CardUtils.getRandomNumber(1,10);

        assertThat( randomNumber, greaterThan(0));

    }

    @Test

    public void contCifrasLessThan4(){

        int contCifras = CardUtils.contadorCifras(123);

        assertThat( contCifras, lessThan (4));

    }





}
