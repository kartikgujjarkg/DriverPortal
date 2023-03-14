package com.example.demo.kafka;

import com.example.demo.entity.Verification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaTaskProducerService {

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafkaTaskTopic}")
    String taskTopic;

    public KafkaTaskProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageToCreateVerificationTask(Verification verification){
        System.out.println("Topic name is"+taskTopic);
        this.kafkaTemplate.send(taskTopic,verification);
        System.out.println("Sent Kakfa message");
    }

}
