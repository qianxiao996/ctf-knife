package com.qianxiao996.ctfknife.Controller.Encrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Encrypt.Controller_Xor.hexStringToByteArray;

public class Utils {
    // 将字节数组转换为十六进制字符串表示，方便查看结果
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
//    public static byte[] Str_To_Bytes(String text, String key_encding){
//        byte[] result = new byte[0];
//        if (Objects.equals(key_encding, "UTF8")){
//            result =text.getBytes(StandardCharsets.UTF_8);
//        }
//        else if (Objects.equals(key_encding, "UTF16")){
//            result =text.getBytes(StandardCharsets.UTF_16);
//        } else if (Objects.equals(key_encding, "UTF16BE")) {
//            result =text.getBytes(StandardCharsets.UTF_16BE);
//        } else if (Objects.equals(key_encding, "UTF16LE")) {
//            result =text.getBytes(StandardCharsets.UTF_16LE);
//        } else if (Objects.equals(key_encding, "Base64")) {
//            try {
//                result = Base64.getDecoder().decode(text);
//            }catch (Exception  e){
//                return  ("Error: "+e.getMessage()).getBytes();
//            }
//        } else if (Objects.equals(key_encding, "Hex")) {
//            result = hexStringToByteArray(text);
//        } else if (Objects.equals(key_encding, "Latin1")) {
//            result =text.getBytes(StandardCharsets.ISO_8859_1);
//        } else if (key_encding.equals("Raw")) {
//            result =text.getBytes();
//        }else{
//            result =text.getBytes();
//        }
//        return result;
//    }
//
}
