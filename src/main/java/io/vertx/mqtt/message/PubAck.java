package io.vertx.mqtt.message;

public final class PubAck extends MqttMessage {
    private final Integer messageId;
    public PubAck(int messageId) {
        this.messageId = messageId;
    }

    public int messageId() {
        return messageId;
    }
}
