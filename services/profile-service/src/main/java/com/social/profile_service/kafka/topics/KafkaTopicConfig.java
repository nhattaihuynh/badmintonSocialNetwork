package com.social.profile_service.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic profileCreateTopic() {
        return TopicBuilder.name("profile-create")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
