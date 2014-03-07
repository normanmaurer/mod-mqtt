package io.vertx.mqtt.message;

public final class Connect extends MqttMessage {
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

    public Connect() {
        super(false, (byte) -1, false);
    }

    public boolean isCleanSessionFlag() {
        return cleanSessionFlag;
    }

    public void setCleanSessionFlag(boolean cleanSessionFlag) {
        this.cleanSessionFlag = cleanSessionFlag;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isPasswordFlag() {
        return passwordFlag;
    }

    public void setPasswordFlag(boolean passwordFlag) {
        this.passwordFlag = passwordFlag;
    }

    public byte getProcotolVersion() {
        return procotolVersion;
    }

    public void setProcotolVersion(byte procotolVersion) {
        this.procotolVersion = procotolVersion;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public boolean isUserFlag() {
        return userFlag;
    }

    public void setUserFlag(boolean userFlag) {
        this.userFlag = userFlag;
    }

    public boolean isWillFlag() {
        return willFlag;
    }

    public void setWillFlag(boolean willFlag) {
        this.willFlag = willFlag;
    }

    public byte getWillQos() {
        return willQos;
    }

    public void setWillQos(byte willQos) {
        this.willQos = willQos;
    }

    public boolean isWillRetain() {
        return willRetain;
    }

    public void setWillRetain(boolean willRetain) {
        this.willRetain = willRetain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientID(String clientID) {
        this.clientId = clientId;
    }

    public String getWillTopic() {
        return willtopic;
    }

    public void setWillTopic(String topic) {
        this.willtopic = topic;
    }

    public String getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(String willMessage) {
        this.willMessage = willMessage;
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
