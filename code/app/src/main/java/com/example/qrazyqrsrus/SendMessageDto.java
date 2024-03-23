package com.example.qrazyqrsrus;

import com.google.gson.Gson;

public class SendMessageDto {
    private String to;
    private NotificationBody notification;

    public SendMessageDto(String to, NotificationBody notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationBody getNotification() {
        return notification;
    }

    public void setNotification(NotificationBody notification) {
        this.notification = notification;
    }

    /**
     * This function converts our data transfer object to a Json file
     * @param dtoToConvert the data transfer object we are converting
     * @return a json file
     */
    public String getJson(SendMessageDto dtoToConvert){
        Gson gson = new Gson();
        String json = gson.toJson(dtoToConvert);
        return json;
    }

}
