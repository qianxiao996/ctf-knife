package com.qianxiao996.ctfknife.Controller;

import com.qianxiao996.ctfknife.Base.Class_Base_Decode;
import com.qianxiao996.ctfknife.Base.Class_Base_Encode;
import com.qianxiao996.ctfknife.Crypto.Class_Crypto;
import com.qianxiao996.ctfknife.Encrypt.Class_Decrypt;
import com.qianxiao996.ctfknife.Encrypt.Class_Encrypt;
import com.qianxiao996.ctfknife.Tools.Class_Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class Controller_One {

    @FXML
    public Label label;

    @FXML
    public TextField line_text;
    private Ctfknife_Controller appController; //声明父类

    private TextArea selectedTextArea_Result;
    private String encode_type;
    private String source_text;
    private String input_encoding;
    private boolean is_line;
    private String label_text;
    private String type;
    private String output_encodeing;

    void setMainController(Ctfknife_Controller appController, TextArea selectedTextArea_Result, String encode_type, String source_text, String type){
        //获取父类的对象
        this.appController=appController;
        this.selectedTextArea_Result = selectedTextArea_Result;
        this.encode_type=encode_type;
        this.source_text = source_text;
        this.encode_type=encode_type;
        this.input_encoding=appController.input_encodeing.getValue();
        this.output_encodeing=appController.output_encodeing.getValue();
        this.is_line=appController.encode_line.isSelected();
        this.type=type;
    }
    @FXML
    void button_click(ActionEvent event) {
        this.input_encoding=appController.input_encodeing.getValue();
        this.output_encodeing=appController.output_encodeing.getValue();
        this.is_line=appController.encode_line.isSelected();
        String text = line_text.getText();
//        selectedTextArea_Result.setText(text);
//        System.out.println(selectedTextArea_Result.getText());
        //给主窗口添加值存储
        appController.Change_Ui_One_Value(text);
//      调用编码解码
        if(Objects.equals(type, "encode_base")){
            Class_Base_Encode myThread = new Class_Base_Encode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encodeing,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(text,"UTF-8");
            t.setDaemon(true);
            t.start();

        } else if (Objects.equals(type, "decode_base")) {
            Class_Base_Decode myThread = new Class_Base_Decode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encodeing,is_line);
            myThread.setOne(text,"UTF-8");
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "encrypt")) {
            Class_Encrypt myThread = new Class_Encrypt();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encodeing,is_line);
            myThread.setOne(text,"UTF-8");
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "decrypt")) {
            Class_Decrypt myThread = new Class_Decrypt();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encodeing,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(text,"UTF-8");
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "tools")) {
            Class_Tools myThread = new Class_Tools();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encodeing,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(text,"UTF-8");
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "crypto")) {
            Class_Crypto myThread = new Class_Crypto();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encodeing,is_line);
            Thread t = new Thread(myThread);
            myThread.setOne(text,"UTF-8");
            t.setDaemon(true);
            t.start();
        }
        //关闭当前窗口
        Stage stage = (Stage) line_text.getScene().getWindow();
        // do what you have to do
        stage.close();



    }

}
