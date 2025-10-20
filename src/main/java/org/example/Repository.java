package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Repository {

    private static final ObjectMapper mapper = new ObjectMapper();

    static List<Map<String, String>> readRecords(File file) {
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


    static void writeRecords(File file, List<Map<String,String>> records) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, records);
    }

    static Map<String, String> findRecordsById(List<Map<String, String>> records, String id){
        return records.stream().filter(r -> id.equals(r.get("id"))).findFirst().orElseThrow(() -> new IllegalArgumentException("No such id is present in database!"));
    }
}
