package dev.modzelewski.iggy.producer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
record MessagesGenerator(IggyProducer iggyProducer) {

  @Scheduled(fixedRate = 100)
  void generateMessages() {
    iggyProducer.sendMessage("Message " + System.currentTimeMillis() + " from producer " + IggyProducer.clientId);
  }

}
