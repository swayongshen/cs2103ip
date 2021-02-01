package duke;

import duke.command.Command;

public class Duke {
    /** Stores list of tasks */
    private TaskList list;

    /**
     * Initializes a Duke object with an empty TaskList.
     */
    public Duke() {
        try {
            this.list = new TaskList();
        } catch (DukeException e) {
            System.err.println(e.getMessage());
        }
    }

    public String executeCommand(Command command) throws DukeException {
        return command.execute(this.list);
    }

    /**
     *  Provides responses to commands that are passed in by the user.
     */
    public String getResponse(String input) {
        Command command;
        try {
            command = Parser.handleInput(input);
            return this.executeCommand(command);
        } catch (DukeException e) {
            return e.getMessage();
        }
    }

}
