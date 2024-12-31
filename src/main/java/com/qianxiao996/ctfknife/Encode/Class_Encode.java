package com.qianxiao996.ctfknife.Encode;


import com.qianxiao996.ctfknife.Encode.Modules.BrainFuck.BFCodex;
import com.qianxiao996.ctfknife.Encode.Modules.BrainFuck.Ook;
import com.qianxiao996.ctfknife.Utils.Conn;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import javax.script.ScriptException;
import javafx.scene.control.TextArea;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;

public class Class_Encode extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String input_encoding;

    private Boolean is_line;
    private String output_encoding;

    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding,String output_encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_").replace(" ", "_");
        this.souce_text = souce_text.trim();
        this.input_encoding = input_encoding;
        this.output_encoding = output_encoding;

        this.is_line =is_line;
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
        Class<? extends Class_Encode> clazz = this.getClass();
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
                method = clazz.getMethod(Encode_type, String.class);
                try{
                    text = Get_Real_Str(text,input_encoding);
                    String result = (String) method.invoke(this, text);
                    textarea_result.appendText(result+"\r\n");
                }catch (Exception e){
                    textarea_result.appendText(text+"编码失败\r\n"+ e.getMessage());
                }
//                System.out.println(result);
            } catch (NoSuchMethodException  e) {
                textarea_result.appendText(text+"编码失败\r\n");
                throw new RuntimeException(e);
            }

        }
    }
    public String URL(String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, input_encoding);
    }
    public String Unicode(String text) throws UnsupportedEncodingException {

        return StringEscapeUtils.escapeJava(text);
    }

    public String Escape__U_(String text) throws UnsupportedEncodingException {
        return StringEscapeUtils.escapeJava(text).replace("\\u","%u").replace("\\U","%u");
    }
    public String HtmlEncode(String text) throws UnsupportedEncodingException {
        return StringEscapeUtils.escapeHtml4(text);
    }

    public String ASCII_2进制_(String text) throws UnsupportedEncodingException {
        StringBuilder return_Data= new StringBuilder();
        char[] all_char_list = text.toCharArray();
        for(char i:all_char_list){
            String binary = Integer.toBinaryString((int) i);
            return_Data.append((String) binary);
            return_Data.append((String) " ");
//            System.out.println(binary); // 输出结果为'1000001'
        }
        return  return_Data.toString();
    }
    public String ASCII_8进制_(String text) throws UnsupportedEncodingException {
        StringBuilder return_Data= new StringBuilder();
        char[] all_char_list = text.toCharArray();
        for(char i:all_char_list){
            String binary = Integer.toOctalString((int) i);
            return_Data.append((String) binary);
            return_Data.append((String) " ");
//            System.out.println(binary); // 输出结果为'1000001'
        }
        return  return_Data.toString();
    }
    public String ASCII_10进制_(String text) throws UnsupportedEncodingException {
        StringBuilder return_Data= new StringBuilder();
        char[] all_char_list = text.toCharArray();
        for(char i:all_char_list){
            String binary = Integer.toString((int) i);
            return_Data.append((String) binary);
            return_Data.append((String) " ");
        }
        return  return_Data.toString();
    }

    public String ASCII_16进制_(String text) throws UnsupportedEncodingException {
        StringBuilder return_Data= new StringBuilder();
        char[] all_char_list = text.toCharArray();
        for(char i:all_char_list){
            String binary = Integer.toHexString((int) i);
            return_Data.append((String) binary);
            return_Data.append((String) " ");
        }
        return  return_Data.toString();
    }


    public static String Shellcode(String text) {
        char[] chars = text.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append("\\x").append(Integer.toHexString((int) aChar));
        }
        return hex.toString();
    }

    public static String Str_Hex(String text) {
        char[] chars = text.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char aChar : chars) {
            hex.append(Integer.toHexString((int) aChar));
        }
        return hex.toString();
    }

    public static String Qwerty(String text) {
        String   DIC_QWE = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        String   DIC_ABC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] chars = text.toCharArray();
        StringBuilder ciphertext = new StringBuilder();
        for (char i : chars) {
            int ddd= DIC_ABC.indexOf(i);
            if(ddd!=-1){
                ciphertext.append(DIC_QWE.charAt(ddd));
            }else{
                ciphertext.append(i);
            }
        }
        return ciphertext.toString();
    }
    public static String 图片_Hex(String filepath) throws IOException {
        Path path = Paths.get(filepath);
        if (Files.exists(path)) {
            byte[] aaa = read_file(filepath);
            assert aaa != null;
            if(!Arrays.toString(aaa).isEmpty()){
                return Hex.encodeHexString(aaa);
            }else{
                return "转换失败！";
            }
        } else {
            return "文件不存在！";
        }
    }
    public static String 图片_Base64(String filepath) throws IOException {
        Path path = Paths.get(filepath);
        if (Files.exists(path)) {
            byte[] aaa = read_file(filepath);
            assert aaa != null;
            if(!Arrays.toString(aaa).isEmpty()){
                return "data:image/jpg;"+Base64.getEncoder().encodeToString(aaa);
            }else{
                return "转换失败！";
            }
        } else {
            return "文件不存在！";
        }

    }

    public  static byte[] read_file(String filepath) throws IOException {
        try{
            FileInputStream fis = new FileInputStream(filepath);
            java.io.ByteArrayOutputStream bos=new java.io.ByteArrayOutputStream();
            byte[] buff=new byte[1024];
            int len=0;
            while((len=fis.read(buff))!=-1){
                bos.write(buff,0,len);
            }
            //得到图片的字节数组
            byte[] result=bos.toByteArray();
            return  result;
        }catch(IOException e){
        }
        return null;
    }

    public String JsFuck(String text) throws IOException, ScriptException, NoSuchMethodException {
        return Conn.ExEjs("js/jsfuck.js","JSFuck",text);
    }
    public String JJEncode(String text) throws IOException, ScriptException, NoSuchMethodException {
        return Conn.ExEjs("js/jjencode.js","keyup",text);
    }
    public String AAEncode(String text) throws IOException, ScriptException, NoSuchMethodException {
        return Conn.ExEjs("js/aaencode.js","aaencode",text);
    }
    public String jother(String text) throws IOException, ScriptException, NoSuchMethodException {
        return Conn.ExEjs("js/jother-1.0.rc.js","JotherCmd",text);
    }

    public String 百家姓编码(String text) throws IOException, ScriptException, NoSuchMethodException {
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
        //key value 反转
        Map<String, String> myNewHashMap = new HashMap<>();
        for(Map.Entry<String, String> entry : CODE.entrySet()){
            myNewHashMap.put(entry.getValue(), entry.getKey());
        }
        ArrayList<String> return_data = new ArrayList<>();
        for(int i=0;i<text.length();++i){
            String result =  myNewHashMap.get(String.valueOf(text.charAt(i)));
            if(result!=null){
                return_data.add(result);
            }else{
                return_data.add(String.valueOf(text.charAt(i)));
            }
//            return_data.add(myNewHashMap.get(String.valueOf(text.charAt(i))));
        }
//        System.out.println(return_data);
        return String.join("", return_data);
    }

    public String 核心价值观编码(String text) {
        return  Conn.ExEjs("js/hexinjiazhiguan.js","valuesEncode",text);
    }


    public String XXEncode(String text) {
        return  Conn.ExEjs("js/Xxencode.js","encode",text);
    }
    public String UUEncode(String text) {
        return  Conn.ExEjs("js/Uuencode.js","encode",text);
    }
    public String BrainFuck(String text) {
        return  BFCodex.mTextToBF(text);
    }
    public String Ook(String text) {
        return  Ook.encode(text);
    }
    public String Ook_Short_(String text) {
        return  Ook.encode_Short(text);
    }


    public String Quoted_Printable(String text) {
        return Conn.ExEjs_2("js/Quoted-Printable.js","escapeToQuotedPrintable",text,input_encoding);
    }

}

