import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {
    public static void main(String[] args) {
        Order order = new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5, 25000);
        TestUtils.check("new Order(\"Мария\",...).getClient --> Мария", "Мария".equals(order.getClient()));
        TestUtils.check("new Order(...,ShootType.WEDDING,...).getShootType --> WEDDING", order.getShootType() == ShootType.WEDDING);
        TestUtils.check("new Order(...,2026-11-14 15:00,...).getDateTimeShoot --> 2026-11-14T15:00", order.getDateTimeShoot().equals(LocalDateTime.of(2026, 11, 14, 15, 0)));
        TestUtils.check("new Order(...,5,...).getHours --> 5.0", TestUtils.equalsDouble(order.getHours(), 5, 0.001));
        TestUtils.check("new Order(...,25000).getPrice --> 25000", order.getPrice() == 25000);
        boolean thrown = false;
        try {
            Order emptyClientOrder = new Order(" ", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5, 25000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("emptyClientOrder --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Order nullClientOrder = new Order(null, ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5, 25000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("nullClientOrder --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Order nullShootTypeOrder = new Order("Мария", null, LocalDateTime.of(2026, 11, 14, 15, 0), 5, 25000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("nullShootTypeOrder --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Order nullDateTimeShootOrder = new Order("Мария", ShootType.WEDDING, null, 5, 25000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("nullDateTimeShootOrder --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Order zeroHoursOrder = new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 0, 25000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("zeroHoursOrder --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Order negativeHoursOrder = new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), -2, 25000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("negativeHoursOrder --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            Order negativePriceOrder = new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5, -5000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("negativePriceOrder --> IllegalArgumentException", thrown);
        TestUtils.check("new Order(...,0).getPrice --> 0", new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5, 0).getPrice() == 0);
        TestUtils.check("order.getStages().size() --> 6", order.getStages().size() == 6);

        boolean allNotStarted = true;
        for (Stage stage : order.getStages()) {
            if (stage.getStatus() != StageStatus.NOT_STARTED) {
                allNotStarted = false;
                break;
            }
        }
        TestUtils.check("все этапы нового заказа --> NOT_STARTED",
                order.getStages().size() == 6 && allNotStarted);

        order.getStages().clear();
        TestUtils.check("getStages().clear() не меняет заказ --> 6 этапов", order.getStages().size() == 6);
        TestUtils.check("order.getStage(StageType.RETOUCH).getType() == StageType.RETOUCH", order.getStage(StageType.RETOUCH).getType() == StageType.RETOUCH);
        thrown = false;
        try {
            order.getStage(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("order.getStage(null) → IllegalArgumentException", thrown);
        Stage first = order.getStage(StageType.RETOUCH);
        Stage second = order.getStage(StageType.RETOUCH);
        TestUtils.check("getStage(RETOUCH) дважды --> один и тот же объект", first == second);
//       frames
        Order order2 = new Order("Человек", ShootType.PORTRAIT, LocalDateTime.of(2026, 11, 15, 15, 0), 3, 2000);
        TestUtils.check("new Order(..).hasShotFrames --> false", !order2.hasShotFrames());
        TestUtils.check("new Order(..).cullingMinutes() --> 0", TestUtils.equalsDouble(order2.cullingMinutes(), 0.0, 0.001));
        Order order1 = new Order("Человек", ShootType.PORTRAIT, LocalDateTime.of(2026, 11, 15, 15, 0), 3, 2000);
        order1.setShotFrames(8000);
        TestUtils.check("order1.hasShotFrames() --> true", order1.hasShotFrames());
        TestUtils.check("order1.getShotFrames() --> 8000", order1.getShotFrames() == 8000);
        TestUtils.check("order1.cullingMinutes() --> 240", TestUtils.equalsDouble(order1.cullingMinutes(), 240.0, 0.001));
        thrown = false;
        try {
            order1.setShotFrames(0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("setShotFrames(0) --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            order1.setShotFrames(-500);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("setShotFrames(-500) --> IllegalArgumentException", thrown);
        TestUtils.check("order1.getShotFrames() после некорректного ввода по прежнему --> 8000", order1.getShotFrames() == 8000);
        thrown = false;
        try {
            order1.setSelectedFrames(9000);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("Отобрано больше, чем снято: setSelectedFrames(9000) при 8000 → IllegalArgumentException", thrown);
        Order orderWithoutShots = new Order("Человек", ShootType.PORTRAIT, LocalDateTime.of(2026, 11, 15, 15, 0), 3, 2000);
        thrown = false;
        try {
            orderWithoutShots.setSelectedFrames(100);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        TestUtils.check("Сразу этап 2, без внесения отснятых кадров orderWithoutShots.setSelectedFrames(100) --> IllegalStateException", thrown);
        order1.setSelectedFrames(800);
        TestUtils.check("order1.getSelectedFrames() --> 800", order1.getSelectedFrames() == 800);
        thrown = false;
        try {
            order1.setSelectedFrames(0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("setSelectedFrames(0) --> IllegalArgumentException", thrown);
        order1.setSelectedFrames(8000);
        TestUtils.check("Граница: setSelectedFrames(8000) при 8000 снятых → разрешено", order1.getSelectedFrames() == 8000);
        order1.setSelectedFrames(800);
        TestUtils.check("setSelectedFrames(800) → colorMinutes()", TestUtils.equalsDouble(order1.colorMinutes(), 800.0 / 100 * ShootType.PORTRAIT.getColorMinutesPer100(), 0.001));
        TestUtils.check("setSelectedFrames(800) → retouchMinutes()", TestUtils.equalsDouble(order1.retouchMinutes(), 800.0 * ShootType.PORTRAIT.getRetouchMinutesPerFrame(), 0.001));
        thrown = false;
        try {
            order1.setShotFrames(500);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("500 снятых при уже 800 отобранных order1.setShotFrames(500) --> IllegalArgumentException", thrown);
        TestUtils.check("после отказа order1.getShotFrames() == 8000", order1.getShotFrames() == 8000);
        Order orderWedding = new Order("Анна", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 12, 50000);
        TestUtils.check("Свадьба orderWedding.deadline() --> 2026-11-28", orderWedding.deadline().equals(LocalDate.of(2026, 11, 28)));
        Order orderPortrait = new Order("Ванна", ShootType.PORTRAIT, LocalDateTime.of(2026, 11, 14, 15, 0), 1, 5000);
        TestUtils.check(" Портрет orderPortrait.deadline() --> 2026-11-19", orderPortrait.deadline().equals(LocalDate.of(2026, 11, 19)));
        Order orderWeddingEvening = new Order("Жанна", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 23, 30), 2, 7000);
        TestUtils.check("свадьба 14.11 в 23:30 → срок 2026-11-28, время не влияет", orderWeddingEvening.deadline().equals(LocalDate.of(2026, 11, 28)));
        Order orderReportage = new Order("Жанна", ShootType.REPORTAGE, LocalDateTime.of(2026, 11, 28, 23, 30), 12, 50000);
        TestUtils.check("Репортаж orderReportage.deadline() --> 2026-12-05", orderReportage.deadline().equals(LocalDate.of(2026, 12, 5)));
        TestUtils.check("orderWedding.plannedSelectedFrames() --> 0", orderWedding.plannedSelectedFrames() == 0);
        orderWedding.setShotFrames(8000);
        TestUtils.check("orderWedding.stageDays(StageType.CULLING) --> 2", orderWedding.stageDays(StageType.CULLING) == 2);
        TestUtils.check("orderWedding.plannedSelectedFrames() --> 800", orderWedding.plannedSelectedFrames() == 800);
        orderWedding.setSelectedFrames(800);
        TestUtils.check("orderWedding.stageDeadline(StageType.CULLING) --> 21.11.2026", LocalDate.of(2026, 11, 21).equals(orderWedding.stageDeadline(StageType.CULLING).orElse(null)));
        TestUtils.check("orderWedding.stageDays(StageType.COLOR) --> 1", orderWedding.stageDays(StageType.COLOR) == 1);
        TestUtils.check("orderWedding.stageDeadline(StageType.COLOR) --> 22.11.2026", LocalDate.of(2026, 11, 22).equals(orderWedding.stageDeadline(StageType.COLOR).orElse(null)));
        TestUtils.check("orderWedding.stageDays(StageType.RETOUCH) --> 5", orderWedding.stageDays(StageType.RETOUCH) == 5);
        TestUtils.check("orderWedding.stageDeadline(StageType.RETOUCH) --> 27.11.2026", LocalDate.of(2026, 11, 27).equals(orderWedding.stageDeadline(StageType.RETOUCH).orElse(null)));
        TestUtils.check("orderWedding.stageDeadline(StageType.EXPORT) --> 27.11.2026", LocalDate.of(2026, 11, 27).equals(orderWedding.stageDeadline(StageType.EXPORT).orElse(null)));
        TestUtils.check("orderWedding.stageDeadline(StageType.DELIVERY) --> 27.11.2026", LocalDate.of(2026, 11, 27).equals(orderWedding.stageDeadline(StageType.DELIVERY).orElse(null)));
        TestUtils.check("orderWedding.stageDays(StageType.EXPORT) --> 0", orderWedding.stageDays(StageType.EXPORT) == 0);
        TestUtils.check("orderWedding.stageDays(StageType.DELIVERY) --> 0", orderWedding.stageDays(StageType.DELIVERY) == 0);
        TestUtils.check("orderWedding.stageDays(StageType.REVISIONS) --> 1", orderWedding.stageDays(StageType.REVISIONS) == 1);
        Order noFrames = new Order("Ольга", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        TestUtils.check("заказ без кадров → stageDeadline(RETOUCH) пусто", noFrames.stageDeadline(StageType.RETOUCH).isEmpty());
        TestUtils.check("orderWedding.stageDeadline(StageType.REVISIONS) --> 28.11.2026", LocalDate.of(2026, 11, 28).equals(orderWedding.stageDeadline(StageType.REVISIONS).orElse(null)));
        thrown = false;
        try {
            orderWedding.stageDeadline(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("orderWedding.stageDeadline(null) --> IAE", thrown);
        Order order3 = new Order("Клиентушка", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 12, 0), 2, 2000);
        order3.setShotFrames(8000);
        order3.setSelectedFrames(1200);
        TestUtils.check("order3.stageDeadline(StageType.CULLING) --> 18.11.2026", LocalDate.of(2026, 11, 18).equals(order3.stageDeadline(StageType.CULLING).orElse(null)));
        TestUtils.check("order3.stageDeadline(StageType.COLOR) --> 20.11.2026", LocalDate.of(2026, 11, 20).equals(order3.stageDeadline(StageType.COLOR).orElse(null)));
        TestUtils.check("order3.stageDeadline(StageType.RETOUCH) --> 27.11.2026", LocalDate.of(2026, 11, 27).equals(order3.stageDeadline(StageType.RETOUCH).orElse(null)));
        TestUtils.check("order3.stageDeadline(StageType.EXPORT) --> 27.11.2026", LocalDate.of(2026, 11, 27).equals(order3.stageDeadline(StageType.EXPORT).orElse(null)));
        TestUtils.check("order3.stageDeadline(StageType.DELIVERY) --> 27.11.2026", LocalDate.of(2026, 11, 27).equals(order3.stageDeadline(StageType.DELIVERY).orElse(null)));
        TestUtils.check("order3.stageDeadline(StageType.REVISIONS) --> 28.11.2026", LocalDate.of(2026, 11, 28).equals(order3.stageDeadline(StageType.REVISIONS).orElse(null)));
        TestUtils.check("order3.stageDays(StageType.COLOR) --> 2", order3.stageDays(StageType.COLOR) == 2);
        TestUtils.check("order3.stageDays(StageType.RETOUCH) --> 7", order3.stageDays(StageType.RETOUCH) == 7);
        Order onlyShot = new Order("Ирина", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        onlyShot.setShotFrames(8000);
        TestUtils.check("onlyShot.stageDeadline(StageType.CULLING) --> 21.11.2026", LocalDate.of(2026, 11, 21).equals(onlyShot.stageDeadline(StageType.CULLING).orElse(null)));
        Order rounding = new Order("Олег", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        rounding.setShotFrames(8335);
        TestUtils.check("прогноз 8335 × 10% = 833.5 → Math.round → 834",
                rounding.plannedSelectedFrames() == 834);
        Order fresh = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        TestUtils.check("новый заказ → isCompleted false", !fresh.isCompleted());
        TestUtils.check("новый заказ → currentStage() --> CULLING ", fresh.currentStage().isPresent() && fresh.currentStage().get().getType() == StageType.CULLING);
        fresh.currentStage().get().start();
        TestUtils.check("Текущий этап в работе → currentStage() --> IN_PROGRESS ", (fresh.currentStage().isPresent() && fresh.currentStage().get().getType() == StageType.CULLING) && (fresh.currentStage().get().getStatus() == StageStatus.IN_PROGRESS));
        fresh.currentStage().get().finish(60);
        TestUtils.check("Этап завершен, следующий этап стал текущим → currentStage() --> COLOR ", fresh.currentStage().isPresent() && fresh.currentStage().get().getType() == StageType.COLOR);


        Order allDone = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        for (StageType type : StageType.values()) {
            allDone.getStage(type).start();
            allDone.getStage(type).finish(60);
        }
        TestUtils.check("все 6 этапов DONE → isCompleted true", allDone.isCompleted());
        TestUtils.check("все 6 этапов DONE → currentStage() --> Empty ", allDone.currentStage().isEmpty());

        Order fiveDone = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        for (StageType type : StageType.values()) {
            if (type != StageType.REVISIONS) {
                fiveDone.getStage(type).start();
                fiveDone.getStage(type).finish(60);
            }
        }
        TestUtils.check("5 из 6 DONE (правки не начаты) → isCompleted false", !fiveDone.isCompleted());

        Order orderDue = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        orderDue.setShotFrames(8000);
        orderDue.setSelectedFrames(800);
        List<Stage> due = orderDue.stagesDue(LocalDate.of(2026, 11, 20));
        TestUtils.check("Список горящих заданий пуст, ничего не горит раньше срок --> isEmpty()", due.isEmpty());
        due = orderDue.stagesDue(LocalDate.of(2026, 11, 21));
        TestUtils.check("21.11: срок отбора сегодня --> size = 1, горит отбор", due.size() == 1 && due.get(0).getType() == StageType.CULLING);
        due = orderDue.stagesDue(LocalDate.of(2026, 11, 22));
        TestUtils.check("просроченный + сегодняшний --> size = 2, горят отбор и цвет", due.size() == 2 && due.get(0).getType() == StageType.CULLING && due.get(1).getType() == StageType.COLOR);
        due = orderDue.stagesDue(LocalDate.of(2026, 11, 28));
        TestUtils.check("size = 6, все сроки прошли", due.size() == 6 && due.get(0).getType() == StageType.CULLING && due.get(1).getType() == StageType.COLOR && due.get(2).getType() == StageType.RETOUCH && due.get(3).getType() == StageType.EXPORT && due.get(4).getType() == StageType.DELIVERY && due.get(5).getType() == StageType.REVISIONS);
        orderDue.getStage(StageType.CULLING).start();
        orderDue.getStage(StageType.CULLING).finish(60);
        due = orderDue.stagesDue(LocalDate.of(2026, 11, 22));
        TestUtils.check("Отбор окончен --> size = 1, горит цвет", due.size() == 1 && due.get(0).getType() == StageType.COLOR);
        Order order4 = new Order("Света", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 8, 40000);
        TestUtils.check("без платежей --> остаток 40000", order4.remainingAmount() == 40000);
        order4.addPayment(10000, LocalDate.of(2026, 11, 20));
        TestUtils.check("Предоплата 10000 --> остаток 30000", order4.remainingAmount() == 30000);
        order4.addPayment(30000, LocalDate.of(2026, 11, 20));
        TestUtils.check("Предоплата еще 30000 --> 0", order4.remainingAmount() == 0);
        order4.addPayment(3000, LocalDate.of(2026, 11, 20));
        TestUtils.check("Чаевые 3000 --> -3000", order4.remainingAmount() == -3000);
        TestUtils.check("Оплата paidAmount() --> 43000", order4.paidAmount() == 43000);
        thrown = false;
        try {
            order4.addPayment(0, LocalDate.of(2026, 11, 20));
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("Оплата 0 --> IAE", thrown);
        thrown = false;
        try {
            order4.addPayment(100, null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("Дата оплаты null --> IAE", thrown);
        order4.getPayments().clear();
        TestUtils.check("Оплата по прежнему --> 43000", order4.paidAmount() == 43000);
    }
}
