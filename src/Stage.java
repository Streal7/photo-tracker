public class Stage {
    private final StageType type;
    private StageStatus status;

    Stage(StageType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип не может быть пустым");
        }
        this.type = type;
        status = StageStatus.NOT_STARTED;
    }

    void start() {
        throw new UnsupportedOperationException("Ещё не реализовано");
    }

    void finish() {
        throw new UnsupportedOperationException("Ещё не реализовано");
    }

    public StageType getType() {
        return type;
    }

    public StageStatus getStatus() {
        return status;
    }
}
