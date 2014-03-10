package io.vertx.mqtt.message;

public abstract class MqttMessage<T extends MqttMessage> {

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

    public enum Qos {
        OST_ONE,
        LEAST_ONE,
        EXACTLY_ONCE,
        RESERVED;

        public static Qos valueOf(byte qos) {
            switch (qos) {
                case 0:
                    return OST_ONE;
                case 1:
                    return LEAST_ONE;
                case 2:
                    return EXACTLY_ONCE;
                case 3:
                    return RESERVED;
                default:
                    throw new IllegalArgumentException("Unknown qos of value: " + qos);
            }
        }
    }

    private boolean dup;
    private Qos qos;
    private boolean retain;

    public boolean isDup() {
        return dup;
    }

    public T setDup(boolean dup) {
        this.dup = dup;
        return (T) this;
    }

    public Qos getQos() {
        return qos;
    }

    public T setQos(Qos qos) {
        this.qos = qos;
        return (T) this;
    }

    public boolean isRetain() {
        return retain;
    }

    public T setRetain(boolean retain) {
        this.retain = retain;
        return (T) this;
    }
}
