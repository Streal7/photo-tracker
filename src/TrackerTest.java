import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class TrackerTest {
    public static void main(String[] args) {
        // 1. Пустой трекер
        Tracker empty = new Tracker();
        TestUtils.check("пустой трекер → горящих нет",
                empty.stagesDueToday(LocalDate.of(2026, 11, 22)).isEmpty());
        TestUtils.check("пустой трекер → в работе 0", empty.ordersInProgress() == 0);
        TestUtils.check("пустой трекер → ближайшего срока нет", empty.nearestDeadline().isEmpty());

        // 2. null
        boolean thrown = false;
        try {
            empty.addOrder(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("addOrder(null) --> IllegalArgumentException", thrown);

        // 3. Два заказа: A (14.11) и B (16.11)
        Tracker tracker = new Tracker();
        Order orderA = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        orderA.setShotFrames(8000);
        orderA.setSelectedFrames(800);
        Order orderB = new Order("Коля", ShootType.WEDDING, LocalDateTime.of(2026, 11, 16, 15, 0), 8, 40000);
        orderB.setShotFrames(8000);
        orderB.setSelectedFrames(800);
        tracker.addOrder(orderA);
        tracker.addOrder(orderB);

        // 4. Горит 22.11: отбор и цвет A
        List<Stage> due = tracker.stagesDueToday(LocalDate.of(2026, 11, 22));
        TestUtils.check("22.11 --> горят отбор и цвет A",
                due.size() == 2
                        && due.get(0) == orderA.getStage(StageType.CULLING)
                        && due.get(1) == orderA.getStage(StageType.COLOR));

        // 5. Горит 23.11: + отбор B
        due = tracker.stagesDueToday(LocalDate.of(2026, 11, 23));
        TestUtils.check("23.11 --> отбор и цвет A + отбор B",
                due.size() == 3
                        && due.get(0) == orderA.getStage(StageType.CULLING)
                        && due.get(1) == orderA.getStage(StageType.COLOR)
                        && due.get(2) == orderB.getStage(StageType.CULLING));

        // 6. В работе
        TestUtils.check("в работе A и B --> 2", tracker.ordersInProgress() == 2);
        for (StageType type : StageType.values()) {
            orderB.getStage(type).start();
            orderB.getStage(type).finish(60);
        }
        TestUtils.check("B завершён --> в работе 1", tracker.ordersInProgress() == 1);

        // 7. Ближайший срок: поздний добавлен первым
        Tracker deadlines = new Tracker();
        Order wedding = new Order("Анна", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        Order portrait = new Order("Ира", ShootType.PORTRAIT, LocalDateTime.of(2026, 11, 15, 12, 0), 2, 8000);
        deadlines.addOrder(wedding);   // сдача 28.11
        deadlines.addOrder(portrait);  // сдача 20.11
        TestUtils.check("свадьба 28.11 + портрет 20.11 --> ближайший 20.11",
                LocalDate.of(2026, 11, 20).equals(deadlines.nearestDeadline().orElse(null)));
        for (StageType type : StageType.values()) {
            portrait.getStage(type).start();
            portrait.getStage(type).finish(60);
        }
        TestUtils.check("портрет завершён --> ближайший 28.11",
                LocalDate.of(2026, 11, 28).equals(deadlines.nearestDeadline().orElse(null)));
        Tracker tracker2 = new Tracker();
        Order orderA2 = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        orderA2.addPayment(10000, LocalDate.of(2026, 10, 30));
        orderA2.addPayment(30000, LocalDate.of(2026, 11, 28));
        Order orderB2 = new Order("Анна", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        tracker2.addOrder(orderA2);
        tracker2.addOrder(orderB2);
        TestUtils.check("Октябрь 2026 --> 10000", tracker2.earningsForMonth(YearMonth.of(2026, 10)) == 10000);
        orderB2.addPayment(5000, LocalDate.of(2026, 11, 15));
        orderB2.addPayment(20000, LocalDate.of(2026, 12, 2));
        TestUtils.check("Ноябрь 2026: A 30000 + B 5000 --> 35000", tracker2.earningsForMonth(YearMonth.of(2026, 11)) == 35000);
        TestUtils.check("Декабрь 2026: B 20000 --> 20000", tracker2.earningsForMonth(YearMonth.of(2026, 12)) == 20000);
        TestUtils.check("Ноябрь 2025: платежей нет --> 0", tracker2.earningsForMonth(YearMonth.of(2025, 11)) == 0);
        TestUtils.check("totalEarnings: все 4 платежа --> 65000", tracker2.totalEarnings() == 65000);
        thrown = false;
        try {
            tracker2.earningsForMonth(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("earningsForMonth(null) --> IAE", thrown);
    }
}