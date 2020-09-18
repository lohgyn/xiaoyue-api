package com.line.xiaoyue.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity()
@Table(name = "insight_followers")
public class NumberOfFollower implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("followers")
    private Integer followers;

    @JsonProperty("targetedReaches")
    @Column(name = "targeted_reaches")
    private Integer targetedReaches;

    @JsonProperty("blocks")
    private Integer blocks;

    @JsonIgnore
	@Column(name = "retrieved_date", columnDefinition = "DATE")
    private LocalDate retrievedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getRetrievedDate() {
        return retrievedDate;
    }

    public void setRetrievedDate(LocalDate retrievedDate) {
        this.retrievedDate = retrievedDate;
    }

    @Override
    public String toString() {
        return "NumberOfFollower [blocks=" + blocks + ", followers=" + followers + ", status=" + status
                + ", targetedReaches=" + targetedReaches + "]";
    }
}