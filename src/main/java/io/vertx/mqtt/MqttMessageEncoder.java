package io.vertx.mqtt;

import io.vertx.mqtt.message.ConnAck;
import io.vertx.mqtt.message.Connect;
import io.vertx.mqtt.message.Disconnect;
import io.vertx.mqtt.message.MqttIdBasedMessage;
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
import org.vertx.java.core.buffer.Buffer;

import java.nio.charset.Charset;
import java.util.List;

public final class MqttMessageEncoder {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final Buffer DISCONNECT_ENCODED = encodeMessageTypeOnly(MqttMessage.DISCONNECT);
    private static final Buffer PING_REQ_ENCODED = encodeMessageTypeOnly(MqttMessage.PINGREQ);
    private static final Buffer PING_RESP_ENCODED = encodeMessageTypeOnly(MqttMessage.PINGRESP);

    private MqttMessageEncoder() {
        // utility class
    }

    public static Buffer encode(ConnAck connAck) {
        Buffer buffer = new Buffer(4);
        writeMessageType(buffer, MqttMessage.CONNACK);
        writeRemainingLength(buffer, 2);
        buffer.appendByte((byte) 0);
        buffer.appendByte(connAck.getReturnCode().code());
        return buffer;
    }


    public static Buffer encode(Connect connect) {
        Buffer header = new Buffer(24);
        writeString(header, "MQisdp");
        header.appendByte((byte) 0x03);

        byte flags = 0;
        if (connect.isCleanSessionFlag()) {
            flags |= 0x02;
        }
        if (connect.isWillFlag()) {
            flags |= 0x04;
        }
        if (connect.isWillRetain()) {
            flags |= 0x20;
        }
        if (connect.isPasswordFlag()) {
            flags |= 0x40;
        }
        if (connect.isUserFlag()) {
            flags |= 0x80;
        }
        flags |= connect.getWillQos() << 3;
        header.appendByte(flags);
        header.appendShort((short) connect.getKeepAlive());

        String id = connect.getClientId();
        if (id != null) {
            writeString(header, id);
            if (connect.isWillFlag()) {
                writeString(header, connect.getWillTopic());
                writeString(header, connect.getWillMessage());
            }
            if (connect.isUserFlag()) {
                String username = connect.getUsername();
                if (username != null) {
                    writeString(header, username);

                    String password = connect.getPassword();
                    if (password != null) {
                        writeString(header, password);
                    }
                }
            }
        }

        Buffer buffer = new Buffer();
        writeMessageType(buffer, MqttMessage.CONNECT);
        writeRemainingLength(buffer, header.length());
        buffer.appendBuffer(header);
        return buffer;
    }

    public static Buffer encode(Disconnect disconnect) {
        return DISCONNECT_ENCODED;
    }

    public static Buffer encode(PingReq pingReq) {
        return PING_REQ_ENCODED;
    }

    public static Buffer encode(PingResp pingResp) {
        return PING_RESP_ENCODED;
    }

    public static Buffer encode(PubAck pubAck) {
        Buffer buffer = new Buffer(4);
        writeMessageType(buffer, MqttMessage.PUBACK);
        writeRemainingLength(buffer, 2);
        writeMessageId(buffer, pubAck);
        return buffer;
    }

    public static Buffer encode(PubComp pubComp) {
        Buffer buffer = new Buffer(4);
        writeMessageType(buffer, MqttMessage.PUBCOMP);
        writeRemainingLength(buffer, 2);
        writeMessageId(buffer, pubComp);
        return buffer;
    }

    public static Buffer encode(PubReq pubReq) {
        Buffer buffer = new Buffer(4);
        writeMessageType(buffer, MqttMessage.PUBREC);
        writeRemainingLength(buffer, 2);
        writeMessageId(buffer, pubReq);
        return buffer;
    }

    public static Buffer encode(PubRel pubRel) {
        Buffer buffer = new Buffer(4);
        writeMessageType(buffer, MqttMessage.PUBREL);
        writeRemainingLength(buffer, 2);
        writeMessageId(buffer, pubRel);
        return buffer;
    }

