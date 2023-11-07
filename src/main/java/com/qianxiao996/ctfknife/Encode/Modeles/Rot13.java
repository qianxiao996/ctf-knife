package com.qianxiao996.ctfknife.Encode.Modeles;

public class Rot13 {
    public static void main(String[] args){
        String text="asdsd";
        String result = cipherEncryption(text);
        System.out.println(result);
        String result2 = cipherDecryption(result);
        System.out.println(result2);
    }

    public static String cipherDecryption(String text) {
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String result = "";
        for (int a = 0; a < text.length(); a++) {
            String ch = text.substring(a, a + 1);
            int l = lower.indexOf(ch);
            int u = upper.indexOf(ch);
            if (l != -1) {
                result += lower.substring((l + 13) % 26, (l + 14) % 26 != 0 ? (l + 14) % 26 : l + 14);
            } else if (u != -1) {
                result += upper.substring((u + 13) % 26, (u + 14) % 26 != 0 ? (u + 14) % 26 : u + 14);
            } else {
                result += ch;
            }
        }
        return result;
    }

    public static String cipherEncryption(String text) {

        StringBuilder bld = new StringBuilder(text.length());
        for(char c : text.toCharArray())
            bld.append(rot13(c));
        return bld.toString();
    }
    private static char rot13(char c)
    {
        if((c >= 'A') && (c <= 'Z'))
            c = (char) ((((c - 'A') + 13) % 26) + 'A');
        if((c >= 'a') && (c <= 'z'))
            c = (char) ((((c - 'a') + 13) % 26) + 'a');
        return c;
    }
}