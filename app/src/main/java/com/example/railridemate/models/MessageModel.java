package com.example.railridemate.models;

public class MessageModel {
    private String msgId;
    private String senderId;
    private String message;
    private boolean isAdmin;
    private String senderUsername; // Add this line
    private String replyToMessageId;


    private String replyUserId; // Add this line
    private String replyUsername; // Add this line

    // Constructors, getters, and setters for other fields
    public String getsenderUsername() {
        return senderUsername;
    }

    public void setsenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUsername() {
        return replyUsername;
    }

    public void setReplyUsername(String replyUsername) {
        this.replyUsername = replyUsername;
    }

    public MessageModel(String msgId, String senderId, String message, boolean isAdmin, String replyToMessageId) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.message = message;
        this.isAdmin = isAdmin;
        this.replyToMessageId = replyToMessageId;
    }

    public MessageModel() {}

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(String replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }
}
