package com.learnKafka.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnKafka.model.FailureRecord;
import com.learnKafka.model.Status;
import com.learnKafka.repository.FailureRecordRepository;
import com.learnKafka.service.LibraryEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetryScheduler {

    @Autowired
    FailureRecordRepository failureRecordRepository;

    @Autowired
    LibraryEventService libraryEventService;

    @Scheduled(fixedRate = 10000)
    public void retryFailedrecords() {
        failureRecordRepository.findAllByStatus("RETRY").
                forEach(failureRecord -> {
                    ConsumerRecord<Long, String> consumerRecord = buildConsumerRecord(failureRecord);
                    try {
                        libraryEventService.processLibraryEvent(consumerRecord);
                        failureRecord.setStatus(Status.SUCCESS);
                        failureRecordRepository.save(failureRecord);
                    } catch (Exception e) {
//                        throw new RuntimeException(e);
                        log.error("Error in retry faied records: " + e.getMessage());
                    }
                });
    }

    private ConsumerRecord<Long, String> buildConsumerRecord(FailureRecord failureRecord) {
        return new ConsumerRecord<Long, String>(
                failureRecord.getTopic(),
                Math.toIntExact(failureRecord.getPartition()),
                failureRecord.getOffset_value(),
                failureRecord.getKey(),
                failureRecord.getErrorRecord());
    }
}
