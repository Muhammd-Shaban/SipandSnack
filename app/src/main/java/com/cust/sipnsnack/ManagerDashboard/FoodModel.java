package com.cust.sipnsnack.ManagerDashboard;

public class FoodModel {

    String orderId;
    String feedback;
    String customerName;
    String customerPhoneNo;

    public FoodModel(String orderId, String feedback, String customerName, String customerPhoneNo) {
        this.orderId = orderId;
        this.feedback = feedback;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
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
}
