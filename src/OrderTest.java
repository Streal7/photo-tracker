import java.time.LocalDate;
import java.time.LocalDateTime;

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
    }
}
