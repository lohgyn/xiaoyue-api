package com.line.xiaoyue.service;

import java.time.LocalDate;

import com.line.xiaoyue.model.NumberOfFollower;

public interface PublicAPIService {
    NumberOfFollower getFollowerInsightByDate(LocalDate date);
}
