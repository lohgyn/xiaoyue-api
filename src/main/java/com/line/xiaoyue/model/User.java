package com.line.xiaoyue.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 421574679290753025L;

	@JsonProperty("userId")
    private String id;

    @JsonProperty("displayName")
    private String name;

    @JsonProperty("pictureUrl")
    private String pictureUrl;

    @JsonProperty("statusMessage")
    private String statusMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}