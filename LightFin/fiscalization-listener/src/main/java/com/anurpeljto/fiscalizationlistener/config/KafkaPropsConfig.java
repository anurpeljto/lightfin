package com.anurpeljto.fiscalizationlistener.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KafkaPropsConfig {

    private String topic;
}
