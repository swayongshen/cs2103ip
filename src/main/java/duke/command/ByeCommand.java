package duke.command;

import duke.DukeException;
import duke.TaskList;

public class ByeCommand extends Command {

    /**
     * Instantiates a new ByeCommand object.
     * @param commandSplit user command split by spaces.
     */
    public ByeCommand(String[] commandSplit) {
        super(commandSplit);
        assert commandSplit[0].equals("bye") : "Bye command should have \"bye\" keyword.";
    }

    /**
     * Rewrites all Tasks in the list to the storage before saying bidding user goodbye.
     * @param list the task list.
     * @throws DukeException if failed to rewrite tasks.
     */
    public String execute(TaskList list) throws DukeException {
        list.rewriteTasks();
        return ("Bye. Hope to see you again soon!");
    }
}
