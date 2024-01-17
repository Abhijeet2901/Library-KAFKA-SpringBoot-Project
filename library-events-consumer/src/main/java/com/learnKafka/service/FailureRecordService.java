package com.learnKafka.service;

import com.learnKafka.model.FailureRecord;
import com.learnKafka.model.Status;
import com.learnKafka.repository.FailureRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FailureRecordService {

    @Autowired
    FailureRecordRepository failureRecordRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;


    public void save(ConsumerRecord<Long, String> consumerRecord, Exception e, Status status) {
        FailureRecord failureRecord = new FailureRecord();
        failureRecord.setId(sequenceGeneratorService.getNextSequenceId("failure-record_sequence"));
        failureRecord.setTopic(consumerRecord.topic());
        failureRecord.setKey(consumerRecord.key());
        failureRecord.setErrorRecord(consumerRecord.value());
        failureRecord.setPartition((long) consumerRecord.partition());
        failureRecord.setOffset_value(consumerRecord.offset());
        failureRecord.setException(e.getCause().getMessage());
        failureRecord.setStatus(status);
        failureRecordRepository.save(failureRecord);

    }
}
