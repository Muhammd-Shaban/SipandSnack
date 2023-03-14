package com.cust.sipnsnack.ManagerDashboard;

public class PendingOrderItems {
    private String itemId;
    private String itemName;
    private String itemCategory;
    private String itemPrice;
    private String itemDescription;
    private String itemSize;
    private String itemImgUrl;
    private String itemQty;
    private String itemTotalPrice;

    public PendingOrderItems(String itemId, String itemName, String itemCategory, String itemPrice, String itemDescription, String itemSize, String itemImgUrl, String itemQty, String itemTotalPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemPrice = itemPrice;
        this.itemDescription = itemDescription;
        this.itemSize = itemSize;
        this.itemImgUrl = itemImgUrl;
        this.itemQty = itemQty;
        this.itemTotalPrice = itemTotalPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(String itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }
}
