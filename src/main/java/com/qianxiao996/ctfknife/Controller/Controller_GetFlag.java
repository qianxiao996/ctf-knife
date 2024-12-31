package com.qianxiao996.ctfknife.Controller;

import com.qianxiao996.ctfknife.GetFlag.Class_GetFlag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller_GetFlag {
    @FXML
    private CheckBox checkbox_base;

    @FXML
    private CheckBox checkbox_decode;

    @FXML
    private CheckBox checkbox_decrypt;

    @FXML
    TextField line_text_key1;
    @FXML
    TextField line_text_key2;
    @FXML
    TextField line_text_getflag;
    private Ctfknife_Controller appController; //声明父类
    private TextArea selectedTextArea_Result;
    private String source_text;
    private String input_encoding;
    private String output_encoding;

    void setMainController(Ctfknife_Controller appController, TextArea selectedTextArea_Result, String source_text){
        //获取父类的对象
        this.appController=appController;
        this.selectedTextArea_Result = selectedTextArea_Result;
        this.source_text = source_text;
        this.input_encoding=appController.input_encodeing.getValue();
        this.output_encoding=appController.output_encodeing.getValue();
    }

    @FXML
    void button_click(ActionEvent event) {
        this.input_encoding=appController.input_encodeing.getValue();
        this.output_encoding=appController.output_encodeing.getValue();
        String temp_key_1 = line_text_key1.getText();
        String temp_key_2 = line_text_key2.getText();
        String key_flag = line_text_getflag.getText();
        ArrayList<String> list_checkbox = new ArrayList<>();
        if(checkbox_base.isSelected()){
            list_checkbox.add("base");
        }
        if(checkbox_decode.isSelected()){
            list_checkbox.add("decode");
        }
        if(checkbox_decrypt.isSelected()){
            list_checkbox.add("decrypt");
        }
        //给主窗口添加值存储
        appController.Change_Ui_get_flag_Value(temp_key_1,temp_key_2,key_flag);
//      调用编码解码
        Class_GetFlag myThread = new Class_GetFlag();
        myThread.setValue(list_checkbox,selectedTextArea_Result,appController.encode_getflag_go,source_text,input_encoding,output_encoding,temp_key_1,temp_key_2,key_flag);
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        t.start();
        //关闭当前窗口
        Stage stage = (Stage) line_text_key1.getScene().getWindow();
        // do what you have to do
        stage.close();



    }

    @FXML
    void button_close(ActionEvent event) {
        //关闭当前窗口
        Stage stage = (Stage) line_text_key1.getScene().getWindow();
        // do what you have to do
        stage.close();

    }



}
