package com.anurpeljto.fiscalizationlistener.config;

import lombok.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KafkaPropsConfig {

    private String topic;
}
