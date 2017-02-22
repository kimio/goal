package goalreports.helper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport; 
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author felipekn
 */
public class GoogleApiHelper {
    
    private String APPLICATION_NAME = null;
    private String CLIENT_SECRET_JSON = null;
    
    /**
     * Construtor
     * @param applicationName Nome da aplicação
     * @param clientSecretJson Cliente Secret Json do Google SpreadSheet API
     */
    public GoogleApiHelper(String applicationName,String clientSecretJson){
        APPLICATION_NAME = applicationName;
        CLIENT_SECRET_JSON = clientSecretJson;
    }

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/2/drive-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    private static final List<String> SCOPES =
        Arrays.asList(SheetsScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (IOException | GeneralSecurityException t) {
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(CLIENT_SECRET_JSON);
        
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).
                authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    private Sheets getSheetsService() throws IOException {
        if(APPLICATION_NAME==null){
            System.out.println("Invalid Application Name");
            return null;
        }
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }   
    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    public File uploadDriveFile(java.io.File localFile){
        File file = null;
        try {
            Drive service = getDriveService();
            File fileMetadata = new File();
            fileMetadata.setName(localFile.getName());
            java.io.File filePath = new java.io.File(localFile.getAbsolutePath());
            FileContent mediaContent = new FileContent("image/jpeg", filePath);
            file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
        } catch (IOException ex) {
            Logger.getLogger(GoogleApiHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }
    public List<File> getDriveListFiles(){
        Drive service;
        List<File> files = null;
        try {
            service = getDriveService();
            FileList result = service.files().list()
             .setFields("nextPageToken, files(id, name)").execute();
            
            files = result.getFiles();
        } catch (IOException ex) {
            Logger.getLogger(GoogleApiHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (files == null) {
            System.out.println("No files found.");
            return null;
        }
        return files;
    }
    public List<List<Object>> getSpreadSheetValues(String spreadsheetId,String range) throws IOException {
        Sheets service = getSheetsService();
        ValueRange response = service.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute();
        List<List<Object>> values = response.getValues();
                
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        }
        return values;
    }
    private void setSpreadSheetValue(String spreadsheetId,String cell,String value) throws IOException{
        Sheets service = getSheetsService();
        List<Object> data1 = new ArrayList<>();
        data1.add(value);
        
        List<List<Object>> data = new ArrayList<>();
        data.add(data1);
        
        ValueRange vr = new ValueRange().setValues(data);
  
        service.spreadsheets().values().
                update(spreadsheetId, cell, vr).setValueInputOption("RAW").execute();
    }
    public void setSpreadSheetCellImage(String spreadsheetId,String cell,String imageUrl) throws IOException{
        imageUrl = "=IMAGE(\""+imageUrl+"\")";
        setSpreadSheetValue(spreadsheetId,cell,imageUrl);
    }
    public void setSpreadSheetCellValue(String spreadsheetId,String cell,String value) throws IOException{
        setSpreadSheetValue(spreadsheetId,cell,value);
    }

    public File uploadDriveFile(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
