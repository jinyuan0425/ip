public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void setDone() {
        this.isDone = true;
    }

    public void setNotDone() {
        this.isDone = false;
    }
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }

    public String write() {
        String complete = isDone ? "1" : "0";
        return complete + " | " + this.description;
    }
}