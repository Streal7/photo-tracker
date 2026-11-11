public class StageTest {
    public static void main(String[] args) {
        Stage stage = new Stage(StageType.COLOR);
        TestUtils.check("stage.getStatus() --> NOT_STARTED", stage.getStatus() == StageStatus.NOT_STARTED);
        TestUtils.check("new Stage(StageType.COLOR).getType() --> COLOR", new Stage(StageType.COLOR).getType() == StageType.COLOR);
        boolean thrown = false;
        try {
            Stage nullTypeStage = new Stage(null);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("new Stage(null) --> IllegalArgumentException", thrown);
    }
}
