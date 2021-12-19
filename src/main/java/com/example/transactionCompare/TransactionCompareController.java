package com.example.transactionCompare;

import com.example.transactionCompare.FileModel.ClientProfile;
import com.example.transactionCompare.FileModel.ReturnGenericResponse;
import com.example.transactionCompare.FileModel.ReturnResultData;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

import static java.util.Comparator.comparing;

@CrossOrigin(origins = "*", allowedHeaders = "*") // Accept request from all sources
@RestController
@RequestMapping("/api") // Main mapping after URL
public class TransactionCompareController {

    // Empty Constructor
    public TransactionCompareController() {
    }
    /*
        File 1 after uploading will be placed in HashMap. Each line will be placed as it is without splitting.

        File 2 will be parsed without HashMap and during parsing will be compared with the values from HashMap
        which contains all rows from the file. If row from File 2 and HashMap match that same row will be removed
        from HashMap. If match is not found row from File 2 will be parsed based on delimiter and Object will be created.

        After File 2 is finished HashMap will be iterated through and all the records left inside will be parsed
        based on delimiter and Objects will be created.

        All the Objects will be placed inside the list of Objects and placed inside response, along with total
        number of records and unmatched records for each file.
    */


    // Post request for files upload
    @PostMapping("/uploadFiles")
    public Object uploadFiles(@RequestParam("csvFile") MultipartFile csvFile, @RequestParam("csvFile2") MultipartFile csvFile2) {
        //----------Validating Files---------------
        if (csvFile == null) {
            ReturnGenericResponse returnGenericResponse = new ReturnGenericResponse(); // Object which is returned in case of error
            returnGenericResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            returnGenericResponse.setResponseMessage("File 1 missing");
            return returnGenericResponse;
        } else if (csvFile2 == null) {
            ReturnGenericResponse returnGenericResponse = new ReturnGenericResponse(); // Object which is returned in case of error
            returnGenericResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            returnGenericResponse.setResponseMessage("File 2 missing");
            return returnGenericResponse;
        } else if(!FilenameUtils.getExtension(csvFile.getOriginalFilename()).equals("csv")){
            ReturnGenericResponse returnGenericResponse = new ReturnGenericResponse(); // Object which is returned in case of error
            returnGenericResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            returnGenericResponse.setResponseMessage("File 1 wrong Format");
            return returnGenericResponse;
        }else if(!FilenameUtils.getExtension(csvFile2.getOriginalFilename()).equals("csv")){
            ReturnGenericResponse returnGenericResponse = new ReturnGenericResponse(); // Object which is returned in case of error
            returnGenericResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            returnGenericResponse.setResponseMessage("File 2 wrong Format");
            return returnGenericResponse;
        //-----------------------------------------
        }else {
            try {
                //-----Creating List of objects and HashMap-----
                HashMap<Integer, String> file1Map = new HashMap<Integer, String>(); // HashMap which contains parsed data from file 1
                List<ClientProfile> clientProfileListFile1 = new ArrayList<>(); // List of objects from File 1
                List<ClientProfile> clientProfileListFile2 = new ArrayList<>(); // List of objects from File 2
                //----------------------------------------------

                //----------Reading File 1----------------------
                byte[] csvFileBytes = csvFile.getBytes();
                ByteArrayInputStream inputCsvFileStream = new ByteArrayInputStream(csvFileBytes);
                BufferedReader brFile = new BufferedReader(new InputStreamReader(inputCsvFileStream));
                String csvFileLines = "";
                Integer numberOfFile1Lines = 0;
                while ((csvFileLines = brFile.readLine()) != null) {
                    file1Map.put(numberOfFile1Lines, csvFileLines); // Placing values inside HashMap
                    numberOfFile1Lines++;
                }
                brFile.close(); // Closing buffer reader
                //----------------------------------------------

                //----------Reading File 2----------------------
                byte[] csvFile2Bytes = csvFile2.getBytes();
                ByteArrayInputStream inputCsvFile2Stream = new ByteArrayInputStream(csvFile2Bytes);
                BufferedReader brFile2 = new BufferedReader(new InputStreamReader(inputCsvFile2Stream));
                String csvFile2Lines = "";
                Integer numberOfFile2Lines = 0;
                while ((csvFile2Lines = brFile2.readLine()) != null) {
                    if (file1Map.containsValue(csvFile2Lines)) {
                        // If File 1 and 2 have matching records
                        for (Object o : file1Map.keySet()) {
                            if (file1Map.get(o).equals(csvFile2Lines)) {
                                file1Map.remove(o); // Removing matching Records from File 1 HashMap
                                break;
                            }
                        }
                    } else {
                        //if File 1 does not contain the same record as File 2
                        String[] splitCsvLineFile2 = csvFile2Lines.split(","); // Splitting row into Object data
                        String[] splitCsvLineFile2FinalSize = new String[8]; // Creating new string array so that rows which are missing data do not break the application
                        System.arraycopy(splitCsvLineFile2, 0, splitCsvLineFile2FinalSize, 0, splitCsvLineFile2.length);
                        ClientProfile clientProfileFile2 = new ClientProfile(splitCsvLineFile2FinalSize);
                        clientProfileListFile2.add(clientProfileFile2); // Adding objects to object list
                    }
                    numberOfFile2Lines++;
                }
                brFile2.close(); // Closing buffer reader for file 2
                //----------------------------------------------------

                //------HashMap to ClientProfile List of Objects------
                for (Map.Entry<Integer, String> entry : file1Map.entrySet()) { // Iterating through HashMap of File 1
                    String[] splitCsvLineFile1 = entry.getValue().split(","); // Splitting each row
                    String[] splitCsvLineFile1FinalSize = new String[8]; // Creating new string array so that rows which are missing data do not break the application
                    System.arraycopy(splitCsvLineFile1, 0, splitCsvLineFile1FinalSize, 0, splitCsvLineFile1.length);
                    ClientProfile clientProfileFile1 = new ClientProfile(splitCsvLineFile1FinalSize);
                    clientProfileListFile1.add(clientProfileFile1); // Adding objects to object list
                }
                //----------------------------------------------------

                //------Matching records with the same date-----------
                int connectionValue = 0;
                for (ClientProfile clientProfileCloseMatchesFile1 : clientProfileListFile1) {
                    for (ClientProfile clientProfileCloseMatchesFile2 : clientProfileListFile2) {
                        if (clientProfileCloseMatchesFile1.getTransactionDate().equals(clientProfileCloseMatchesFile2.getTransactionDate())) {
                            clientProfileCloseMatchesFile1.setConnection(connectionValue);
                            clientProfileCloseMatchesFile2.setConnection(connectionValue);
                        }
                    }
                    connectionValue++;
                }
                //---------------------------------------------------

                //----- Sorting lists based on date field -----------
                Collections.sort(clientProfileListFile1, comparing(ClientProfile::getTransactionDate));
                Collections.sort(clientProfileListFile2, comparing(ClientProfile::getTransactionDate));
                //---------------------------------------------------

                //---------------Building response ------------------
                ReturnResultData returnResultData = new ReturnResultData(); // Object which is returned as response in case of success
                returnResultData.setTotalNumberFile1(numberOfFile1Lines);
                returnResultData.setTotalNumberFile2(numberOfFile2Lines);
                returnResultData.setClientProfileFile1(clientProfileListFile1);
                returnResultData.setClientProfileFile2(clientProfileListFile2);
                returnResultData.setUnmatchedRecordsFile1(clientProfileListFile1.size());
                returnResultData.setUnmatchedRecordsFile2(clientProfileListFile2.size());
                returnResultData.setStatus(HttpStatus.OK.value());
                returnResultData.setResponseMessage("SUCCESS");
                return returnResultData;
                //---------------------------------------------------

            } catch (Exception ex) {
                ReturnGenericResponse returnGenericResponse = new ReturnGenericResponse(); // Object which is returned in case of error
                returnGenericResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                returnGenericResponse.setResponseMessage(ex.getLocalizedMessage());
                return returnGenericResponse;
            }
        }
    }
}
