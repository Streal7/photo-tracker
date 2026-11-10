import java.time.LocalDate;

public class Payment {
    private final int amount;
    private final LocalDate date;

    Payment(int amount, LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Дата не может быть пустой");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма не может быть меньше нуля");
        }
        this.date = date;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
