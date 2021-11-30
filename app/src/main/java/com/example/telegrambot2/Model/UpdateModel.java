package com.example.telegrambot2.Model;

public class UpdateModel {

    private int update_id;
    private int message_id;
    private String chat_id;
    private String chat_title;
    private String chat_user_name;
    private int date_time;
    private String text;

    public UpdateModel() {
    }

    public UpdateModel(int update_id, int message_id, String chat_id, String chat_title,
                       String chat_user_name, int date_time, String text) {
        this.update_id = update_id;
        this.message_id = message_id;
        this.chat_id = chat_id;
        this.chat_title = chat_title;
        this.chat_user_name = chat_user_name;
        this.date_time = date_time;
        this.text = text;


    }

    public int getUpdateId() {
        return update_id;
    }

    public void setUpdateId(int update_id) {
        this.update_id = update_id;
    }

    public int getMessageId() {
        return message_id;
    }

    public void setMessageId(int message_id) {
        this.message_id = message_id;
    }

    public String getChatId() {
        return chat_id;
    }


    public void setChatId(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChatTitle() {
        return chat_title;
    }

    public void setChatTitle(String chat_title) {
        this.chat_title = chat_title;
    }

    public String getChaUserName() {
        return chat_user_name;
    }

    public void setChatUserName(String chat_user_name) {
        this.chat_user_name = chat_user_name;
    }

    public int getDateTime() {
        return date_time;
    }

    public void setDateTime(int date_time) {
        this.date_time = date_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Chat Name=" + chat_title + System.getProperty("line.separator") +
                "User name=" + chat_user_name + System.getProperty("line.separator") +
                "Message=" + text + System.getProperty("line.separator") +
                "Date=" + date_time;
    }
}
