package com.example.telegrambot2;

public class ChatModel {

    private String chat_id;
    private String title;
    private String user_name;
    private String type;
    private String invite_link;

    public ChatModel() {
    }

    public ChatModel(String chat_id, String title, String user_name,
                     String type, String invite_link) {
        this.chat_id = chat_id;
        this.title = title;
        this.user_name = user_name;
        this.type = type;
        this.invite_link = invite_link;

    }

    public String getChatId() {
        return chat_id;
    }


    public void setChatId(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvite_link() {
        return invite_link;
    }

    public void setInvite_link(String invite_link) {
        this.invite_link = invite_link;
    }

    @Override
    public String toString() {
        return "Chat Id: " + chat_id + '\'' +
                ", Chat Name=" + title + '\'' +
                ", Invite Link=" + invite_link;
    }
}
