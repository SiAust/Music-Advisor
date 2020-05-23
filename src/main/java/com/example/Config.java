package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    // todo: Encrypt the secret key and use this method to decrypt before returning?
    public static String getSecret() {
        Properties properties = new Properties();
        String fileName = ".\\src\\main\\java\\com\\example\\config";
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e);
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return properties.getProperty("secret");
    }

}
