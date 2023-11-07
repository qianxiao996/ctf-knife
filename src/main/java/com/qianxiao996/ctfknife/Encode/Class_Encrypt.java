package com.qianxiao996.ctfknife.Encode;
import com.qianxiao996.ctfknife.Encode.Modeles.*;
import javafx.scene.control.TextArea;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Class_Encrypt extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String encoding;

    private Boolean is_line;

    //临时变量
    public  String temp_key_1 = "";
    private String temp_key_2="";

    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_").replace(".", "_");
        this.souce_text = souce_text.trim();
        this.encoding = encoding;
        this.is_line =is_line;
    }
    public void setOne(String temp_key_1) {
        this.temp_key_1 = temp_key_1;
    }
    public void setTwo(String temp_key_1,String temp_key_2) {
        this.temp_key_1 = temp_key_1;
        this.temp_key_2 = temp_key_2;
    }

    public void run()  {
        //得到所有源文本换列表
        List<String> all_souce_text = new ArrayList<>();
        if(is_line==Boolean.TRUE){
            //换行分割
            String[] text_list = souce_text.split("\r?\n|\r");
            all_souce_text = Arrays.asList(text_list);
        }else {
            all_souce_text.add(souce_text);
        }

        //遍历编码
//        Class_Encode obj = new Class_Encode();
//        Class clazz = obj.getClass();
        Class<? extends Class_Encrypt> clazz = this.getClass();
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
                method = clazz.getMethod("Fuc_"+Encode_type, String.class);
                try{
                    String result = (String) method.invoke(this, text);
                    textarea_result.appendText(result+"\r\n");
                }catch (Exception e){
                    textarea_result.appendText(text+"解密失败\r\n");
                }
//                System.out.println(result);
            } catch (NoSuchMethodException  e) {
                textarea_result.appendText(text+"编码失败\r\n");
                throw new RuntimeException(e);
            }

        }
    }
    public String  Fuc_Rot5(String text) {
        return Rot5.cipherEncryption(text);

    }

    //rot13加密实现
    public String  Fuc_Rot13(String text) {
        return Rot13.cipherEncryption(text);

    }

    public String  Fuc_Rot18(String text) {
        return Rot18.cipherEncryption(text);
    }
    public String  Fuc_Rot47(String text) {
        return Rot47.cipherEncryption(text);
    }
    public String  Fuc_凯撒密码(String text) {
        return Caeser.jiami(text);
    }

    public String  Fuc_培根密码(String text) {
        return  new Baconian().encode(text);
    }
    public String  Fuc_栅栏密码(String text) {
        //分栏数目
        int k = Integer.parseInt(temp_key_1);
        List<String>  col = Arrays.asList(text.split(""));
        try {
            List<String> resCol = new LinkedList<>();
            int colLen = col.size();
            for (int i = 0; i < k; i ++) {
                for (int j = 0; j <colLen ; j += k) {
                    int loc = i + j;
                    if (loc < colLen) {
                        resCol.add(col.get(loc));
                    }
                }
            }
            return String.join("", resCol);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "编码失败！";
        }
    }
    public String  Fuc_摩斯密码(String text) {
        Morse_Encrypt encryption=new Morse_Encrypt();//摩斯加密
        encryption.setpwd(text);//将自制加密后的密文进行摩斯加密
//        System.out.println(encryption.getresuilt());//打印摩斯加密后的密文
        return  encryption.getresuilt();
    }
    public String  Fuc_云影密码_01248_(String text) {
        char[] charArray = text.toUpperCase().toCharArray();
        ArrayList<Character> positions = new ArrayList<>();
        ArrayList<Integer> tmp = new ArrayList<>();
        char c;
        for(c = 'A'; c <= 'Z'; ++c){
            positions.add(c);
        }
        for(char i:charArray){
            for (int j = 0; j < positions.size(); j++) {
                if(i== positions.get(j)){
                    tmp.add(j + 1);
                }
            }

        }
        ArrayList<String> result = new ArrayList<>();
        for (int pos :tmp) {
            ArrayList<String> res = new ArrayList<>();
            if (pos >= 8) {
                for (int j = 0; j < pos / 8; j++) {
                    res.add("8");
                }
            }
            if (pos % 8 >= 4) {
                for (int j = 0; j < pos % 8 / 4; j++) {
                    res.add("4");
                }
            }
            if (pos % 4 >= 2) {
                for (int j = 0; j < pos % 4 / 2; j++) {
                    res.add("2");
                }
            }
            if (pos % 2 >= 1) {
                for (int j = 0; j < pos % 2 / 1; j++) {
                    res.add("1");
                }
            }
            String aa =  String.join("", res);
            result.add(aa);

        }
        return String.join("0", result);
    }
    public String  Fuc_当铺密码(String sourceText) {
        try{
            ArrayList<String> mapping_data = new ArrayList<String>(Arrays.asList("田","由", "中", "人", "工", "大", "王", "夫", "井", "羊"));
            String[] text_list = sourceText.split("");
            StringBuilder result = new StringBuilder();
            for (String i : text_list){
                int k = Integer.parseInt(i);
                result.append(mapping_data.get(k));
            }
            return result.toString();
        }catch (Exception e){
            return "当铺密码明文只能为数字！";
        }
    }
    public String  Fuc_四方密码(String text) throws FileNotFoundException, ScriptException, NoSuchMethodException {
//        temp_key_1 ="securitysecuritysecuritys" ;
//        temp_key_2="informationinformationinf";
        //补齐长度25
        String temp_key1=temp_key_1;
        String temp_key2=temp_key_2;
        while (temp_key1.length()<25){
            temp_key1+=temp_key_1;
        }
        while (temp_key2.length()<25){
            temp_key2+=temp_key_2;
        }
        String key_1 = temp_key1.substring(0,25);
        String key_2 = temp_key2.substring(0,25);
        // 获取 JavaScript 引擎
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        engine.eval(new FileReader("js/FourSquareCipher.js"));
        Invocable invocable = (Invocable) engine;
        Object result = invocable.invokeFunction("encode", text,key_1,key_2);
//        System.out.println("执行结果：" + result);
        return (String) result;

    }
    public String  Fuc_仿射密码(String sourceText){
        return new FangShe().encode(sourceText,temp_key_1,temp_key_2);
    }
    public String  Fuc_a1z26密码(String sourceText){
        return A1z26.a1z26(sourceText, "encode");
    }
    public String  Fuc_埃特巴什码(String Source){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Source.length(); i++) {
            char c = Source.charAt(i);
            if(c>=65&&c<=90)
                c = (char)(90-(c-65));
            if(c>=97&&c<=122)
                c = (char)(122-(c-97));
            sb.append(c);
        }
        return sb.toString();
    }

    public String  Fuc_维吉尼亚密码(String sourceText){
        return Vigenere.encrypt(sourceText,temp_key_1);
    }
    public String  Fuc_棋盘密码_ADFGX_(String sourceText){
        return new Adfgx().encrypt(sourceText,temp_key_1);
    }


}

