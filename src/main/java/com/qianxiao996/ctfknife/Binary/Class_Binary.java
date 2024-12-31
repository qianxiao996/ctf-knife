package com.qianxiao996.ctfknife.Binary;

import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.scene.control.TextArea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;


public class Class_Binary extends Thread {

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

    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding,String output_encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace(" ", "_").replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_").replace(".", "_");
        this.souce_text = souce_text.trim();
        this.input_encoding = input_encoding;
        this.output_encoding = output_encoding;
        this.is_line =is_line;
    }
    public void setOne(String temp_key_1,String temp_key_1_encode )  {
        this.temp_key_1 =  Conn.Get_Real_Str(temp_key_1,temp_key_1_encode);
        this.temp_key_1_encode = temp_key_1_encode;
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
        Class<? extends Class_Binary> clazz = this.getClass();
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
                textarea_result.appendText(text+" 进制转换失败,请确保源文本输入正确！\r\n");
                throw new RuntimeException(e);
            }

        }
    }
    public String  Fuc_2_8(String text) {
        return Integer.toOctalString(Integer.parseInt(text,2));
    }
    public String  Fuc_2_10(String text) {
        return String.valueOf(Integer.parseInt(text, 2));
    }
    public String  Fuc_2_16(String text) {
        return Integer.toHexString(Integer.parseInt(text,2));
    }
    public String  Fuc_10_2(String text) {
        return Integer.toBinaryString(Integer.parseInt(text));
    }
    public String  Fuc_10_8(String text) {
        return Integer.toOctalString(Integer.parseInt(text));
    }
    public String  Fuc_10_16(String text) {
        return Integer.toHexString(Integer.parseInt(text));
    }
    public String  Fuc_8_2(String text) {
        return Integer.toBinaryString(Integer.parseInt(text,8));
    }
    public String  Fuc_8_10(String text) {
        return String.valueOf(Integer.parseInt(text, 8));
    }
    public String  Fuc_8_16(String text) {
        return Integer.toHexString(Integer.parseInt(text,8));
    }
    public String  Fuc_16_2(String text) {
        return Integer.toBinaryString(Integer.parseInt(text,16));
    }
    public String  Fuc_16_8(String text) {
        return Integer.toOctalString(Integer.parseInt(text,16));
    }
    public String  Fuc_16_10(String text) {
        return String.valueOf(Integer.parseInt(text, 16));
    }
    public String  Fuc_自定义(String text) {
        StringBuilder result = new StringBuilder();
        int decimal = Integer.parseInt(text,Integer.parseInt(temp_key_1));
        int base = Integer.parseInt(temp_key_2);
        while (decimal > 0) {
            int remainder = decimal % base;
            if (remainder < 10) {
                result.append(remainder);
            } else {
                result.append((char) ('A' + remainder - 10));
            }
            decimal /= base;
        }
        return result.reverse().toString();
    }


}

