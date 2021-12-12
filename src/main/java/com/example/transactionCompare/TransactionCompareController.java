package com.example.transactionCompare;

import com.example.transactionCompare.fileModel.ClientProfile;
import com.example.transactionCompare.fileModel.ReturnResultData;
import com.opencsv.CSVParser;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class TransactionCompareController {

    public TransactionCompareController() {
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/uploadFiles")
    public ReturnResultData uploadFiles(@RequestParam("csvFile") MultipartFile csvFile, @RequestParam("csvFile2") MultipartFile csvFile2) throws IOException {
        ReturnResultData returnResultData = new ReturnResultData();
        System.out.print("USAO SAM: " + csvFile);
        try {
            HashMap<Integer, String> file1Map = new HashMap<Integer, String>();
            List<ClientProfile> clientProfileListFile1 = new ArrayList<>();
            List<ClientProfile> clientProfileListFile2 = new ArrayList<>();

            byte[] csvFileBytes = csvFile.getBytes();
            ByteArrayInputStream inputCsvFileStream = new ByteArrayInputStream(csvFileBytes);
            BufferedReader brFile = new BufferedReader(new InputStreamReader(inputCsvFileStream ));
            String csvFileLines = "";
            Integer numberOfFile1Lines = 0;
            while ((csvFileLines = brFile.readLine()) != null) {
                file1Map.put(numberOfFile1Lines, csvFileLines);
                numberOfFile1Lines++;
            }
            brFile.close();

            byte[] csvFile2Bytes = csvFile2.getBytes();
            ByteArrayInputStream inputCsvFile2Stream = new ByteArrayInputStream(csvFile2Bytes);
            BufferedReader brFile2 = new BufferedReader(new InputStreamReader(inputCsvFile2Stream ));
            String csvFile2Lines = "";
            Integer numberOfFile2Lines = 0;
            while ((csvFile2Lines = brFile2.readLine()) != null) {
                if (file1Map.containsValue(csvFile2Lines)) {
                    for (Object o : file1Map.keySet()) {
                        if (file1Map.get(o).equals(csvFile2Lines)) {
                            file1Map.remove(o);
                            break;
                        }
                    }
                }else {
                    //if file1 doesn't have line like in file2
                    String[] splitCsvLineFile2 = csvFile2Lines.split(",");
                    String[] splitCsvLineFile2FinalSize = new String[8];
                    System.arraycopy(splitCsvLineFile2, 0, splitCsvLineFile2FinalSize, 0, splitCsvLineFile2.length);
                    ClientProfile clientProfileFile2 = new ClientProfile(splitCsvLineFile2FinalSize);
                    clientProfileListFile2.add(clientProfileFile2);
                }
                numberOfFile2Lines++;
            }
            brFile2.close();

            for(Map.Entry<Integer, String> entry : file1Map.entrySet()) {
                String[] splitCsvLineFile1 = entry.getValue().split(",");
                String[] splitCsvLineFile1FinalSize = new String[8];
                System.arraycopy(splitCsvLineFile1, 0, splitCsvLineFile1FinalSize, 0, splitCsvLineFile1.length);
                ClientProfile clientProfileFile1 = new ClientProfile(splitCsvLineFile1FinalSize);
                clientProfileListFile1.add(clientProfileFile1);
            }

            returnResultData.setTotalNumberFile1(numberOfFile1Lines);
            returnResultData.setTotalNumberFile2(numberOfFile2Lines);
            returnResultData.setClientProfileFile1(clientProfileListFile1);
            returnResultData.setClientProfileFile2(clientProfileListFile2);
            returnResultData.setUnmatchedRecordsFile1(clientProfileListFile1.size());
            returnResultData.setUnmatchedRecordsFile2(clientProfileListFile2.size());
//            CsvToBean<ClientProfile> csvToBean = new CsvToBeanBuilder(reader)
//                    .withType(ClientProfile.class)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//
//            List<ClientProfile> clientProfileFile1 = csvToBean.parse();
//            for(ClientProfile csvUser: clientProfileFile1) {
//                System.out.println("Name : " + csvUser.getProfileName());
//            }

        }catch (Exception ex){
            System.out.println(ex);
        }
        return returnResultData;
    }
}
