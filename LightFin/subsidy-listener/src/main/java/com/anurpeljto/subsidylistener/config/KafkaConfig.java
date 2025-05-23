package com.anurpeljto.subsidylistener.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    private final KafkaConfigProps kafkaConfigProps;

    public KafkaConfig(final KafkaConfigProps kafkaConfigProps){
        this.kafkaConfigProps = kafkaConfigProps;
    }

    @Bean
    public NewTopic getTopic() {
        return TopicBuilder.name(kafkaConfigProps.getTopic())
                .partitions(10)
                .replicas(1)
                .build();
    }
}
