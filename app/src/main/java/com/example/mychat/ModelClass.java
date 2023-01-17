package com.example.mychat;

public class ModelClass {

    String message, from, messageId;
    Long timeStamp;

    public ModelClass(){}

//    public ModelClass(String message, String from) {
//        this.message = message;
//        this.from = from;
//    }



    public ModelClass(String message, String from, Long timeStamp) {
        this.message = message;
        this.from = from;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getTimeStamp() {
//        System.out.println(timeStamp);
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
