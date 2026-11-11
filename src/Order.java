import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String client;
    private final ShootType shootType;
    private final LocalDateTime dateTimeShoot;
    private final double hours;
    private final int price;
    private final List<Stage> stages = new ArrayList<>();

    Order(String client, ShootType shootType, LocalDateTime dateTimeShoot, double hours, int price) {
        if (client == null || client.isBlank()) {
            throw new IllegalArgumentException("Поле клиент не может быть пустым");
        }
        if (shootType == null) {
            throw new IllegalArgumentException("Тип съемки не может быть пустым");
        }
        if (dateTimeShoot == null) {
            throw new IllegalArgumentException("Время съемки не может быть пустым");
        }
        if (hours <= 0) {
            throw new IllegalArgumentException("Часы съемки должны быть больше нуля");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }

        this.client = client;
        this.shootType = shootType;
        this.dateTimeShoot = dateTimeShoot;
        this.hours = hours;
        this.price = price;
        for (StageType type : StageType.values()) {
            stages.add(new Stage(type));
        }
    }

    public String getClient() {
        return client;
    }

    public ShootType getShootType() {
        return shootType;
    }

    public LocalDateTime getDateTimeShoot() {
        return dateTimeShoot;
    }

    public double getHours() {
        return hours;
    }

    public int getPrice() {
        return price;
    }
    public List<Stage> getStages() {
        return new ArrayList<>(stages);
    }
}
