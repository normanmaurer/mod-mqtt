package io.vertx.mqtt.message;

public final class Connect extends MqttMessage<Connect> {
    private String protocolName;
    private byte procotolVersion;
    private boolean cleanSessionFlag;
    private boolean willFlag;
    private byte willQos;
    private boolean willRetain;
    private boolean passwordFlag;
    private boolean userFlag;
    private int keepAlive;
    private String username;
    private String password;
    private String clientId;
    private String willtopic;
    private String willMessage;

    public boolean isCleanSessionFlag() {
        return cleanSessionFlag;
    }

    public Connect setCleanSessionFlag(boolean cleanSessionFlag) {
        this.cleanSessionFlag = cleanSessionFlag;
        return this;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public Connect setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public boolean isPasswordFlag() {
        return passwordFlag;
    }

    public Connect setPasswordFlag(boolean passwordFlag) {
        this.passwordFlag = passwordFlag;
        return this;
    }

    public byte getProcotolVersion() {
        return procotolVersion;
    }

    public Connect setProcotolVersion(byte procotolVersion) {
        this.procotolVersion = procotolVersion;
        return this;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public Connect setProtocolName(String protocolName) {
        this.protocolName = protocolName;
        return this;
    }

    public boolean isUserFlag() {
        return userFlag;
    }

    public Connect setUserFlag(boolean userFlag) {
        this.userFlag = userFlag;
        return this;
    }

    public boolean isWillFlag() {
        return willFlag;
    }

    public Connect setWillFlag(boolean willFlag) {
        this.willFlag = willFlag;
        return this;
    }

    public byte getWillQos() {
        return willQos;
    }

    public Connect setWillQos(byte willQos) {
        this.willQos = willQos;
        return this;
    }

    public boolean isWillRetain() {
        return willRetain;
    }

    public Connect setWillRetain(boolean willRetain) {
        this.willRetain = willRetain;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Connect setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Connect setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Connect setClientID(String clientID) {
        this.clientId = clientId;
        return this;
    }

    public String getWillTopic() {
        return willtopic;
    }

    public Connect setWillTopic(String topic) {
        this.willtopic = topic;
        return this;
    }

    public String getWillMessage() {
        return willMessage;
    }

    public Connect setWillMessage(String willMessage) {
        this.willMessage = willMessage;
        return this;
    }

    public static enum ConnectReturnCode {
        CONNECTION_ACCEPTED((byte) 0x00),
        UNNACEPTABLE_PROTOCOL_VERSION((byte) 0x01),
        IDENTIFIER_REJECTED((byte) 0x02),
        SERVER_UNAVAILABLE((byte) 0x03),
        BAD_USERNAME_OR_PASSWORD((byte) 0x04),
        NOT_AUTHORIZED((byte) 0x05);

        private final byte code;
        private ConnectReturnCode(byte code) {
            this.code = code;
        }

        public byte code() {
            return code;
        }

        public static ConnectReturnCode valueOf(byte code) {
            switch (code) {
                case 0x00:
                    return CONNECTION_ACCEPTED;
                case 0x01:
                    return UNNACEPTABLE_PROTOCOL_VERSION;
                case 0x02:
                    return IDENTIFIER_REJECTED;
                case 0x03:
                    return SERVER_UNAVAILABLE;
                case 0x04:
                    return BAD_USERNAME_OR_PASSWORD;
                case 0x05:
                    return NOT_AUTHORIZED;
                default:
                    throw new IllegalArgumentException("Unkown code: "+ code);
            }
        }
    }
}
