package com.cust.sipnsnack.Customers;

public class MyOrdersModel {
    private String name;
    private String paymentType;
    private String quantity;
    private String totalBill;
    private String orderTime;
    private String orderDate;
    private String orderId;
    private String bikerName;
    private String bikerPhone;
    private String bikerUsername;
    private String addressType, longitude, latitude, address;

    public MyOrdersModel(String name, String paymentType, String quantity, String totalBill,
                         String orderTime, String orderDate) {
        this.name = name;
        this.paymentType = paymentType;
        this.quantity = quantity;
        this.totalBill = totalBill;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
    }

    public MyOrdersModel(String name, String paymentType, String quantity, String totalBill,
                         String orderTime, String orderDate, String orderId) {
        this.name = name;
        this.paymentType = paymentType;
        this.quantity = quantity;
        this.totalBill = totalBill;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.orderId = orderId;
    }

    public MyOrdersModel(String name, String paymentType, String quantity, String totalBill,
                         String orderTime, String orderDate, String orderId, String bikerName,
                         String bikerPhone, String adrType, String lon, String lat, String address) {
        this.name = name;
        this.paymentType = paymentType;
        this.quantity = quantity;
        this.totalBill = totalBill;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.bikerName = bikerName;
        this.bikerPhone = bikerPhone;
        this.address = address;
        this.addressType = adrType;
        this.longitude = lon;
        this.latitude = lat;

    }

    public MyOrdersModel(String name, String paymentType, String quantity, String totalBill,
                         String orderTime, String orderDate, String orderId, String bikerName,
                         String bikerPhone, String bikerUsername) {
        this.name = name;
        this.paymentType = paymentType;
        this.quantity = quantity;
        this.totalBill = totalBill;
        this.orderTime = orderTime;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.bikerName = bikerName;
        this.bikerPhone = bikerPhone;
        this.bikerUsername = bikerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBikerName() {
        return bikerName;
    }

    public void setBikerName(String bikerName) {
        this.bikerName = bikerName;
    }

    public String getBikerPhone() {
        return bikerPhone;
    }

    public void setBikerPhone(String bikerPhone) {
        this.bikerPhone = bikerPhone;
    }

    public String getBikerUsername() {
        return bikerUsername;
    }

    public void setBikerUsername(String bikerUsername) {
        this.bikerUsername = bikerUsername;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
