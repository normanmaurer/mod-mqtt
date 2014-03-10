package io.vertx.mqtt;

import io.vertx.mqtt.message.ConnAck;
import io.vertx.mqtt.message.Connect;
import io.vertx.mqtt.message.Disconnect;
import io.vertx.mqtt.message.MqttMessage;
import io.vertx.mqtt.message.PingReq;
import io.vertx.mqtt.message.PingResp;
import io.vertx.mqtt.message.PubAck;
import io.vertx.mqtt.message.PubComp;
import io.vertx.mqtt.message.PubRel;
import io.vertx.mqtt.message.PubReq;
import io.vertx.mqtt.message.Publish;
import io.vertx.mqtt.message.SubAck;
import io.vertx.mqtt.message.Subscribe;
import io.vertx.mqtt.message.UnsubAck;
import io.vertx.mqtt.message.Unsubscribe;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;

import java.util.ArrayList;
import java.util.List;

public final class MqttProtocolParser implements Handler<Buffer> {
    private static final Disconnect DISCONNECT = new Disconnect();
    private static final PingReq PING_REQ = new PingReq();
    private static final PingResp PING_RESP = new PingResp();

    private final Handler<MqttMessage> handler;
    private int remainingLength;
    private byte messageType;
    private boolean dupFlag;
    private byte qosLevel;
    private boolean retainFlag;
    private Buffer received;
    private int offset;

    MqttProtocolParser(Handler<MqttMessage> handler) {
        this.handler = handler;
    }

    @Override
    public void handle(Buffer buffer) {
        appendData(buffer);
        parse();
    }

    private void parse() {
        int read = 0;
        int len = received.length() - offset;
        while (read < len) {
            if (len < 2) {
                break;
            } else {
                read += readHeader(received);
                if (received.length() - read >= remainingLength) {
                    // create new message
                    read += remainingLength;
                    MqttMessage.Qos qos = MqttMessage.Qos.valueOf(qosLevel);
                    MqttMessage message = createMessage(
                            received, offset, messageType, remainingLength, qos);
                    message.setDup(dupFlag);
                    message.setQos(qos);
                    message.setRetain(retainFlag);
                    handler.handle(message);
                } else {
                    break;
                }
            }
        }

        if (read == len) {
            //Nothing left
            offset = 0;
            received = null;
        } else {
            received = received.getBuffer(offset, received.length());
            offset = received.length();
        }
    }

    private MqttMessage createMessage(
            Buffer buffer, int offset, byte messageType, int remainingLength, MqttMessage.Qos qos) {
        switch (messageType) {
            case MqttMessage.CONNACK:
                // first byte is skipped as it is reserved
                byte code = buffer.getByte(++offset);
                ConnAck ack = new ConnAck();
                return ack.setReturnCode(Connect.ConnectReturnCode.valueOf(code));
            case MqttMessage.CONNECT:
                return createConnect(buffer, offset, remainingLength);
            case MqttMessage.DISCONNECT:
                return DISCONNECT;
            case MqttMessage.PINGREQ:
                return PING_REQ;
            case MqttMessage.PINGRESP:
                return PING_RESP;
            case MqttMessage.PUBACK:
                int pubId = buffer.getShort(offset) & 0xffff;
                PubAck pubAck = new PubAck();
                return pubAck.setMessageId(pubId);
            case MqttMessage.PUBCOMP:
                int compId = buffer.getShort(offset) & 0xffff;
                PubComp comp = new PubComp();
                return comp.setMessageId(compId);
            case MqttMessage.PUBLISH:
                int oldOffset = offset;
                Publish publish = new Publish();
                // read topic;
                int len = buffer.getShort(offset) & 0xffff;
                offset += 2;
                String topic = buffer.getString(offset, len, "UTF-8");
                offset += len;
                publish.setTopicName(topic);

                switch (qos) {
                    case LEAST_ONE:
                    case EXACTLY_ONCE:
                        publish.setMessageId(buffer.getShort(offset) & 0xffff);
                        offset += 2;
                }

                publish.setPayload(buffer.getBuffer(offset, oldOffset + remainingLength));
                return publish;
            case MqttMessage.PUBREC:
                int reqId = buffer.getShort(offset) & 0xffff;
                return new PubReq(reqId);
            case MqttMessage.PUBREL:
                int relId = buffer.getShort(offset) & 0xffff;
                PubRel rel = new PubRel();
                return rel.setMessageId(relId);
            case MqttMessage.SUBACK:
                SubAck subAck = new SubAck();
                subAck.setMessageId(buffer.getShort(offset) & 0xffff);
                offset += 2;
                remainingLength -= 2;

                List<MqttMessage.Qos> qoses = new ArrayList<>(remainingLength);
                for (int i = 0; i < remainingLength; i++) {
                    qoses.add(MqttMessage.Qos.valueOf(buffer.getByte(offset++)));
                }
                subAck.setQoses(qoses);
                return subAck;
            case MqttMessage.SUBSCRIBE:
                // TODO: Maybe check for valid QOS type ?
                int subId = buffer.getShort(offset) & 0xffff;
                List<Subscribe.TopicQosPair> pairs = new ArrayList<>();
                int start = offset + 2;
                int end = offset + remainingLength;
                while (start < end) {
                    // read up the topics;
                    int l = buffer.getShort(start) & 0xffff;
                    start += 2;
                    String t = buffer.getString(start, l, "UTF-8");
                    start += l;
                    byte topicQos = (byte) (buffer.getByte(start++) & 0x03);
                    pairs.add(new Subscribe.TopicQosPair(MqttMessage.Qos.valueOf(topicQos), t));
                }

                Subscribe subscribe = new Subscribe();
                return subscribe.setMessageId(subId).setTopicQosPairs(pairs);
            case MqttMessage.UNSUBACK:
                int unsubAckId = buffer.getShort(offset) & 0xffff;
                UnsubAck unsubAck = new UnsubAck();
                return unsubAck.setMessageId(unsubAckId);
            case MqttMessage.UNSUBSCRIBE:
                // TODO: Maybe check for valid QOS type ?
                int unsubId = buffer.getShort(offset) & 0xffff;
                List<String> topics = new ArrayList<>();
                int s = offset + 2;
                int e = offset + remainingLength;
                while (s < e) {
                    // read up the topics;
                    int l = buffer.getShort(s) & 0xffff;
                    s += 2;
                    topics.add(buffer.getString(s, l, "UTF-8"));
                    s += l;
                }
                Unsubscribe unsubscribe = new Unsubscribe();
                return unsubscribe.setMessageId(unsubId).setTopics(topics);

        }
        throw new IllegalArgumentException("Unknown messageType: " + messageType);
    }

