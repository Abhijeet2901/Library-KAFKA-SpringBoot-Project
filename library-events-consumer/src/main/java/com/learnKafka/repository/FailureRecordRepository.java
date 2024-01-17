package com.learnKafka.repository;

import com.learnKafka.model.FailureRecord;
import com.learnKafka.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailureRecordRepository extends MongoRepository<FailureRecord,Long> {
    @Query(value = "{'status':?0}")
    List<FailureRecord> findAllByStatus(String status);
}
