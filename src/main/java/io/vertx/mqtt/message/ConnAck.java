package io.vertx.mqtt.message;


public final class ConnAck extends MqttMessage {

    private final Connect.ConnectReturnCode code;

    public ConnAck(boolean dup, byte qos, boolean retain, Connect.ConnectReturnCode code) {
        super(dup, qos, retain);
        this.code = code;
    }

    public Connect.ConnectReturnCode connectReturnCode() {
        return code;
    }

}