    private Connect createConnect(Buffer buffer, int offset , int remainingLength) {
        Connect connect = new Connect();
        int oldOffset = offset;
        offset += 2; // size is 0x06  so skip the first 2 bytes
        String protoName = buffer.getString(offset, offset + 6, "UTF-8");
        offset += 6;

        connect.setProtocolName(protoName);

        //ProtocolVersion 1 byte
        connect.setProcotolVersion(buffer.getByte(offset++));

        //Connection flag 1 byte
        byte connectFlags = buffer.getByte(offset++);
        boolean cleanSessionFlag = ((connectFlags & 0x02) >> 1) == 1;
        boolean willFlag = ((connectFlags & 0x04) >> 2) == 1;
        byte willQos = (byte) ((connectFlags & 0x18) >> 3);
        if (willQos > 2) {
            throw new IllegalArgumentException("QOS must be in range 0..2: " + willQos);
        }
        boolean willRetain = ((connectFlags & 0x20) >> 5) == 1;
        boolean passwordFlag = ((connectFlags & 0x40) >> 6) == 1;
        boolean userFlag = ((connectFlags & 0x80) >> 7) == 1;
        connect.setCleanSessionFlag(cleanSessionFlag);
        connect.setWillFlag(willFlag);
        connect.setWillQos(willQos);
        connect.setWillRetain(willRetain);
        connect.setPasswordFlag(passwordFlag);
        connect.setUserFlag(userFlag);

        // keep alive is an 1 unsigned short
        int keepAlive = buffer.getShort(offset) & 0xffff;
        offset +=2;
        connect.setKeepAlive(keepAlive);

        if (remainingLength == offset - oldOffset) {
            // everything consumed
            return connect;
        }

        // read the client id
        int len = buffer.getShort(offset) & 0xffff;
        offset += 2;
        String clientID = buffer.getString(offset, len, "UTF-8");
        connect.setClientID(clientID);

        if (willFlag) {
            int willTopicLen = buffer.getShort(offset) & 0xffff;
            offset += 2;
            String willTopic = buffer.getString(offset, willTopicLen, "UTF-8");
            offset += willTopicLen;

            connect.setWillTopic(willTopic);
        }

        if (willFlag) {
            int willMessageLen = buffer.getShort(offset) & 0xffff;
            offset += 2;
            String willMessage = buffer.getString(offset, willMessageLen, "UTF-8");
            offset += willMessageLen;

            connect.setWillMessage(willMessage);
        }

        if (userFlag) {
            int userNameLen = buffer.getShort(offset) & 0xffff;
            offset += 2;
            String userName = buffer.getString(offset, userNameLen, "UTF-8");
            offset += userNameLen;
            connect.setUsername(userName);
        }

        if (passwordFlag) {
            int passwordLen = buffer.getShort(offset) & 0xffff;
            offset += 2;
            String password = buffer.getString(offset, passwordLen, "UTF-8");
            offset += passwordLen;
            connect.setPassword(password);
        }
        return connect;
    }

    private void appendData(Buffer buffer) {
        if (received == null) {
            received = buffer;
        } else {
            received.appendBuffer(buffer);
        }
    }

    private int readHeader(Buffer buffer) {
        byte firstByte = buffer.getByte(0);
        messageType = (byte) ((firstByte & 0x00F0) >> 4);
        dupFlag = ((byte) ((firstByte & 0x0008) >> 3) == 1);
        qosLevel = (byte) ((firstByte & 0x0006) >> 1);
        retainFlag = ((byte) (firstByte & 0x0001) == 1);
        remainingLength = decodeRemainingLength(buffer, 1);
        return 2;
    }

    private static int decodeRemainingLength(Buffer in, int offset) {
        int multiplier = 1;
        int value = 0;
        byte digit;
        do {
            if (in.length() == offset) {
                // everything consumed :/
                return -1;
            }
            digit = in.getByte(offset++);
            value += (digit & 0x7f) * multiplier;
            multiplier *= 128;
        } while ((digit & 0x80) != 0);
        return value;
    }
}
