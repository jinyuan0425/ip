import java.util.ArrayList;
import java.util.Scanner;  // Import the Scanner class
public class Duke {
    static ArrayList<Task> list = new ArrayList<Task>(); // List to be returned when input is "list"
    static int counter = 0; // Items in list
    public static enum Type {
        LIST,
        MARK,
        UNMARK,
        TODO,
        DEADLINE,
        EVENT,
        DELETE,
    }

    public static String greet() { // Greets user on initialisation
        return "Good day to you, I'm ButlerBot.\n" +
                "How may I be of service to you?\n";
    }

    public static String thank() { // Exits the Bot
        return "Goodbye and have a nice day.";
    }

    public static String command(String input) throws DukeException { // Checks the input
        if (input.startsWith("list")) { // list command
            return enumCommand(Type.LIST, input);
        } else if (input.startsWith("mark ")) { // mark command
            return enumCommand(Type.MARK, input);
        } else if (input.startsWith("unmark ")) { // unmark command
            return enumCommand(Type.UNMARK, input);
        } else if (input.startsWith("todo ")) { // todo command
            return enumCommand(Type.TODO, input);
        } else if (input.startsWith("deadline ")) { // deadline command
            return enumCommand(Type.DEADLINE, input);
        } else if (input.startsWith("event ")) { // event command
            return enumCommand(Type.EVENT, input);
        } else if (input.startsWith("delete ")) { // delete command
            return enumCommand(Type.DELETE, input);
        } else {
            throw new DukeException("I'm afraid I do not quite understand. Could you kindly repeat it?");
        }
    }

    public static String enumCommand(Type command, String input) throws DukeException {
        Type type = command;

        switch (type) {
            case LIST:
                return list();
            case MARK:
                return mark(input);
            case UNMARK:
                return unmark(input);
            case TODO:
                return todo(input);
            case DEADLINE:
                return deadline(input);
            case EVENT:
                return event(input);
            case DELETE:
                return delete(input);
            default:
                throw new DukeException("I'm afraid I do not quite understand. Could you kindly repeat it?");
        }
    }

    public static String mark(String input) throws DukeException {
        int index = Integer.valueOf(input.substring(5)) - 1;
        if (index >= 0 && index < counter) {
            list.get(index).setDone(); // Item mark complete
            return "Congratulations on finishing the task. I will now mark it as complete:\n" +
                    list.get(index).toString()+ "\n";
        } else { // Index error
            throw new DukeException("I'm afraid the task does not exist. " +
                    "Perhaps you might want to see your list again?");
        }
    }

    public static String unmark(String input) throws DukeException {
        int index = Integer.valueOf(input.substring(7)) - 1;
        if (index >= 0 && index < counter) {
            list.get(index).setNotDone(); // Item mark complete
            return "No worries. I will now mark it as incomplete:\n" +
                    list.get(index).toString() + "\n";
        } else { // Index error
            throw new DukeException("I'm afraid the task does not exist. " +
                    "Perhaps you might want to see your list again?");
        }
    }

    public static String list() throws DukeException {
        String result = "";
        for (int i = 0; i < list.size(); i++) { // Generates the String representation of the list
            result += i + 1 + ". " + list.get(i) + "\n";
        }
        if (result != "") {
            return result;
        } else { // Empty list
            throw new DukeException("There is nothing on your list currently. " +
                    "Perhaps you might want to add a new task?");
        }
    }

    public static void addTask(Task task) throws DukeException {
        list.add(task); // Adds task to the list
    }

    public static String todo(String input) throws DukeException {
        String task = input.substring(5);
        if (task != "") {
            Task item = new Todo(task);
            addTask(item);
            counter += 1;
            String response = "Understood, I will add the following todo to your list:\n" + item.toString();
            String listLength = "Please note that there are " + counter + " tasks in the list.";
            return response + "\n" + listLength + "\n";
        } else { // No task
            throw new DukeException("I am missing some information. " +
                    "I must have not heard you correctly. " +
                    "Perhaps you can say it again?");
        }
    }

    public static String deadline(String input) throws DukeException {
        String desc = input.substring(9);
        String task = desc.split(" /by ")[0];
        String by = desc.split(" /by ")[1];
        if (task != "" && by != "") {
            Task item = new Deadline(task, by);
            addTask(item);
            counter += 1;
            String response = "Understood, I will add the following deadline to your list:\n" + item.toString();
            String listLength = "Please note that there are " + counter + " tasks in the list.";
            return response + "\n" + listLength + "\n";
        } else { // No task or by
            throw new DukeException("I am missing some information. " +
                    "I must have not heard you correctly. " +
                    "Perhaps you can say it again?");
        }
    }

    public static String event(String input) throws DukeException {
        String desc = input.substring(6);
        String[] eventTime = desc.split(" /from ");
        String task = eventTime[0];
        String[] time = eventTime[1].split(" /to ");
        String from = time[0];
        String to = time[1];
        if (task != "" && from != "" && to != "") {
            Task item = new Event(task, from, to);
            addTask(item);
            counter += 1;
            String response = "Understood, I will add the following event to your list:\n" + item.toString();
            String listLength = "Please note that there are " + counter + " tasks in the list.";
            return response + "\n" + listLength + "\n";
        } else { // No task, from or to
            throw new DukeException("I am missing some information. " +
                    "I must have not heard you correctly. " +
                    "Perhaps you can say it again?");
        }
    }

    public static String delete(String input) throws DukeException {
        int index = Integer.valueOf(input.substring(7)) - 1;
        if (index >= 0 && index < counter) {
            Task task = list.get(index);
            list.remove(index);
            counter -= 1;
            String response = "Understood, I will remove the following task from your list:\n" + task.toString();
            String listLength = "Please note that there are " + counter + " tasks in the list.";
            return response + "\n" + listLength + "\n";
        } else { // Index error
            throw new DukeException("I'm afraid the task does not exist. " +
                    "Perhaps you might want to see your list again?");
        }
    }

    public static void main(String[] args) throws DukeException {

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println(greet()); // Greets user

        String echo = myObj.nextLine(); // Reads user input

        while (!echo.equals("bye")) {
            try {
                System.out.println(command(echo)); // Checks input
            } catch (DukeException ex) {
                System.err.print(ex); // Prints error
            } finally {
                echo = myObj.nextLine(); // Scan for next input
            }
        }
        System.out.println(thank()); // Exits the bot
    }
}
