package com.qianxiao996.ctfknife.Crypto;

import com.qianxiao996.ctfknife.Controller.Encrypt.Utils;
import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.scene.control.TextArea;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.qianxiao996.ctfknife.Utils.Conn.Get_Real_Str;

public class Class_Crypto extends Thread {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private TextArea textarea_result;
    private String Encode_type;
    private String souce_text;
    private String input_encoding;

    private Boolean is_line;

    //临时变量
    public  String temp_key_1 = "";
    private String output_encoding;
    private String temp_key_1_encode;
    private String temp_key_2;

    public void setOne(String temp_key_1,String temp_key_1_encode) {
        this.temp_key_1 =  Get_Real_Str(temp_key_1,temp_key_1_encode);
        this.temp_key_1_encode = temp_key_1_encode;
    }
    public void setValue(TextArea textarea_result, String Encode_type, String souce_text, String input_encoding, String output_encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type.replace(" ", "_").replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_").replace(".", "_").replace("-", "_");
        this.souce_text =  souce_text.trim();
        this.input_encoding = input_encoding;
        this.output_encoding = output_encoding;

        this.is_line =is_line;
    }
    public void setTwo(String key1, String key1Encode, String key2, String key2Encode) {
        this.temp_key_1 = Conn.Get_Real_Str(key1,key1Encode);
        this.temp_key_2 = Conn.Get_Real_Str(key2,key2Encode);
    }
    public void run() {
        //得到所有源文本换列表
        List<String> all_souce_text = new ArrayList<>();
        if(is_line==Boolean.TRUE){
            //换行分割
            String[] text_list = souce_text.split("\r?\n|\r");
            all_souce_text = Arrays.asList(text_list);
        }else {
            all_souce_text.add(souce_text);
        }
        Class<? extends Class_Crypto> clazz = this.getClass();
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
                textarea_result.appendText(text+"错误！\r\n"+e.getMessage());
            }

        }
    }
    public String  Fuc_MD4(String text) {
        try{
            Security.addProvider(new BouncyCastleProvider());
            // 创建一个MD4消息摘要实例
            MessageDigest md4 = MessageDigest.getInstance("MD4");
            byte[] dataBytes = text.getBytes(input_encoding);
            byte[] hash = md4.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }
    public String  Fuc_MD5(String text) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] dataBytes = text.getBytes(input_encoding);
            byte[] hash = md.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }

    public String  Fuc_Sha1(String text) {
        try{
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] dataBytes = text.getBytes(input_encoding);
            // 更新摘要并完成哈希计算
            byte[] hash = sha1.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }

    public String  Fuc_Sha256(String text) {
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] dataBytes = text.getBytes(input_encoding);
            // 更新摘要并完成哈希计算
            byte[] hash = sha.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }
    public String  Fuc_Sha512(String text) {
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            byte[] dataBytes = text.getBytes(input_encoding);
            // 更新摘要并完成哈希计算
            byte[] hash = sha.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }

    public String  Fuc_Sha384(String text) {
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-384");
            byte[] dataBytes = text.getBytes(input_encoding);
            // 更新摘要并完成哈希计算
            byte[] hash = sha.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }
    public String  Fuc_Sha224(String text) {
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-224");
            byte[] dataBytes = text.getBytes(input_encoding);
            // 更新摘要并完成哈希计算
            byte[] hash = sha.digest(dataBytes);
            return Hex.toHexString(hash);
        }catch (Exception e){
            return  text+" 计算失败:"+e.getMessage();
        }
    }

    public String  Fuc_HMACSHA1(String text) {
        try {
            // 初始化 HMAC-SHA1 消息认证码实例
            Mac sha1_HMAC = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret_key = new SecretKeySpec(temp_key_1.getBytes(), "HmacSHA1");
            sha1_HMAC.init(secret_key);
            // 计算 HMAC
            byte[] hmacData = sha1_HMAC.doFinal(text.getBytes(input_encoding));
            // 将计算出的 HMAC 编码为Base64字符串进行比较
            return Hex.toHexString(hmacData);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String  Fuc_HMACSHA224(String text) {
        try {
            // 初始化 HMAC-SHA1 消息认证码实例
            Mac sha_HMAC = Mac.getInstance("HmacSHA224");
            SecretKeySpec secret_key = new SecretKeySpec(temp_key_1.getBytes(), "HmacSHA224");
            sha_HMAC.init(secret_key);
            // 计算 HMAC
            byte[] hmacData = sha_HMAC.doFinal(text.getBytes(input_encoding));
            // 将计算出的 HMAC 编码为Base64字符串进行比较
            return Hex.toHexString(hmacData);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }

    public String  Fuc_HMACSHA256(String text) {
        try {
            // 初始化 HMAC-SHA1 消息认证码实例
            Mac sha_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(temp_key_1.getBytes(), "HmacSHA256");
            sha_HMAC.init(secret_key);
            // 计算 HMAC
            byte[] hmacData = sha_HMAC.doFinal(text.getBytes(input_encoding));
            // 将计算出的 HMAC 编码为Base64字符串进行比较
            return Hex.toHexString(hmacData);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String  Fuc_HMACSHA384(String text) {
        try {
            // 初始化 HMAC-SHA1 消息认证码实例
            Mac sha_HMAC = Mac.getInstance("HmacSHA384");
            SecretKeySpec secret_key = new SecretKeySpec(temp_key_1.getBytes(), "HmacSHA384");
            sha_HMAC.init(secret_key);
            // 计算 HMAC
            byte[] hmacData = sha_HMAC.doFinal(text.getBytes(input_encoding));
            // 将计算出的 HMAC 编码为Base64字符串进行比较
            return Hex.toHexString(hmacData);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String  Fuc_HMACSHA512(String text) {
        try {
            // 初始化 HMAC-SHA1 消息认证码实例
            Mac sha_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(temp_key_1.getBytes(), "HmacSHA512");
            sha_HMAC.init(secret_key);
            // 计算 HMAC
            byte[] hmacData = sha_HMAC.doFinal(text.getBytes(input_encoding));
            // 将计算出的 HMAC 编码为Base64字符串进行比较
            return Hex.toHexString(hmacData);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String Fuc_HMACMD5(String text) throws Exception {
        try {
            // 指定 HMAC 算法名称
            String algorithm = "HmacMD5";
            // 创建 Mac 实例并初始化
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec secretKeySpec = new SecretKeySpec(temp_key_1.getBytes(), algorithm);
            mac.init(secretKeySpec);
            // 执行摘要计算
            byte[] hmacBytes = mac.doFinal(text.getBytes(input_encoding));
            return  Hex.toHexString(hmacBytes);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String Fuc_Sm3(String text) throws Exception {
        try {
            // 获取 SM3 消息摘要实例
            MessageDigest digest = MessageDigest.getInstance("SM3", "BC");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(text.getBytes(input_encoding));
            // 将字节数组转换为十六进制字符串表示
            return Utils.bytesToHex(hashBytes);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String Fuc_Sha3_256(String text) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(text.getBytes(input_encoding));
            // 将字节数组转换为十六进制字符串表示
            return Utils.bytesToHex(hashBytes);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String Fuc_Sha3_224(String text) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-224");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(text.getBytes(input_encoding));
            // 将字节数组转换为十六进制字符串表示
            return Utils.bytesToHex(hashBytes);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }

    public String Fuc_Sha3_384(String text) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-384");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(text.getBytes(input_encoding));
            // 将字节数组转换为十六进制字符串表示
            return Utils.bytesToHex(hashBytes);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }
    public String Fuc_Sha3_512(String text) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-512");
            // 计算输入字符串的哈希值
            byte[] hashBytes = digest.digest(text.getBytes(input_encoding));
            // 将字节数组转换为十六进制字符串表示
            return Utils.bytesToHex(hashBytes);
        } catch (Exception e) {
            return  text+" 签名失败："+e.getMessage();
        }
    }


}
