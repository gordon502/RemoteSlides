package org.example.files;

import java.io.File;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.example.Settings;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLSettingsLoader {

    public static boolean load(String filePath) {
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
