package io.vertx.mqtt.message;

import java.util.Arrays;
import java.util.List;


public final class Unsubscribe extends MqttMessage {
    private final int messageId;
    private final List<String> topics;
    public Unsubscribe(boolean dup, byte qos, boolean retain, int messageId, String... topics) {
        this(dup, qos, retain, messageId, Arrays.asList(topics));
    }

    public Unsubscribe(boolean dup, byte qos, boolean retain, int messageId, List<String> topics) {
        super(dup, qos, retain);
        this.messageId = messageId;
        this.topics = topics;
    }

    public int messageId() {
        return messageId;
    }

    public List<String> topics() {
        return topics;
    }
}
