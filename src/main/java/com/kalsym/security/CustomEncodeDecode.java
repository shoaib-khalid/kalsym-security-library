package com.kalsym.security;

import java.io.UnsupportedEncodingException;

/**
 * Encodes the string into base64 and after adding add some salt for security
 * reasons it removes salt from encoded string and decodes encoded string back
 * to original string
 *
 * @author zeeshan
 */
public class CustomEncodeDecode {

//    private static final String codes = "aybzAcwBdCefX0Y5Z1klEmDi2jnF3G4H6IJ7KrLVMqNxOhP8QRoptuS9TsUgvW+/=";
    private static final String codes = "ABC0123456DEFYZabcdefghijklmnopqrGHIJKLMNOPQRSTUVWXstuvwxyz789+/=";

    /**
     * it encodes the string to base64
     *
     * @param msg
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws java.lang.Exception
     */
    public static String encodeMessage(String msg) throws UnsupportedEncodingException, Exception {
        try {
            if (msg != null) {
                byte[] in = null;
                try {
                    in = msg.getBytes("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    //Logger.getLogger(CustomEncodeDecode.class.getName()).log(Level.SEVERE, null, ex);
                    throw new UnsupportedEncodingException("" + ex);
                    //return null;
                }
                try {
                    StringBuffer out = new StringBuffer((in.length * 4) / 3);
                    int b;
                    for (int i = 0; i < in.length; i += 3) {
                        b = (in[i] & 0xFC) >> 2;
                        out.append(codes.charAt(b));
                        b = (in[i] & 0x03) << 4;
                        if (i + 1 < in.length) {
                            b |= (in[i + 1] & 0xF0) >> 4;
                            out.append(codes.charAt(b));
                            b = (in[i + 1] & 0x0F) << 2;
                            if (i + 2 < in.length) {
                                b |= (in[i + 2] & 0xC0) >> 6;
                                out.append(codes.charAt(b));
                                b = in[i + 2] & 0x3F;
                                out.append(codes.charAt(b));
                            } else {
                                out.append(codes.charAt(b));
                                out.append('=');
                            }
                        } else {
                            out.append(codes.charAt(b));
                            out.append("==");
                        }
                    }
                    String beforeSalt = out.toString();

                    if (beforeSalt != null) {
                        String saltAddedString = addSalt(out.toString());
                        return saltAddedString;
                    }
                    return null;
                } catch (Exception ex) {
                    //Logger.getLogger(CustomEncodeDecode.class.getName()).log(Level.SEVERE, null, ex);
                    throw new Exception(ex);
                    //return null;
                }
            }
            return null;
        } catch (Exception ex) {
            //Logger.getLogger(CustomEncodeDecode.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
            //return null;
        }
    }

    /**
     * Decodes encoded string back to original string
     *
     * @param encodedMsg
     * @return
     * @throws java.lang.Exception
     */
    public static String decodeEncodedMessage(String encodedMsg) throws Exception {
        try {
            if (encodedMsg != null) {
                encodedMsg = removeSalt(encodedMsg);
            }
            if (encodedMsg != null) {
                if (encodedMsg.length() % 4 != 0) {
                    System.err.println("Invalid base64 encodedMsg");
                    return null;
                }
                byte decoded[] = new byte[((encodedMsg.length() * 3) / 4) - (encodedMsg.indexOf('=') > 0 ? (encodedMsg.length() - encodedMsg.indexOf('=')) : 0)];
                char[] inChars = encodedMsg.toCharArray();
                int j = 0;
                int b[] = new int[4];
                for (int i = 0; i < inChars.length; i += 4) {
                    // This could be made faster (but more complicated) by precomputing these index locations
                    b[0] = codes.indexOf(inChars[i]);
                    b[1] = codes.indexOf(inChars[i + 1]);
                    b[2] = codes.indexOf(inChars[i + 2]);
                    b[3] = codes.indexOf(inChars[i + 3]);
                    decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
                    if (b[2] < 64) {
                        decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
                        if (b[3] < 64) {
                            decoded[j++] = (byte) ((b[2] << 6) | b[3]);
                        }
                    }
                }

                String decodedMsg = null;
                try {
                    decodedMsg = new String(decoded, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    //Logger.getLogger(CustomEncodeDecode.class.getName()).log(Level.SEVERE, null, ex);
                    decodedMsg = null;
                }
                return decodedMsg;
            }
            return null;
        } catch (Exception ex) {
            //Logger.getLogger(CustomEncodeDecode.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex);
            //return null;
        }
    }

    /**
     * Adds salt into encoded string
     *
     * @param msg
     * @return
     */
    private static String addSalt(String msg) {
        char[] charArray = msg.toCharArray();
        int i = 4;
        while (i < charArray.length + 4) {
            if (i % 3 == 0) {
                //int number = (int) charArray[i - 4];
                //charArray[i - 4] = (char) ++number;
                int number = codes.indexOf(charArray[i - 4]);
                if (number < codes.length() - 1) {
                    charArray[i - 4] = codes.charAt(++number);
                }
            }
            i++;
        }
        msg = String.valueOf(charArray);
        return msg;
    }

    /**
     * Removes salt from encoded string
     *
     * @param msg
     * @return
     */
    private static String removeSalt(String msg) {
        char[] charArray = msg.toCharArray();
        int i = 4;
        while (i < charArray.length + 4) {
            if (i % 3 == 0) {
                //int number = (int) charArray[i - 4];
                //charArray[i - 4] = (char) ++number;
                int number = codes.indexOf(charArray[i - 4]);
                if (number < codes.length() - 1) {
                    charArray[i - 4] = codes.charAt(--number);
                }
            }
            i++;
        }
        msg = String.valueOf(charArray);
        return msg;
    }
}
