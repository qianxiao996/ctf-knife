package com.qianxiao996.ctfknife.Encrypt.Modeles;

public class Rot47 {
    public static void main(String[] args){
        String text="asdsd";
        String result = cipherEncryption(text);
        System.out.println(result);
        String result2 = cipherDecryption(result);
        System.out.println(result2);
    }

    public static String cipherDecryption(String message) {
        int key = 47;
        String decrypText = "";
        for (int i = 0; i < message.length(); i++) {
            int temp = (int)message.charAt(i) - key;
            if((int)message.charAt(i) == 32){
                decrypText += " ";
            } else if(temp < 32){
                temp += 94;
                decrypText += (char)temp;
            } else {
                decrypText += (char)temp;
            } // if-else
        } // for
        return decrypText;
    }

    public static String cipherEncryption(String message) {
        int key = 47;
        String encrypText = "";
        for (int i = 0; i < message.length(); i++) {
            int temp = (int)message.charAt(i) + key;
            if((int)message.charAt(i) == 32){
                encrypText += " ";
            } else if(temp > 126){
                temp -= 94;
                encrypText += (char)temp;
            } else {
                encrypText += (char)temp;
            } // if-else
        } // for
        return encrypText;
    }
}