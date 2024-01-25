package ru.netology.banklogin.test;

import org.junit.jupiter.api.*;
import ru.netology.banklogin.data.DataHelper;

import static ru.netology.banklogin.data.DataGenerator.LoginAuthentication.getLogonForRegisteredUser;


import static ru.netology.banklogin.data.DataGenerator.transactionCardByCard;
import static ru.netology.banklogin.data.SQLHelper.*;

public class BankTransactionTest {
    String firstCard = DataHelper.getFirstCard().getCard();
    String secondCard = DataHelper.getSecondCard().getCard();

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }


    @Test
    @DisplayName("Should successful transaction from card to card")
    void shouldSuccessfulTransactionCardByCard() {
        int amount = 5_000;
        getLogonForRegisteredUser();
        var expectedFirstCardBalance = (getCardBalance(firstCard).getBalance() /100) - amount;
        var expectedSecondCardBalance = (getCardBalance(secondCard).getBalance() / 100) + amount;
        transactionCardByCard(firstCard, secondCard, amount);
        var actualFirstCardBalance = getCardBalance(firstCard).getBalance() /100;
        var actualSecondCardBalance = getCardBalance(secondCard).getBalance() /100;

        Assertions.assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(expectedSecondCardBalance, actualSecondCardBalance);
    }
}

