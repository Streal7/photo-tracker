public enum StageType {
    CULLING("Отбор"),
    COLOR("Цветокоррекция"),
    RETOUCH("Ретушь"),
    EXPORT("Экспорт"),
    DELIVERY("Отдача"),
    REVISIONS("Правки");

    private final String title;

    StageType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
