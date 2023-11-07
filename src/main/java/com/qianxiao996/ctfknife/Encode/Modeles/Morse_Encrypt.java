package com.qianxiao996.ctfknife.Encode.Modeles;
import java.util.ArrayList;


public class Morse_Encrypt {
    private String pwd;

    public void setpwd(String pwd) {
        this.pwd=pwd;
    }

    private ArrayList<String> strArrayList(){//将字符串拆解为集合
        String string=pwd;
        ArrayList<String> list=new ArrayList<String>();
        for (int i = 0; i < string.length(); i++) {
            list.add(String.valueOf(string.charAt(i)));
            //System.out.println(string.charAt(i));
        }
        return list;
    }
    private ArrayList<String> pwdArrayList(){//加密为摩斯密码集合
        ArrayList<String> list=strArrayList();
        //System.out.println(list.get(0));
        ArrayList<String> pwdList=new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String a=list.get(i);
            if ("A".equalsIgnoreCase(a)) {
                pwdList.add(".-");
            }else if ("B".equalsIgnoreCase(a)) {
                pwdList.add("-...");
            }else if ("C".equalsIgnoreCase(a)) {
                pwdList.add("-.-.");
            }else if ("D".equalsIgnoreCase(a)) {
                pwdList.add("-..");
            }else if ("E".equalsIgnoreCase(a)) {
                pwdList.add(".");
            }else if ("F".equalsIgnoreCase(a)) {
                pwdList.add("..-.");
            }else if ("G".equalsIgnoreCase(a)) {//
                pwdList.add("--.");
            }else if ("H".equalsIgnoreCase(a)) {
                pwdList.add("....");
            }else if ("I".equalsIgnoreCase(a)) {
                pwdList.add("..");
            }else if ("J".equalsIgnoreCase(a)) {
                pwdList.add(".---");
            }else if ("K".equalsIgnoreCase(a)) {
                pwdList.add("-.-");
            }else if ("L".equalsIgnoreCase(a)) {
                pwdList.add(".-..");
            }else if ("M".equalsIgnoreCase(a)) {
                pwdList.add("--");
            }else if ("N".equalsIgnoreCase(a)) {
                pwdList.add("-.");
            }else if ("O".equalsIgnoreCase(a)) {
                pwdList.add("---");
            }else if ("P".equalsIgnoreCase(a)) {
                pwdList.add(".--.");
            }else if ("Q".equalsIgnoreCase(a)) {
                pwdList.add("--.-");
            }else if ("R".equalsIgnoreCase(a)) {
                pwdList.add(".-.");
            }else if ("S".equalsIgnoreCase(a)) {
                pwdList.add("...");
            }else if ("T".equalsIgnoreCase(a)) {
                pwdList.add("-");
            }else if ("U".equalsIgnoreCase(a)) {
                pwdList.add("..-");
            }else if ("V".equalsIgnoreCase(a)) {
                pwdList.add("...-");
            }else if ("W".equalsIgnoreCase(a)) {
                pwdList.add(".--");
            }else if ("X".equalsIgnoreCase(a)) {
                pwdList.add("-..-");
            }else if ("Y".equalsIgnoreCase(a)) {
                pwdList.add("-.--");
            }else if ("Z".equalsIgnoreCase(a)) {
                pwdList.add("--..");
            }else if ("0".equalsIgnoreCase(a)) {
                pwdList.add("-----");
            }else if ("1".equalsIgnoreCase(a)) {
                pwdList.add(".----");
            }else if ("2".equalsIgnoreCase(a)) {
                pwdList.add("..---");
            }else if ("3".equalsIgnoreCase(a)) {
                pwdList.add("...--");
            }else if ("4".equalsIgnoreCase(a)) {
                pwdList.add("....-");
            }else if ("5".equalsIgnoreCase(a)) {
                pwdList.add(".....");
            }else if ("6".equalsIgnoreCase(a)) {
                pwdList.add("-....");
            }else if ("7".equalsIgnoreCase(a)) {
                pwdList.add("--...");
            }else if ("8".equalsIgnoreCase(a)) {
                pwdList.add("---..");
            }else if ("9".equalsIgnoreCase(a)) {
                pwdList.add("----.");
            }else if ("9".equalsIgnoreCase(a)) {
                pwdList.add("----.");
            }else if (".".equalsIgnoreCase(a)) {//����
                pwdList.add(".-.-.-");
            }else if (":".equalsIgnoreCase(a)) {
                pwdList.add("---...");
            }else if (",".equalsIgnoreCase(a)) {
                pwdList.add("--..--");
            }else if (";".equalsIgnoreCase(a)) {
                pwdList.add("-.-.-.");
            }else if ("?".equalsIgnoreCase(a)) {
                pwdList.add("..--..");
            }else if ("=".equalsIgnoreCase(a)) {
                pwdList.add("-...-");
            }else if ("'".equalsIgnoreCase(a)) {
                pwdList.add(".----.");
            }else if ("/".equalsIgnoreCase(a)) {
                pwdList.add("-..-.");
            }else if ("!".equalsIgnoreCase(a)) {
                pwdList.add("-.-.--");
            }else if ("-".equalsIgnoreCase(a)) {
                pwdList.add("-....-");
            }else if ("_".equalsIgnoreCase(a)) {
                pwdList.add("..--.-");
            }else if ("(".equalsIgnoreCase(a)) {
                pwdList.add("-.--.");
            }else if (")".equalsIgnoreCase(a)) {
                pwdList.add("-.--.-");
            }else if ("$".equalsIgnoreCase(a)) {
                pwdList.add("...-..-");
            }else if ("&".equalsIgnoreCase(a)) {
                pwdList.add("......");
            }else if ("@".equalsIgnoreCase(a)) {
                pwdList.add(".--.-.");
            }else if ("*".equalsIgnoreCase(a)) {
                pwdList.add("--..-.-.");
            }else if ("%".equalsIgnoreCase(a)) {
                pwdList.add("..--.--.");
            }else if ("#".equalsIgnoreCase(a)) {
                pwdList.add("-.--....");
            }else if ("~".equalsIgnoreCase(a)) {
                pwdList.add("....----");
            }else if ("\"".equalsIgnoreCase(a)) {
                pwdList.add(".-..-.");
            }else if ("+".equalsIgnoreCase(a)) {
                pwdList.add("--.-.-.-");
            }else if (" ".equals(a)) {
                pwdList.add("........");
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
            b=b+pwdArrayList().get(i)+" ";
        }
        return b;
    }

}