package com.cust.sipnsnack.Bikers;

public class BikersModel {
    private String bikerUsername;
    private String bikerName;
    private String bikerPassword;
    private String bikerPhoneNo;
    private String bikerAddress;
    private String bikerAvailability;

    /*public BikersModel(String bikerUsername, String bikerName, String bikerPassword, String bikerPhoneNo, String bikerAddress) {
        this.bikerUsername = bikerUsername;
        this.bikerName = bikerName;
        this.bikerPassword = bikerPassword;
        this.bikerPhoneNo = bikerPhoneNo;
        this.bikerAddress = bikerAddress;
    }*/

    public BikersModel(String bikerUsername, String bikerName, String bikerPhoneNo, String bikerAddress, String bikerAvailability) {
        this.bikerUsername = bikerUsername;
        this.bikerName = bikerName;
        this.bikerPhoneNo = bikerPhoneNo;
        this.bikerAddress = bikerAddress;
        this.bikerAvailability = bikerAvailability;
    }

    public String getBikerUsername() {
        return bikerUsername;
    }

    public void setBikerUsername(String bikerUsername) {
        this.bikerUsername = bikerUsername;
    }

    public String getBikerName() {
        return bikerName;
    }

    public void setBikerName(String bikerName) {
        this.bikerName = bikerName;
    }

    public String getBikerPassword() {
        return bikerPassword;
    }

    public void setBikerPassword(String bikerPassword) {
        this.bikerPassword = bikerPassword;
    }

    public String getBikerPhoneNo() {
        return bikerPhoneNo;
    }

    public void setBikerPhoneNo(String bikerPhoneNo) {
        this.bikerPhoneNo = bikerPhoneNo;
    }

    public String getBikerAddress() {
        return bikerAddress;
    }

    public void setBikerAddress(String bikerAddress) {
        this.bikerAddress = bikerAddress;
    }

    public String getBikerAvailability() {
        return bikerAvailability;
    }

    public void setBikerAvailability(String bikerAvailability) {
        this.bikerAvailability = bikerAvailability;
    }
}
