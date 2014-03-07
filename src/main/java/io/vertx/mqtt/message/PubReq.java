package io.vertx.mqtt.message;

public final class PubReq extends MqttMessage {

    private final Integer messageId;
    public PubReq(int messageId) {
        this.messageId = messageId;
    }

    public int messageId() {
        return messageId;
    }
}
