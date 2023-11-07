package com.qianxiao996.ctfknife.Encode;

import cn.hutool.core.codec.*;
import com.qianxiao996.ctfknife.Encode.Modeles.Base64_Custom;
import com.qianxiao996.ctfknife.Encode.Modeles.Base91;
import javafx.scene.control.TextArea;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.qianxiao996.ctfknife.Encode.Ascii85.Ascii85Coder;
import static com.github.charlemaznable.core.codec.Base92.base92;
public class Class_Base_Encode extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String encoding;

    private Boolean is_line;

    //临时变量
    public  String temp_key_1 = "";

    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_");
        this.souce_text = souce_text.trim();
        this.encoding = encoding;
        this.is_line =is_line;
    }
    public void setOne(String temp_key_1) {
        this.temp_key_1 = temp_key_1;
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
        Class clazz = this.getClass();
//        ArrayList<String> open_file_list = new ArrayList<>();
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Base64(自定义)"));
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
                method = clazz.getMethod("Fuc_"+Encode_type, String.class);
                try{
                    String result = (String) method.invoke(this, text);
                    textarea_result.appendText(result+"\r\n");
                }catch (Exception e){
                    textarea_result.appendText(text+"编码失败\r\n");
                    throw new RuntimeException(e);
                }
//                System.out.println(result);
            } catch (NoSuchMethodException  e) {
                textarea_result.appendText(text+"编码失败\r\n");
                throw new RuntimeException(e);
            }

        }
    }


    public String Fuc_Base16(String text){
        byte[] byteArray = text.getBytes();
        Base16Codec base16 = new Base16Codec(false);
        text = new String(base16.encode(byteArray));
        return  text;
    }
    public String Fuc_Base32(String text){
//        byte[] byteArray = text.getBytes();
        text = Base32.encode(text);
        return  text;
    }
    public String Fuc_Base36(String text){
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        text = new BigInteger(1, bytes).toString(36);
        return  text;
    }

    public String Fuc_Base58(String text){
        byte[] byteArray = text.getBytes();
        Base58Codec base58= new Base58Codec();
        return base58.encode(byteArray);
    }
    public String Fuc_Base62(String text){
        byte[] byteArray = text.getBytes();
        Base62Codec base62= new Base62Codec();
        return new String(base62.encode(byteArray,true));
    }
    public String Fuc_Base64(String text){
//        byte[] byteArray = text.getBytes();
        text = Base64.encode(text,encoding);
        return  text;
    }

    public String Fuc_Base64_自定义_(String text){
        String CUSTOM_CHARS = temp_key_1;
        if (CUSTOM_CHARS.length()!=64){
            return "编码表长度需要为64";
        }
//        System.out.println(CUSTOM_CHARS);
//        String CUSTOM_CHARS  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
//        return Base64_Zidingyi.encode(text.getBytes(),CUSTOM_CHARS);
        return Base64_Custom.encode(CUSTOM_CHARS,text);
    }

    public String Fuc_Base85(String text){
        byte[] byteArray = text.getBytes();
        String result =Ascii85Coder.encodeBytesToAscii85(byteArray);
        return result.replace("~>","");
    }

    public String Fuc_Base91(String text){
        return Base91.base91(text, "encode");
    }

    public String Fuc_Base92(String text){
        return (base92(text.getBytes()));
    }


}

