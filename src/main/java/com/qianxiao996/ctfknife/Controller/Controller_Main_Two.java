package com.qianxiao996.ctfknife.Controller;

import com.qianxiao996.ctfknife.Base.Class_Base_Decode;
import com.qianxiao996.ctfknife.Base.Class_Base_Encode;
import com.qianxiao996.ctfknife.Crypto.Class_Crypto;
import com.qianxiao996.ctfknife.Encrypt.Class_Decrypt;
import com.qianxiao996.ctfknife.Encrypt.Class_Encrypt;
import com.qianxiao996.ctfknife.Tools.Class_Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Objects;

import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Main_Two {

    @FXML
    private TextField text_key_1;

    @FXML
    private ComboBox<String> key_1_encoding;

    @FXML
    private TextField text_key_2;

    @FXML
    private ComboBox<String> key_2_encoding;

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
    private Button button_encrypt;

    @FXML
    private Button button_decrypt;

    @FXML
    private TextArea TextArea_Result;
    private com.qianxiao996.ctfknife.Controller.Ctfknife_Controller Ctfknife_Controller;
    private String encode_type;
    private String input_encoding;
    private String output_encoding;
    private boolean is_line;
    private String type;

    void setMainController(Ctfknife_Controller Ctfknife_Controller ,String type, String encode_type){
        //获取父类的对象
        this.Ctfknife_Controller =Ctfknife_Controller;
        this.encode_type=encode_type;
        this.input_encoding=Ctfknife_Controller.input_encodeing.getValue();
        this.output_encoding=Ctfknife_Controller.output_encodeing.getValue();
        this.is_line=Ctfknife_Controller.encode_line.isSelected();
        this.type=type;
    }
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
        key_1_encoding.getItems().addAll("UTF-8","Hex","Base64");
        key_1_encoding.getSelectionModel().select(0);
        key_2_encoding.getItems().addAll("UTF-8","Hex","Base64");
        key_2_encoding.getSelectionModel().select(0);
    }
    @FXML
    void func_button_decrypt(ActionEvent event) {
        this.input_encoding=Ctfknife_Controller.input_encodeing.getValue();
        this.output_encoding=Ctfknife_Controller.output_encodeing.getValue();
        this.is_line=Ctfknife_Controller.encode_line.isSelected();
        String source_text = TextArea_Source.getText();
        TextArea_Result.setText(source_text);
        String key_1 = text_key_1.getText();
        String key_1_encode = key_1_encoding.getValue();
        String key_2 = text_key_2.getText();
        String key_2_encode = key_2_encoding.getValue();
        if(Objects.equals(type, "encode_base") || Objects.equals(type, "decode_base")) {
            Class_Base_Decode myThread = new Class_Base_Decode();
            myThread.setValue(TextArea_Result, encode_type, source_text, input_encoding, output_encoding, is_line);
            myThread.setOne(key_1, key_1_encode);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();

        }else if (Objects.equals(type, "encrypt")|| Objects.equals(type, "decrypt")) {
            Class_Decrypt myThread = new Class_Decrypt();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setTwo(key_1,key_1_encode,key_2,key_2_encode);
            myThread.setOne(key_1,key_1_encode);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "tools")) {
            Class_Tools myThread = new Class_Tools();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            myThread.setOne(key_1,key_1_encode);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "crypto")) {
            Class_Crypto myThread = new Class_Crypto();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setTwo(key_1,key_1_encode,key_2,key_2_encode);
            myThread.setOne(key_1,key_1_encode);
            t.setDaemon(true);
            t.start();
        }
    }

    @FXML
    void func_button_encrypt(ActionEvent event) {
        this.input_encoding=Ctfknife_Controller.input_encodeing.getValue();
        this.output_encoding=Ctfknife_Controller.output_encodeing.getValue();
        this.is_line=Ctfknife_Controller.encode_line.isSelected();
        String source_text = TextArea_Source.getText();
        TextArea_Result.setText(source_text);
        String key_1 = text_key_1.getText();
        String key_1_encode = key_1_encoding.getValue();
        String key_2 = text_key_2.getText();
        String key_2_encode = key_2_encoding.getValue();
        if(Objects.equals(type, "encode_base") || Objects.equals(type, "decode_base")){
            Class_Base_Encode myThread = new Class_Base_Encode();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(key_1,key_1_encode);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "encrypt") || Objects.equals(type, "decrypt")) {
            Class_Encrypt myThread = new Class_Encrypt();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            myThread.setOne(key_1,key_1_encode);
            myThread.setTwo(key_1,key_1_encode,key_2,key_2_encode);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "tools")) {
            Class_Tools myThread = new Class_Tools();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(key_1,key_1_encode);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "crypto")) {
            Class_Crypto myThread = new Class_Crypto();
            myThread.setValue(TextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(key_1,key_1_encode);
            myThread.setTwo(key_1,key_1_encode,key_2,key_2_encode);
            t.setDaemon(true);
            t.start();
        }
    }

}
