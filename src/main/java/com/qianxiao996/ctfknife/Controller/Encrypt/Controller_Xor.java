package com.qianxiao996.ctfknife.Controller.Encrypt;

import com.qianxiao996.ctfknife.Encode.Class_Decode;
import com.qianxiao996.ctfknife.Utils.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.f_alert_informationDialog;
import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Xor {

    @FXML
    private Button source_textarea_copy;

    @FXML
    private Button source_textarea_paste;

    @FXML
    private Button source_textarea_clear;

    @FXML
    private TextArea TextArea_Source;

    @FXML
    private ComboBox<String> key_encoding;
    @FXML
    private Button button_to_source;
    @FXML
    private ComboBox<String> out_encoding;
    @FXML
    private Button result_textarea_copy;

    @FXML
    private TextField ket_text;
    @FXML
    private Button result_textarea_paste;

    @FXML
    private Button result_textarea_clear;

    @FXML
    private TextArea TextArea_Result;

    public void initialize() {
        set_button_icon(source_textarea_copy,"img/copy.png");
        set_button_icon(result_textarea_copy,"img/copy.png");
        set_button_icon(source_textarea_paste,"img/paste.png");
        set_button_icon(result_textarea_paste,"img/paste.png");
        set_button_icon(source_textarea_clear,"img/clear.png");
        set_button_icon(result_textarea_clear,"img/clear.png");
        set_button_icon(button_to_source,"img/to.png");

        set_button_copy(source_textarea_copy,TextArea_Source);
        set_button_copy(result_textarea_copy,TextArea_Result);
        set_button_paste(source_textarea_paste,TextArea_Source);
        set_button_paste(result_textarea_paste,TextArea_Result);
        set_button_clear(source_textarea_clear,TextArea_Source);
        set_button_clear(result_textarea_clear,TextArea_Result);
        set_button_to_source(button_to_source,TextArea_Source,TextArea_Result);
        key_encoding.getItems().addAll("UTF8","UTF16BE", "Hex", "Base64", "Binary","Decimal","Latin1");
        key_encoding.getSelectionModel().select(0);;
        out_encoding.getItems().addAll("UTF8", "UTF16BE","Hex", "Base64", "Binary","Decimal","Latin1");
        out_encoding.getSelectionModel().select(0);;
    }
    @FXML
    void func_xor(ActionEvent event) throws IOException {
        String key =  ket_text.getText();
        String key_encding = key_encoding.getValue();
        byte[] key_value = new byte[0];
        if (key.isEmpty()){
            f_alert_informationDialog("提示","","请输入key");
            return;
        }
        String text   = TextArea_Source.getText();
        if(text.isEmpty()){
            f_alert_informationDialog("提示","","请输入源文本");
            return;
        }
        if (Objects.equals(key_encding, "UTF8")){
            key_value =key.getBytes(StandardCharsets.UTF_8);
        }
        else if (Objects.equals(key_encding, "Hex")){
            key_value = hexStringToByteArray(key);
        } else if (Objects.equals(key_encding, "Base64")) {
            try {
                key_value = Base64.getDecoder().decode(key);
            }catch (Exception  e){
                TextArea_Result.setText(e.getMessage());
                return;
            }

        } else if (Objects.equals(key_encding, "Binary")) {
            key_value= binaryStringToByteArray(key.replace(" ",""));
        } else if (Objects.equals(key_encding, "Decimal")) {
            key_value= decimalStringToByteArray(key);
        } else if (Objects.equals(key_encding, "UTF16BE")) {
            key_value =key.getBytes(StandardCharsets.UTF_16BE);
        } else if (Objects.equals(key_encding, "Latin1")) {
            key_value =key.getBytes(StandardCharsets.ISO_8859_1);
        }
        try{
            if(new String(key_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(key_value));
                return;
            }
        }catch (Exception e){

        }

        // 将原始字符串转换为字节数组
//        byte[] originalBytes = text.getBytes();
        byte[] encodedBytes = xorEncode(text.getBytes(), key_value);
        String result = "";
        if(new String(encodedBytes).startsWith("Error:")){
            TextArea_Result.setText(new  String(encodedBytes));
            return;
        }
        String out_encode = out_encoding.getValue();
        if(out_encode.equals("UTF8")){
            result = new String(encodedBytes, StandardCharsets.UTF_8);
        } else if (out_encode.equals("Hex")) {
            result = Utils.bytesToHex(encodedBytes);
        } else if (out_encode.equals("Base64")) {
            result = new String(Base64.getEncoder().encode(encodedBytes));
        } else if (out_encode.equals("Binary")) {
            result = bytestoBinaryString(encodedBytes);
        } else if (out_encode.equals("Decimal")) {
            result =bytestoDecimalString(encodedBytes," ");
        } else if (out_encode.equals("UTF16BE")) {
            result = new String(encodedBytes, StandardCharsets.UTF_16BE);
        }else if (out_encode.equals("Latin1"))
        {
            result = new String(encodedBytes, StandardCharsets.ISO_8859_1);
        }

        TextArea_Result.setText(result);
    }

    // 将字节数组转换为十进制字符串的方法
    public static String bytestoDecimalString(byte[] bytes, String delimiter) {
        if (bytes == null) {
            return "null";
        }
        StringBuilder decimalStringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            // 将字节转换为无符号整数（0到255）
            int unsignedByte = bytes[i] & 0xFF;
            decimalStringBuilder.append(unsignedByte);
            // 如果不是最后一个元素，则添加分隔符
            if (i < bytes.length - 1) {
                decimalStringBuilder.append(delimiter);
            }
        }
        return decimalStringBuilder.toString();
    }

    public static String bytestoBinaryString(byte[] bytes) {
        StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            // 将字节转换为无符号整数（0到255）
            int unsignedByte = b & 0xFF;
            // 将整数转换为二进制字符串，并确保其长度为8位
            String binaryString = String.format("%8s", Integer.toBinaryString(unsignedByte)).replace(' ', '0');
            binaryStringBuilder.append(binaryString).append(" "); // 添加空格分隔符以提高可读性
        }
        // 移除最后一个多余的空格
        return binaryStringBuilder.toString().trim();
    }
    // XOR编码方法，使用字符串作为密钥
    public static byte[] xorEncode(byte[] data, byte[] keyBytes) {
        byte[] encodedData = new byte[data.length];
//        byte[] keyBytes = key.getBytes();
        int keyLength = keyBytes.length;

        for (int i = 0; i < data.length; i++) {
            // 对每个数据字节与密钥中对应的字节（循环使用密钥）进行异或操作
            encodedData[i] = (byte) (data[i] ^ keyBytes[i % keyLength]);
        }
        return encodedData;
    }


    // 将十六进制字符串转换为字节数组的方法   不足2位补0
    public static byte[] hexStringToByteArray(String s) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

