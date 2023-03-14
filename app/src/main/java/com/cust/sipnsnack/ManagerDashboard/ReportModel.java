package com.cust.sipnsnack.ManagerDashboard;

public class ReportModel {

    private String id;
    private String name;
    private String phoneNo;
    private String username;
    private String report;
    private String time;
    private String date;

    public ReportModel(String id, String name, String phoneNo, String username, String report, String time, String date) {
        this.id = id;
        this.name = name;
        this.phoneNo = phoneNo;
        this.username = username;
        this.report = report;
        this.time = time;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
