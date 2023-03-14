package com.cust.sipnsnack.ManagerDashboard;

public class MyOrders {
    private String customerUsername;
    private String customerName;
    private String customerPhoneNo;
    private String customerAddress;
    private String paymentType;
    private String receiptImage;
    private String totalQty;
    private String totalBill;
    private String acceptedBy;
    private String orderDate;
    private String orderTime;
    private String orderId;
    private String addressType;
    private String longitude;
    private String latitude;

    public MyOrders(String customerUsername, String customerName, String customerPhoneNo,
                    String customerAddress, String paymentType, String receiptImage, String date,
                    String time, String ordID) {
        this.customerUsername = customerUsername;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.customerAddress = customerAddress;
        this.paymentType = paymentType;
        this.receiptImage = receiptImage;
        this.orderDate = date;
        this.orderTime = time;
        this.orderId = ordID;
    }


    public MyOrders(String customerUsername, String customerName, String customerPhoneNo,
                    String customerAddress, String paymentType, String receiptImage, String date,
                    String time, String ordID, String lon, String lat, String adrType, String abc) {
        this.customerUsername = customerUsername;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.customerAddress = customerAddress;
        this.paymentType = paymentType;
        this.receiptImage = receiptImage;
        this.orderDate = date;
        this.orderTime = time;
        this.orderId = ordID;
        this.longitude = lon;
        this.latitude = lat;
        this.addressType = adrType;
    }



    public MyOrders(String customerUsername, String customerName, String customerPhoneNo,
                    String customerAddress, String paymentType, String receiptImage,
                    String totalQty, String totalBill, String acceptedBy, String orderDate,
                    String orderTime, String id, String adrType, String lat, String lon) {
        this.customerUsername = customerUsername;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.customerAddress = customerAddress;
        this.paymentType = paymentType;
        this.receiptImage = receiptImage;
        this.totalQty = totalQty;
        this.totalBill = totalBill;
        this.acceptedBy = acceptedBy;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderId = id;
        this.addressType = adrType;
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
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

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getReceiptImage() {
        return receiptImage;
    }

    public void setReceiptImage(String receiptImage) {
        this.receiptImage = receiptImage;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
}
