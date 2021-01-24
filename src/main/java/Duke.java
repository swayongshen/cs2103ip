import java.util.Scanner;

public class Duke {
    /** Stores list of tasks */
    TaskList list;

    public Duke() {
        greet();
        try {
            this.list = new TaskList();
        } catch (DukeException e) {
            System.err.println(e);
        }
    }


    private void greet() {
        Printer.printWithStyle(new String[]{"Hello! I'm Duke", "What can I do for you?"});
    }


    private void addToDo(String[] userInputSplit) throws DukeException {
        if (userInputSplit.length <= 1) {
            throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
        }
        String task = Helper.join(userInputSplit, 1, userInputSplit.length - 1);
        list.add(new ToDo(task));
    }


    private void addDeadline(String[] userInputSplit) throws DukeException {
        //Index of /by keyword
        int byIndex = 0;
        for (int i = 0; i < userInputSplit.length; i++) {
            if (userInputSplit[i].equals("/by")) {
                byIndex = i;
            }
        }
        if (byIndex == 0) {
            throw new DukeException("Missing /by keyword for new deadline.");
        } else if (byIndex == 1) {
            throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
        }
        if (byIndex == userInputSplit.length - 1) {
            throw new DukeException("Missing date of the deadline.");
        }
        String task = Helper.join(userInputSplit, 1, byIndex - 1);
        String date = Helper.join(userInputSplit, byIndex + 1, userInputSplit.length - 1);
        list.add(new Deadline(task, date));
    }


    private void addEvent(String[] userInputSplit) throws DukeException {
        //Index of /at keyword
        int atIndex = 0;
        for (int i = 0; i < userInputSplit.length; i++) {
            if (userInputSplit[i].equals("/at")) {
                atIndex = i;
            }
        }
        if (atIndex == 0) {
            throw new DukeException("Missing /at keyword for new Event.");
        } else if (atIndex == 1) {
            throw new DukeException("☹ OOPS!!! The description of an Event cannot be empty.");
        }
        if (atIndex == userInputSplit.length - 1) {
            throw new DukeException("Missing date of the Event.");
        }
        String task = Helper.join(userInputSplit, 1, atIndex - 1);
        String date = Helper.join(userInputSplit, atIndex + 1, userInputSplit.length - 1);
        list.add(new Event(task, date));
    }


    private void doneTask(String[] userInputSplit) throws DukeException {
        try {
            int taskNumber = Integer.parseInt(userInputSplit[1]);
            this.list.done(taskNumber);
        } catch (Exception e) {
            throw new DukeException("Please enter a valid task number to mark a task as done.");
        }
    }

    private void deleteTask(String[] userInputSplit) throws DukeException {
        try {
            int taskNumber = Integer.parseInt(userInputSplit[1]);
            this.list.remove(taskNumber);
        } catch (Exception e) {
            throw new DukeException("Please enter a valid task number to delete a task.");
        }
    }

    public void bye() {
        Printer.printWithStyle("Bye. Hope to see you again soon!");
    }

    /**
     * Parses the user input to find and handle commands containing keywords.
     * @param userInput
     * @throws DukeException if command is in an incorrect format.
     */
    public void handleInput(String userInput) throws DukeException {
        String[] splitBySpaces = userInput.trim().split("\\s+");
        String keyword = splitBySpaces[0];
        if (keyword.equals("list")) {
            this.list.printList();
        } else if (keyword.equals("done")) {
            doneTask(splitBySpaces);
        } else if (keyword.equals("deadline")) {
            addDeadline(splitBySpaces);
        } else if (keyword.equals("todo")) {
            addToDo(splitBySpaces);
        } else if (keyword.equals("event")) {
            addEvent(splitBySpaces);
        } else if (keyword.equals("delete")) {
            deleteTask(splitBySpaces);
        } else {
            throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }


    public static void main(String[] args) {
        Duke duke = new Duke();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (!input.equals("bye")) {
                try {
                    duke.handleInput(input);
                } catch (DukeException e) {
                    Printer.printWithStyle(e.getMessage());
                }
            } else {
                duke.bye();
                break;
            }
        }
    }
}
