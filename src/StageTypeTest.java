public class StageTypeTest {

    public static void main(String[] args) {
        TestUtils.check("StageType.COLOR.getTitle() --> Цветокоррекция", "Цветокоррекция".equals(StageType.COLOR.getTitle()));
        TestUtils.check("StageType.CULLING.getTitle() --> Отбор", "Отбор".equals(StageType.CULLING.getTitle()));
        TestUtils.check("StageType.RETOUCH.getTitle() --> Ретушь", "Ретушь".equals(StageType.RETOUCH.getTitle()));
        TestUtils.check("StageType.EXPORT.getTitle() --> Экспорт", "Экспорт".equals(StageType.EXPORT.getTitle()));
        TestUtils.check("StageType.DELIVERY.getTitle() --> Отдача", "Отдача".equals(StageType.DELIVERY.getTitle()));
        TestUtils.check("StageType.REVISIONS.getTitle() --> Правки", "Правки".equals(StageType.REVISIONS.getTitle()));
        TestUtils.check("StageType.values().length --> 6", StageType.values().length == 6);
    }
}
