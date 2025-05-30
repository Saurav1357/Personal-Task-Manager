import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {
    static ArrayList<Task> tasks = new ArrayList<>(); // Corrected

    public static void main(String[] args) {
        System.out.println(Priority.HIGH);

        Scanner scanner = new Scanner(System.in);
        loadTasksFromFile();

        boolean running = true;

        System.out.println("📋 Welcome to Your Personal Task Manager!");

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Mark Task as Done");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addTask(scanner);
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    markTaskAsDone(scanner);
                    break;
                case 4:
                    deleteTask(scanner);
                    break;
                case 5:
                    running = false;
                    System.out.println("👋 Exiting Task Manager. Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    public static void loadTasksFromFile() {
        File file = new File("tasks.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: description||isDone||dueDate||priority
                String[] parts = line.split("\\|\\|");
                if (parts.length == 4) {
                    String description = parts[0];
                    boolean isDone = Boolean.parseBoolean(parts[1]);
                    String dueDateString = parts[2];
                    String priorityString = parts[3];

                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                    Date dueDate = dateFormat.parse(dueDateString);
                    Priority priority = Priority.valueOf(priorityString);

                    Task task = new Task(description, dueDate, priority);
                    if (isDone) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to load tasks: " + e.getMessage());
        }
    }

    public static void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + "||" +
                        task.isDone() + "||" +
                        task.getDueDate() + "||" +
                        task.getPriority());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save tasks: " + e.getMessage());
        }
    }

    public static void addTask(Scanner scanner) {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();

        System.out.print("Enter due date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dueDate = null;

        try {
            dueDate = dateFormat.parse(dateInput);
        } catch (Exception e) {
            System.out.println("❌ Invalid date format. Task added with today’s date.");
            dueDate = new Date();
        }

        System.out.print("Enter priority (HIGH / MEDIUM / LOW): ");
        String priorityInput = scanner.nextLine().toUpperCase();
        Priority priority = Priority.MEDIUM; // default

        try {
            priority = Priority.valueOf(priorityInput);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠ Invalid priority. Defaulting to MEDIUM.");
        }

        Task task = new Task(description, dueDate, priority);
        tasks.add(task);
        System.out.println("✅ Task added!");
        saveTasksToFile();
    }

    public static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("📭 No tasks yet.");
        } else {
            System.out.println("📝 Your Tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    public static void markTaskAsDone(Scanner scanner) {
        viewTasks();
        System.out.print("Enter task number to mark as done: ");
        int index = scanner.nextInt() - 1;

        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsDone();
            System.out.println("✅ Task marked as done!");
        } else {
            System.out.println("❌ Invalid task number.");
        }
    }

    public static void deleteTask(Scanner scanner) {
        viewTasks();
        System.out.print("Enter task number to delete: ");
        int index = scanner.nextInt() - 1;

        if (index >= 0 && index < tasks.size()) {
            Task removed = tasks.remove(index);
            System.out.println("🗑️ Task deleted: " + removed.getDescription());

            // Now save the updated tasks to the file
            saveTasksToFile();
        } else {
            System.out.println("❌ Invalid task number.");
        }
    }
}
