package com.qianxiao996.ctfknife.Encode;

import com.qianxiao996.ctfknife.Encode.Modules.BrainFuck.BFCodex;
import com.qianxiao996.ctfknife.Encode.Modules.BrainFuck.Ook;
import com.qianxiao996.ctfknife.Utils.Conn;
import com.qianxiao996.ctfknife.Utils.Result;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.imageio.stream.FileImageOutputStream;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;

import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;

public class Class_Decode extends Thread {

    private static TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String input_encoding;

    private Boolean is_line;
    private String temp_key_1;
    private String output_encoding;

    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding,String output_encoding, Boolean is_line) {
        Class_Decode.textarea_result = textarea_result;
        this.Encode_type = Encode_type;
        this.souce_text = souce_text.trim();
        this.input_encoding = input_encoding;
        this.output_encoding = output_encoding;
        this.is_line =is_line;
        setOne(textarea_result.getText(),"UTF-8");
    }
    public void setOne(String temp_key_1,String temp_key_1_encode) {
        this.temp_key_1 =  Conn.Get_Real_Str(temp_key_1,temp_key_1_encode);
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
        Class<? extends Class_Decode> clazz = this.getClass();
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Hex->图片","Base64->图片","Hex->文件","Base64->文件"));
        if(!open_file_list.contains(Encode_type)){
            textarea_result.clear();
        }
        for(String text:all_souce_text){
            Method method;
            try {
                String temp_methods_name  = Encode_type.replace(" ", "_").replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_");
                method = clazz.getMethod("Fuc_"+temp_methods_name, String.class, String.class);
                try{
                    text = Get_Real_Str(text,input_encoding);
                    Result result = (Result) method.invoke(this, Encode_type,text);
                    if(result.is_success()){
                        textarea_result.appendText(result.getResult_text()+"\r\n");
                    }else{
                        textarea_result.appendText(result.getResult_text()+"\r\n");
                    }
                }catch (Exception e){
                    textarea_result.appendText(text+"解码失败\r\n");
                }
//                System.out.println(result);
            } catch (NoSuchMethodException  e) {
                textarea_result.appendText(text+"解码失败\r\n");
                throw new RuntimeException(e);
            }

        }
    }
    public Result Fuc_URL(String name, String text) throws UnsupportedEncodingException {
        try{
            String result = URLDecoder.decode(text, input_encoding);
            return new Result(name,true,result);
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_Unicode(String name,String text) {
        try{
            String result = StringEscapeUtils.unescapeJava(text);
            return new Result(name,true,result);
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_Escape__U_(String name,String text) {
        try{
            String result = StringEscapeUtils.unescapeJava(text.replace("%u","\\u").replace("%U","\\u"));
            return new Result(name,true,result);
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_HtmlEncode(String name,String text) {
        try{
            String result = StringEscapeUtils.unescapeHtml4(text);
            return new Result(name,true,result);
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }

    public Result Fuc_ASCII_2进制_(String name,String text) {
        try{
            StringBuilder return_Data= new StringBuilder();
            String[] text_list = text.split(" ");
            for(String i:text_list) {
                int asciiCode = Integer.parseInt(i, 2);
                char character = (char) asciiCode;
                return_Data.append(character);
            }
            return new Result(name,true,return_Data.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_ASCII_8进制_(String name,String text) {
        try{
            StringBuilder return_Data= new StringBuilder();
            String[] text_list = text.split(" ");
            for(String i:text_list) {
                int asciiCode = Integer.parseInt(i, 8);
                char character = (char) asciiCode;
                return_Data.append(character);
            }
            return new Result(name,true,return_Data.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_ASCII_10进制_(String name,String text) {
        try{
            StringBuilder return_Data= new StringBuilder();
            String[] text_list = text.split(" ");
            for(String i:text_list) {
                int asciiCode = Integer.parseInt(i, 10);
                char character = (char) asciiCode;
                return_Data.append(character);
            }
            return new Result(name,true,return_Data.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_ASCII_16进制_(String name,String text) {
        try{
            StringBuilder return_Data= new StringBuilder();
            String[] text_list = text.split(" ");
            for(String i:text_list) {
                int asciiCode = Integer.parseInt(i, 16);
                char character = (char) asciiCode;
                return_Data.append(character);
            }
            return new Result(name,true,return_Data.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_Shellcode(String name,String text) {
        try{
            char[] hexs = text.replace("\\x","").toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hexs.length / 2; i++) {
                int high = Character.digit(hexs[i * 2], 16);
                int low = Character.digit(hexs[i * 2 + 1], 16);
                sb.append((char) (high * 16 + low));
            }
            return new Result(name,true,sb.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public static Result Fuc_Hex_Str(String name, String text) {
        try{
            char[] hexs = text.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hexs.length / 2; i++) {
                int high = Character.digit(hexs[i * 2], 16);
                int low = Character.digit(hexs[i * 2 + 1], 16);
                sb.append((char) (high * 16 + low));
            }
            return new Result(name,true,sb.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_Qwerty(String name, String text) {
        try{
            String   DIC_QWE = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
            String   DIC_ABC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            char[] chars = text.toCharArray();
            StringBuilder ciphertext = new StringBuilder();
            for (char i : chars) {
                int ddd= DIC_QWE.indexOf(i);
                ciphertext.append(DIC_ABC.charAt(ddd));
            }
            return new Result(name,true,ciphertext.toString());
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public   Result Fuc_Hex_图片(String name,String hex_text) {
        try{
            byte[] bytes = stringToByte(hex_text);
            String file_save_path = textarea_result.getText();
            textarea_result.clear();
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(file_save_path));
            imageOutput.write(bytes, 0, bytes.length);
            imageOutput.close();
            return new Result(name,true,"文件保存成功！位置："+file_save_path);
        } catch(Exception ex) {
            ex.printStackTrace();
            return new Result(name,false,"转换失败!");
        }
    }
    public static byte[] stringToByte(String s) {
        int length = s.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ((Character.digit(s.charAt(i * 2), 16) << 4) | Character.digit(s.charAt((i * 2) + 1), 16));
        }
        return bytes;
    }
    public  Result Fuc_Base64_图片(String name,String base64_text) {
        try{
            base64_text  = base64_text.split(";base64,")[base64_text.split(";base64,").length-1];
            String file_save_path = textarea_result.getText();
            textarea_result.clear();
            //获取JDK8里的解码器Base64.Decoder,将base64字符串转为字节数组
            byte[] bytes = Base64.getDecoder().decode(base64_text);
            //构建字节数组输入流
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(file_save_path));
            imageOutput.write(bytes, 0, bytes.length);
            imageOutput.close();
            return new Result(name,true,"文件保存成功！位置："+file_save_path);
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result Fuc_JsFuck( String name,String text) {
        try{
            String result =  Conn.ExEjs("js/jsfuck_decode.js","decode",text);
            return new Result(name,true,(String)result);
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }
    public Result Fuc_JJEncode(String name,String text) {
        String result =  Conn.ExEjs("js/jjencode.js","jjdecode",text);
        return new Result(name,true,result);
    }
    public Result Fuc_AAEncode(String name,String text) throws IOException, ScriptException, NoSuchMethodException {
        String result =  Conn.ExEjs("js/aaencode.js","aadecode",text);
        return new Result(name,true, (String)result);
    }

    public Result Fuc_jother(String name,String text) {
//        String result =  Conn.ExEjs("js/jother-1.0.rc.js","Jother",text);
//        return new Result(name,true, (String)result);
        return new Result(name,false,"暂不支持Jother解密，但可以在浏览器按F12打开console，输入密文后回车，可得到解密结果");
    }
    public Result Fuc_百家姓编码(String name,String text) {
        try{
            Map<String, String> CODE = new HashMap<>();
            CODE.put("赵","0");
            CODE.put("钱","1");
            CODE.put("孙","2");
            CODE.put("李","3");
            CODE.put("周","4");
            CODE.put("吴","5");
            CODE.put("郑","6");
            CODE.put("王","7");
            CODE.put("冯","8");
            CODE.put("陈","9");
            CODE.put("褚","a");
            CODE.put("卫","b");
            CODE.put("蒋","c");
            CODE.put("沈","d");
            CODE.put("韩","e");
            CODE.put("杨","f");
            CODE.put("朱","g");
            CODE.put("秦","h");
            CODE.put("尤","i");
            CODE.put("许","j");
            CODE.put("何","k");
            CODE.put("吕","l");
            CODE.put("施","m");
            CODE.put("张","n");
            CODE.put("孔","o");
            CODE.put("曹","p");
            CODE.put("严","q");
            CODE.put("华","r");
            CODE.put("金","s");
            CODE.put("魏","t");
            CODE.put("陶","u");
            CODE.put("姜","v");
            CODE.put("戚","w");
            CODE.put("谢","x");
            CODE.put("邹","y");
            CODE.put("喻","z");
            CODE.put("福","A");
            CODE.put("水","B");
            CODE.put("窦","C");
            CODE.put("章","D");
            CODE.put("云","E");
            CODE.put("苏","F");
            CODE.put("潘","G");
            CODE.put("葛","H");
            CODE.put("奚","I");
            CODE.put("范","J");
            CODE.put("彭","K");
            CODE.put("郎","L");
            CODE.put("鲁","M");
            CODE.put("韦","N");
            CODE.put("昌","O");
            CODE.put("马","P");
            CODE.put("苗","Q");
            CODE.put("凤","R");
            CODE.put("花","S");
            CODE.put("方","T");
            CODE.put("俞","U");
            CODE.put("任","V");
            CODE.put("袁","W");
            CODE.put("柳","X");
            CODE.put("唐","Y");
            CODE.put("罗","Z");
            CODE.put("薛",".");
            CODE.put("伍","-");
            CODE.put("余","_");
            CODE.put("米","+");
            CODE.put("贝","=");
            CODE.put("姚","/");
            CODE.put("孟","?");
            CODE.put("顾","#");
            CODE.put("尹","%");
            CODE.put("江","&");
            CODE.put("钟","*");
            ArrayList<String> return_data = new ArrayList<>();
            for(int i=0;i<text.length();++i){
                if(CODE.get(String.valueOf(text.charAt(i)))!=null){
                    return_data.add(CODE.get(String.valueOf(text.charAt(i))));
                }else{
                    return_data.add(String.valueOf(text.charAt(i)));
                }
            }
            return new Result(name,true,String.join("", return_data));
        }catch (Exception e){
            return new Result(name,false,e.getMessage());
        }
    }

    public Result Fuc_核心价值观编码(String name,String text) {
        String result =   Conn.ExEjs("js/hexinjiazhiguan.js","valuesDecode",text);
        return new Result(name, true, result);


    }



    public Result Fuc_UUEncode(String name,String text) {
        String result =   Conn.ExEjs("js/Uuencode.js","decode",text);
        return new Result(name, true, result);
    }
    public Result Fuc_XXEncode(String name,String text) {
        String result =   Conn.ExEjs("js/Xxencode.js","decode",text);
        return new Result(name, true, result);
    }

    public Result Fuc_BrainFuck(String name,String text) {
        String result = (BFCodex.mBFToText(text));
        return new Result(name, true, result);
    }

    public Result Fuc_Ook(String name,String text) {
        String result = Ook.decode(text);
        return new Result(name, true, result);
    }
    public Result Fuc_Ook_Short_(String name,String text) {
        String result = Ook.decode_Short(text);
        return new Result(name, true, result);
    }
    public Result Fuc_Quoted_Printable(String name,String text) {
        String result =  Conn.ExEjs_2("js/Quoted-Printable.js","unescapeFromQuotedPrintable",text,input_encoding);
        return new Result(name, true, result);
    }

    public Result Fuc_Base64_Hex(String name,String text) {
        try{
            byte[] text2 = Base64.getDecoder().decode(text.getBytes(input_encoding));
            String result = Hex.toHexString(text2);
            return new Result(name, true, result);
        }catch (Exception e){
            return new Result(name, false, e.getMessage());
        }

    }
    public Result Fuc_Hex_文件(String name,String hex_text) {
        try{
            byte[] bytes = stringToByte(hex_text);
            String file_save_path = textarea_result.getText();
            textarea_result.clear();
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(file_save_path));
            imageOutput.write(bytes, 0, bytes.length);
            imageOutput.close();
            return new Result(name,true,"文件保存成功！位置："+file_save_path);
        }catch (Exception e){
            return new Result(name, false, e.getMessage());
        }
    }
    public Result Fuc_Base64_文件(String name,String base64_text) {
        try{
            String file_save_path = textarea_result.getText();
            textarea_result.clear();
            //获取JDK8里的解码器Base64.Decoder,将base64字符串转为字节数组
            byte[] bytes = Base64.getDecoder().decode(base64_text);
            //构建字节数组输入流
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(file_save_path));
            imageOutput.write(bytes, 0, bytes.length);
            imageOutput.close();
            return new Result(name,true,"文件保存成功！位置："+file_save_path);
        }catch (Exception e){
            return new Result(name, false, e.getMessage());
        }
    }

}