package com.qianxiao996.ctfknife.Encode;

import com.qianxiao996.ctfknife.Encode.Modeles.*;
import javafx.scene.control.TextArea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Class_Tools extends Thread {

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
        Class<? extends Class_Tools> clazz = this.getClass();
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
                method = clazz.getMethod("Fuc_"+Encode_type, String.class);
                String result = (String) method.invoke(this, text);
                textarea_result.appendText(result+"\r\n");
//                System.out.println(result);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                textarea_result.appendText(text+"Error:"+ e.getMessage() +"\r\n");
                throw new RuntimeException(e);
            }

        }
    }
    public String  Fuc_替换(String text) {
        return text.replace(temp_key_1,temp_key_2);

    }
    public String  Fuc_分割(String text) {
        return String.join(" ", text.split(temp_key_1));
    }

    public String  Fuc_拆分(String text) {
        int length = Integer.parseInt(temp_key_1);
        ArrayList<String> result = new ArrayList();
        for(int i = 0; i < text.length(); i += length) {
            String part = text.substring(i, Math.min(text.length(), i + length));
            result.add(part);
        }
        return String.join(" ",result);
    }
    public String  Fuc_统计(String text) {
        // 创建一个 HashMap 用于存储字符及其出现的次数
        HashMap<Character, Integer> charCountMap = new HashMap<>();

        // 遍历字符串中的每个字符
        for (char c : text.toCharArray()) {
            // 如果字符已经在 HashMap 中，增加其计数
            if (charCountMap.containsKey(c)) {
                charCountMap.put(c, charCountMap.get(c) + 1);
            }
            // 如果字符不在 HashMap 中，将其添加到 HashMap 并设置计数为 1
            else {
                charCountMap.put(c, 1);
            }
        }

        // 输出每个字符及其出现的次数
        StringBuilder result = new StringBuilder("字符: 出现次数\r\n");
        for (char c : charCountMap.keySet()) {
            result.append(c + ": " + charCountMap.get(c)+"\r\n");
        }
        return result.toString();
    }
    public String  Fuc_反转(String text) {
        return new StringBuilder(text).reverse().toString();
    }
    public String  Fuc_全小写(String text) {
        return text.toLowerCase();
    }
    public String  Fuc_全大写(String text) {
        return text.toUpperCase();
    }

}

