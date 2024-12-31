package com.qianxiao996.ctfknife.Controller.Encrypt;

import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Arrays;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.f_alert_informationDialog;
import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Des {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    @FXML
    private ComboBox<String> mode;

    @FXML
    private ComboBox<String> padding_combox;

    @FXML
    private ComboBox<String> input_encoding;

    @FXML
    private ComboBox<String> output_encoding;

    @FXML
    private TextField key_text;

    @FXML
    private ComboBox<String> key_encoding;

    @FXML
    private TextField iv_text;

    @FXML
    private ComboBox<String> iv_encoding;

    @FXML
    private Button source_textarea_copy;

    @FXML
    private Button source_textarea_paste;

    @FXML
    private Button source_textarea_clear;

    @FXML
    private TextArea TextArea_Source;

    @FXML
    private Button button_to_source;

    @FXML
    private Button result_textarea_copy;

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
        key_encoding.getItems().addAll("UTF8", "Hex", "Base64","Latin1");
        key_encoding.getSelectionModel().select(0);;
        iv_encoding.getItems().addAll("UTF8", "Hex", "Base64","Latin1");
        iv_encoding.getSelectionModel().select(0);

        input_encoding.getItems().addAll("Raw","Hex", "Base64");
        input_encoding.getSelectionModel().select(0);;
        output_encoding.getItems().addAll("Raw","Hex", "Base64");
        output_encoding.getSelectionModel().select(0);

        mode.getItems().addAll("ECB","CBC","CFB","OFB","CTR","GCM");
        mode.getSelectionModel().select(0);
        padding_combox.getItems().addAll("PKCS7Padding","PKCS5Padding","ZeroBytePadding","ISO10126Padding","X923Padding","NoPadding");
        padding_combox.getSelectionModel().select(0);
    }
    @FXML
    void func_decrypt(ActionEvent event) {
        String key =  key_text.getText();
        String key_encode = key_encoding.getValue();
        String iv = iv_text.getText();
        String iv_encode = iv_encoding.getValue();
        byte[] key_value ;
        if(!key.isEmpty()){
            key_value = Conn.Str_To_Bytes(key,key_encode);
            if(new String(key_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(key_value));
                return;
            }else{
                if (key_value.length>24){
                    key_value = Arrays.copyOfRange(key_value,0,24);
                } else if (key_value.length>8 && key_value.length<24) {
                    key_value = Arrays.copyOfRange(key_value,0,8);
                }
                if(key_value.length!=8 && key_value.length!=24){
                    f_alert_informationDialog("提示","","key的长度必须为8、24比特！");
                    return;
                }
            }
        }else{
            f_alert_informationDialog("提示","","key的长度必须为8、24比特");
            return;
        }
        byte[] iv_value = new byte[0];
        if(!Objects.equals(mode.getValue(), "ECB")  && !iv.isEmpty()){
            iv_value = Conn.Str_To_Bytes(iv,iv_encode);
            if(new String(iv_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(iv_value));
                return;
            }else{
                if (iv_value.length>8){
                    iv_value = Arrays.copyOfRange(iv_value,0,8);
                }
                if(iv_value.length!=8){
                    f_alert_informationDialog("提示","","iv的长度必须为8比特");
                    return;
                }
            }
        }
        String text   = TextArea_Source.getText();
        if(text.isEmpty()){
            f_alert_informationDialog("提示","","请输入源文本");
            return;
        }
        String input_encoding_text = input_encoding.getValue();
        byte[]  input_text = Conn.Str_To_Bytes(text,input_encoding_text);
        if(new String(input_text).startsWith("Error:")){
            TextArea_Result.setText(new  String(input_text));
            return;
        }
        String mode_text = mode.getValue();
        String padding_text = padding_combox.getValue();
        byte[] encodedBytes = DESDecrypt(input_text, key_value,iv_value,mode_text,padding_text);
        String output_encoding_text = output_encoding.getValue();
        if(new String(encodedBytes).startsWith("Error:")){
            TextArea_Result.setText(new  String(encodedBytes));
            return;
        }
        String result = Conn.Bytes_To_Str(encodedBytes,output_encoding_text);
        TextArea_Result.setText(result);

    }

    @FXML
    void func_encrypt(ActionEvent event) {

        String key =  key_text.getText();
        String key_encode = key_encoding.getValue();
        String iv = iv_text.getText();
        String iv_encode = iv_encoding.getValue();
        byte[] key_value ;
        if(!key.isEmpty()){
            key_value = Conn.Str_To_Bytes(key,key_encode);
            if(new String(key_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(key_value));
                return;
            }else{
                if (key_value.length>24){
                    key_value = Arrays.copyOfRange(key_value,0,24);
                } else if (key_value.length>8 && key_value.length<24) {
                    key_value = Arrays.copyOfRange(key_value,0,8);
                }
                if(key_value.length!=8 && key_value.length!=24){
                    f_alert_informationDialog("提示","","key的长度必须为8、24比特！");
                    return;
                }
            }
        }else{
            f_alert_informationDialog("提示","","key的长度必须为8、24比特");
            return;
        }
        byte[] iv_value = new byte[0];
        if(!Objects.equals(mode.getValue(), "ECB")  && !iv.isEmpty()){
            iv_value = Conn.Str_To_Bytes(iv,iv_encode);
            if(new String(iv_value).startsWith("Error:")){
                TextArea_Result.setText(new  String(iv_value));
                return;
            }else{
                if (iv_value.length>8){
                    iv_value = Arrays.copyOfRange(iv_value,0,8);
                }
                if(iv_value.length!=8){
                    f_alert_informationDialog("提示","","iv的长度必须为8比特");
                    return;
                }
            }
        }
        String text   = TextArea_Source.getText();
        if(text.isEmpty()){
            f_alert_informationDialog("提示","","请输入源文本");
            return;
        }
        String input_encoding_text = input_encoding.getValue();
        byte[]  input_text = Conn.Str_To_Bytes(text,input_encoding_text);
        if(new String(input_text).startsWith("Error:")){
            TextArea_Result.setText(new  String(input_text));
            return;
        }


        String mode_text = mode.getValue();
        String padding_text = padding_combox.getValue();
        byte[] encodedBytes = DESEncrypt(input_text, key_value,iv_value,mode_text,padding_text);
        String output_encoding_text = output_encoding.getValue();
        if(new String(encodedBytes).startsWith("Error:")){
            TextArea_Result.setText(new  String(encodedBytes));
            return;
        }
        String result = Conn.Bytes_To_Str(encodedBytes,output_encoding_text);
        TextArea_Result.setText(result);
        

    }
    private byte[] DESEncrypt(byte[] plainText, byte[] key,byte[] iv,String mode,String padding) {
        try {
            String encrypt_type = "DES";
            if(key.length>8){
                encrypt_type ="DESede";
            }
            SecretKey secretKey = new SecretKeySpec(key, encrypt_type);
            Cipher cipher = Cipher.getInstance(encrypt_type+"/"+mode+"/"+padding, "BC");
            IvParameterSpec iv_ =  new IvParameterSpec(iv);
            if(Objects.equals(mode, "ECB")){
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }else{
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv_);
            }
            return cipher.doFinal(plainText);
        }catch (Exception e)
        {
            return ("Error:"+e.getMessage()).getBytes();
        }
    }
    private byte[] DESDecrypt(byte[] plainText, byte[] key,byte[] iv,String mode,String padding) {
        try {
            String encrypt_type = "DES";
            if(key.length>8){
                encrypt_type ="DESede";
            }
            SecretKey secretKey = new SecretKeySpec(key, encrypt_type);
            Cipher cipher = Cipher.getInstance(encrypt_type+"/"+mode+"/"+padding, "BC");
            IvParameterSpec iv_ =  new IvParameterSpec(iv);
            if(Objects.equals(mode, "ECB")){
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }else{
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv_);
            }
            return cipher.doFinal(plainText);
        }catch (Exception e)
        {
            return ("Error:"+e.getMessage()).getBytes();
        }
    }

}
