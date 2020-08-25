package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Settings {
    public static boolean isSettingsLoaded = false;

    public static String login;
    public static String password;

    public static String serverAddr;
    public static String contentPath;
    public static String dbFileName;

    public static int propertiesID = -1; // last properties id in table "properties" in db

    public static boolean loadFromXML(String filePath) {
        File settingsFile = new File(filePath);

        if (!settingsFile.exists()) return false;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(settingsFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();

        Settings.login = doc.getElementsByTagName("login").item(0).getTextContent();
        Settings.password = doc.getElementsByTagName("password").item(0).getTextContent();
        Settings.serverAddr = doc.getElementsByTagName("address").item(0).getTextContent();
        Settings.contentPath = doc.getElementsByTagName("contentpath").item(0).getTextContent();
        Settings.dbFileName = doc.getElementsByTagName("dbfilename").item(0).getTextContent();
        Settings.isSettingsLoaded = true;

        return true;
    }
}
