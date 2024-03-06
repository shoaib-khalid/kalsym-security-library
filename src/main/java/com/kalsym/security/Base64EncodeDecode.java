package com.kalsym.security;

import java.io.IOException;
import java.nio.ByteBuffer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author zeeshan
 */
public class Base64EncodeDecode {

    public static String encodeMessage(String msg) {
        String encodedMsg = "";
        if (null != msg) {
            BASE64Encoder encoder = new BASE64Encoder();
            ByteBuffer buf = ByteBuffer.wrap(msg.getBytes());
            encodedMsg = encoder.encodeBuffer(buf);
        }
        return encodedMsg;
    }

    public static String decodeMessage(String msg) throws IOException {
        String decodedMsg = "";
        if (null != msg) {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(msg);
            decodedMsg = new String(buf);
        }
        return decodedMsg;
    }
}
