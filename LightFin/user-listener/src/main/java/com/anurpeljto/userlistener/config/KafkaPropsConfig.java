package com.anurpeljto.userlistener.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
