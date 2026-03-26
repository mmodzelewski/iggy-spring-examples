package dev.modzelewski.iggy.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iggy")
public record IggyProperties(String host, int port, String username, String password, String streamName,
                             String topicName, String consumerGroup) {
}
