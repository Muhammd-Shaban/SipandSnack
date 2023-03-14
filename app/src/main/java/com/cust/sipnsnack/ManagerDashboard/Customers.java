package com.cust.sipnsnack.ManagerDashboard;

public class Customers {
    private String userName;
    private String name;
    private String cancelOrders;
    private String status;

    public Customers(String userName, String name, String cancelOrders, String status) {
        this.userName = userName;
        this.name = name;
        this.cancelOrders = cancelOrders;
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCancelOrders() {
        return cancelOrders;
    }

    public void setCancelOrders(String cancelOrders) {
        this.cancelOrders = cancelOrders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
