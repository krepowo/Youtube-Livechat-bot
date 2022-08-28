package com.github.krepowo.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class PropertyManager {
    public String GetProp(String s) throws IOException {
        String versionString = null;

        Properties mainProperties = new Properties();

        FileInputStream file;

        String path = "./config.properties";
        try{
            file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
            versionString = mainProperties.getProperty(s);

        } catch (Exception e) {
            File curDir = new File("src/main/java/com/github/krepowo/config/config.properties");
            String realPath = curDir.getAbsolutePath();

            FileInputStream fis = new FileInputStream(realPath);
            Properties prop = new Properties();
            prop.load(fis);
            versionString = prop.getProperty(s);
        }

        return versionString;
    }
    public String getProp(String s) throws IOException {
        String versionString = null;

        Properties mainProperties = new Properties();

        FileInputStream file;

        String path = "./config.properties";
        try{
            file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
            versionString = mainProperties.getProperty(s);

        } catch (Exception e) {
            File curDir = new File("src/main/java/com/github/krepowo/config/config.properties");
            String realPath = curDir.getAbsolutePath();

            FileInputStream fis = new FileInputStream(realPath);
            Properties prop = new Properties();
            prop.load(fis);
            versionString = prop.getProperty(s);
        }

        return versionString;
    }
}
