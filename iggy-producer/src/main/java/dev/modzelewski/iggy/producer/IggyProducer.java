package dev.modzelewski.iggy.producer;

import org.apache.iggy.Iggy;
import org.apache.iggy.client.blocking.tcp.IggyTcpClient;
import org.apache.iggy.identifier.StreamId;
import org.apache.iggy.identifier.TopicId;
import org.apache.iggy.message.Message;
import org.apache.iggy.message.Partitioning;
import org.apache.iggy.topic.CompressionAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class IggyProducer {

  private static final Logger log = LoggerFactory.getLogger(IggyProducer.class);
  private static final UUID clientId = UUID.randomUUID();

  private final IggyTcpClient iggyClient;
  private final StreamId streamId;
  private final TopicId messagesTopicId;
  private final TopicId heartbeatsTopicId = TopicId.of("heartbeats");

  IggyProducer(IggyProperties iggyProperties) {
    log.info("Connecting to Iggy at {}:{}, client id: {}", iggyProperties.host(), iggyProperties.port(), clientId);
    iggyClient = Iggy.tcpClientBuilder()
        .blocking()
        .host(iggyProperties.host())
        .port(iggyProperties.port())
        .credentials(iggyProperties.username(), iggyProperties.password())
        .buildAndLogin();

    streamId = StreamId.of(iggyProperties.streamName());
    messagesTopicId = TopicId.of(iggyProperties.topicName());
    iggyClient
        .streams()
        .getStream(streamId)
        .ifPresentOrElse((_) -> log.info("Stream {} already exists", streamId.getName()), () -> {
              log.info("Creating stream {}", streamId.getName());
              iggyClient.streams().createStream(streamId.getName());
              iggyClient
                  .topics()
                  .createTopic(streamId, 1L, CompressionAlgorithm.None, BigInteger.ZERO, BigInteger.ZERO, Optional.empty(), heartbeatsTopicId.getName());
              iggyClient
                  .topics()
                  .createTopic(streamId, 1L, CompressionAlgorithm.None, BigInteger.ZERO, BigInteger.ZERO, Optional.empty(), messagesTopicId.getName());
            }
        );
  }

  public void sendMessage(String message) {
    iggyClient
        .messages()
        .sendMessages(streamId, messagesTopicId, Partitioning.balanced(), List.of(Message.of(message)));
  }

  @Scheduled(fixedDelay = 5000)
  void sendHeartbeat() {
    iggyClient
        .messages()
        .sendMessages(streamId, heartbeatsTopicId, Partitioning.balanced(), List.of(Message.of(clientId.toString())));
  }

}
