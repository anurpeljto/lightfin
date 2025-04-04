package com.anurpeljto.fiscalizationlistener.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private final KafkaPropsConfig kafkaPropsConfig;

    public KafkaConfig(final KafkaPropsConfig kafkaPropsConfig){
        this.kafkaPropsConfig = kafkaPropsConfig;
    }

    @Bean
    public NewTopic getTopic(){
        return TopicBuilder.name(kafkaPropsConfig.getTopic())
                .partitions(10)
                .replicas(1)
                .build();
    }
}