    public static Buffer encode(Publish publish) {
        Buffer header = new Buffer(64);
        writeString(header, publish.getTopicName());

        MqttMessage.Qos qos = publish.getQos();

        switch (qos) {
            case LEAST_ONE:
            case EXACTLY_ONCE:
                writeMessageId(header, publish);
        }
        Buffer payload = publish.getPayload();


        Buffer buffer = new Buffer();
        writeMessageType(buffer, MqttMessage.PUBLISH);
        writeRemainingLength(buffer, header.length() + payload.length());
        buffer.appendBuffer(header);
        buffer.appendBuffer(payload);

        return buffer;
    }


    public static Buffer encode(SubAck subAck) {
        List<MqttMessage.Qos> qoses = subAck.getQoses();
        Buffer buffer = new Buffer(qoses.size() + 3);
        writeMessageType(buffer, MqttMessage.SUBACK);
        writeRemainingLength(buffer, 2 + qoses.size());
        writeMessageId(buffer, subAck);

        for (MqttMessage.Qos qos: qoses) {
            writeQos(buffer, qos);
        }
        return buffer;
    }

    public static Buffer encode(Subscribe subscribe) {
        Buffer header = new Buffer();
        writeMessageId(header, subscribe);
        for (Subscribe.TopicQosPair pair: subscribe.topicQosPairs()) {
            writeString(header, pair.topic());
            writeQos(header, pair.qos());
        }
        Buffer buffer = new Buffer();
        writeMessageTypeWithFlags(buffer, MqttMessage.SUBSCRIBE, subscribe);
        writeRemainingLength(buffer, header.length());
        buffer.appendBuffer(header);
        return buffer;
    }

    public static Buffer encode(UnsubAck unsubAck) {
        Buffer buffer = new Buffer(4);
        writeMessageType(buffer, MqttMessage.UNSUBACK);
        writeRemainingLength(buffer, 2);
        writeMessageId(buffer, unsubAck);
        return buffer;
    }

    public static Buffer encode(Unsubscribe unsubscribe) {
        Buffer header = new Buffer();
        writeMessageId(header, unsubscribe);

        for (String topic: unsubscribe.getTopics()) {
            writeString(header, topic);
        }
        Buffer buffer = new Buffer();
        writeMessageTypeWithFlags(buffer, MqttMessage.SUBSCRIBE, unsubscribe);
        writeRemainingLength(buffer, header.length());
        buffer.appendBuffer(header);
        return buffer;
    }

    private static void writeQos(Buffer buffer, MqttMessage.Qos qos) {
        buffer.appendByte((byte) qos.ordinal());
    }

    private static void writeMessageId(Buffer buffer, MqttIdBasedMessage message) {
        buffer.appendShort((short) message.getMessageId());
    }

    private static Buffer encodeMessageTypeOnly(byte type) {
        Buffer buffer = new Buffer(2);
        writeMessageType(buffer, type);
        buffer.appendByte((byte) 0);
        return buffer;
    }

    private static void writeMessageType(Buffer buffer, byte type) {
        buffer.appendByte((byte) (type << 4));
    }

    private static void writeString(Buffer buffer, String text) {
        byte[] bytes = text.getBytes(CHARSET);
        buffer.appendShort((short) bytes.length);
        buffer.appendBytes(bytes);
    }

    private static void writeRemainingLength(Buffer buffer, int len)  {
        byte digit;
        do {
            digit = (byte) (len % 128);
            len = len / 128;
            // if there are more digits to encode, set the top bit of this digit
            if (len > 0) {
                digit = (byte) (digit | 0x80);
            }
            buffer.appendByte(digit);
        } while (len > 0);
    }

    private static void writeMessageTypeWithFlags(Buffer buffer, byte messageType, MqttMessage message) {
        byte flags = 0;
        if (message.isDup()) {
            flags |= 0x08;
        }
        if (message.isRetain()) {
            flags |= 0x01;
        }

        flags |= ((message.getQos().ordinal() & 0x03) << 1);
        buffer.appendByte((byte) (messageType << 4 | flags));
    }
}
