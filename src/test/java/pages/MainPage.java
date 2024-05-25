package pages;

import data.DataHelper;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selectors.byText;

public class MainPage {
    private SelenideElement paymentButton = $(byText("Купить"));
    private SelenideElement creditButton = $(byText("Купить в кредит"));
    private SelenideElement paymentByCard = $(byText("Оплата по карте"));
    private SelenideElement paymentByCredit = $(byText("Кредит по данным карты"));

    public PaymentPage payByCard() {
        paymentButton.click();
        paymentByCard.shouldBe(visible);
        return new PaymentPage();
    }

    public CreditPage payByCredit() {
        creditButton.click();
        paymentByCredit.shouldBe(visible);
        return new CreditPage();
    }
}
