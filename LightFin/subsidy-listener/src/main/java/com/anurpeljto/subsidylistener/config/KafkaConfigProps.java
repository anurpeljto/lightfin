package com.anurpeljto.subsidylistener.config;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KafkaConfigProps {

    private String topic;
}
