package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.Model.TaskCLIModel;

import java.io.File;
import java.io.IOException;

public class Task_CLI {

    public static void main(String[] args) throws IOException {

        File file = new File("newFile.json");

        ObjectMapper mapper = new ObjectMapper();
        TaskCLIModel model = new TaskCLIModel(5,"description","todo","created time","updated time");
        mapper.writeValue(file,model);



    }
}
