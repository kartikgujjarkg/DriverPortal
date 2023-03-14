package com.example.demo.kafka;

import com.example.demo.entity.Verification;
import com.example.demo.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.example.demo.Constants.Constants.consumerGroupId;
import static com.example.demo.Constants.Constants.taskUpdateTopic;

@Service
public class KafkaTaskUpdateConsumerService {

    @Autowired
    VerificationService verificationService;

    @KafkaListener(topics = taskUpdateTopic, groupId = consumerGroupId)
    public Verification consumeVerificationTask(Verification verification){
        verificationService.updateVerificationStatus(verification);
        return verification;
    }

}
