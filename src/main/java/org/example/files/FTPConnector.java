package org.example.files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

/**
 * Class responsible for downloading media and DB files
 * from remote FTP server.
 */
public class FTPConnector {
    private final FTPClient ftpClient = new FTPClient();

    private String login;
    private String password;
    private String serverAddr;
    private Integer port;

    public FTPConnector(String login, String password, String serverAddr, Integer port) {
        this.login = login;
        this.password = password;
        this.serverAddr = serverAddr;
        this.port = port;
    }


    public boolean downloadFile(String contentPath, String fileName) {
        if (!checkConnection())
            return false;
        
        File downloadFileCopy = new File("./content/" + fileName + "copy");

        try {
            downloadFileCopy.createNewFile();

            ftpClient.connect(serverAddr, 21);
            ftpClient.login(login, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);

            String remoteFile1 = contentPath + fileName;

            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFileCopy));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            System.out.println(ftpClient.getReplyCode());
            outputStream1.close();

            if (success) {
                //create and copy to final file if download was successful
                File finalFile = new File("./content/" + fileName);
                FileUtils.copyFile(downloadFileCopy, finalFile); //
            }

            downloadFileCopy.delete();

            ftpClient.logout();
            ftpClient.disconnect();

            return success;
        }
        catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            //ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    downloadFileCopy.delete();
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        return false;
    }

    /**
     * Method for checking if it is possible to connect to server with given
     * login and password.
     * @return success or not
     */
    public boolean checkConnection() {
        try {
            ftpClient.connect(serverAddr, port);
            ftpClient.login(login, password);

            ftpClient.logout();
            ftpClient.disconnect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
