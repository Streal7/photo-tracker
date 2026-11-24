import java.time.LocalDate;

record DueStage(String client, StageType stageType, LocalDate deadline) {
}
