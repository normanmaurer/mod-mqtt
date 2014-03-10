package io.vertx.mqtt.message;

import java.util.Collections;
import java.util.List;

public final class Subscribe extends MqttIdBasedMessage<Subscribe> {
    private List<TopicQosPair> topics = Collections.emptyList();


    public Subscribe setTopicQosPairs(List<TopicQosPair> topics) {
        this.topics = Collections.unmodifiableList(topics);
        return this;
    }

    public List<TopicQosPair> topicQosPairs() {
        return topics;
    }

    public static final class TopicQosPair {
        private final Qos qos;
        private final String topic;

        public TopicQosPair(Qos qos, String topic) {
            this.qos = qos;
            this.topic = topic;
        }

        public Qos qos() {
            return qos;
        }

        public String topic() {
            return topic;
        }
    }
}
