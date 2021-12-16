package com.example.transactionCompare.FileModel;

import java.util.List;

public class ReturnResultData {

    private Integer totalNumberFile1;
    private Integer totalNumberFile2;
    private List<ClientProfile> clientProfileFile1;
    private List<ClientProfile> clientProfileFile2;
    private Integer unmatchedRecordsFile1;
    private Integer unmatchedRecordsFile2;
    private Integer status;
    private String responseMessage;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getTotalNumberFile1() {
        return totalNumberFile1;
    }

    public void setTotalNumberFile1(Integer totalNumberFile1) {
        this.totalNumberFile1 = totalNumberFile1;
    }

    public Integer getTotalNumberFile2() {
        return totalNumberFile2;
    }

    public void setTotalNumberFile2(Integer totalNumberFile2) {
        this.totalNumberFile2 = totalNumberFile2;
    }

    public List<ClientProfile> getClientProfileFile1() {
        return clientProfileFile1;
    }

    public void setClientProfileFile1(List<ClientProfile> clientProfileFile1) {
        this.clientProfileFile1 = clientProfileFile1;
    }

    public List<ClientProfile> getClientProfileFile2() {
        return clientProfileFile2;
    }

    public void setClientProfileFile2(List<ClientProfile> clientProfileFile2) {
        this.clientProfileFile2 = clientProfileFile2;
    }

    public Integer getUnmatchedRecordsFile1() {
        return unmatchedRecordsFile1;
    }

    public void setUnmatchedRecordsFile1(Integer unmatchedRecordsFile1) {
        this.unmatchedRecordsFile1 = unmatchedRecordsFile1;
    }

    public Integer getUnmatchedRecordsFile2() {
        return unmatchedRecordsFile2;
    }

    public void setUnmatchedRecordsFile2(Integer unmatchedRecordsFile2) {
        this.unmatchedRecordsFile2 = unmatchedRecordsFile2;
    }
}
