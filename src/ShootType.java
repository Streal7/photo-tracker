enum ShootType {
    WEDDING("Свадьба", 14, 1.0, 20.0),
    REPORTAGE("Репортаж", 7, 1.0, 15.0),
    PORTRAIT("Портрет", 5, 3.0, 20.0);

    private final String title;
    private final int deadlineDays;
    private final double retouchMinutesPerFrame;
    private final double colorMinutesPer100;

    ShootType(String title, int deadlineDays, double retouchMinutesPerFrame, double colorMinutesPer100) {
        this.title = title;
        this.deadlineDays = deadlineDays;
        this.retouchMinutesPerFrame = retouchMinutesPerFrame;
        this.colorMinutesPer100 = colorMinutesPer100;
    }

    public String getTitle() {
        return title;
    }

    public int getDeadlineDays() {
        return deadlineDays;
    }

    public double getRetouchMinutesPerFrame() {
        return retouchMinutesPerFrame;
    }

    public double getColorMinutesPer100() {
        return colorMinutesPer100;
    }
}
