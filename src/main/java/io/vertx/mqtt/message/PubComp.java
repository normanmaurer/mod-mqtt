package io.vertx.mqtt.message;


public final class PubComp extends MqttMessage {
    private final Integer messageId;
    public PubComp(int messageId) {
        this.messageId = messageId;
    }

    public int messageId() {
        return messageId;
    }
}
