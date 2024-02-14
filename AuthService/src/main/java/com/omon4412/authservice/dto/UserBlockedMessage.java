package com.omon4412.authservice.dto;

public class UserBlockedMessage extends ResponseMessage {

    protected Long blockedUserId;

    public UserBlockedMessage(String message, Long blockedUserId) {
        super(message);
        this.blockedUserId = blockedUserId;
    }
}
