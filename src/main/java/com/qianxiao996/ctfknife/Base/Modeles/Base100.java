package com.qianxiao996.ctfknife.Base.Modeles;

public class Base100 {
    /**
     * Base100.decode()
     *
     * @param data base100 text
     * @return plain text
     */
    public static String encode(String data) {
        return new String(encode(data.getBytes()));
    }

    /**
     * Base100.encode()
     *
     * @param data plain text
     * @return base100 text
     */
    public static byte[] encode(byte[] data) {
        byte[] out = new byte[data.length * 4];

        int i = 0;
        for (byte b : data) {
            // python(byte): 0~255
            // java(byte): -128~127
            out[4 * i + 0] = (byte) (240);
            out[4 * i + 1] = (byte) (159);
            out[4 * i + 2] = (byte) (((b & 0xff) + 55) / 64 + 143);
            out[4 * i + 3] = (byte) (((b & 0xff) + 55) % 64 + 128);

            i++;
        }

        return out;
    }

    /**
     * Base100.decode()
     *
     * @param data base100 text
     * @return plain text
     */
    public static String decode(String data) {
        return new String(decode(data.getBytes()));
    }

    /**
     * Base100.decode()
     *
     * @param data base100 text
     * @return plain text
     */
    public static byte[] decode(byte[] data) {
        if (data.length % 4 != 0) {
            return "Length of string should be divisible by 4".getBytes();
        }

        byte tmp = 0;
        byte[] out = new byte[data.length / 4];
        int i = 0;
        for (byte b : data) {
            if (i % 4 == 2) {
                tmp = (byte) (((b - 143) * 64) % 256);
            } else if (i % 4 == 3) {
                out[i / 4] = (byte) ((b - 128 + tmp - 55) & 0xff);
            }
            i++;
        }

        return out;
    }

}