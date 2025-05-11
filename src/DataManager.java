import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

class DataManager {
    private static final String FILE_PATH = System.getProperty("user.home") + File.separator + "water_tracker.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public void saveEntry(WaterEntry entry) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = entry.timestamp().format(formatter) + "," + entry.amount();
            writer.write(line);
            writer.newLine();
        }
    }

    public List<WaterEntry> loadEntries() throws IOException {
        List<WaterEntry> entries = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return entries;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) continue;
                try {
                    LocalDateTime timestamp = LocalDateTime.parse(parts[0], formatter);
                    int amount = Integer.parseInt(parts[1]);
                    entries.add(new WaterEntry(timestamp, amount));
                } catch (DateTimeParseException | NumberFormatException ignored) {
                }
            }
        }
        return entries;
    }

    public int getTodaysTotal() throws IOException {
        LocalDate today = LocalDate.now();
        return loadEntries().stream()
            .filter(entry -> entry.timestamp().toLocalDate().equals(today))
            .mapToInt(WaterEntry::amount)
            .sum();
    }
}
