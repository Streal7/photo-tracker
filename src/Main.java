import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final Tracker tracker = new Tracker();

    public static void main(String[] args) {
        printMenu();
    }

    static void printMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====Photo Tracker v.1.0====");
        while (true) {
            System.out.println("1. Новый заказ");
            System.out.println("2. Список заказов");
            System.out.println("3. Ввести кадры");
            System.out.println("4. Начать / завершить этап");
            System.out.println("5. Добавить платёж");
            System.out.println("6. Что горит сегодня");
            System.out.println("7. Сводка: в работе, ближайшая сдача");
            System.out.println("8. Заработок за месяц и всего");
            System.out.println("0. Выход");
            System.out.println("*".repeat(20));
            System.out.println();
            int cmd = readIntInRange(scanner, "Выберите пункт меню:", 0, 8);
            switch (cmd) {
                case 1 -> handleAddOrder(scanner);
                case 2 -> printOrders(tracker);
                case 0 -> {
                    return;
                }
            }
        }
    }

    static void handleAddOrder(Scanner scanner) {
        String client = readNonEmptyLine(scanner, "Введите имя клиента: ");
        ShootType[] types = ShootType.values();
        int index = 0;
        for (ShootType type : types) {
            System.out.println((index + 1) + ". " + type.getTitle());
            index++;
        }
        int number = readIntInRange(scanner, "Выберите тип съемки: ", 1, types.length);
        ShootType shootType = types[number - 1];
        LocalDateTime dateTimeShoot = readDateTime(scanner, "Введите дату и время съемки: ");
        double hours = readDouble(scanner, "Введите количество часов съемки: ");
        int price = readInt(scanner, "Введите цену съемки: ");
        try {
            Order order = new Order(client, shootType, dateTimeShoot, hours, price);
            tracker.addOrder(order);
            System.out.println("Заказ добавлен.");
        } catch (IllegalArgumentException e) {
            System.out.println("Заказ оформлен некорректно. Ошибка: " + e.getMessage());
        }
    }


    static String readNonEmptyLine(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String result = scanner.nextLine().trim();
            if (!result.isEmpty()) {
                return result;
            }
            System.out.println("Строка не может быть пустой");
        }
    }

    static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return LocalDate.parse(line, DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Неверная дата, формат дд.мм.гггг");
            }
        }
    }

    static LocalDateTime readDateTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(line, DATE_TIME);
            } catch (DateTimeParseException e) {
                System.out.println("Неверная дата, формат дд.мм.гггг ЧЧ:ММ");
            }
        }
    }

    static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Требуется ввести целое число");
            }
        }
    }

    static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Требуется ввести число, дробная часть через точку");
            }
        }
    }

    static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int number = readInt(scanner, prompt);
            if (number >= min && number <= max) {
                return number;
            }
            System.out.println("Введите число от " + min + " до " + max);
        }
    }

    static void printOrders(Tracker tracker) {
        int index = 1;
        for (Order order : tracker.getOrders()) {
            System.out.printf("%2d %15s %10s %15s %4.2f %5d %8s%n", index, order.getClient(), order.getShootType().getTitle(), order.getDateTimeShoot().format(DATE_TIME), order.getHours(), order.getPrice(), order.deadline().format(DATE));
            index++;
        }
    }
}
