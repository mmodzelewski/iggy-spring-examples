package dev.modzelewski.iggy.producer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
record MessagesGenerator(IggyProducer iggyProducer) {

  @Scheduled(fixedDelay = 500)
  void generateMessages() {
    iggyProducer.sendMessage("Message " + System.currentTimeMillis() + " from producer " + UUID.randomUUID());
  }

}
