package com.qianxiao996.ctfknife.Encode.Modeles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 * @ClassName: Base64_Custom
 * @Description: 用于编码请求参数
 * @author 浅笑996
 * @date 2023-08-15 上午10:52:1
 */
public class Base64_Custom {
    //对照表
    private static String base64hash = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static void checkSecurity(){
        if(base64hash == null || base64hash.length() != 64){
            throw new RuntimeException("was initialize failed!");
        }
    }
    /**
     *
     * @Title: encode
     * @Description: 编码
     * @param @param src
     * @param @return
     * @return String
     * @throws
     */
    public static String encode(String CUSTOM_CHARS,String src){

        base64hash = CUSTOM_CHARS;
        byte[] data = src.getBytes();
        char[] legalChars = base64hash.toCharArray();
        int start = 0;
        int len = data.length;
        StringBuilder buf = new StringBuilder(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }
    /**
     *
     * @Title: decode
     * @Description: 解码
     * @param @param src
     * @param @return
     * @return String
     * @throws
     */
    public static String decode(String CUSTOM_CHARS,String src){
        base64hash = CUSTOM_CHARS;
        if(Objects.equals(src, "")){
            return "";
        }
        checkSecurity();
        byte temp = 0;
        String result = "";
        for(int i=0;i<src.length();i++){
            temp = (byte) base64hash.indexOf(src.charAt(i));
            if(temp==-1){
                result+="000000";
            }else{
                String t = Integer.toBinaryString(temp);
                if(t.length()==7){
                    t = t.substring(1);
                }else if(t.length()==8){
                    t = t.substring(2);
                }
                while(t.length()<6){
                    t = "0"+t;
                }
                result+=t;
            }
        }
        while(result.endsWith("00000000")){
            result = result.substring(0,result.length()-8);
        }
        byte[] bytes = new byte[result.length()/8];
        for(int i=0;i<bytes.length;i++){
            bytes[i]= Integer.valueOf(result.substring(i*8,(i+1)*8),2).byteValue();
        }
        return new String(bytes);
    }
//    public void setBase64hash(String base64hash) {
//        Security.base64hash = base64hash;
//    }
    /**
     *
     * @Title: randomTable
     * @Description: 生成随机对照表
     * @param @return
     * @return String
     * @throws
     */
    public static String randomTable(){
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-/";
        ArrayList<Character> list = new ArrayList<Character>();
        for(int i=0;i<base.length();i++){
            list.add(base.charAt(i));
        }
        Collections.shuffle(list);
        base = "";
        for(Character ch:list){
            base += ch;
        }
        return base;
    }
}