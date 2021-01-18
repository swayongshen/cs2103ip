import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    TaskList list;

    public Duke() {
        greet();
        this.list = new TaskList();
    }

    public static void printWithStyle(String[] output) {
        System.out.println("    ________________________________________________________________");
        for (String str : output) {
            System.out.println("    " + str);
        }
        System.out.println("    ________________________________________________________________");
    }

    public static void printWithStyle(String output) {
        System.out.println("    ________________________________________________________________");
        System.out.println("    " + output );
        System.out.println("    ________________________________________________________________");
    }

    /**
     * Joins a sub-array of strings into 1 string where each element in sub-array is separate by a space.
     *
     * @param arr array containing sub-array to join
     * @param start start index of sub-array to join
     * @param end end index of sub-array to join
     * @return string of sub-array joined with space
     */
    private String join(String[] arr, int start, int end) {
        StringBuilder output = new StringBuilder();
        for (int i = start; i <= end; i++) {
            output.append(arr[i]);
            if (i < end) {
                output.append(" ");
            }
        }
        return output.toString();
    }

    void greet() {
        printWithStyle(new String[]{"Hello! I'm Duke", "What can I do for you?"});
    }

    void handleInput(String userInput) {
        String[] splitBySpaces = userInput.trim().split("\\s+");
        String keyword = splitBySpaces[0];
        if (keyword.equals("list")) {
            this.list.printList();
        } else if (keyword.equals("done")) {
            doneTask(splitBySpaces);
        } else if (keyword.equals("deadline")) {
            System.out.println("Hey");
            addDeadline(splitBySpaces);
        } else if (keyword.equals("todo")) {
            addToDo(splitBySpaces);
        } else if (keyword.equals("event")) {
            addEvent(splitBySpaces);
        } else {
            this.list.add(new Task(userInput));
        }
    }

    void addToDo(String[] userInputSplit) {
        String task = join(userInputSplit, 1, userInputSplit.length - 1);
        list.add(new ToDo(task));
    }
    void addDeadline(String[] userInputSplit) {
        System.out.println(Arrays.toString(userInputSplit));
        //Index of /by keyword
        int byIndex = 0;
        for (int i = 0; i < userInputSplit.length; i++) {
            if (userInputSplit[i].equals("/by")) {
                byIndex = i;
            }
        }
        String task = join(userInputSplit, 1, byIndex - 1);
        String date = join(userInputSplit, byIndex + 1, userInputSplit.length - 1);
        list.add(new Deadline(task, date));
        ;
    }

    void addEvent(String[] userInputSplit) {
        //Index of /at keyword
        int atIndex = 0;
        for (int i = 0; i < userInputSplit.length; i++) {
            if (userInputSplit[i].equals("/at")) {
                atIndex = i;
            }
        }
        String task = join(userInputSplit, 1, atIndex - 1);
        String date = join(userInputSplit, atIndex + 1, userInputSplit.length - 1);
        list.add(new Event(task, date));
    }

    void doneTask(String[] userInputSplit) {
        int taskNumber = Integer.parseInt(userInputSplit[1]);
        this.list.done(taskNumber);
    }


    void bye() {
        printWithStyle("Bye. Hope to see you again soon!");
    }

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println(logo);
        Duke duke = new Duke();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("bye")) {
            duke.handleInput(input);
            input = scanner.nextLine();
        }
        duke.bye();

    }
}
