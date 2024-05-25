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
import static org.junit.jupiter.api.Assertions.*;

class PaymentUiTests {
    private static DataHelper.CardData cardData;
    private static MainPage mainPage;
    private static PaymentPage paymentPage;
    private static List<SQLHelper.PaymentEntity> payments;
    private static List<SQLHelper.OrderEntity> orders;

    @BeforeAll
    static void setUpAll() {
        SQLHelper.setDown();
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

    @AfterEach
    public void setDown() {
        SQLHelper.setDown();
    }

    @Test
    void shouldPayByCardSuccessfully() {
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
}

