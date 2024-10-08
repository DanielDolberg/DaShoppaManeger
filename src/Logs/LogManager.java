package Logs;

import MenuClasses.IMethodObserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LogManager implements IMethodObserver {

    private final String directoryPath;
    private final String fileName;
    private File logFile;
    private String timestamp;

    @Override
    public void Invoke() {
        ReadFromFile();
    }

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
    }

    // Method to update the current timestamp
    private void updateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timestamp = sdf.format(new Date());
    }

    // Asynchronous write to the log file
    public void WriteToFile(String text) {
        updateTimestamp();  // Update timestamp with the current time

        // Create a new thread for writing to file asynchronously
        new WriteTask(text).start();
    }

    // Inner class for handling asynchronous write task
    private class WriteTask extends Thread {
        private String textToWrite;

        public WriteTask(String text) {
            this.textToWrite = text;
        }

        @Override
        public void run() {
            // Write to the log file in a new thread
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)))) {
                pw.println("[" + timestamp + "] " + textToWrite);  // Write timestamp and text
                System.out.println("Successfully wrote to log file asynchronously.");
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }
    }

    // Synchronous read from the log file
    public void ReadFromFile() {
        Scanner scanner = new Scanner(System.in);
        // Validate logs date
        String logsDate;
        do {
            System.out.println("Enter logs date (format: yyyy-MM-dd) for example: 2024-09-23");
            logsDate = scanner.nextLine();
            if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", logsDate)) {
                System.out.println("Invalid logs date format. Please follow the format yyyy-MM-dd.");
            }
        } while (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", logsDate));

        // Read from the log file synchronously
        try {
            boolean found = false;
            boolean headingPrinted = false;  // To ensure heading is printed only once
            Scanner s = new Scanner(logFile);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.startsWith("[" + logsDate + " ")) {
                    if (!headingPrinted) {
                        System.out.println("Printing logs of date: " + logsDate);
                        headingPrinted = true;
                    }
                    // Extract only the time part
                    String timePart = line.substring(line.indexOf(" ") + 1, line.indexOf("]"));
                    String logMessage = line.substring(line.indexOf("]") + 2);  // Extracts the log message

                    // Print the extracted time and log message
                    System.out.println("[" + timePart + "] " + logMessage);
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
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
