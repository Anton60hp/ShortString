package ru;


import com.google.gson.JsonObject;

import ru.service.ConfigReader;


public class Config {
    static JsonObject configFromFile = ConfigReader.Read();
    public static int DEFAULT_USES = configFromFile.get("DEFAULT_USES").getAsInt();
    public static int DEFAULT_EXPIRATION_TIME = configFromFile.get("DEFAULT_EXPIRATION_TIME").getAsInt();
}




