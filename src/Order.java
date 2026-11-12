import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    private static final int CULLING_MINUTES_PER_100 = 3;
    private static final int FRAMES_IN_PACKAGE = 100;
    private static final int PROCESSING_HOURS_PER_DAY = 3;
    private static final int REVISION_DAYS = 1;
    private static final int MINUTES_PER_HOUR = 60;
    private static final double SELECTION_RATE = 0.10;

    private final String client;
    private final ShootType shootType;
    private final LocalDateTime dateTimeShoot;
    private final double hours;
    private final int price;
    private final List<Stage> stages = new ArrayList<>();
    private int shotFrames;
    private int selectedFrames;

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

    public Stage getStage(StageType stageType) {
        if (stageType == null) {
            throw new IllegalArgumentException("Тип не может быть пустым");
        }
        for (Stage stage : stages) {
            if (stage.getType() == stageType) {
                return stage;
            }
        }
        throw new IllegalArgumentException("В заказе нет этапа: " + stageType);
    }

    public int getShotFrames() {
        return shotFrames;
    }

    public int getSelectedFrames() {
        return selectedFrames;
    }

    boolean hasShotFrames() {
        return shotFrames > 0;
    }

    boolean hasSelectedFrames() {
        return selectedFrames > 0;
    }

    public void setShotFrames(int shotFrames) {
        if (shotFrames <= 0) {
            throw new IllegalArgumentException("Количество отснятых кадров должно быть больше 0");
        }
        if (selectedFrames > shotFrames) {
            throw new IllegalArgumentException("Количество отобранных кадров не может быть больше количества отснятых кадров");
        }
        this.shotFrames = shotFrames;
    }

    public void setSelectedFrames(int selectedFrames) {
        if (!hasShotFrames()) {
            throw new IllegalStateException("Пропущен этап внесения отснятых кадров");
        }
        if (selectedFrames <= 0) {
            throw new IllegalArgumentException("Количество отобранных кадров должно быть больше 0");
        }
        if (selectedFrames > shotFrames) {
            throw new IllegalArgumentException("Количество отобранных кадров не может быть больше количества отснятых кадров");
        }

        this.selectedFrames = selectedFrames;
    }

    double cullingMinutes() {
        if (shotFrames == 0) {
            return 0;
        }
        return (double) shotFrames * CULLING_MINUTES_PER_100 / FRAMES_IN_PACKAGE;
    }

    double colorMinutes() {
        if (selectedFrames == 0) {
            return 0;
        }
        return (double) selectedFrames / FRAMES_IN_PACKAGE * shootType.getColorMinutesPer100();
    }

    double retouchMinutes() {
        if (selectedFrames == 0) {
            return 0;
        }
        return selectedFrames * shootType.getRetouchMinutesPerFrame();
    }

    public LocalDate deadline() {
        return dateTimeShoot.toLocalDate().plusDays(shootType.getDeadlineDays());
    }

    private int daysFor(double minutes) {
        return (int) Math.ceil(minutes / MINUTES_PER_HOUR / PROCESSING_HOURS_PER_DAY);
    }

    int stageDays(StageType type) {
        return switch (type) {
            case CULLING -> daysFor(cullingMinutes());
            case COLOR -> daysFor(colorMinutes());
            case RETOUCH -> daysFor(retouchMinutes());
            case EXPORT, DELIVERY -> 0;
            case REVISIONS -> REVISION_DAYS;
        };
    }

    Optional<LocalDate> stageDeadline(StageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип не может отсутствовать");
        }
        if (!hasSelectedFrames()) {
            return Optional.empty();
        }
        LocalDate date = deadline();                           // начинаем со срока сдачи
        for (int i = stages.size() - 1; i >= 0; i--) {         // этапы с последнего
            StageType current = stages.get(i).getType();
            if (current == type) {
                return Optional.of(date);                      // срок этого этапа = текущая date
            }
            date = date.minusDays(stageDays(current));         // сдвиг ПОСЛЕ проверки
        }
        throw new IllegalArgumentException("В заказе нет этапа: " + type);
    }

}
