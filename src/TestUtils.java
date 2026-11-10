public class TestUtils {
    public static void check(String prompt, boolean passes) {
        System.out.println((passes ? "✅" : "❌") + " " + prompt);
    }

    public static boolean equalsDouble(double a, double b, double eps) {
        return (Math.abs(a - b) < eps);
    }
}