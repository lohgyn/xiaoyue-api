package com.line.xiaoyue.repository;

import java.time.LocalDate;

import com.line.xiaoyue.model.NumberOfFollower;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightFollowersRepository extends CrudRepository<NumberOfFollower, Long> {

    public NumberOfFollower findByRetrievedDate(LocalDate retrievedDate);
}