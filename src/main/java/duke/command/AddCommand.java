package duke.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import duke.CallbackFunction;
import duke.DukeException;
import duke.Helper;
import duke.TaskList;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.TaskException;
import duke.task.ToDo;
import javafx.util.Pair;

public class AddCommand extends Command {

    /**
     * Instantiates a new AddCommand object.
     * @param commandSplit user command split by spaces.
     */
    public AddCommand(String[] commandSplit) {
        super(commandSplit);
        assert commandSplit.length > 1 && isValidAddKeyword(commandSplit[0]) : "Add command should have valid keyword.";
    }

    private static boolean isValidAddKeyword(String keyword) {
        Set<String> validKeywords = new HashSet<>(Arrays.asList("deadline", "event", "todo"));
        return validKeywords.contains(keyword);
    }

    /**
     * Checks what type of add command it is (event, to do, deadline) then call the respective functions
     * e.g. addDeadline to add a new task.
     * @param list the task list.
     * @return response that the task has been added.
     * @throws DukeException if adding a task failed.
     */
    @Override
    public Pair<String, CallbackFunction> execute(TaskList list) throws DukeException {
        String keyword = commandSplit[0];

        //Deadline
        if (keyword.equals("deadline")) {
            try {
                return new Pair<>(addDeadline(list), CallbackFunction.empty());
            } catch (TaskException e) {
                throw new DukeException("Failed to add deadline to tasks. " + e.getMessage());
            }
        }

        //To do
        if (keyword.equals("todo")) {
            return new Pair<>(addToDo(list), CallbackFunction.empty());
        }

        //Event
        try {
            return new Pair<>(addEvent(list), CallbackFunction.empty());
        } catch (TaskException e) {
            throw new DukeException("Failed to add event to tasks. " + e.getMessage());
        }
    }

    private Integer getKeywordIndex(String keyword, String[] arr) {
        int keywordIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(keyword)) {
                keywordIndex = i;
            }
        }
        return keywordIndex;
    }

    private String addDeadline(TaskList list) throws DukeException, TaskException {
        String[] userInputSplit = this.commandSplit;
        //Index of /by keyword
        int byIndex = getKeywordIndex("/by", userInputSplit);

        //No by keyword.
        if (byIndex == 0) {
            throw new DukeException("Missing /by keyword for new deadline.");
        }

        //by keyword immediately after deadline keyword
        if (byIndex == 1) {
            throw new DukeException("OOPS!!! The description of a deadline cannot be empty.");
        }

        //by keyword is the last word in input
        if (byIndex == userInputSplit.length - 1) {
            throw new DukeException("Missing date of the deadline.");
        }

        String task = Helper.join(userInputSplit, 1, byIndex - 1);
        String date = Helper.join(userInputSplit, byIndex + 1, userInputSplit.length - 1);
        return list.add(new Deadline(task, date));
    }

    private String addToDo(TaskList list) throws DukeException {
        String[] userInputSplit = this.commandSplit;
        if (userInputSplit.length <= 1) {
            throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
        }
        String task = Helper.join(userInputSplit, 1, userInputSplit.length - 1);
        return list.add(new ToDo(task));
    }

    private String addEvent(TaskList list) throws DukeException, TaskException {
        String[] userInputSplit = this.commandSplit;
        //Index of /at keyword
        int atIndex = getKeywordIndex("/at", userInputSplit);
        if (atIndex == 0) {
            throw new DukeException("Missing /at keyword for new Event.");
        } else if (atIndex == 1) {
            throw new DukeException("OOPS!!! The description of an Event cannot be empty.");
        }
        if (atIndex == userInputSplit.length - 1) {
            throw new DukeException("Missing date of the Event.");
        }
        String task = Helper.join(userInputSplit, 1, atIndex - 1);
        String date = Helper.join(userInputSplit, atIndex + 1, userInputSplit.length - 1);
        return list.add(new Event(task, date));
    }
}
