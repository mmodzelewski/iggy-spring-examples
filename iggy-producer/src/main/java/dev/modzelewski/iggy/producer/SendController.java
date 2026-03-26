package dev.modzelewski.iggy.producer;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
record SendController(IggyProducer iggyProducer) {

  @RequestMapping
  void send(@RequestBody Message message) {
    iggyProducer.sendMessage(message.message());
  }

  record Message(String message) {
  }

}
