package com.learnKafka.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "failure-record")
public class FailureRecord {

    @Transient
    public static final String SEQUENCE_NAME = "failure-record_sequence";

    @Id
    private Long id;

    private String topic;
    private Long key;
    private String errorRecord;
    private Long partition;
    private String errorRecordType;
    private Long offset_value;
    private String exception;
    private Status status;

}
