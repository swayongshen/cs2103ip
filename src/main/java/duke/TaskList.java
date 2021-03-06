package duke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

import duke.task.Task;
import duke.task.TaskDateComparator;
import duke.task.TaskDescriptionComparator;

public class TaskList {
    private final ArrayList<Task> list;
    private final Storage storage;

    /**
     * Reads task from storage and stores into an ArrayList
     * @throws DukeException if fail to read tasks from storage
     */
    public TaskList() throws DukeException {
        this.storage = new Storage();
        this.list = storage.readTasksFromFile();
    }

    /**
     * Adds a task to the list of tasks and prints to console the number of tasks in the list.
     * @param task the Task to be added to list.
     * @return response that task has been added.
     * @throws DukeException if fail to write the added task to storage.
     */
    public String add(Task task) throws DukeException {
        this.list.add(task);
        storage.writeTaskToFile(task);
        return Helper.formatStrings(
            "Got it. I've added this task:",
            System.lineSeparator(),
            "    " + task.toString(),
            "Now you have " + this.list.size() + " tasks in the list."
        );
    }

    /**
     * Marks a task as done and also rewrite all tasks in the storage to reflect this change.
     * @param taskNumber task number of the task to be marked as done.
     * @return response that a task has been marked as done.
     * @throws DukeException if failed to rewrite tasks to storage.
     */
    public String done(int taskNumber) throws DukeException {
        String doneMsg = this.list.get(taskNumber - 1).done();
        rewriteTasks();
        return doneMsg;
    }

    /**
     * Removes a task from the list and prints to console number of tasks left in the list.
     * @param taskNumbers task numbers of tasks to be removed.
     * @return response that a task has been removed.
     * @throws DukeException if fail to rewrite tasks.
     */
    public String remove(ArrayList<Integer> taskNumbers) throws DukeException {
        //To store descriptions of removed tasks.
        ArrayList<String> descriptionsOfTasksToRemove = new ArrayList<>();

        //Sort the task numbers in descending order.
        taskNumbers.sort(Collections.reverseOrder());

        //Remove tasks from list, starting from higher task numbers.
        taskNumbers.forEach(taskNumber -> {
            descriptionsOfTasksToRemove.add(this.list.get(taskNumber - 1).toString());
            this.list.remove(taskNumber - 1);
        });

        //Reverse descriptions back to ascending order (look nicer).
        Collections.reverse(descriptionsOfTasksToRemove);

        //Rewrite all tasks
        rewriteTasks();

        //Return response
        String header = Helper.formatStrings(
            "Noted. I've removed this task:",
            System.lineSeparator());
        String body = Helper.formatStrings(descriptionsOfTasksToRemove);
        String footer = System.lineSeparator() + "Now you have " + this.list.size() + " tasks in the list.";
        return header + body + footer;
    }

    /**
     * Clears storage file of its contents and rewrites all tasks to storage file.
     * @throws DukeException if failed to clear storage file or failed to write a task to storage file.
     */
    public void rewriteTasks() throws DukeException {
        storage.clearFile();
        for (Task task : this.list) {
            storage.writeTaskToFile(task);
        }
    }

    /**
     * Applies a function to all tasks in the list.
     * @param funct function to be applied.
     */
    public void forEach(Consumer<Task> funct) {
        for (Task task : list) {
            funct.accept(task);
        }
    }

    /**
     * Returns an ArrayList of tasks in which all tasks satisfy the predicate.
     * @param predicate the function used to filter tasks.
     * @return ArrayList of filtered tasks.
     */
    public ArrayList<Task> filter(Predicate<Task> predicate) {
        ArrayList<Task> output = new ArrayList<>();
        for (Task task : list) {
            if (predicate.test(task)) {
                output.add(task);
            }
        }
        return output;
    }

    /**
     * Sorts the list by dates of the tasks.
     * @param isAscending sort ascending if true, else sort descending.
     */
    public String sortByDate(boolean isAscending) {
        if (isAscending) {
            this.list.sort(new TaskDateComparator());
        } else {
            this.list.sort(new TaskDateComparator().reversed());
        }

        String header = "The list has been sorted by date in "
                + (isAscending ? "ascending" : "descending") + " order.";
        String body = this.toString();
        return Helper.formatStrings(header, body);
    }

    /**
     * Sorts the list by description of the tasks.
     * @param isAscending sort ascending if true, else sort descending.
     */
    public String sortByDescription(boolean isAscending) {
        if (isAscending) {
            this.list.sort(new TaskDescriptionComparator());
        } else {
            this.list.sort(new TaskDescriptionComparator().reversed());
        }

        String header = "The list has been sorted by description in "
                + (isAscending ? "ascending" : "descending") + " order.";
        String body = this.toString();
        return Helper.formatStrings(header, body);
    }

    public int size() {
        return this.list.size();
    }

    /**
     * Formats tasks into a numbered list of tasks.
     * @return a String representation of the TaskList.
     */
    @Override
    public String toString() {
        //Empty list
        if (this.list.size() == 0) {
            return "Your list is empty!";
        }

        String[] printedArray = new String[this.list.size() + 1];
        printedArray[0] = "Here are the tasks in your list:" + System.lineSeparator();
        for (int i = 0; i < this.list.size(); i++) {
            String numberIndex = String.valueOf(i + 1);
            String taskInString = this.list.get(i).toString();
            String listEntry = numberIndex + "." + taskInString;
            printedArray[i + 1] = listEntry;
        }
        return Helper.formatStrings(printedArray);
    }
}
