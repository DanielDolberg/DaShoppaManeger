package Logs;
import MenuClasses.IMethodObserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LogManager  implements IMethodObserver {

    private String directoryPath;
    private String fileName;
    private File logFile;
    private String timestamp;

    public LogManager() {
        this.fileName = "app.log";
        this.directoryPath = "Logs";
        initialize();
    }

    public LogManager(String fileName, String directoryPath) {
        this.fileName = fileName;
        this.directoryPath = directoryPath;
        initialize();
    }

    // Initialize the logs directory, log file, and set the current timestamp
    private void initialize() {
        // Create the logs directory if it doesn't exist
        File logDir = new File(directoryPath);
        if (!logDir.exists()) {
            logDir.mkdirs();  // Creates the logs directory
        }

        // Create the log file
        logFile = new File(logDir, fileName);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();  // Create the log file if it doesn't exist
            }
        } catch (IOException e) {
            System.out.println("An error occurred while trying to create the log file at: " + logFile.getAbsolutePath());
            System.out.println("Error details: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize the timestamp format for each log entry
//        updateTimestamp();
    }
    // Method to update the current timestamp
    private void updateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timestamp = sdf.format(new Date());
    }

    // Write to the file using the generated logFile and timestamp
    public void WriteToFile(String text) {
        // Update timestamp with the current time before writing
        updateTimestamp();

        // Write to the log file
        try (PrintWriter pw = new PrintWriter (new BufferedWriter(new FileWriter(logFile, true)))) {
            pw.println("[" + timestamp + "] " + text);  // Write timestamp and text
        } catch (IOException e) {
            System.out.println("Log file not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void ReadFromFile() {

        Scanner scanner = new Scanner(System.in);
        // Validate full name
        String logsDate;
        do {
            System.out.println("Enter logs date (format: yyyy-MM-dd) for example: 2024-09-23");
            logsDate= scanner.nextLine();
            if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", logsDate)) {
                System.out.println("Invalid logs date format. Please follow the format yyyy-MM-dd.");
            }
        } while (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", logsDate));

        // Read from the log file
        try {
            boolean found = false;
            Scanner s = new Scanner(logFile);
            while(s.hasNextLine())
            {
                String line = s.nextLine();
                if (line.startsWith("[" + logsDate + " ")) {
                    // Extract the log message by splitting the line
                    String logMessage = line.substring(line.indexOf("]") + 2);  // Get everything after "] "
                    System.out.println(logMessage);  // Print the extracted log message
                    found = true;
                }
            }
            s.close();  // Close the scanner
            if (!found) {
                System.out.println("No logs found for the date: " + logsDate);
            }
            System.out.println("\n");
        } catch (FileNotFoundException e) {
            System.out.println("Log file not found: " + e.getMessage());
        }
    }
    @Override
    public void Invoke() {
        ReadFromFile();
    }
}
