package io.vertx.mqtt.message;

import java.util.Arrays;
import java.util.List;

public final class Subscribe extends MqttMessage {
    private final int messageId;

    private final List<TopicQosPair> topics;
    public Subscribe(boolean dup, byte qos, boolean retain, int messageId, TopicQosPair... topics) {
        this(dup, qos, retain, messageId, Arrays.asList(topics));
    }

    public Subscribe(boolean dup, byte qos, boolean retain, int messageId, List<TopicQosPair> topics) {
        super(dup, qos, retain);
        this.messageId = messageId;
        this.topics = topics;
    }

    public int messageId() {
        return messageId;
    }

    public List<TopicQosPair> topicQosPairs() {
        return topics;
    }

    public static final class TopicQosPair {
        private final byte qos;
        private final String topic;

        public TopicQosPair(byte qos, String topic) {
            this.qos = qos;
            this.topic = topic;
        }

        public byte qos() {
            return qos;
        }

        public String topic() {
            return topic;
        }
    }
}
