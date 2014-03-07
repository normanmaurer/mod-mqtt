package io.vertx.mqtt.message;

public class UnsubAck extends MqttMessage {

    private final Integer messageId;
    public UnsubAck(int messageId) {
        this.messageId = messageId;
    }

    public int messageId() {
        return messageId;
    }
}
