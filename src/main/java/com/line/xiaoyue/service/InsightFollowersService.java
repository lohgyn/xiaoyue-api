
package com.line.xiaoyue.service;

import java.time.LocalDate;

import com.line.xiaoyue.model.NumberOfFollower;

public interface InsightFollowersService {
    NumberOfFollower getInsightByDate(LocalDate date);
    
    NumberOfFollower saveInsight(NumberOfFollower numberOfFollower);
    
}