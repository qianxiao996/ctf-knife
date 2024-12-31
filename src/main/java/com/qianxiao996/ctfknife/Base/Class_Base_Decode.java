package com.qianxiao996.ctfknife.Base;

import cn.hutool.core.codec.*;
import com.qianxiao996.ctfknife.Base.Modeles.Ascii85.Ascii85Coder;
import com.qianxiao996.ctfknife.Base.Modeles.Base100;
import com.qianxiao996.ctfknife.Base.Modeles.Base64_Custom;
import com.qianxiao996.ctfknife.Base.Modeles.Base91;
import com.qianxiao996.ctfknife.Utils.Conn;
import com.qianxiao996.ctfknife.Utils.Result;
import javafx.scene.control.TextArea;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.charlemaznable.core.codec.Base92.unBase92;
import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;
public class Class_Base_Decode extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String input_encoding;

    private Boolean is_line;

    public  String temp_key_1 = "";
    private String output_encoding;
    private String temp_key_1_encode;

    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding, String output_encoding,Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type;
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
        Class<? extends Class_Base_Decode> clazz = this.getClass();
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
                text = Get_Real_Str(text,input_encoding);
                String temp_methods_name  = Encode_type.replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_");
                method = clazz.getMethod("Fuc_"+temp_methods_name, String.class, String.class);
                try{
                    Result result = (Result) method.invoke(this, Encode_type,text);
                    if(result.is_success()){
                        textarea_result.appendText(result.getResult_text()+"\r\n");
                    }else{
                        textarea_result.appendText(result.getResult_text()+"\r\n");
                    }
                }catch (Exception e){
                    textarea_result.appendText(text+"解码失败\r\n");
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                textarea_result.appendText(text+"解码失败\r\n");
                throw new RuntimeException(e);
            }

        }
    }


    public Result  Fuc_Base16(String name, String text){
        try{
            Base16Codec base16 = new Base16Codec(false);
            text = new String(base16.decode(text),output_encoding);
            return new Result(name,true,text);
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_Base32(String name, String text){
        try{
            text = new String(Base32.decode(text),output_encoding);
            return new Result(name,true,text);
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }


    public Result  Fuc_Base36(String name, String text){
        try{
            byte[] bytes = new BigInteger(text, 36).toByteArray();
            int zeroPrefixLength = zeroPrefixLength(bytes);
            return new Result(name,true,new String(bytes, zeroPrefixLength, bytes.length-zeroPrefixLength, output_encoding));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    //base36使用
    private int zeroPrefixLength(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return i;
            }
        }
        return bytes.length;
    }
        

    public Result  Fuc_Base58(String name, String text){
        try{
            Base58Codec base58= new Base58Codec();
            return new Result(name,true,new String(base58.decode(text),output_encoding));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_Base62(String name,String text) {
        try{
            byte[] byteArray = text.getBytes(input_encoding);
            Base62Codec base62= new Base62Codec();
            return new Result(name,true,new String(base62.decode(byteArray,true),output_encoding));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_Base64(String name, String text){
        try{
            text = new String(Base64.decode(text),output_encoding);
            return new Result(name,true,text);
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
//    public Result  Fuc_Base64_自定义_(String name, String text){
//        BaseX base64 = new BaseX("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
//        return base64.encode(text.getBytes());
//    }

    public Result  Fuc_Base64_自定义_(String name,String text) {
        try{
            String CUSTOM_CHARS = temp_key_1;
            if (CUSTOM_CHARS.length()!=64){
                return new Result(name,false,"编码表长度需要为64，当前长度:"+CUSTOM_CHARS.length());
            }
    //        String CUSTOM_CHARS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    //        String aa = new String(Base64_Zidingyi.decode(text,CUSTOM_CHARS));
            return new Result(name,true,Base64_Custom.decode(CUSTOM_CHARS,text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
//        return  aa;
    }


    public Result Fuc_Base85(String name, String text){
        try{
            return new Result(name,true,(Ascii85Coder.decodeAscii85StringToString(text+"~>")));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_Base91(String name, String text){
        try {
    //        https://github.com/norkator/cryptography/blob/master/src/cryptography/Encoding.java
            return new Result(name,true,Base91.base91(text, "decode"));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_Base92(String name, String text){
        try{
            return new Result(name,true,new String(unBase92(text),output_encoding));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result Fuc_Base100(String name,String text){
        try{
            return new Result(name,true,new String(Base100.decode(text.getBytes(input_encoding)),output_encoding));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }


}

