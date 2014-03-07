package io.vertx.mqtt.message;

public abstract class MqttMessage {

    public static final byte CONNECT = 1;
    public static final byte CONNACK = 2;
    public static final byte PUBLISH = 3;
    public static final byte PUBACK = 4;
    public static final byte PUBREC = 5;
    public static final byte PUBREL = 6;
    public static final byte PUBCOMP = 7;
    public static final byte SUBSCRIBE = 8;
    public static final byte SUBACK = 9;
    public static final byte UNSUBSCRIBE = 10;
    public static final byte UNSUBACK = 11;
    public static final byte PINGREQ = 12;
    public static final byte PINGRESP = 13;
    public static final byte DISCONNECT = 14;

    private final boolean dup;
    private final byte qos;
    private final boolean retain;

    protected MqttMessage(boolean dup, byte qos,boolean retain) {
        this.dup = dup;
        this.qos = qos;
        this.retain = retain;
    }

    protected MqttMessage() {
        this(false, (byte) -1, false);
    }

    public boolean isDup() {
        return dup;
    }

    public byte qos() {
        return qos;
    }

    public boolean isRetain() {
        return retain;
    }
}
