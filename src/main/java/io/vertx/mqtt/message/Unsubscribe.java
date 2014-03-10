package io.vertx.mqtt.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class Unsubscribe extends MqttIdBasedMessage<Unsubscribe> {
    private List<String> topics = Collections.emptyList();

    public Unsubscribe setTopics(List<String> topics) {
        this.topics = Collections.unmodifiableList(topics);
        return this;
    }

    public List<String> getTopics() {
        return topics;
    }
}
