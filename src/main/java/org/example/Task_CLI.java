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

    public static void main(String[] args) throws IOException {

        File file = new File("task_tracker.json");

        final ObjectMapper mapper = new ObjectMapper();

        if(!file.exists()){
            file.createNewFile();
        }

        String command = args[0];

        switch(command.toLowerCase()){
            case "add":
                if(args.length < 2){
                    System.out.println("usage: java Task_CLI add <task_description>");
                }else{
                    List<Map<String,String>> records;
                    try {
                        records = mapper.readValue(file, new TypeReference<>() {
                        });
                    } catch (MismatchedInputException e) {
                        records = new ArrayList<>();
                    }
                    String newId = String.valueOf(records.stream().mapToInt(r -> Integer.parseInt( r.get("id").toString())).max().orElse(0) + 1);
                    String description = args[1];
                    String status = "todo";
                    String createdTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String updatedTime = createdTime;

                    Map<String,String> newRecord = new LinkedHashMap<>();
                    newRecord.put("id", newId);
                    newRecord.put("description", description);
                    newRecord.put("status", status);
                    newRecord.put("createdTime", createdTime);
                    newRecord.put("updatedTime", updatedTime);
                    records.add(newRecord);

                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
                }
                break;

            case "update":
                if(args.length < 3){
                    System.out.println("usage: java Task_CLI update <id> <task_description>");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.stream().anyMatch(r -> args[1].equals(r.get("id")))){
                        Map<String,String> updatedRecord = records.stream().filter(r -> args[1].equals(r.get("id"))).findFirst().orElse(null);
                        String updatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        updatedRecord.put("description", args[2]);
                        updatedRecord.put("updatedTime", updatedTime);
                        mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
                    }else{
                        System.out.println("no such id is present in database!");
                    }
                }
                break;

            case "delete":
                if(args.length < 2){
                    System.out.println("usage: java Task_CLI delete <id>");
                }else{
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.stream().anyMatch(r -> args[1].equals(r.get("id")))){
                        records.removeIf(r -> args[1].equals(r.get("id")));
                        mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
                    }else{
                        System.out.println("no such id is present in database!");
                    }
                }
                break;

            case "mark-in-progress":
                if(args.length < 2){
                    System.out.println("usage: java Task_CLI mark-in-progress <id>");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.stream().anyMatch(r -> args[1].equals(r.get("id")))){
                        Map<String,String> updatedRecord = records.stream().filter(r -> args[1].equals(r.get("id"))).findFirst().orElse(null);
                        String updatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        updatedRecord.put("status", "in-progress");
                        updatedRecord.put("updatedTime", updatedTime);
                        mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
                    }else{
                        System.out.println("no such id is present in database!");
                    }
                }

                break;

            case "mark-done":
                if(args.length < 2){
                    System.out.println("usage: java Task_CLI mark-done <id>");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.stream().anyMatch(r -> args[1].equals(r.get("id")))){
                        Map<String,String> updatedRecord = records.stream().filter(r -> args[1].equals(r.get("id"))).findFirst().orElse(null);
                        String updatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        updatedRecord.put("status", "done");
                        updatedRecord.put("updatedTime", updatedTime);
                        mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
                    }else{
                        System.out.println("no such id is present in database!");
                    }
                }
                break;

            case "list":
                if(args.length < 1){
                    System.out.println("usage: java Task_CLI list");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.isEmpty()){
                        System.out.println("There is no records in the database!");
                    }else {
                        records.forEach(record -> System.out.println(record));
                    }
                }
                break;

            case "list-done":
                if(args.length < 1){
                    System.out.println("usage: java Task_CLI list-done");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.isEmpty()){
                        System.out.println("There is no records in the database!");
                    }else {
                        records.stream().filter(r -> "done".equals(r.get("status"))).collect(Collectors.toList()).forEach(record -> System.out.println(record));
                    }
                }
                break;

            case "list-todo":
                if(args.length < 1){
                    System.out.println("usage: java Task_CLI list-todo");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.isEmpty()){
                        System.out.println("There is no records in the database!");
                    }else {
                        records.stream().filter(r -> "todo".equals(r.get("status"))).collect(Collectors.toList()).forEach(record -> System.out.println(record));
                    }
                }
                break;

            case "list-in-progress":
                if(args.length < 1){
                    System.out.println("usage: java Task_CLI list-in-progress");
                }else {
                    List<Map<String, String>> records = mapper.readValue(file, new TypeReference<>() {});
                    if(records.isEmpty()){
                        System.out.println("There is no records in the database!");
                    }else {
                        records.stream().filter(r -> "in-progress".equals(r.get("status"))).collect(Collectors.toList()).forEach(record -> System.out.println(record));
                    }
                }
                break;

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
        return records.stream().filter(r -> id.equals(r.get("id"))).findFirst().orElse(null);
    }

    private static String currentTime(){
        return dateFormat.format(new Date());
    }


}
