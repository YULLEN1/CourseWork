package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static groovy.xml.dom.DOMCategory.parent;

public class PaymentPage {

    private SelenideElement cardNumber = $(byText("Номер карты")).parent().$(".input__control");
    private SelenideElement month = $(byText("Месяц")).parent().$(".input__control");
    private SelenideElement year = $(byText("Год")).parent().$(".input__control");
    private SelenideElement holder = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement cvc = $(byText("CVC/CVV")).parent().$(".input__control");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement cardNumberError = $(byText("Номер карты")).parent().$(".input__sub");
    private SelenideElement monthError = $(byText("Месяц")).parent().$(".input__sub");
    private SelenideElement yearError = $(byText("Год")).parent().$(".input__sub");
    private SelenideElement expiredCardError = $(byText("Истек срок действия карты")).parent().$(".input__sub");
    private SelenideElement holderError = $(byText("Владелец")).parent().$(".input__sub");
    private SelenideElement cvcError = $(byText("CVC/CVV")).parent().$(".input__sub");

    public void fillForm(DataHelper.CardData cardData) {
        cardNumber.setValue(cardData.getNumber());
        month.setValue(cardData.getMonth());
        year.setValue(cardData.getYear());
        holder.setValue(cardData.getHolder());
        cvc.setValue(cardData.getCvc());
        continueButton.click();
    }

    public void notFilledForm() {
        continueButton.click();
        cardNumberError.shouldBe(visible);
        monthError.shouldBe(visible);
        yearError.shouldBe(visible);
        holderError.shouldBe(visible);
        cvcError.shouldBe(visible);
    }

    public void cardNumberErrorVisible(String expectedText) {
        cardNumberError.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }

    public void monthErrorVisible(String expectedText) {
        monthError.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }

    public void yearErrorVisible(String expectedText) {
        yearError.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }

    public void expiredCardErrorVisible(String expectedText) {
        expiredCardError.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }

    public void holderErrorVisible(String expectedText) {
        holderError.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }

    public void cvcErrorVisible(String expectedText) {
        cvcError.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }

    public void successfulPayment() {
        $(".notification_status_ok").shouldBe(Condition.visible, Duration.ofSeconds(30));
    }

    public void declinedPayment() {
        $x("//div[contains(@class, 'notification_status_error')]").shouldBe(Condition.visible, Duration.ofSeconds(20));
    }
}

