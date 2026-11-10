public class ShootTypeTest {
    public static void check(String prompt, boolean passes) {
        System.out.println((passes ? "✅" : "❌") + " " + prompt);
    }

    public static boolean equalsDouble(double a, double b, double eps) {
        return (Math.abs(a - b) < eps);
    }

    public static void main(String[] args) {
        check("WEDDING.getDeadlineDays() → 14", ShootType.WEDDING.getDeadlineDays() == 14);
        check("REPORTAGE.getDeadlineDays() → 7", ShootType.REPORTAGE.getDeadlineDays() == 7);
        check("PORTRAIT.getDeadlineDays() → 5", ShootType.PORTRAIT.getDeadlineDays() == 5);
        check("ShootType.values().length == 3", ShootType.values().length == 3);
        check("ShootType.WEDDING.getRetouchMinutesPerFrame() -> 1", equalsDouble(ShootType.WEDDING.getRetouchMinutesPerFrame(), 1.0, 0.001));
        check("ShootType.REPORTAGE.getRetouchMinutesPerFrame() -> 1", equalsDouble(ShootType.REPORTAGE.getRetouchMinutesPerFrame(), 1.0, 0.001));
        check("ShootType.PORTRAIT.getRetouchMinutesPerFrame() -> 3", equalsDouble(ShootType.PORTRAIT.getRetouchMinutesPerFrame(), 3.0, 0.001));
        check("ShootType.WEDDING.getColorMinutesPer100() -> 20", equalsDouble(ShootType.WEDDING.getColorMinutesPer100(), 20.0, 0.001));
        check("ShootType.REPORTAGE.getColorMinutesPer100() -> 15", equalsDouble(ShootType.REPORTAGE.getColorMinutesPer100(), 15.0, 0.001));
        check("ShootType.PORTRAIT.getColorMinutesPer100() -> 20", equalsDouble(ShootType.PORTRAIT.getColorMinutesPer100(), 20.0, 0.001));
        check("ShootType.WEDDING.getTitle() -> Свадьба", "Свадьба".equals(ShootType.WEDDING.getTitle()));
        check("ShootType.REPORTAGE.getTitle() -> Репортаж", "Репортаж".equals(ShootType.REPORTAGE.getTitle()));
        check("ShootType.PORTRAIT.getTitle() -> Портрет", "Портрет".equals(ShootType.PORTRAIT.getTitle()));


    }
}
