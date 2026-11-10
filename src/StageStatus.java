public enum StageStatus {
    NOT_STARTED("Не начат"),
    IN_PROGRESS("В процессе"),
    DONE("Завершен");

    private final String title;

    StageStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
