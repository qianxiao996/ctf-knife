package com.qianxiao996.ctfknife.Encode.Modeles;
import java.util.ArrayList;

public class Morse_Decrypt {
    private String pwd;

    public void setpwd(String pwd) {
        this.pwd=pwd;
    }
    private ArrayList<String> strArrayList(){
        String b=pwd;
        //System.out.println(b);
        ArrayList<String> list=new ArrayList<String>();
        String[] c=b.split(" ");
        for (int i = 0; i < c.length; i++) {
            //System.out.println(c[i]);
            String d=c[i];
            list.add(d);
        }
        return list;
    }
    private ArrayList<String> pwdArrayList(){
        ArrayList<String> list=strArrayList();
        //System.out.println(list.get(0));
        ArrayList<String> pwdList=new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String a=list.get(i);
            if (".-".equals(a)) {
                pwdList.add("A");
            }else if ("-...".equals(a)) {
                pwdList.add("B");
            }else if ("-.-.".equals(a)) {
                pwdList.add("C");
            }else if ("-..".equals(a)) {
                pwdList.add("D");
            }else if (".".equals(a)) {
                pwdList.add("E");
            }else if ("..-.".equals(a)) {
                pwdList.add("F");
            }else if ("--.".equals(a)) {
                pwdList.add("G");
            }else if ("....".equals(a)) {
                pwdList.add("H");
            }else if ("..".equals(a)) {
                pwdList.add("I");
            }else if (".---".equals(a)) {
                pwdList.add("J");
            }else if ("-.-".equals(a)) {
                pwdList.add("K");
            }else if (".-..".equals(a)) {
                pwdList.add("L");
            }else if ("--".equals(a)) {
                pwdList.add("M");
            }else if ("-.".equals(a)) {
                pwdList.add("N");
            }else if ("---".equals(a)) {
                pwdList.add("O");
            }else if (".--.".equals(a)) {
                pwdList.add("P");
            }else if ("--.-".equals(a)) {
                pwdList.add("Q");
            }else if (".-.".equals(a)) {
                pwdList.add("R");
            }else if ("...".equals(a)) {
                pwdList.add("S");
            }else if ("-".equals(a)) {
                pwdList.add("T");
            }else if ("..-".equals(a)) {
                pwdList.add("U");
            }else if ("...-".equals(a)) {
                pwdList.add("V");
            }else if (".--".equals(a)) {
                pwdList.add("W");
            }else if ("-..-".equals(a)) {
                pwdList.add("X");
            }else if ("-.--".equals(a)) {
                pwdList.add("Y");
            }else if ("--..".equals(a)) {
                pwdList.add("Z");
            }else if ("-----".equals(a)) {
                pwdList.add("0");
            }else if (".----".equals(a)) {
                pwdList.add("1");
            }else if ("..---".equals(a)) {
                pwdList.add("2");
            }else if ("...--".equals(a)) {
                pwdList.add("3");
            }else if ("....-".equals(a)) {
                pwdList.add("4");
            }else if (".....".equals(a)) {
                pwdList.add("5");
            }else if ("-....".equals(a)) {
                pwdList.add("6");
            }else if ("--...".equals(a)) {
                pwdList.add("7");
            }else if ("---..".equals(a)) {
                pwdList.add("8");
            }else if ("----.".equals(a)) {
                pwdList.add("9");
            }else if (".-.-.-".equals(a)) {//�ַ�
                pwdList.add(".");
            }else if ("---...".equals(a)) {
                pwdList.add(":");
            }else if ("--..--".equals(a)) {
                pwdList.add(",");
            }else if ("-.-.-.".equals(a)) {
                pwdList.add(";");
            }else if ("..--..".equals(a)) {
                pwdList.add("?");
            }else if ("-...-".equals(a)) {
                pwdList.add("=");
            }else if (".----.".equals(a)) {
                pwdList.add("'");
            }else if ("-..-.".equals(a)) {
                pwdList.add("/");
            }else if ("-.-.--".equals(a)) {
                pwdList.add("!");
            }else if ("-....-".equals(a)) {
                pwdList.add("-");
            }else if ("..--.-".equals(a)) {
                pwdList.add("_");
            }else if ("-.--.".equals(a)) {
                pwdList.add("(");
            }else if ("-.--.-".equals(a)) {
                pwdList.add(")");
            }else if ("...-..-".equals(a)) {
                pwdList.add("$");
            }else if ("......".equals(a)) {
                pwdList.add("&");
            }else if (".--.-.".equals(a)) {
                pwdList.add("@");
            }else if ("--..-.-.".equals(a)) {
                pwdList.add("*");
            }else if ("..--.--.".equals(a)) {
                pwdList.add("%");
            }else if ("-.--....".equals(a)) {
                pwdList.add("#");
            }else if ("....----".equals(a)) {
                pwdList.add("~");
            }else if (".-..-.".equals(a)) {
                pwdList.add("\"");
            }else if ("--.-.-.-".equals(a)) {
                pwdList.add("+");
            }else if("........".equals(a)) {
                pwdList.add(" ");
            }
            else {
                pwdList.add("*");
            }
        }
        return pwdList;
    }
    public String getresuilt(){//将集合转换为字符串
        String b="";
        for (int i = 0; i <pwdArrayList().size(); i++) {
            b=b+pwdArrayList().get(i);
        }
        return b;
    }
}