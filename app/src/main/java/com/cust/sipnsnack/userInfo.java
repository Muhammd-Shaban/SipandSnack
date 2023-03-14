package com.cust.sipnsnack;

public class userInfo {
    public String Name, Username, Password, PhoneNo, Address, Role, Status, CancelledOrders;

    public userInfo() { }

    public userInfo(String name, String username, String password, String phoneNo, String address, String role, String status, String cancelledOrders) {
        Name = name;
        Username = username;
        Password = password;
        PhoneNo = phoneNo;
        Address = address;
        Role = role;
        Status = status;
        CancelledOrders = cancelledOrders;
    }
}
