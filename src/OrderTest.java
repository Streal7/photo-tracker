import java.time.LocalDateTime;

public class OrderTest {
        public static void main(String[] args) {
            Order order = new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5, 25000);
            TestUtils.check("new Order(\"Мария\",...).getClient --> Мария", "Мария".equals(order.getClient()));
            TestUtils.check("new Order(...,ShootType.WEDDING,...).getShootType --> WEDDING", order.getShootType() == ShootType.WEDDING);
            TestUtils.check("new Order(...,2026-11-14 15:00,...).getDateTimeShoot --> 2026-11-14T15:00", order.getDateTimeShoot().equals(LocalDateTime.of(2026, 11, 14, 15, 0)));
            TestUtils.check("new Order(...,5,...).getHours --> 5.0", TestUtils.equalsDouble(order.getHours(),5,0.001));
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
            TestUtils.check("new Order(...,0).getPrice --> 0", new Order("Мария", ShootType.WEDDING, LocalDateTime.of(2026, 11, 14, 15, 0), 5,0).getPrice() == 0);

        }
    }
