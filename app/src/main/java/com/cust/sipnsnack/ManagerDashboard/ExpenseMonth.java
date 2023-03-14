package com.cust.sipnsnack.ManagerDashboard;

public class ExpenseMonth {
    private String date;
    private String total;
    private String crockery;
    private String kitchen;
    private String biker;
    private String maintenance;
    private String others;

    public ExpenseMonth(String date, String total, String crockery, String kitchen, String biker, String maintenance, String others) {
        this.date = date;
        this.total = total;
        this.crockery = crockery;
        this.kitchen = kitchen;
        this.biker = biker;
        this.maintenance = maintenance;
        this.others = others;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCrockery() {
        return crockery;
    }

    public void setCrockery(String crockery) {
        this.crockery = crockery;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getBiker() {
        return biker;
    }

    public void setBiker(String biker) {
        this.biker = biker;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
