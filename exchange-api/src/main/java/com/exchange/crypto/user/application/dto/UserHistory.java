package com.exchange.crypto.user.application.dto;

import com.exchange.crypto.user.domain.entity.User;

public class UserHistory {

    private User user;
    private Number revisionNumber;
    private String revisionType;

    public UserHistory(User user, Number revisionNumber, String revisionType) {
        this.user = user;
        this.revisionNumber = revisionNumber;
        this.revisionType = revisionType;
    }

    public User getUser() { return user; }
    public Number getRevisionNumber() { return revisionNumber; }
    public String getRevisionType() { return revisionType; }
}