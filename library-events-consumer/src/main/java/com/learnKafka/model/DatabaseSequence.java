package com.learnKafka.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sequence")
public class DatabaseSequence {

    private Long sequenceId;

    private String sequenceName;

}
