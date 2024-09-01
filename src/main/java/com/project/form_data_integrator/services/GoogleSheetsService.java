package com.project.form_data_integrator.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.project.form_data_integrator.dto.RegistrationDTO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsService {
    private static final String FILE_LOCATION = "src/main/java/com/project/form_data_integrator/googleFile/googlesheetid.txt";
    private  final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public boolean checkEmail(String email) throws GeneralSecurityException, IOException {
        File googleFile = new File(FILE_LOCATION);
        if(!googleFile.exists()){
            createSheet();
        }

        String sheetId = getSheetId();


        final String range = "Register Sheet 1!D2:D";

        Sheets service = getSheetsService();

        ValueRange valueRange = service.spreadsheets().values()
                .get(sheetId, range)
                .execute();

        List<List<Object>> values = valueRange.getValues();
        if(values == null) return false;

        for (List<Object> row : values) {
            if(row.getFirst().equals(email)){
                return true;
            }
        }

        return false;
    }

    public void registerNewUser(RegistrationDTO request) throws GeneralSecurityException, IOException {
        int emptyRow = getEmptyRow();
        Sheets service = getSheetsService();
        String sheetId = getSheetId();

        List<List<Object>> values = List.of(
                Arrays.asList(request.name(), request.phone(), request.country(),
                        request.email(), BCrypt.hashpw(request.password(), BCrypt.gensalt()))
        );

        String range = STR."Register Sheet 1!A\{emptyRow}";

        ValueRange valueRange = new ValueRange()
                .setRange(range)
                .setValues(values);

        service.spreadsheets().values()
                .update(sheetId, valueRange.getRange(), valueRange)
                .setValueInputOption("RAW")
                .execute();
    }

    public void createSheet() throws GeneralSecurityException, IOException {
        Sheets sheetsService = getSheetsService();
        SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
        spreadsheetProperties.setTitle("Users registration");
        SheetProperties sheetProperties = new SheetProperties();
        sheetProperties.setTitle("Register Sheet 1");
        Sheet sheet = new Sheet().setProperties(sheetProperties);
        Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties).setSheets(Collections.singletonList(sheet));
        String spreadsheetId = sheetsService.spreadsheets().create(spreadsheet).execute().getSpreadsheetId();
        File googleFile = new File(FILE_LOCATION);
        googleFile.createNewFile();
        try(FileWriter fw = new FileWriter(googleFile)){
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(spreadsheetId);
            bw.flush();
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        List<List<Object>> header = List.of(
                Arrays.asList("Name", "Phone", "Country", "Email", "Password")
        );

        ValueRange valueRange = new ValueRange().setRange("Register Sheet 1!A1:E1")
                .setValues(header);

        sheetsService.spreadsheets().values()
                .update(spreadsheetId, valueRange.getRange(), valueRange)
                .setValueInputOption("RAW")
                .execute();
    }

    //creating credentials
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        List<String> SCOPES =
                Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);

        String CREDENTIALS_FILE_PATH = "/credentials.json";
        InputStream in = GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException(STR."Resource not found: \{CREDENTIALS_FILE_PATH}");
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        String TOKENS_DIRECTORY_PATH = "tokens/path";
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(System.getProperty("user.home"),TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    //create a service
    private Sheets getSheetsService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        String APPLICATION_NAME = "Form data integrator";
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private int getEmptyRow() throws GeneralSecurityException, IOException {
        String sheetId = getSheetId();
        Sheets service = getSheetsService();
        String range = "Register Sheet 1!A:A";

        ValueRange valueRange = service.spreadsheets().values()
                .get(sheetId, range)
                .execute();

        List<List<Object>> values = valueRange.getValues();

        return values.size() + 1;
    }

    private static String getSheetId() {
        String id = "";
        try(FileReader fr = new FileReader(FILE_LOCATION)){
            BufferedReader br = new BufferedReader(fr);
            String row;
            while((row = br.readLine()) != null){
                id = row;
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return id;
    }
}