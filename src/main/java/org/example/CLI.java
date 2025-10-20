package org.example;

import java.io.File;
import java.io.IOException;

import static org.example.Service.*;
import static org.example.Utility.executeCommand;
import static org.example.Utility.safeExecute;

public class CLI {

    private static final File file = new File("task_tracker.json");

    public static void main(String[] args) throws IOException {
        if(!file.exists()){
            file.createNewFile();
        }

        String command = args[0];

        switch (command.toLowerCase()) {
            case "add" -> executeCommand(args, 2, "usage: java Task_CLI add <description>", () -> safeExecute(() -> addRecord(file, args[1])));
            case "update" -> executeCommand(args, 3, "usage: java Task_CLI update <id> <description>", () -> safeExecute(() -> updateRecord(file, args[1], args[2])));
            case "delete" -> executeCommand(args, 2, "usage: java Task_CLI delete <id>", () -> safeExecute(() -> deleteRecords(file, args[1])));
            case "mark-in-progress" -> executeCommand(args, 2, "usage: java Task_CLI mark-in-progress <id>", () -> safeExecute(() -> updateStatus(file, args[1], "in-progress")));
            case "mark-done" -> executeCommand(args, 2, "usage: java Task_CLI mark-done <id>", () -> safeExecute(() -> updateStatus(file, args[1], "done")));
            case "list", "list-done", "list-todo", "list-in-progress" -> {
                String status = switch (command) {
                    case "list-done" -> "done";
                    case "list-todo" -> "todo";
                    case "list-in-progress" -> "in-progress";
                    default -> null;
                };
                listTasks(file, status);
            }
            default -> System.out.println("""
            Invalid command: %s
            Available commands: add, update, delete, mark-in-progress, mark-done, list, list-done, list-todo, list-in-progress
            """.formatted(command));
        }
    }
}
