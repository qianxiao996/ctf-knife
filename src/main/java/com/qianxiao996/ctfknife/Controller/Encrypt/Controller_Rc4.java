package com.qianxiao996.ctfknife.Controller.Encrypt;

import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.f_alert_informationDialog;
import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Rc4 {

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
    private ComboBox<String> input_encoding;

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
        key_encoding.getItems().addAll("UTF8","UTF16","UTF16BE","UTF16LE","Latin1", "Hex", "Base64");
        key_encoding.getSelectionModel().select(0);;
        out_encoding.getItems().addAll("Latin1","UTF8","UTF16","UTF16BE","UTF16LE", "Hex", "Base64");
        out_encoding.getSelectionModel().select(0);;
        input_encoding.getItems().addAll("Latin1","UTF8","UTF16","UTF16BE","UTF16LE", "Hex", "Base64");
        input_encoding.getSelectionModel().select(0);
    }
    @FXML
    void func_encrypt(ActionEvent event) throws IOException {
        String key =  ket_text.getText();
        String key_encding = key_encoding.getValue();
        if (key.isEmpty()){
            f_alert_informationDialog("提示","","请输入key");
            return;
        }
        String text   = TextArea_Source.getText();
        if(text.isEmpty()){
            f_alert_informationDialog("提示","","请输入源文本");
            return;
        }
        String input_encoding_text = input_encoding.getValue();
        byte[]  input_text = Conn.Str_To_Bytes(text,input_encoding_text);
        try{
            if(new String(input_text).startsWith("Error:")){
                TextArea_Result.setText(new  String(input_text));
                return;
            }
        }catch (Exception e){

        }
        byte[] key_value = Conn.Str_To_Bytes(key,key_encding);
        try{
            if(new String(key_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(key_value));
                return;
            }
        }catch (Exception e){
        }
        // 将原始字符串转换为字节数组
        byte[] encodedBytes = Rc4Encode(input_text, key_value);
        if(new String(encodedBytes).startsWith("Error:")){
            TextArea_Result.setText(new  String(encodedBytes));
            return;
        }
        String out_encode = out_encoding.getValue();
        String result = Conn.Bytes_To_Str(encodedBytes,out_encode);
        TextArea_Result.setText(result);
    }

    @FXML
    void func_decrypt(ActionEvent event) throws IOException {
        String key =  ket_text.getText();
        String key_encding = key_encoding.getValue();
        if (key.isEmpty()){
            f_alert_informationDialog("提示","","请输入key");
            return;
        }
        String text   = TextArea_Source.getText();
        if(text.isEmpty()){
            f_alert_informationDialog("提示","","请输入源文本");
            return;
        }
        String input_encoding_text = input_encoding.getValue();
        byte[]  input_text = Conn.Str_To_Bytes(text,input_encoding_text);
        try{
            if(new String(input_text).startsWith("Error:")){
                TextArea_Result.setText(new  String(input_text));
                return;
            }
        }catch (Exception e){

        }

        byte[] key_value = Conn.Str_To_Bytes(key,key_encding);
        try{
            if(new String(key_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(key_value));
                return;
            }
        }catch (Exception e){

        }
        // 将原始字符串转换为字节数组
        byte[] encodedBytes = Rc4Decode(input_text, key_value);
        if(new String(encodedBytes).startsWith("Error:")){
            TextArea_Result.setText(new  String(encodedBytes));
            return;
        }
        String out_encode = out_encoding.getValue();
        String result = Conn.Bytes_To_Str(encodedBytes,out_encode);
        TextArea_Result.setText(result);
    }

    private byte[]  Rc4Encode(byte[] originalText,byte[] key) {
        try {
            // 确保密钥长度至少为40位（5字节）
            // 确保密钥长度至少为40位（5字节）
            if (key.length < 5) {
                // 计算需要的总长度，确保至少是MIN_KEY_LENGTH的倍数，并且不低于MIN_KEY_LENGTH
                int requiredLength = ((5 + key.length - 1) / key.length) * key.length;
                // 创建一个新的数组，长度为所需的长度
                byte[] paddedKey = new byte[requiredLength];
                // 使用循环复制的方式填充密钥，每次复制整个原始密钥
                for (int i = 0; i < requiredLength; i += key.length) {
                    System.arraycopy(key, 0, paddedKey, i, key.length);
                }
                key = paddedKey;
            }
            SecretKey secretKey = new SecretKeySpec(key, "RC4");
            // 创建并初始化Cipher对象用于加密
            Cipher cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(originalText);
        }catch (Exception e){
            return ("Error: "+e.getMessage()).getBytes();
        }
    }
    private byte[]  Rc4Decode(byte[] encryptedBytes,byte[] key) {
        try {
            // 确保密钥长度至少为40位（5字节）
            if (key.length < 5) {
                // 计算需要的总长度，确保至少是MIN_KEY_LENGTH的倍数，并且不低于MIN_KEY_LENGTH
                int requiredLength = ((5 + key.length - 1) / key.length) * key.length;
                // 创建一个新的数组，长度为所需的长度
                byte[] paddedKey = new byte[requiredLength];
                // 使用循环复制的方式填充密钥，每次复制整个原始密钥
                for (int i = 0; i < requiredLength; i += key.length) {
                    System.arraycopy(key, 0, paddedKey, i, key.length);
                }
                key = paddedKey;
            }
            SecretKey secretKey = new SecretKeySpec(key, "RC4");
            // 创建并初始化Cipher对象用于加密
            Cipher cipher = Cipher.getInstance("RC4");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(encryptedBytes);
        }catch (Exception e){
            return ("Error: "+e.getMessage()).getBytes();
        }
    }
}
