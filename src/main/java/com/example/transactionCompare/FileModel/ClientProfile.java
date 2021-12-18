package com.example.transactionCompare.FileModel;

public class ClientProfile {

    private String profileName;
    private String transactionDate;
    private String transactionAmount;
    private String transactionNarrative;
    private String transactionDescription;
    private String transactionID;
    private String transactionType;
    private String walletReference;
    private Integer connection;
    public ClientProfile() {

    }

    public ClientProfile(String[] profileData) {
            this.profileName = profileData[0];
            this.transactionDate = profileData[1];
            this.transactionAmount = profileData[2];
            this.transactionNarrative = profileData[3];
            this.transactionDescription = profileData[4];
            this.transactionID = profileData[5];
            this.transactionType = profileData[6];
            this.walletReference = profileData[7];
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionNarrative() {
        return transactionNarrative;
    }

    public void setTransactionNarrative(String transactionNarrative) {
        this.transactionNarrative = transactionNarrative;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getWalletReference() {
        return walletReference;
    }

    public void setWalletReference(String walletReference) {
        this.walletReference = walletReference;
    }

    public Integer getConnection() {
        return connection;
    }

    public void setConnection(Integer connection) {
        this.connection = connection;
    }
}
