package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final Faker fakerWithCyrillicLocale = new Faker(new Locale("ru", "RU"));

    public static CardData getValidApprovedCard() {
        return new CardData(getNumberByStatus("approved"), generateMonth(1), generateYear(2),
                generateValidHolder(), generateValidCVC());
    }

    public static CardData getValidDeclinedCard() {
        return new CardData(getNumberByStatus("declined"), generateMonth(1), generateYear(2),
                generateValidHolder(), generateValidCVC());
    }

    public static String generateMonth(int shiftMonth) {
        return LocalDate.now().plusMonths(shiftMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateYear(int shiftYear) {
        return LocalDate.now().plusYears(shiftYear).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateValidCVC() {
        return faker.numerify("###");
    }

    public static String generateValidHolder() {
        return faker.name().fullName().toUpperCase();
    }

    @Value
    public static class CardData {
        private final String number;
        private final String month;
        private final String year;
        private final String holder;
        private final String cvc;
    }

    public static String getNumberByStatus(String status) {
        if (status.equalsIgnoreCase("APPROVED")) {
            return "1111 2222 3333 4444";
        } else if (status.equalsIgnoreCase("DECLINED")) {
            return "5555 6666 7777 8888";
        }
        return null;
    }

    public static String generateHolderOf2Letters() {
        return faker.letterify("## ##", true);
    }

    public static String generateHolderOf26Letters() {
        return faker.letterify("############## #############", true);
    }

    public static String generateHolderOf25Letters() {
        return faker.letterify("############# #############", true);
    }

    public static String generateCardNumberIsNull() {
        return "0000 0000 0000 0000";
    }

    public static String generateRandomCardNumber() {
        return faker.numerify("#### #### #### ####");
    }

    public static String generateCardNumberIsOf15Digit() {
        return faker.numerify("#### #### #### ###");

    }

    public static String generateCardNumberIsOf17Digit() {
        return faker.numerify("#### #### #### #### #");
    }

    public static String generateMonthLessThanCurrent(int shiftMonth) {
        return LocalDate.now().minusMonths(shiftMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String generateYearLessThanCurrent(int shiftYear) {
        return LocalDate.now().minusYears(shiftYear).format(DateTimeFormatter.ofPattern("yy"));
    }

    }






