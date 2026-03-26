package dev.modzelewski.iggy.consumer;

import org.apache.iggy.Iggy;
import org.apache.iggy.client.blocking.tcp.IggyTcpClient;
import org.apache.iggy.consumergroup.Consumer;
import org.apache.iggy.identifier.ConsumerId;
import org.apache.iggy.identifier.StreamId;
import org.apache.iggy.identifier.TopicId;
import org.apache.iggy.message.PollingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class IggyConsumer {

  private static final Logger log = LoggerFactory.getLogger(IggyConsumer.class);

  private final IggyTcpClient iggyClient;
  private final StreamId streamId;
  private final TopicId topicId;
  private final Consumer consumer;

  IggyConsumer(IggyProperties properties) {
    log.info("Connecting to Iggy at {}:{}", properties.host(), properties.port());
    iggyClient = Iggy.tcpClientBuilder()
        .blocking()
        .host(properties.host())
        .port(properties.port())
        .credentials(properties.username(), properties.password())
        .buildAndLogin();

    streamId = StreamId.of(properties.streamName());
    topicId = TopicId.of(properties.topicName());

    var groupName = properties.consumerGroup();
    var groupId = ConsumerId.of(groupName);
    iggyClient.consumerGroups().getConsumerGroup(streamId, topicId, groupId)
        .ifPresentOrElse(
            (_) -> log.info("Consumer group {} already exists", groupName),
            () -> {
              log.info("Creating consumer group {}", groupName);
              iggyClient.consumerGroups().createConsumerGroup(streamId, topicId, groupName);
            });
    iggyClient.consumerGroups().joinConsumerGroup(streamId, topicId, groupId);
    consumer = Consumer.group(groupId);
  }

  @Scheduled(fixedRate = 500)
  void pollMessages() {
    var polledMessages = iggyClient
        .messages()
        .pollMessages(streamId, topicId, Optional.empty(), consumer, PollingStrategy.next(), 10L, true);

    if (polledMessages.count() > 0) {
      log.info("Polled {} message(s), partition: {}, current offset: {}", polledMessages.count(), polledMessages.partitionId(), polledMessages.currentOffset());
      for (var message : polledMessages.messages()) {
        log.info("Received: {}", new String(message.payload(), StandardCharsets.UTF_8));
      }
    }
  }

}
