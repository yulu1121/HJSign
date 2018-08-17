package com.anshi.hjsign.entry;

public class PersonDetailEntry {
    private String roomTitle;
    private String sex;
    private String status;
    private String name;

    public PersonDetailEntry(String roomTitle, String sex, String status, String name) {
        this.roomTitle = roomTitle;
        this.sex = sex;
        this.status = status;
        this.name = name;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
