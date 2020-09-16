package com.line.xiaoyue.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Friendship implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */

    @JsonProperty("friendFlag")
    private boolean friend;

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

}