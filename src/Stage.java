public class Stage {
    private final StageType type;
    private StageStatus status;
    private int actualMinutes;

    Stage(StageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип не может быть пустым");
        }
        this.type = type;
        status = StageStatus.NOT_STARTED;
    }

    public int getActualMinutes() {
        return actualMinutes;
    }

    void start() {
        if (status != StageStatus.NOT_STARTED) {
            throw new IllegalStateException("Невозможно начать этап из этого состояния " + status.getTitle());
        }
        status = StageStatus.IN_PROGRESS;
    }

    void finish(int actualMinutes) {
        if (actualMinutes <= 0) {
            throw new IllegalArgumentException("Время должно быть больше нуля");
        }
        if (status != StageStatus.IN_PROGRESS) {
            throw new IllegalStateException("Невозможно завершить этап из статуса " + status.getTitle());
        }
        this.actualMinutes = actualMinutes;
        status = StageStatus.DONE;
    }

    public StageType getType() {
        return type;
    }

    public StageStatus getStatus() {
        return status;
    }

}
