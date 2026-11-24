import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tracker {
    private final List<Order> orders = new ArrayList<>();

    List<DueStage> stagesDueToday(LocalDate today) {
        List<DueStage> result = new ArrayList<>();
        for (Order order : orders) {
            for (Stage stage : order.stagesDue(today)) {
                Optional<LocalDate> deadline = order.stageDeadline(stage.getType());
                if (deadline.isPresent()) {
                    DueStage row = new DueStage(order.getClient(), stage.getType(), deadline.get());
                    result.add(row);
                }
            }
        }
        return result;
    }

    int ordersInProgress() {
        int count = 0;
        for (Order order : orders) {
            if (!order.isCompleted()) {
                count++;
            }
        }
        return count;
    }

    void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Заказ не может быть пустым");
        }
        orders.add(order);
    }

    Optional<LocalDate> nearestDeadline() {

        LocalDate nearest = null;
        for (Order order : orders) {
            if (order.isCompleted()) {
                continue;
            }
            LocalDate d = order.deadline();
            if (nearest == null || d.isBefore(nearest)) {
                nearest = d;
            }
        }
        return Optional.ofNullable(nearest);
    }

    int earningsForMonth(YearMonth month) {
        if (month == null) {
            throw new IllegalArgumentException("Должен быть указан месяц оплаты");
        }
        int earnings = 0;
        for (Order order : orders) {
            for (Payment p : order.getPayments()) {  // платежи этого заказа
                LocalDate paidOn = p.getDate();
                if (YearMonth.from(paidOn).equals(month)) {
                    earnings += p.getAmount();
                }
            }

        }
        return earnings;
    }

    int totalEarnings() {
        int earnings = 0;
        for (Order order : orders) {
            earnings += order.paidAmount();
        }
        return earnings;
    }
    List<Order> getOrders(){
        return new ArrayList<>(orders);
    }
}
