import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final Tracker tracker = new Tracker();

    public static void main(String[] args) {
        Path path = Path.of("orders.txt");
        System.out.println(path.toAbsolutePath());
        if (!loadOrders(path)){
            return;
        }
        printMenu();
        try{
            OrderFileStorage.saveToFile(path, tracker.getOrders());
        }catch (IOException e){
            System.out.println("Ошибка сохранения");
        }
    }

    static void printMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("====Photo Tracker v.1.0====");
        while (true) {
            System.out.println("*".repeat(20));
            System.out.println();
            System.out.println("1. Новый заказ");
            System.out.println("2. Список заказов");
            System.out.println("3. Ввести кадры");
            System.out.println("4. Начать / завершить этап");
            System.out.println("5. Добавить платёж");
            System.out.println("6. Что горит сегодня");
            System.out.println("7. Сводка: в работе, ближайшая сдача");
            System.out.println("8. Заработок за месяц и всего");
            System.out.println("0. Выход");
            System.out.println();
            System.out.println("*".repeat(20));
            System.out.println();
            int cmd = readIntInRange(scanner, "Выберите пункт меню: ", 0, 8);
            switch (cmd) {
                case 1 -> handleAddOrder(scanner);
                case 2 -> printOrders(tracker);
                case 3 -> handleSetFrames(scanner);
                case 4 -> handleAdvanceStage(scanner);
                case 5 -> handleAddPayment(scanner);
                case 6 -> handlePrintDueStages();
                case 7 -> handlePrintSummary();
                case 8 -> handleEarnings(scanner);
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

    static void handleSetFrames(Scanner scanner) {
        Optional<Order> orderBox = readOrder(scanner);
        if (orderBox.isEmpty()) {
            return;
        }
        Order order = orderBox.get();
        System.out.println("1. Ввести количество отснятых кадров");
        System.out.println("2. Ввести количество отобранных кадров");
        int cmd = readIntInRange(scanner, "Выберите что нужно ввести: ", 1, 2);
        if (cmd == 1) {
            try {
                order.setShotFrames(readInt(scanner, "Введите количество отснятых кадров: "));
                System.out.println("На отбор потребуется " + minutesPerTime(order.cullingMinutes()));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        } else {
            try {
                order.setSelectedFrames(readInt(scanner, "Введите количество отобранных кадров: "));
                System.out.println("На ретушь потребуется " + minutesPerTime(order.retouchMinutes()));
            } catch (IllegalStateException | IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
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
        if (tracker.getOrders().isEmpty()) {
            System.out.println("Список заказов пуст");
        }
        for (Order order : tracker.getOrders()) {
            System.out.printf("%2d %15s %10s %15s %4.2f %5d %8s%n", index, order.getClient(), order.getShootType().getTitle(), order.getDateTimeShoot().format(DATE_TIME), order.getHours(), order.getPrice(), order.deadline().format(DATE));
            index++;
        }
    }

    static Optional<Order> readOrder(Scanner scanner) {
        List<Order> orders = tracker.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Список заказов пуст");
            return Optional.empty();
        }
        printOrders(tracker);
        int number = readIntInRange(scanner, "Выберите номер заказа: ", 1, orders.size());
        int index = number - 1;
        return Optional.of(orders.get(index));
    }

    static void handleAdvanceStage(Scanner scanner) {
        Optional<Order> orderOptional = readOrder(scanner);
        if (orderOptional.isEmpty()) {
            System.out.println("Заказ не выбран");
            return;
        }

        Order order = orderOptional.get();
        Optional<Stage> currentStageOptional = order.currentStage();

        if (currentStageOptional.isEmpty()) {
            System.out.println("Все этапы завершены, заказ выполнен");
            return;
        }

        Stage currentStage = currentStageOptional.get();

        if (currentStage.getStatus() == StageStatus.NOT_STARTED) {
            currentStage.start();
            System.out.println("Этап " + currentStage.getType().getTitle() + " начат");
            return;
        }

        if (currentStage.getStatus() == StageStatus.IN_PROGRESS) {
            int actualMinutes = readIntInRange(
                    scanner,
                    "Введите фактическое время выполнения этапа",
                    1,
                    5000
            );

            currentStage.finish(actualMinutes);

            Optional<Stage> nextStageOptional = order.currentStage();
            if (nextStageOptional.isPresent()) {
                nextStageOptional.get().start();
                System.out.println("Начат следующий этап - " + nextStageOptional.get().getType().getTitle());
            } else {
                System.out.println("Все этапы завершены, заказ выполнен");
            }
        }
    }

    static void handleAddPayment(Scanner scanner) {
        Optional<Order> orderBox = readOrder(scanner);
        if (orderBox.isEmpty()) {
            return;
        }
        Order order = orderBox.get();
        int amount = readInt(scanner, "Введите сумму оплаты: ");
        LocalDate paymentDate = readDate(scanner, "Введите дату оплаты: ");
        try {
            order.addPayment(amount, paymentDate);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return;
        }
        System.out.println("Платеж добавлен.");
        System.out.println("Оплачено всего: " + order.paidAmount() + "₽");
        System.out.println("Осталось оплатить: " + order.remainingAmount() + "₽");
    }

    static void handleEarnings(Scanner scanner) {
        int month = readIntInRange(scanner, "Введите месяц: ", 1, 12);
        int year = readIntInRange(scanner, "Введите год: ", 2026, 2100);
        YearMonth yearMonth = YearMonth.of(year, month);
        System.out.println("За месяц: " + tracker.earningsForMonth(yearMonth));
        System.out.println("За все время: " + tracker.totalEarnings());
    }

    static void handlePrintSummary() {
        int inProgress = tracker.ordersInProgress();
        System.out.println("Заказов в работе: " + inProgress);
        if (tracker.nearestDeadline().isPresent()) {
            LocalDate deadline = tracker.nearestDeadline().get();
            String formattedDeadline = deadline.format(DATE);
            System.out.println("Ближайшая сдача: " + formattedDeadline);
            return;
        }
        System.out.println("Ближайшая сдача: —");
    }

    static void handlePrintDueStages() {
        LocalDate today = LocalDate.now();
        List<DueStage> listOfDue = tracker.stagesDueToday(today);
        if (listOfDue.isEmpty()) {
            System.out.println("Нет этапов со сроком на сегодня или раньше");
            return;
        }
        for (DueStage dueStage : listOfDue) {
            String client = dueStage.client();
            String stageTitle = dueStage.stageType().getTitle();
            LocalDate deadline = dueStage.deadline();
            String label = deadline.isBefore(today) ? "просрочен" : "сегодня";
            System.out.printf("%-10s | %-16s | %s | %s%n", client, stageTitle, deadline.format(DATE), label);
        }
    }


    static String minutesPerTime(double minutes) {
        return String.format("%d ч., %d мин.", (int) minutes / 60, (int) minutes % 60);
    }

    static boolean loadOrders(Path path) {
        try {
            List<Order> orders = OrderFileStorage.loadFromFile(path);
            for (Order order : orders) {
                tracker.addOrder(order);
            }
        } catch (NoSuchFileException e) {
            System.out.println("Файл заказов пока не создан. Начинаем с пустого списка");
            return true;
        } catch (IOException | IllegalArgumentException | DateTimeParseException e) {
            System.out.println("Ошибка " + e);
            return false;
        }
        return true;
    }
}
