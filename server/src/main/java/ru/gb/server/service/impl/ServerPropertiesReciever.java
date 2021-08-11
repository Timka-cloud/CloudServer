package ru.gb.server.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerPropertiesReciever {
    private static final String pathToProperties = "server/src/main/resources/server.properties";
    private static final Properties properties = new Properties();

    private static String getProperties(String propertyName) {
        try (InputStream in = new FileInputStream(pathToProperties)) {
            properties.load(in);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            throw new IllegalArgumentException("Значение " + propertyName + " отсутствует");
        }
    }

    public static int getPort() {
        return Integer.parseInt(ServerPropertiesReciever.getProperties("port").trim());
    }

    public static String getCloudDirectory() {
        return ServerPropertiesReciever.getProperties("cloudDirectory");
    }

    public static String getDbDriver() {
        return ServerPropertiesReciever.getProperties("dbDriver");
    }

    public static String getDbURL() {
        return ServerPropertiesReciever.getProperties("dbURL");
    }

}
