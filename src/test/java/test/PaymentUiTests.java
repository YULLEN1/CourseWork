package test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import pages.MainPage;
import pages.PaymentPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentUiTests {
    private static DataHelper.CardData cardData;
    private static MainPage mainPage;
    private static PaymentPage paymentPage;
    private static List<SQLHelper.PaymentEntity> payments;
    private static List<SQLHelper.OrderEntity> orders;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
        mainPage = new MainPage();
    }

    @BeforeEach
    public void setDown() {
        SQLHelper.setDown();
    }

    //Позитивные сценарии

    @Test
    @DisplayName("1. Should buy if card has a status Approved")
    void shouldBuyIfCardIsApproved() {
        val cardData = DataHelper.getValidApprovedCard();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.successfulPayment();

        payments = SQLHelper.getPayments();
        orders = SQLHelper.getOrders();
        assertEquals(1, payments.size());
        assertEquals(1, orders.size());
        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("approved"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
    }

    @Test
    @DisplayName("2. Should not buy if card has a status Declined")
    void shouldRefuseIfCardIsDeclined() {
        val cardData = DataHelper.getValidDeclinedCard();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.declinedPayment();

        payments = SQLHelper.getPayments();
        orders = SQLHelper.getOrders();
        assertEquals(1, payments.size());
        assertEquals(1, orders.size());
        assertTrue(payments.get(0).getStatus().equalsIgnoreCase("declined"));
        assertEquals(payments.get(0).getTransaction_id(), orders.get(0).getPayment_id());
    }


    @Test
    @DisplayName("3. Should buy if name and surname consist of 2 letters")
    void shouldBuyIfNameAndSurnameOf2Letter() {
        var holder = DataHelper.generateHolderOf2Letters();
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), holder, generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.successfulPayment();
    }

    @Test
    @DisplayName("4. Should buy if name and surname consist of 26 letters")
    void shouldBuyIfNameAndSurnameOf26Letter() {
        var holder = DataHelper.generateHolderOf26Letters();
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), holder, generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.successfulPayment();
    }

    @Test
    @DisplayName("5. Should buy if name and surname consist of 25 letters")
    void shouldBuyIfNameAndSurnameOf25Letter() {
        var holder = DataHelper.generateHolderOf25Letters();
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), holder, generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.successfulPayment();
    }

    // Негативные сценарии
    @Test
    @DisplayName("6. Should decline if card number consists of nulls")
    void shouldDeclineIfCardNumberIsOfNulls() {
        val cardData = new DataHelper.CardData(generateCardNumberIsNull(), generateMonth(7), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cardNumberErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("7. Should decline if card number field is empty")
    void shouldDeclineIfCardNumberIsEmpty() {
        val cardData = new DataHelper.CardData("", generateMonth(7), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cardNumberErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("8. Should decline if card number is random")
    void shouldDeclineIfCardNumberIsRandom() {
        val cardData = new DataHelper.CardData(generateRandomCardNumber(), generateMonth(7), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.declinedPayment();
    }

    @Test
    @DisplayName("9. Should decline if card number consists of 15 digits")
    void shouldDeclineIfCardNumberIsOf15Digit() {
        val cardData = new DataHelper.CardData(generateCardNumberIsOf15Digit(), generateMonth(7), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cardNumberErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("10. Should decline if card number consists of 17 digits")
    void shouldDeclineIfCardNumberIsOf17Digit() {
        val cardData = new DataHelper.CardData(generateCardNumberIsOf17Digit(), generateMonth(7), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.successfulPayment();
    }

    @Test
    @DisplayName("11. Should decline if month is outdated")
    void shouldDeclineIfMonthIsOutdated() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonthLessThanCurrent(4), generateYear(0), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("12. Should decline if month is of special characters")
    void shouldDeclineIfMonthIsOfSymbols() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateSymbols(2), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("13. Should decline if month consists of latin letters")
    void shouldDeclineIfMonthIsLatin() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonthOfLatin(), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("14. Should decline if month consists of latin letters")
    void shouldDeclineIfMonthIsCyrillic() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonthOfCyrillic(), generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("15. Should decline if month consists of nulls")
    void shouldDeclineIfMonthIsNull() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), "00", generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("16. Should decline if month is 13")
    void shouldDeclineIfMonthIs13l() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), "13", generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("17. Should decline if month field is empty")
    void shouldDeclineIfMonthIsEmpty() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), "", generateYear(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.monthErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("18. Should decline if year is outdated")
    void shouldDeclineIfYearIsOutdated() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYearLessThanCurrent(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.yearErrorVisible("Истёк срок действия карты");
    }

    @Test
    @DisplayName("19. Should decline if year consists of special characters")
    void shouldDeclineIfYearIsOfSymbols() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateSymbols(2), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.yearErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("20. Should decline if year consists of latin letters")
    void shouldDeclineIfYearIsLatin() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYearOfLatin(), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.yearErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("21. Should decline if year consists of cyrillic letters")
    void shouldDeclineIfYearIsCyrillic() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYearOfCyrillic(), generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.yearErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("22. Should decline if year is null")
    void shouldDeclineIfYearIsNull() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), "00", generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.yearErrorVisible("Истёк срок действия карты");
    }

    @Test
    @DisplayName("23. Should decline if year field is empty")
    void shouldDeclineIdYearIsEmpty() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), "", generateValidHolder(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.yearErrorVisible("Неверный формат");
    }
    @Test
    @DisplayName("24. Should decline if name and surname of holder consist of cyrillic letters")
    void shouldDeclineIfHolderIsCyrillic() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderCyrillic(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("25. Should decline if name and surname of holder consist of digits")
    void  shouldDeclineIfHolderIsOfDigits() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderOfDigits(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("26. Should decline if name and surname of holder consist of special characters")
    void  shouldDeclineIfHolderIsOfSymbols() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderOfSymbols(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("27. Should decline if name of holder is latin without surname")
    void  shouldDeclineIfHolderIsLatinWithoutSurname() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderWithoutSurname(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("28. Should decline if name of holder is cyrillic and surname is latin")
    void  shouldDeclineIfNameIsCyrillicAndSurnameIsLatin() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderWithNameCyrillic(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("29. Should decline if name of holder is latin and surname is cyrillic")
    void  shouldDeclineIfNameIsLatinAndSurnameIsCyrillic() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderWithSurnameIsCyrillic(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("30. Should decline if holder field is empty")
    void  shouldDeclineIfHolderIsEmpty() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2),"", generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("31. Should decline if name of holder is double with hyphen")
    void  shouldDeclineIfDoubleNameWithHyphen() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderWithHyphen(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("32. Should decline if name and surname of holder consist of one letter in sum")
    void  shouldDeclineIfOneLetterNameAndSurname() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderofOneLetter(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("33. Should decline if name and surname of holder consist of 27 letters")
    void  shouldDeclineIfNameAndSurnameOf27Letter() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateHolderOf27Letters(), generateValidCVC());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.holderErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("34. Should decline if CVC is 000")
    void  shouldDeclineIfCVVIs3Nulls() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateValidHolder(), "000");
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cvcErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("35. Should decline if CVC consists of special characters")
    void  shouldDeclineIfCVVIsOfSymbols() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateValidHolder(), generateSymbols(3));
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cvcErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("36. Should decline if CVC consists of cyrillic letters")
    void  shouldDeclineIfCVVIsCyrillic() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateValidHolder(), generateCVCCyrillic());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cvcErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("37. Should decline if CVC consists of latin letters")
    void  shouldDeclineIfCVVIsLatin() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateValidHolder(), generateCVCLatin());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cvcErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("38. Should decline if CVC consists of 2 digits")
    void  shouldDeclineIfCVVIsOf2Digits() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateValidHolder(), generateCVC2Digits());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cvcErrorVisible("Неверный формат");
    }

    @Test
    @DisplayName("38. Should decline if CVC consists of 4 digits")
    void  shouldDeclineIfCVVIsOf4Digits() {
        val cardData = new DataHelper.CardData(getNumberByStatus("APPROVED"), generateMonth(7), generateYear(2), generateValidHolder(), generateCVC2Digits());
        val paymentPage = mainPage.payByCard();
        paymentPage.fillForm(cardData);
        paymentPage.cvcErrorVisible("Неверный формат");
    }























































}

