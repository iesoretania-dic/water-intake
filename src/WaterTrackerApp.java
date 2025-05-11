import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class WaterTrackerApp {
    private static final int DAILY_GOAL = 2000; // 2L in mL
    private static final Scanner sc = new Scanner(System.in);
    private static final DataManager dataManager = new DataManager();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput();
            switch (choice) {
                case 1 -> logWater();
                case 2 -> showProgress();
                case 3 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Exiting...");
        sc.close();
    }

    private static void printMenu() {
        System.out.println("\nWater Tracking App");
        System.out.println("1. Log water intake");
        System.out.println("2. View today's progress");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getIntInput() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.next();
            }
        }
    }

    private static void logWater() {
        System.out.print("Enter amount in mL: ");
        int amount = getIntInput();
        try {
            dataManager.saveEntry(new WaterEntry(LocalDateTime.now(), amount));
            System.out.println("Entry saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving entry: " + e.getMessage());
        }
    }

    private static void showProgress() {
        try {
            int total = dataManager.getTodaysTotal();
            System.out.println("\nToday's Water Intake: " + total + " mL");
            System.out.println("Daily Goal: " + DAILY_GOAL + " mL");

            int remaining = DAILY_GOAL - total;
            if (remaining > 0) {
                System.out.println("Remaining to drink: " + remaining + " mL");
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1);
                long hoursLeft = ChronoUnit.HOURS.between(now, midnight);

                if (hoursLeft > 0) {
                    int suggested = (int) Math.ceil((double) remaining / hoursLeft);
                    System.out.println("Suggested intake per hour: ~" + suggested + " mL");
                }
            } else {
                System.out.println("Congratulations! You've met your daily goal!");
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
