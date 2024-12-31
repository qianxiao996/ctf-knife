package com.qianxiao996.ctfknife.Encrypt.Modeles;
public class Rot18 {
    public static void main(String[] args){
        String text="asdsd";
        String result = cipherEncryption(text);
        System.out.println(result);
        String result2 = cipherDecryption(result);
        System.out.println(result2);
    }

    public static String cipherDecryption(String message) {
        String rot5 = "5678901234";
        String zeroToNine = "0123456789";
        int rot13Key = 13;
        message = message.toUpperCase();

        String decrypText = "";
        for (int i = 0; i < message.length(); i++) {
            String temp = message.charAt(i) + "";
            if((int)message.charAt(i) == 32){
                decrypText += " ";
            } else if (temp.matches("[\\s\\d]+")){
                // ROT5
                for (int j = 0; j < zeroToNine.length(); j++) {
                    if(message.charAt(i) == rot5.charAt(j)){
                        decrypText += zeroToNine.charAt(j);
                    }
                } // inner for
            } else if(temp.matches("[\\s\\w]+")) {
                // ROT13
                int chTemp = (int)temp.charAt(0) - rot13Key;
                if (chTemp < 65){
                    chTemp += 26;
                    decrypText += (char)chTemp;
                } else {
                    decrypText += (char)chTemp;
                }
            } // if-else
        } // for
        return decrypText;
    }

    public static String cipherEncryption(String message) {
        String rot5 = "5678901234";
        String zeroToNine = "0123456789";
        int rot13Key = 13;
        message = message.toUpperCase();
        String encrypText = "";
        for (int i = 0; i < message.length(); i++) {
            String temp = message.charAt(i) + "";
            if((int)message.charAt(i) == 32){
                encrypText += " ";
            } else if (temp.matches("[\\s\\d]+")){
                // ROT5
                for (int j = 0; j < zeroToNine.length(); j++) {
                    if(message.charAt(i) == zeroToNine.charAt(j)){
                        encrypText += rot5.charAt(j);
                    }
                } // inner for
            } else if(temp.matches("[\\s\\w]+")) {
                // ROT13
                int chTemp = (int)temp.charAt(0) + rot13Key;
                if (chTemp > 90){
                    chTemp -= 26;
                    encrypText += (char)chTemp;
                } else {
                    encrypText += (char)chTemp;
                }
            } // if-else
        } // for
        return encrypText;
    }
}