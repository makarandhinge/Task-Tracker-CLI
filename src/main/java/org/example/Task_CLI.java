package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Task_CLI {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    //---------------------------------------------------------Helper Method-----------------------------------------------------------------------------

    private static List<Map<String, String>> readRecords(File file) {
        try {
            if (file.length() == 0) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<>() {});
        } catch (MismatchedInputException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("❌ Failed to read tasks: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    private static void writeRecords(File file, List<Map<String,String>> records) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
    }

    private static Map<String, String> findRecordsById(List<Map<String, String>> records, String id){
        return records.stream().filter(r -> id.equals(r.get("id"))).findFirst().orElseThrow(() -> new IllegalArgumentException("No such id is present in database!"));
    }

    private static String currentTime(){
        return dateFormat.format(new Date());
    }

    private static boolean argumentValidaton(String[] args, int required, String usage){
        if(args.length < required){
            System.out.println(usage);
        }else{
            return true;
        }
        return false;
    }

    private static void executeCommand(String[] args, int requiredArgs, String usage, Runnable action){
        if(argumentValidaton(args, requiredArgs, usage)){
            action.run();
        }
    }

    interface ThrowingRunnable {
        void run() throws IOException;
    }

    private static void safeExecute(ThrowingRunnable action){
        try{
            action.run();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }


    //-----------------------------------------------------Command Method---------------------------------------------------------------------------------

    private static void updateStatus(File file, String id, String status) throws IOException {
        List<Map<String,String>> records = readRecords(file);
        Map<String, String> updatedRecord =  findRecordsById(records, id);
        updatedRecord.put("status", status);
        updatedRecord.put("updatedTime", currentTime());
        writeRecords(file, records);
    }

    private static void listTasks(File file, String filterStatus){
        List<Map<String,String>> records = readRecords(file);
        List<Map<String, String>> filtered = (filterStatus == null)
                ? records
                : records.stream().filter(r -> filterStatus.equals(r.get("status"))).collect(Collectors.toList());
        filtered.forEach(System.out::println);
    }

    private static void deleteRecords(File file, String id) throws IOException {
        List<Map<String,String>> records = readRecords(file);
        records.removeIf(r -> id.equals(r.get("id")));
        writeRecords(file, records);
    }

    private static void updateRecord(File file, String id, String description) throws IOException {
        List<Map<String,String>> records = readRecords(file);
        Map<String, String> updatedRecord =  findRecordsById(records, id);
        updatedRecord.put("description", description);
        updatedRecord.put("updatedTime", currentTime());
        writeRecords(file, records);
    }

    private static void addRecord(File file, String description) throws IOException {
        List<Map<String, String>> records = readRecords(file);
        String newId = String.valueOf(records.stream().mapToInt(r -> Integer.parseInt( r.get("id").toString())).max().orElse(0) + 1);
        Map<String,String> newRecord = new LinkedHashMap<>();
        newRecord.put("id", newId);
        newRecord.put("description", description);
        newRecord.put("status", "todo");
        newRecord.put("createdTime", currentTime());
        newRecord.put("updatedTime", currentTime());
        records.add(newRecord);
        writeRecords(file,records);
    }


}
