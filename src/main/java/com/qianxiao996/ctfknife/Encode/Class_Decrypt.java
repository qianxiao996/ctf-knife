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
import com.qianxiao996.ctfknife.Encode.Utils.Result;

public class Class_Decrypt extends Thread {

    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String encoding;

    private Boolean is_line;

    //临时变量
    public  String temp_key_1 = "";
    private String temp_key_2;

    public void setTwo(String temp_key_1,String temp_key_2) {
        this.temp_key_1 = temp_key_1;
        this.temp_key_2 = temp_key_2;
    }
    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type;
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
        Class<? extends Class_Decrypt> clazz = this.getClass();
        textarea_result.clear();
        for(String text:all_souce_text){
            Method method = null;
            try {
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
                    textarea_result.appendText(text+"解密失败\r\n");
                    throw new RuntimeException(e);

                }
//                System.out.println(result);
            } catch (NoSuchMethodException  e) {
                textarea_result.appendText(text+"解密失败\r\n");
                throw new RuntimeException(e);
            }
        }
    }
    //

    //
    public Result  Fuc_Rot13(String name,String text) {
        try{
            return new Result(name,true,Rot13.cipherDecryption(text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result Fuc_Rot5(String name, String text)
    {
        try{
            return new Result(name,true,Rot5.cipherDecryption(text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_Rot18(String name,String text) {
        try{
            return new Result(name,true,Rot18.cipherDecryption(text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_Rot47(String name,String text) {
        try{
            return new Result(name,true,Rot47.cipherDecryption(text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_凯撒密码(String name,String text) {
        try{
            return new Result(name,true,Caeser.jiemi(text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_栅栏密码(String name,String text) {
        try{
            String zlstr[]= new String[1024];
            int num = 0;
            StringBuffer jg = new StringBuffer();
            int x[] = new int[1024];
            int sl = text.length();
            int a = 0;
            int i=0;
            if (sl!=1&&sl!=2){
                for (i=2;i<sl;i++) {
                    if ((sl%i)==0){
                        x[a]=i;
                        a = a+1;
                    }
                }
            }else{
                jg.append("用肉眼就能看出来\n");
            }
            if (a!=0){
                jg.append("得到因数(排除1和字符串长度):\n");
                for(int yi=0;yi<a;yi++){
                    jg.append(" "+x[yi]);
                }
                jg.append("\n");
                jg.append("\n");
                for(i=0;i<a;i++){
                    jg.append("第"+(i+1)+"栏：");
                    for(int j=0;j<(sl/x[i]);j++){
                        zlstr[num]=(text.substring((0+(x[i]*j)), (x[i]+(x[i]*j))));
                        num++;
                    }
                    int slen = zlstr[0].length();
                    for(int ji = 0;ji<slen;ji++){
                        for(int s=0;s<num;s++){
                            jg.append(zlstr[s].substring(ji, ji+1));
                        }
                    }
                    num = 0;
                    jg.append("\n");
                }
            }else{
                jg.append("解码失败...\n");
                jg.append("尝试去除字符串中的空格解码\n");
                sl = text.replace(" ", "").length();
                jg.append(sl);
                if (sl!=1&&sl!=2){
                    for (i=2;i<sl;i++) {
                        if ((sl%i)==0){
                            x[a]=i;
                            a = a+1;
                        }
                    }
                }else{
                    jg.append("用肉眼就能看出来\n");
                }
                if (a!=0){
                    jg.append("这串密文(去除空格后)是栅栏密码...\n");
                    jg.append("得到因数(排除1和字符串长度):");
                    for(int yi=0;yi<a;yi++){
                        jg.append(" "+x[yi]);
                    }
                    jg.append("\n");
                    jg.append("\n");
                    jg.append("开始解码...");
                    for(i=0;i<a;i++){
                        jg.append("第"+(i+1)+"栏：");
                        for(int j=0;j<(sl/x[i]);j++){
                            zlstr[num]=(text.substring((0+(x[i]*j)), (x[i]+(x[i]*j))));
                            num++;
                        }
                        int slen = zlstr[0].length();
                        for(int ji = 0;ji<slen;ji++){
                            for(int s=0;s<num;s++){
                                jg.append(zlstr[s].substring(ji, ji+1));
                            }
                        }
                        num = 0;
                        jg.append("\n");
                    }
                }
            }
            return new Result(name,true,jg.toString());
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_培根密码(String name,String text) {
        try{
            return new Result(name,true,new Baconian().decode(text));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_摩斯密码(String name,String text) {
        try{
            Morse_Decrypt decryption=new Morse_Decrypt();//摩斯加密
            decryption.setpwd(text);//将自制加密后的密文进行摩斯加密
    //        System.out.println(decryption.getresuilt());//打印摩斯加密后的密文
            return new Result(name,true,decryption.getresuilt());
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_云影密码_01248_(String name,String text) {
        try {
                
            String[] encodedText;
            encodedText= text.split("0");
            ArrayList<String> result = new ArrayList<>();
            for (String c :encodedText) {
                int aaa  = Integer.parseInt(c);
                if (!Character.isDigit(aaa)) {
                    result.add(cloudShadow(c));
                }else {
                    result.add(c);
                }
            }
            return new Result(name,true,String.join("", result));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    //云影密码使用的函数
    private static String cloudShadow(String  str) {
        ArrayList<Character> charList = new ArrayList<>();
        ArrayList<String> result_text = new ArrayList<>();
        char c;
        for(c = 'A'; c <= 'Z'; ++c){
            charList.add(c);
        }
        String[] temp_chr= str.split("");
        int temp=0;
        for (String i :temp_chr) {
            temp= temp+Integer.parseInt(i);
        }
        result_text.add(String.valueOf(charList.get(temp-1)));
        return String.join("", result_text);
    }
    public Result  Fuc_移位密码(String name,String sourceText) {
        try{
            String resultText = "";
            for (int j = 0; j < 26; j++) {
                StringBuilder resultList = new StringBuilder();
                for (int i = 0; i < sourceText.length(); i++) {
                    char c = sourceText.charAt(i);
                    if (Character.isLowerCase(c)) {
                        resultList.append(shiftChar(c, j, true));
                    } else if (Character.isUpperCase(c)) {
                        resultList.append(shiftChar(c, j, false));
                    } else {
                        resultList.append(c);
                    }
                }
                resultText +=  " 向右偏移了" + j + "位: "+resultList.toString() +"\n";
            }
            return new Result(name,true,resultText);
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    //移位密码使用
    private static char shiftChar(char c, int shift, boolean isLowerCase) {
        if (isLowerCase) {
            return (char) ((c - 'a' + shift) % 26 + 'a');
        } else {
            return (char) ((c - 'A' + shift) % 26 + 'A');
        }
    }

    public Result  Fuc_当铺密码(String name,String sourceText) {
            try{
            ArrayList<String> mapping_data = new ArrayList<String>(Arrays.asList("田","由", "中", "人", "工", "大", "王", "夫", "井", "羊"));
            String[] text_list = sourceText.split("");
            StringBuilder result = new StringBuilder();
            for (String i : text_list){
                String temp = i;
                for (String j : mapping_data){
                    if(Objects.equals(i, j)){
                        temp= String.valueOf(mapping_data.indexOf(j));
                    }
                }
                result.append(temp);
            }
            return new Result(name,true,result.toString());
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_四方密码(String name, String text) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        try{
            if(temp_key_1.length()!=25 || temp_key_2.length()!=25){
                return new Result(name,false,"key长度需为25!");
            }
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("nashorn");
            engine.eval(new FileReader("js/FourSquareCipher.js"));
            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction("decode", text,temp_key_1,temp_key_2);
    //        System.out.println("执行结果：" + result);
            return new Result(name,true,(String) result);
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }

    }
    public Result  Fuc_仿射密码(String name, String sourceText){
        try{
            return new Result(name,true,new FangShe().decode(sourceText,temp_key_1,temp_key_2));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_a1z26密码(String name, String sourceText){
        try{
            return new Result(name,true,A1z26.a1z26(sourceText, "decode"));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }

    public Result  Fuc_埃特巴什码(String name,String Source){
        try{
            return new Result(name,true,Atbash.atbash(Source,"decode"));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_维吉尼亚密码(String name, String sourceText){
        try{
            return new Result(name,true,Vigenere.decrypt(sourceText, temp_key_1));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
    public Result  Fuc_棋盘密码_ADFGX_(String name, String sourceText){
        try {
            if(temp_key_1.length()!=25){
                return new Result(name,false,"编码表长度需要为25！");
            }
            return new Result(name,true,new Adfgx().decrypt(sourceText, temp_key_1));
        } catch(Exception ex) {
            return new Result(name,false,ex.getMessage());
        }
    }
}

