public class StageStatusTest {
    public static void main(String[] args) {

        TestUtils.check("StageStatus.DONE.getTitle() --> Завершен", "Завершен".equals(StageStatus.DONE.getTitle()));
        TestUtils.check("StageStatus.IN_PROGRESS.getTitle() --> В процессе", "В процессе".equals(StageStatus.IN_PROGRESS.getTitle()));
        TestUtils.check("StageStatus.NOT_STARTED.getTitle() --> Не начат", "Не начат".equals(StageStatus.NOT_STARTED.getTitle()));
        TestUtils.check("StageStatus.values().length --> 3", StageStatus.values().length == 3);
    }
}
