package com.cust.sipnsnack.Admin;

public class ManagersModel {

    private String managerName;
    private String managerUsername;
    private String managerPhoneNo;

    public ManagersModel(String managerName, String managerUsername, String managerPhoneNo) {
        this.managerName = managerName;
        this.managerUsername = managerUsername;
        this.managerPhoneNo = managerPhoneNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getManagerPhoneNo() {
        return managerPhoneNo;
    }

    public void setManagerPhoneNo(String managerPhoneNo) {
        this.managerPhoneNo = managerPhoneNo;
    }
}
