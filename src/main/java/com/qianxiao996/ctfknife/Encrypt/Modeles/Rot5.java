package com.qianxiao996.ctfknife.Encrypt.Modeles;

import java.util.ArrayList;

public class Rot5 {
    public static void main(String[] args){
        String text="asdsd";
        String result = cipherEncryption(text);
        System.out.println(result);
        String result2 = cipherDecryption(result);
        System.out.println(result2);
    }

    public static String cipherDecryption(String text) {
        char[] char_list = text.toCharArray();
        ArrayList<String> result = new ArrayList<>();
        for(char c:char_list) {
            if ((c >= '0') && (c <= '9'))
            {
                c = (char) ((((c - '0') + 5) % 10) + '0');
//                System.out.println(c);
                result.add(String.valueOf(c));
            }else{
                result.add(String.valueOf(c));
            }

        }
        return String.join("", result);
    }
    public static String rot5 = "5678901234";
    public static String zeroToNine = "0123456789";
    public static String cipherEncryption(String message) {
        //checking if input is integer or not
        // we'll be using regular expressions
        if(!message.matches("[\\s\\d]+")){
            // \\d = integers
            // \\s = white space
            // [] a set, with logical OR
            // + = one or more times
            return "输入的不是数字！";
        }
        StringBuilder decrypText = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if(message.charAt(i) == 32){
                decrypText.append(" ");
            } else {
                for (int j = 0; j < zeroToNine.length(); j++) {
                    //simple substitution
                    if(message.charAt(i) == rot5.charAt(j)){
                        decrypText.append(zeroToNine.charAt(j));
                    }
                } // inner for
            } // if else
        } // for
        return decrypText.toString();
    }
}