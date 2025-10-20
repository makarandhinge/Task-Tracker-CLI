package org.example;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.Repository.*;
import static org.example.Utility.currentTime;

public class Service {

    static void updateStatus(File file, String id, String status) throws IOException {
        List<Map<String,String>> records = readRecords(file);
        Map<String, String> updatedRecord =  findRecordsById(records, id);
        updatedRecord.put("status", status);
        updatedRecord.put("updatedTime", currentTime());
        writeRecords(file, records);
    }

    static void listTasks(File file, String filterStatus){
        List<Map<String,String>> records = readRecords(file);
        List<Map<String, String>> filtered = (filterStatus == null)
                ? records
                : records.stream().filter(r -> filterStatus.equals(r.get("status"))).collect(Collectors.toList());
        filtered.forEach(System.out::println);
    }

    static void deleteRecords(File file, String id) throws IOException {
        List<Map<String,String>> records = readRecords(file);
        records.removeIf(r -> id.equals(r.get("id")));
        writeRecords(file, records);
    }

    static void updateRecord(File file, String id, String description) throws IOException {
        List<Map<String,String>> records = readRecords(file);
        Map<String, String> updatedRecord =  findRecordsById(records, id);
        updatedRecord.put("description", description);
        updatedRecord.put("updatedTime", currentTime());
        writeRecords(file, records);
    }

    static void addRecord(File file, String description) throws IOException {
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
