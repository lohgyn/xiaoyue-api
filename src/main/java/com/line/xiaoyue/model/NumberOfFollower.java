package com.line.xiaoyue.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumberOfFollower implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty("status")
    private String status;

    @JsonProperty("followers")
    private Integer followers;

    @JsonProperty("targetedReaches")
    private Integer targetedReaches;

    @JsonProperty("blocks")
    private Integer blocks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getTargetedReaches() {
        return targetedReaches;
    }

    public void setTargetedReaches(Integer targetedReaches) {
        this.targetedReaches = targetedReaches;
    }

    public Integer getBlocks() {
        return blocks;
    }

    public void setBlocks(Integer blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        return "NumberOfFollower [blocks=" + blocks + ", followers=" + followers + ", status=" + status
                + ", targetedReaches=" + targetedReaches + "]";
    }

}