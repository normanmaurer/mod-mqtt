package io.vertx.mqtt.message;

/**
 * Created by norman on 07.03.14.
 */
public class PubRel extends MqttMessage {
    private final Integer messageId;
    public PubRel(int messageId) {
        this.messageId = messageId;
    }

    public int messageId() {
        return messageId;
    }
}
