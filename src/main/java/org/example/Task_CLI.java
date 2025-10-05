package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.Model.DummyModel;
import org.example.Model.Products;
import org.example.Model.TaskCLIModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Task_CLI {

    public static void main(String[] args) throws IOException {

        File file = new File("task_tracker.json");

        if(!file.exists()){
            file.createNewFile();
        }

        String command = args[0];

        switch(command.toLowerCase()){
            case "add":
                if(args.length < 3){
                    System.out.println("usage: java Task_CLI add <task_name> <task_description>");
                }else{
                    String taskName = args[1];
                    String taskDescription = args[2];
                }
                break;

            case "update":
                break;

            case "delete":
                break;

            case "mark-in-progress":
                break;

            case "mark-done":
                break;

            case "list":
                break;

            case "list done":
                break;

            case "list todo":
                break;

            case "list in-progress":
                break;

            default:
                System.out.println("Invalid command" + command);
                System.out.println("Available commands: add, update, delete, mark-in-progress, mark-done, list, list done, list todo, list in-progress");
        }

    }
}
