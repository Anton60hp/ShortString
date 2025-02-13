package ru.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ConfigReader {
    public static JsonObject Read() {

        JsonParser jsonParser = new JsonParser();

        try (Reader reader = new FileReader("src/main/resources/config.json")) {
//            JsonObject json = jsonParser.parse(reader).getAsJsonObject();
//            System.out.println(json.toString());
            return jsonParser.parse(reader).getAsJsonObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


