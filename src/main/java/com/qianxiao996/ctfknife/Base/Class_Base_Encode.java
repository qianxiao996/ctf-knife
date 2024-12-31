package com.qianxiao996.ctfknife.Base;

import cn.hutool.core.codec.*;
import com.qianxiao996.ctfknife.Base.Modeles.Ascii85.Ascii85Coder;
import com.qianxiao996.ctfknife.Base.Modeles.Base100;
import com.qianxiao996.ctfknife.Base.Modeles.Base64_Custom;
import com.qianxiao996.ctfknife.Base.Modeles.Base91;
import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.scene.control.TextArea;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.charlemaznable.core.codec.Base92.base92;
import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;

public class Class_Base_Encode extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String input_encoding;

    private Boolean is_line;

    //临时变量
    public  String temp_key_1 = "";
    private String output_encoding;
    private String temp_key_1_encode;


    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding, String output_encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_");
        this.souce_text = souce_text.trim();
        this.input_encoding = input_encoding;
        this.output_encoding = output_encoding;
        this.is_line =is_line;
    }

    public void setOne(String temp_key_1,String temp_key_1_encode ) {
        this.temp_key_1 = Conn.Get_Real_Str(temp_key_1,temp_key_1_encode);
        this.temp_key_1_encode = temp_key_1_encode;
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
        Class<? extends Class_Base_Encode> clazz = this.getClass();
//        ArrayList<String> open_file_list = new ArrayList<>();
//        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Base64(自定义)"));
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
                method = clazz.getMethod("Fuc_"+Encode_type, String.class);
                try{
                    text = Get_Real_Str(text,input_encoding);
                    String result = (String) method.invoke(this, text);
                    textarea_result.appendText(result+"\r\n");
                }catch (Exception e){
                    textarea_result.appendText(text+"编码失败\r\n"+e.getMessage());
                }
//                System.out.println(result);
            } catch (NoSuchMethodException  e) {
                textarea_result.appendText(text+"编码失败\r\n"+e.getMessage());
            }

        }
    }


    public String Fuc_Base16(String text) throws UnsupportedEncodingException {
        byte[] byteArray = text.getBytes(input_encoding);
        Base16Codec base16 = new Base16Codec(false);
        text = new String(base16.encode(byteArray));
        return  text;
    }
    public String Fuc_Base32(String text){
//        byte[] byteArray = text.getBytes();
        text = Base32.encode(text);
        return  text;
    }
    public String Fuc_Base36(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes(input_encoding);
        text = new BigInteger(1, bytes).toString(36);
        return  text;
    }

    public String Fuc_Base58(String text) throws UnsupportedEncodingException {
        byte[] byteArray = text.getBytes(input_encoding);
        Base58Codec base58= new Base58Codec();
        return base58.encode(byteArray);
    }
    public String Fuc_Base62(String text) throws UnsupportedEncodingException {
        byte[] byteArray = text.getBytes(input_encoding);
        Base62Codec base62= new Base62Codec();
        return new String(base62.encode(byteArray,true),output_encoding);
    }
    public String Fuc_Base64(String text){
//        byte[] byteArray = text.getBytes();
        text = Base64.encode(text,input_encoding);
        return  text;
    }

    public String Fuc_Base64_自定义_(String text){
        String CUSTOM_CHARS = temp_key_1;
        if (CUSTOM_CHARS.length()!=64){
            return "编码表长度需要为64，当前长度:"+CUSTOM_CHARS.length();
        }
//        System.out.println(CUSTOM_CHARS);
//        String CUSTOM_CHARS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
//        return Base64_Zidingyi.encode(text.getBytes(),CUSTOM_CHARS);
        return Base64_Custom.encode(CUSTOM_CHARS,text);
    }

    public String Fuc_Base85(String text) throws UnsupportedEncodingException {
        byte[] byteArray = text.getBytes(input_encoding);
        String result =Ascii85Coder.encodeBytesToAscii85(byteArray);
        return result.replace("~>","");
    }

    public String Fuc_Base91(String text){
        return Base91.base91(text, "encode");
    }

    public String Fuc_Base92(String text) throws UnsupportedEncodingException {
        return (base92(text.getBytes(input_encoding)));
    }


    public String Fuc_Base100(String text) throws UnsupportedEncodingException {
        return new String(Base100.encode(text.getBytes(input_encoding)),output_encoding);
    }


}

