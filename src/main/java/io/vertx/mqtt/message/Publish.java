package io.vertx.mqtt.message;

import org.vertx.java.core.buffer.Buffer;

public final class Publish extends MqttIdBasedMessage<Publish> {
    private String topicName;
    private Buffer payload;

    public String getTopicName() {
        return topicName;
    }

    public Publish setTopicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public Buffer getPayload() {
        return payload;
    }

    public Publish setPayload(Buffer payload) {
        this.payload = payload;
        return this;
    }
}
