import java.time.LocalDate;

public class PaymentTest {
    public static void main(String[] args) {
        Payment pay = new Payment(15000, LocalDate.of(2026, 11, 14));
        TestUtils.check("new Payment(15000, LocalDate.of(2026, 11, 14)) → getAmount() == 15000", pay.getAmount() == 15000);
        TestUtils.check("pay.getDate() == 2026-11-14", pay.getDate().equals(LocalDate.of(2026, 11, 14)));
        boolean thrown = false;
        try {
            Payment payNegative = new Payment(-500, LocalDate.of(2026, 11, 14));
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("new Payment(-500, …) --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Payment payZero = new Payment(0, LocalDate.of(2026, 11, 14));
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("new Payment(0, …) --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Payment payWithoutDate = new Payment(15000, null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("new Payment(15000, null) --> IllegalArgumentException", thrown);
    }
}
