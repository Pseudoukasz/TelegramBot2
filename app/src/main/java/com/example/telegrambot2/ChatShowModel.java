package com.example.telegrambot2;

public class ChatShowModel extends ChatModel {

    private ChatModel chatModel;

    public ChatModel getChatModel() {
        return this.chatModel;
    }


    @Override
    public String toString() {
        return "Chat Id: " + this.chatModel.getChatId() + '\'' +
                "Chat name: " + this.chatModel.getTitle() + '\'' +
                "Invite Link: " + this.chatModel.getInviteLink()
                ;
    }
}
