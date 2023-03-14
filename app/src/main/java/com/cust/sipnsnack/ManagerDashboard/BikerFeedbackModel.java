package com.cust.sipnsnack.ManagerDashboard;

public class BikerFeedbackModel {

    String orderId;
    String feedback;
    String customerName;
    String customerPhoneNo;
    String bikerName;
    String bikerPhoneNo;

    public BikerFeedbackModel(String orderId, String feedback, String customerName,
                              String customerPhoneNo, String bikerName, String bikerPhoneNo) {
        this.orderId = orderId;
        this.feedback = feedback;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.bikerName = bikerName;
        this.bikerPhoneNo = bikerPhoneNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getBikerName() {
        return bikerName;
    }

    public void setBikerName(String bikerName) {
        this.bikerName = bikerName;
    }

    public String getBikerPhoneNo() {
        return bikerPhoneNo;
    }

    public void setBikerPhoneNo(String bikerPhoneNo) {
        this.bikerPhoneNo = bikerPhoneNo;
    }
}