//        if (s.length() % 2 != 0) {
//            return "Error: Invalid hex string length".getBytes();
//        }
        int len = s.length();
        for (int i = 0; i < len; i += 2) {
            String byteString= "";
            String str_len = s.substring(i);
            if(str_len.length()<2){
                StringBuilder paddedStr = new StringBuilder();
                for (int j = 0; j < 2 - str_len.length(); j++) {
                    paddedStr.append('0');
                }
                paddedStr.append(str_len);
                byteString = paddedStr.toString();
            }else{
                byteString = s.substring(i,i+2);
            }
            byteStream.write((byte) ((Character.digit(byteString.charAt(0), 16) << 4)
                    + Character.digit(byteString.charAt(1), 16)));
        }
        return byteStream.toByteArray();
    }

    // 将二进制字符串转换为字节数组的方法
    public static byte[] binaryStringToByteArray(String binaryString) throws IOException {
//        if (binaryString == null || binaryString.isEmpty() || binaryString.length() % 8 != 0) {
//            return  ("Error: Binary string must not be null or empty and its length must be a multiple of 8.").getBytes();
//        }
        // 检查是否所有字符都是0或1
        for (char c : binaryString.toCharArray()) {
            if (c != '0' && c != '1') {
                return ("Error: Binary string contains non-binary characters.").getBytes();
            }
        }
        // 创建字节数组用于存储转换后的字节
//        byte[] bytes = new byte[binaryString.length() / 8];
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        // 遍历二进制字符串，每次取8个字符作为一个字节
        for (int i = 0; i < binaryString.length(); i += 8) {
            String byteString= "";
            String str_len = binaryString.substring(i);
            if(str_len.length()<8 ){
                StringBuilder paddedStr = new StringBuilder();
                for (int j = 0; j < 8 - str_len.length(); j++) {
                    paddedStr.append('0');
                }
                paddedStr.append(str_len);
                byteString = paddedStr.toString();
            }else{
                byteString = binaryString.substring(i, i + 8);
            }
            // 将8位二进制字符串转换为整数，再转换为字节
            byteStream.write((byte) Integer.parseInt(byteString, 2));
        }
        return byteStream.toByteArray();
    }

    // 将空格分隔的十进制字符串转换为字节数组的方法
    public static byte[] decimalStringToByteArray(String decimalString) {
        if (decimalString == null || decimalString.trim().isEmpty()) {
            return ("Error: Decimal string must not be null or empty.").getBytes();
        }

        // 使用空格分割字符串
        String[] decimalValues = decimalString.trim().split("\\s+");

        // 创建字节数组用于存储转换后的字节
        byte[] bytes = new byte[decimalValues.length];

        // 遍历分割后的字符串数组，将每个十进制数转换为字节
        for (int i = 0; i < decimalValues.length; i++) {
            try {
                int intValue = Integer.parseInt(decimalValues[i].trim());
                if (intValue < 0 || intValue > 255) {
                    return("Error: Decimal value out of byte range: " + intValue).getBytes();
                }
                bytes[i] = (byte) intValue;
            } catch (NumberFormatException e) {
                return("Error: Invalid decimal value at index " + i + ": " + decimalValues[i]).getBytes();
            }
        }

        return bytes;
    }
}
