package com.qianxiao996.ctfknife.Encrypt.Modeles;

import java.util.ArrayList;
import java.util.List;

public class FangShe {
    public  String  encode(String sourceText, String key1, String key2){
        String letterList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";  // 字母表
        String text = sourceText.trim();

        if (!key1.matches("\\d+") || !key2.matches("\\d+")) {
            return "输入有误! 密钥为数字!";
        }

        int a = Integer.parseInt(key1);
        int b = Integer.parseInt(key2);

        if (gcd(a, 26) != 1) {
            return "输入有误! key1和26必须互素!" + "仿射密码";
        }
        StringBuilder ciphertext = new StringBuilder();
        for (char ch : text.toCharArray()) {  // 遍历明文
            if (Character.isLetter(ch)) {  // 明文是否为字母,如果是,则判断大小写,分别进行加密
                int index = (int)(ch);
                if (Character.isUpperCase(ch)) {
                    ciphertext.append(letterList.charAt((a * (index - 65) + b) % 26));
                } else {
                    String temp = String.valueOf(letterList.charAt((a * (index - 97) + b) % 26));
                    ciphertext.append(temp.toLowerCase());
                }
            } else {
                ciphertext.append(ch);
            }
        }
        return ciphertext.toString();
    }
    //求素数
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    public String  decode(String sourceText,String key1,String key2) {
        try {
            if (0 == Integer.parseInt(key1) || 0 == Integer.parseInt(key2)) {
                return "输入有误! 密钥为数字。";
            }
            if (this.gcd(Integer.parseInt(key1), 26) != 1) {
                List<Integer> key1_list = new ArrayList<>();
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < Integer.parseInt(key1); i++) {
                    if (this.gcd(i, 26) == 1) {
                        key1_list.add(i);
                    }
                }
                for (int z : key1_list) {
                    result.append("key1:").append(z).append("   key2:").append(key2).append("   明文:").append(fangshe_getdecrypt(sourceText, z, Integer.parseInt(key2))).append("\n");
                }
                return "输入有误! key1和26必须互素。以下为爆破key1的结果\n" + result.toString();
            } else {
                return fangshe_getdecrypt(sourceText, Integer.parseInt(key1), Integer.parseInt(key2)).trim();
            }
        } catch (NumberFormatException e) {
            return "解密失败";
        }

    }

    public String fangshe_getdecrypt(String sourceText, int key1, int key2){
        String plaintext = "";
        String letterList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (char ch : sourceText.toCharArray()) {
            if (Character.isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    plaintext += letterList.charAt(getInverse(key1, 26) * (ch - 65 - key2) % 26);
                } else {
                    plaintext += Character.toLowerCase(letterList.charAt(getInverse(key1, 26) * (ch - 97 - key2) % 26));
                }
            } else {
                plaintext += ch;
            }
        }
        return plaintext;
    }
    //求逆元
    private int getInverse(int a, int m) {
        for (int i = 0; i < m; i++) {
            if (1 == (a * i) % m) {
                return i;
            }
        }
        return a;
    }

}
