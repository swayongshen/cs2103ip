import java.util.regex.Pattern;

public class Parser {
    /**
     * Parses the user input to find and handle commands containing keywords.
     * @param userInput
     * @throws DukeException if command is in an incorrect format.
     */
    public static Command handleInput(String userInput) throws DukeException {
        String[] splitBySpaces = userInput.trim().split("\\s+");
        String keyword = splitBySpaces[0];
        if (keyword.equals("list")) {
            return new ListCommand(splitBySpaces);
        } else if (keyword.equals("done")) {
            return new DoneCommand(splitBySpaces);
        } else if (keyword.equals("deadline") || keyword.equals("todo") || keyword.equals("event")) {
            return new AddCommand(splitBySpaces);
        } else if (keyword.equals("delete")) {
            return new DeleteCommand(splitBySpaces);
        } else if (keyword.equals("bye")) {
            return new ByeCommand(splitBySpaces);
        } else {
            throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Parses input string as a Task.
     * @param input input string to be parsed.
     * @return Task object corresponding to input string.
     */
    public static Task stringToTask(String input) throws TaskException, DukeException {
        //Check if it is a valid task first
        String TODO_REGEX = "^\\[T\\] \\[(?: |X)\\] ..*$";
        Pattern toDoPattern = Pattern.compile(TODO_REGEX);
        String DEADLINE_REGEX = "^\\[D\\] \\[(?: |X)\\] ..* \\(by: ..*\\)$";
        Pattern deadlinePattern = Pattern.compile(DEADLINE_REGEX);
        String EVENT_REGEX = "^\\[E\\] \\[(?: |X)\\] ..* \\(at: ..*\\)$";
        Pattern eventPattern = Pattern.compile(EVENT_REGEX);
        //If it is a To Do command
        if (toDoPattern.matcher(input).find()) {
            String[] inputSplitBySpaces = input.trim().split("\\s+");
            String taskDescription = Helper.join(inputSplitBySpaces, 2, inputSplitBySpaces.length - 1);
            return new ToDo(taskDescription);
        } else {
            boolean matchDeadline = deadlinePattern.matcher(input).find();
            boolean matchEvent = eventPattern.matcher(input).find();
            //If it is a deadline or event command
            if (matchDeadline || matchEvent) {
                String[] inputSplitBySpaces = input.trim().split("\\s+");
                if (matchDeadline) {
                    //Index of /by
                    int byIndex = Helper.arrayIndexOf(inputSplitBySpaces, "(by:");
                    String taskDescription = Helper.join(inputSplitBySpaces, 2, byIndex - 1);
                    String dueDateWithClosingBracket = Helper.join(inputSplitBySpaces, byIndex + 1,
                            inputSplitBySpaces.length - 1);
                    String dueDate = dueDateWithClosingBracket.substring(0, dueDateWithClosingBracket.length() - 1);
                    return new Deadline(taskDescription, dueDate);
                } else {
                    //Index of /at
                    int atIndex = Helper.arrayIndexOf(inputSplitBySpaces, "(at:");
                    String taskDescription = Helper.join(inputSplitBySpaces, 2, atIndex - 1);
                    String eventDateWithClosingBracket = Helper.join(inputSplitBySpaces, atIndex + 1,
                            inputSplitBySpaces.length - 1);
                    String eventDate = eventDateWithClosingBracket.substring(0, eventDateWithClosingBracket.length() - 1);
                    return new Event(taskDescription, eventDate);
                }
            } else {
                throw new TaskException("Invalid task entry.");
            }
        }
     }
}