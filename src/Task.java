import java.util.Date;

public class Task {
    private String description;
    private boolean isDone;
    private Date dueDate;
    private Priority priority;

    public Task(String description, Date dueDate, Priority priority) {
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.isDone = false;
    }

    public void markAsDone() {
        isDone = true;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isDone() {
        return isDone;
    }

    // 🔽 Add this at the bottom of your class 👇
    @Override
    public String toString() {
        return "Task: " + description +
                " | Due: " + dueDate +
                " | Priority: " + priority +
                " | Done: " + isDone;
    }
}
