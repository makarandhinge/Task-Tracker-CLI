package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class Task_CLI {

    public static void main(String[] args) throws IOException {

        File file = new File("newFile.json");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("id", "1");
        root.put("description", "description");
        root.put("status", "status");
        root.put("created_at", "created_at");
        root.put("updated_at", "updated_at");
        mapper.writeValue(file, root);
    }
}
