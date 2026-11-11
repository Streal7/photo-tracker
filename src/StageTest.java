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
        Stage stage1 = new Stage(StageType.CULLING);
        stage1.start();
        TestUtils.check("stage1.getStatus() после start() --> IN_PROGRESS", stage1.getStatus() == StageStatus.IN_PROGRESS);
        thrown = false;
        try {
            stage1.start();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        TestUtils.check("start() после start() --> IllegalStateException", thrown);
        stage1.finish(60);
        TestUtils.check("stage1.getStatus() после finish(60) --> DONE", stage1.getStatus() == StageStatus.DONE);
        TestUtils.check("stage1.getActualMinutes == 60", stage1.getActualMinutes() == 60);
        thrown = false;
        try {
            stage1.finish(60);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        TestUtils.check("finish(60) со статусом DONE --> IllegalStateException", thrown);
        thrown = false;
        try {
            stage1.start();
        } catch (IllegalStateException e) {
            thrown = true;
        }
        TestUtils.check("start() со статусом == DONE --> IllegalStateException", thrown);
        Stage stageNotStarted = new Stage(StageType.CULLING);
        thrown = false;
        try {
            stageNotStarted.finish(60);
        } catch (IllegalStateException e) {
            thrown = true;
        }
        TestUtils.check("stageNotStarted.finish(60) --> IllegalStateException", thrown);
        thrown = false;
        try {
            stageNotStarted.finish(-60);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("stageNotStarted.finish(-60) --> IllegalArgumentException", thrown);
        Stage stage2 = new Stage(StageType.CULLING);
        stage2.start();
        thrown = false;
        try {
            stage2.finish(0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("stage2.finish(0) --> IllegalArgumentException", thrown);
        thrown = false;
        try {
            stage2.finish(-60);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        TestUtils.check("stage2.finish(-60) --> IllegalArgumentException", thrown);
        TestUtils.check("Статус после неудачного finish по прежнему IN_PROGRESS", stage2.getStatus() == StageStatus.IN_PROGRESS);
        TestUtils.check("stage2.getActualMinutes по прежнему 0", stage2.getActualMinutes() == 0);

    }
}
