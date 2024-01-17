package com.learnKafka.repository;

import com.learnKafka.model.LibraryEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryEventRepository extends MongoRepository<LibraryEvent,Long> {
}
