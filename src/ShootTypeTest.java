public class ShootTypeTest {


    public static void main(String[] args) {
        TestUtils.check("WEDDING.getDeadlineDays() → 14", ShootType.WEDDING.getDeadlineDays() == 14);
        TestUtils.check("REPORTAGE.getDeadlineDays() → 7", ShootType.REPORTAGE.getDeadlineDays() == 7);
        TestUtils.check("PORTRAIT.getDeadlineDays() → 5", ShootType.PORTRAIT.getDeadlineDays() == 5);
        TestUtils.check("ShootType.values().length == 3", ShootType.values().length == 3);
        TestUtils.check("ShootType.WEDDING.getRetouchMinutesPerFrame() -> 1", TestUtils.equalsDouble(ShootType.WEDDING.getRetouchMinutesPerFrame(), 1.0, 0.001));
        TestUtils.check("ShootType.REPORTAGE.getRetouchMinutesPerFrame() -> 1", TestUtils.equalsDouble(ShootType.REPORTAGE.getRetouchMinutesPerFrame(), 1.0, 0.001));
        TestUtils.check("ShootType.PORTRAIT.getRetouchMinutesPerFrame() -> 3", TestUtils.equalsDouble(ShootType.PORTRAIT.getRetouchMinutesPerFrame(), 3.0, 0.001));
        TestUtils.check("ShootType.WEDDING.getColorMinutesPer100() -> 20", TestUtils.equalsDouble(ShootType.WEDDING.getColorMinutesPer100(), 20.0, 0.001));
        TestUtils.check("ShootType.REPORTAGE.getColorMinutesPer100() -> 15", TestUtils.equalsDouble(ShootType.REPORTAGE.getColorMinutesPer100(), 15.0, 0.001));
        TestUtils.check("ShootType.PORTRAIT.getColorMinutesPer100() -> 20", TestUtils.equalsDouble(ShootType.PORTRAIT.getColorMinutesPer100(), 20.0, 0.001));
        TestUtils.check("ShootType.WEDDING.getTitle() -> Свадьба", "Свадьба".equals(ShootType.WEDDING.getTitle()));
        TestUtils.check("ShootType.REPORTAGE.getTitle() -> Репортаж", "Репортаж".equals(ShootType.REPORTAGE.getTitle()));
        TestUtils.check("ShootType.PORTRAIT.getTitle() -> Портрет", "Портрет".equals(ShootType.PORTRAIT.getTitle()));
    }
}
