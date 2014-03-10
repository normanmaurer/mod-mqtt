package io.vertx.mqtt.message;

import java.util.Collections;
import java.util.List;

public final class SubAck extends MqttIdBasedMessage<SubAck> {
    private List<Qos> qoses = Collections.emptyList();

    public SubAck setQoses(List<Qos> qoses) {
        this.qoses = Collections.unmodifiableList(qoses);
        return this;
    }

    public List<Qos> getQoses() {
        return qoses;
    }
}
