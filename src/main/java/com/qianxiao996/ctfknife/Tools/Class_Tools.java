package com.qianxiao996.ctfknife.Tools;

import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.scene.control.TextArea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;

public class Class_Tools extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String input_encoding;

    private Boolean is_line;

    //临时变量
    public  String temp_key_1 = "";
    private String temp_key_2="";
    private String output_encoding;
    private String temp_key_1_encode;


    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding, String output_encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace(" ", "_").replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_").replace(".", "_");
        this.souce_text = souce_text.trim();
        this.input_encoding = input_encoding;
        this.output_encoding = output_encoding;
        this.is_line =is_line;
    }

    public void setOne(String temp_key_1,String temp_key_1_encode) {
        this.temp_key_1 = Conn.Get_Real_Str(temp_key_1,temp_key_1_encode);
        this.temp_key_1_encode = temp_key_1_encode;
    }
    public void setTwo(String key1,String key1Encode,String key2,String key2Encode) {
        this.temp_key_1 = Conn.Get_Real_Str(key1,key1Encode);
        this.temp_key_2 = Conn.Get_Real_Str(key2,key2Encode);
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
                text = Get_Real_Str(text,input_encoding);
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
        // 将 HashMap 转换为 List<Map.Entry<Character, Integer>>
        List<Map.Entry<Character, Integer>> list = new ArrayList<>(charCountMap.entrySet());
        // 使用 lambda 表达式和 Comparator 对列表进行排序，按值从大到小排序
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        // 输出每个字符及其出现的次数
        StringBuilder result = new StringBuilder("字符: 出现次数\r\n");
        // 打印排序后的结果
        StringBuilder maxtomin = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : list) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
            maxtomin.append(entry.getKey());
        }
        String mintomax = new StringBuilder(maxtomin.toString()).reverse().toString();
//        for (char c : charCountMap.keySet()) {
//            result.append(c + ": " + charCountMap.get(c)+"\r\n");
//        }
        return result +"\n出现次数从大到小:\n"+maxtomin+"\n\n出现次数从小到大:\n"+mintomax;
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

