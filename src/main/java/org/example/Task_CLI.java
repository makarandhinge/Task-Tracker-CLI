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

        switch(command.toLowerCase()){
            case "add":
                    if(argumentValidaton(args, 2, "usage: java Task_CLI add <description>")){
                        addRecord(file, args[1]);
                    }else{
                        break;
                    }

            case "update":
                if(argumentValidaton(args, 3, "usage: java Task_CLI update <id> <description>")){
                    updateRecord(file, args[1], args[2]);
                }else{
                    break;
                }

            case "delete":
                if(argumentValidaton(args, 2, "usage: java Task_CLI delete <id>")){
                    deleleRecords(file, args[1]);
                }else{
                    break;
                }

            case "mark-in-progress":
                if(argumentValidaton(args, 2, "usage: java Task_CLI mark-in-progress <id>")){
                    updateStatus(file,args[1],"in-progress");
                }else{
                    break;
                }

                break;

            case "mark-done":
                if(argumentValidaton(args, 2, "usage: java Task_CLI mark-in-done <id>")){
                   updateStatus(file,args[1],"done");
                }else{
                    break;
                }


            case "list":
                if(argumentValidaton(args, 1, "usage: java Task_CLI list")){
                    listTasks(file, "null");
                }else{
                    break;
                }

            case "list-done":
                if(argumentValidaton(args, 1, "usage: java Task_CLI list-done")){
                    listTasks(file, "in-done");
                }else{
                    break;
                }

            case "list-todo":
                if(argumentValidaton(args, 1, "usage: java Task_CLI list-todo")){
                    listTasks(file, "todo");
                }else{
                    break;
                }

            case "list-in-progress":
                if(argumentValidaton(args, 1, "usage: java Task_CLI list-in-progress")){
                    listTasks(file, "in-progress");
                }else{
                    break;
                }

            default:
                System.out.println("Invalid command" + command);
                System.out.println("Available commands: add, update, delete, mark-in-progress, mark-done, list, list-done, list-todo, list-in-progress");
        }

    }

    //---------------------------------------------------------Helper Method-----------------------------------------------------------------------------

    private static List<Map<String,String>> readRecords(File file){
        try{
            return mapper.readValue(file, new TypeReference<>() {});
            }catch(MismatchedInputException e){
            return new ArrayList<>();
        }catch(IOException e){
            throw new RuntimeException("Failed to read tasks", e);
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
        if (!filterStatus.equals("null")) {
            records.stream().filter(r -> filterStatus.equals("status")).collect(Collectors.toList()).forEach(r -> System.out.println(r));
        } else {
            records.forEach(r -> System.out.println(r));
        }
    }

    private static void deleleRecords(File file, String id) throws IOException {
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
