package com.learnKafka.service;

import com.learnKafka.model.DatabaseSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    @Autowired
    MongoTemplate mongoTemplate;

    public Long getNextSequenceId(String sequenceName){
        DatabaseSequence counter = mongoTemplate.findAndModify(new Query(where("sequenceName").is(sequenceName)),
                new Update().inc("sequenceId",1), (new FindAndModifyOptions()).returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSequenceId() : 1;
    }
}
