package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class Task_CLI {

    public static void main(String[] args) throws IOException {

        File file = new File("newFile.json");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        int id = root.get("id").asInt();
        String description = root.get("description").asText();
        String status = root.get("status").asText();
        int createdAt = root.get("created_at").asInt();
        String updatedAt = root.get("updated_at").asText();
        System.out.println("ID: " + id);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);
        System.out.println("Created At: " + createdAt);
        System.out.println("Updated At: " + updatedAt);

    }
}
