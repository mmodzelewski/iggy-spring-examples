package dev.modzelewski.iggy.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iggy")
public record IggyProperties(String host, int port, String username, String password, String streamName,
                             String topicName) {
}
