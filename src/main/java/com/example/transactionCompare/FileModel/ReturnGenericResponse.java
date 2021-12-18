package com.example.transactionCompare.FileModel;

public class ReturnGenericResponse {
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
}
