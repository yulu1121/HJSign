package com.anshi.hjsign.entry;

import java.util.List;

public class RoomEntry {
    private String roomTitle;
    private List<RoomPersonEntry> roomPersonEntryList;

    public RoomEntry(String roomTitle, List<RoomPersonEntry> roomPersonEntryList) {
        this.roomTitle = roomTitle;
        this.roomPersonEntryList = roomPersonEntryList;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public List<RoomPersonEntry> getRoomPersonEntryList() {
        return roomPersonEntryList;
    }

    public void setRoomPersonEntryList(List<RoomPersonEntry> roomPersonEntryList) {
        this.roomPersonEntryList = roomPersonEntryList;
    }

    public static class RoomPersonEntry {
        private String sex;
        private String name;
        private String status;

        public RoomPersonEntry(String sex, String name, String status) {
            this.sex = sex;
            this.name = name;
            this.status = status;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
