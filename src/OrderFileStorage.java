import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderFileStorage {
    Path path = Path.of("orders.txt");

    public static String encode(Order order) {
        return String.format("%s|%s|%s|%s|%s|%s|%s", order.getClient(), order.getShootType().name(), order.getDateTimeShoot().toString(), order.getHours(), order.getPrice(), order.getShotFrames(), order.getSelectedFrames());
    }

    public static Order decode(String line) {
        String[] fields = line.split("\\|", -1);
        if (fields.length != 5 && fields.length != 7) {
            throw new IllegalArgumentException(
                    "Ожидалось 5 или 7 полей заказа, получено " + fields.length);
        }
        String client = fields[0];
        ShootType shootType = ShootType.valueOf(fields[1]);
        LocalDateTime dateTime = LocalDateTime.parse(fields[2]);
        double hours = Double.parseDouble(fields[3]);
        int price = Integer.parseInt(fields[4]);
        Order order = new Order(client, shootType, dateTime, hours, price);
        if (fields.length == 7) {
            int shotFrames = Integer.parseInt(fields[5]);
            int selectedFrames = Integer.parseInt(fields[6]);
            if ((shotFrames < 0) || (selectedFrames < 0)) {
                throw new IllegalArgumentException("Значения не могут быть меньше нуля");
            }
            if ((shotFrames == 0) && (selectedFrames > 0)) {
                throw new IllegalArgumentException("Отобранных кадров не может быть больше, чем отснятых");
            }
            if (shotFrames > 0) {
                order.setShotFrames(shotFrames);
            }
            if (selectedFrames > 0) {
                order.setSelectedFrames(selectedFrames);
            }
        }
        return order;
    }

    public static void saveToFile(Path path, List<Order> orders) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Order order : orders) {
            lines.add(encode(order));
        }
        Files.write(path, lines);
    }

    public static List<Order> loadFromFile(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        List<Order> orders = new ArrayList<>();
        for (String line : lines) {
            orders.add(decode(line));
        }
        return orders;
    }
}
