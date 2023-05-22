
package com.line.xiaoyue.service.impl;

import java.time.LocalDate;


import com.line.xiaoyue.model.NumberOfFollower;
import com.line.xiaoyue.repository.InsightFollowersRepository;
import com.line.xiaoyue.service.InsightFollowersService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsightFollowersServiceImpl implements InsightFollowersService {


    @Autowired
    private InsightFollowersRepository insightFollowersRepository;

    @Override
    public NumberOfFollower getInsightByDate(LocalDate date) {
        return insightFollowersRepository.findByRetrievedDate(date);
    }

    @Override
    @Transactional
    public NumberOfFollower saveInsight(NumberOfFollower numberOfFollower) {
        return insightFollowersRepository.save(numberOfFollower);
    }

}