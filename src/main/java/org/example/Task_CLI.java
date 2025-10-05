package org.example;

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

        File file = new File("products.json");
        URL url = new URL("https://dummyjson.com/products");
        ObjectMapper mapper = new ObjectMapper();
        Products model = mapper.readValue(url,Products.class);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, model);


    }
}
