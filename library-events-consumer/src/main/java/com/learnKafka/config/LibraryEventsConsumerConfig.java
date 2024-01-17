package com.learnKafka.config;

import com.learnKafka.model.Status;
import com.learnKafka.service.FailureRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.List;

@Configuration
@EnableKafka
@Slf4j
public class LibraryEventsConsumerConfig {

//    @Autowired
//    KafkaTemplate kafkaTemplate;
//
//    @Autowired
//    FailureRecordService failureRecordService;
//
//    @Value("${topics.retry}")
//    private String retryTopic;
//    @Value("${topics.dlt}")
//    private String deadLetterTopic;
//    private KafkaProperties properties;
//
//
//    //use to handle msg failures, send failed data to store in DB
//    ConsumerRecordRecoverer consumerRecordRecoverer = (consumerRecord, e) -> {
//        ConsumerRecord<Long, String> record = (ConsumerRecord<Long, String>) consumerRecord;
//        if (e instanceof IllegalArgumentException) {
//            //recovery logic
//            log.info("Inside the recovery with RETRY status");
//            failureRecordService.save(record, e, Status.RETRY);
//
//        } else {
//            //non recoverable logic
//            log.info("Inside the Non recovery with DEAD status");
//            failureRecordService.save(record, e, Status.DEAD);
//        }
//    };
//
//    @Bean
//    @ConditionalOnMissingBean(
//            name = {"kafkaListenerContainerFactory"}
//    )
//    ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory();
//        configurer.configure(factory, (ConsumerFactory) kafkaConsumerFactory.getIfAvailable(() -> {
//            return new DefaultKafkaConsumerFactory(this.properties.buildConsumerProperties());
//        }));
//        // factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        //factory.setConcurrency(3);
//        factory.setCommonErrorHandler(errorHandler());
//        return factory;
//    }
//
//    public DefaultErrorHandler errorHandler() {
//        //retry interval and max attempts for each failed record
//        FixedBackOff fixedBackOff = new FixedBackOff(1000, 2);
//        DefaultErrorHandler errorhandler = new DefaultErrorHandler(
////                publishingRecoverer(),
//                consumerRecordRecoverer
////                fixedBackOff
//        );
//
//        //No error handling, only for selected exceptions
//        var ignoreError = List.of(
//                IllegalArgumentException.class
//        );
//
//        errorhandler.addNotRetryableExceptions();
//
//        ignoreError.forEach(errorhandler::addNotRetryableExceptions);
//
//        //for monitoring the error
//        errorhandler.setRetryListeners((consumerRecord, exception, deliveryAttempt) -> {
//            log.info("failed record in delivery attempt, Exception:{}, deliveryAttempt:{}", exception.getMessage(),
//                    deliveryAttempt);
//        });
//        return errorhandler;
//    }
//
//    //used to handle retry and delete options for msgs,after all retries are exhausted
//    public DeadLetterPublishingRecoverer publishingRecoverer() {
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
//                (r, e) -> {
//                    if (e instanceof IllegalArgumentException) {
//                        return new TopicPartition(retryTopic, r.partition());
//                    } else {
//                        return new TopicPartition(deadLetterTopic, r.partition());
//                    }
//                });
////        CommonErrorHandler errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 2L));
//        return recoverer;
//    }
}
