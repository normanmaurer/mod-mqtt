package io.vertx.mqtt.message;


public final class ConnAck extends MqttMessage<ConnAck> {

    private Connect.ConnectReturnCode code;

    public ConnAck setReturnCode(Connect.ConnectReturnCode code) {
        this.code = code;
        return this;
    }

    public Connect.ConnectReturnCode getReturnCode() {
        return code;
    }

}
