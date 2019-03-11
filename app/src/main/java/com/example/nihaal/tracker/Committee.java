package com.example.nihaal.tracker;

public class Committee {

    public String getCirclememberid() {
        return circlememberid;
    }

    public void setCirclememberid(String circlememberid) {
        this.circlememberid = circlememberid;
    }

    public String getJoined_name() {
        return joined_name;
    }

    public void setJoined_name(String joined_name) {
        this.joined_name = joined_name;
    }

    public String getJoined_imageUrl() {
        return joined_imageUrl;
    }

    public void setJoined_imageUrl(String joined_imageUrl) {
        this.joined_imageUrl = joined_imageUrl;
    }

    //  public String name, imageUrl, userid;
    public String circlememberid, joined_name,joined_imageUrl;
    public Committee(String circlememberid, String joined_name, String joined_imageUrl){
        this.circlememberid = circlememberid;
        this.joined_name = joined_name;
        this.joined_imageUrl = joined_imageUrl;
    }

    public Committee()
    {

    }
//    public Committee(String name, String imageUrl, String userid) {
//        this.name = name;
//        this.imageUrl = imageUrl;
//        this.userid = userid;
//
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getUserid() {
//        return userid;
//    }
//
//    public void setUserid(String userid) {
//        this.userid = userid;
//    }
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
}
