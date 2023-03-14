package com.cust.sipnsnack;

public class ImgUpload {
    private String imgName;
    private String imgUrl;

    public ImgUpload() { }

    public ImgUpload(String imgName, String imgUrl) {
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
