import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tracker {
    private final List<Order> orders = new ArrayList<>();

    List<Stage> stagesDueToday(LocalDate today) {
        List<Stage> result = new ArrayList<>();
        for (Order order : orders) {
            result.addAll(order.stagesDue(today));
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

}
