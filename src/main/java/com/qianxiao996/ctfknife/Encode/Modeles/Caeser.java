package com.qianxiao996.ctfknife.Encode.Modeles;


import java.util.ArrayList;

public class Caeser {
    public char[] texttest;
    public static String jiami(String text) {
        char[] texttest=text.toCharArray();
        ArrayList<String> result = new ArrayList<>();
        for(char cc:texttest) {
            if(cc>='a'&&cc<='z'){
                switch(cc) {
                    case 'x':
                        cc='a';
                        result.add(String.valueOf(cc));
                        continue;
                    case 'y':
                        cc='b';
                        result.add(String.valueOf(cc));
                        continue;
                    case'z':
                        cc='c';
                        result.add(String.valueOf(cc));
                        continue;
                    default:
                        cc=(char) (cc+3);
                        result.add(String.valueOf(cc));
                }
            }
            else
            {
                if(cc>='A'&&cc<='Z'){
                    switch(cc) {
                        case 'X':
                            cc='A';
                            result.add(String.valueOf(cc));
                            continue;
                        case 'Y':
                            cc='B';
                            result.add(String.valueOf(cc));
                            continue;
                        case'Z':
                            cc='C';
                            result.add(String.valueOf(cc));
                            continue;
                        default:
                            cc=(char) (cc+3);
                            result.add(String.valueOf(cc));
                    }
                }else
                {
                    result.add(String.valueOf(cc));
                }
            }
        }
        return  String.join("", result);
    }
    public static String jiemi(String text) {
        ArrayList<String> result = new ArrayList<>();
        char[] texttest=text.toCharArray();
        for(char cc:texttest) {
            if(cc>='a'&&cc<='z') {
                switch(cc) {
                    case 'a':
                        cc='x';
                        result.add(String.valueOf(cc));
                        continue;
                    case 'b':
                        cc='y';
                        result.add(String.valueOf(cc));
                        continue;
                    case'c':
                        cc='z';
                        result.add(String.valueOf(cc));
                        continue;
                    default:
                        cc=(char) (cc-3);
                        result.add(String.valueOf(cc));
                        continue;
                }
            }
            else {
                if(cc>='A'&&cc<='Z') {
                    switch(cc) {
                        case 'A':
                            cc='X';
                            result.add(String.valueOf(cc));
                            continue;
                        case 'B':
                            cc='Y';
                            result.add(String.valueOf(cc));
                            continue;
                        case'C':
                            cc='Z';
                            result.add(String.valueOf(cc));
                            continue;
                        default:
                            cc=(char) (cc-3);
                            result.add(String.valueOf(cc));
                            continue;
                    }
                }else {
                    result.add(String.valueOf(cc));
                }
            }
        }
        return  String.join("", result);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String a = Caeser.jiami("卡塞");
        System.out.println(a);
        String b = Caeser.jiemi(a);
        System.out.println(b);
    }}