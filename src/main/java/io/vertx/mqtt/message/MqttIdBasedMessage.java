package io.vertx.mqtt.message;


public abstract class MqttIdBasedMessage<T extends MqttIdBasedMessage> extends MqttMessage<T> {
    private int messageId;

    public T setMessageId(int messageId) {
        this.messageId = messageId;
        return (T) this;
    }

    public int getMessageId() {
        return messageId;
    }

}
