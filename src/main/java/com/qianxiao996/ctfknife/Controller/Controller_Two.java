package com.qianxiao996.ctfknife.Controller;

import com.qianxiao996.ctfknife.Encrypt.Class_Decrypt;
import com.qianxiao996.ctfknife.Encrypt.Class_Decrypt_go;
import com.qianxiao996.ctfknife.Encrypt.Class_Encrypt;
import com.qianxiao996.ctfknife.Tools.Class_Tools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Controller_Two {

    @FXML
    public Label label_1;

    @FXML
    public Label label_2;

    @FXML
    public TextField line_text_1;

    @FXML
    public TextField line_text_2;
    private Ctfknife_Controller appController; //声明父类

    private TextArea selectedTextArea_Result;
    private String encode_type;
    private String source_text;
    private String input_encoding;
    private boolean is_line;
    private String type;
    private String output_encoding;

    void setMainController(Ctfknife_Controller appController, TextArea selectedTextArea_Result, String encode_type, String source_text, String type){
        //获取父类的对象
        this.appController=appController;
        this.selectedTextArea_Result = selectedTextArea_Result;
        this.encode_type=encode_type;
        this.source_text = source_text;

        this.input_encoding=appController.input_encodeing.getValue();
        this.output_encoding=appController.output_encodeing.getValue();
        this.is_line=appController.encode_line.isSelected();
        this.type=type;
    }
    @FXML
    void button_click(ActionEvent event) {
        this.input_encoding=appController.input_encodeing.getValue();
        this.output_encoding=appController.output_encodeing.getValue();
        this.is_line=appController.encode_line.isSelected();
        String key1 = line_text_1.getText();
        String key2 = line_text_2.getText();
        //给主窗口发送值进行存储
        appController.Change_Ui_Two_Value(key1,key2);

        if (Objects.equals(type, "encrypt")) {
            Class_Encrypt myThread = new Class_Encrypt();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setTwo(key1,"UTF-8",key2,"UTF-8");
            t.setDaemon(true);
            t.start();
        } else if (Objects.equals(type, "decrypt")) {
            Class_Decrypt myThread = new Class_Decrypt();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setTwo(key1,"UTF-8",key2,"UTF-8");
            t.setDaemon(true);
            t.start();
        }  else if (Objects.equals(type, "tools")) {
            Class_Tools myThread = new Class_Tools();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setTwo(key1,"UTF-8",key2,"UTF-8");
            t.setDaemon(true);
            t.start();

        } else if (Objects.equals(type, "decrypt_go")) {
            //一件解密
            ArrayList<String> encode_type = new ArrayList<>(Arrays.asList("Rot5","Rot13","Rot18","Rot47","凯撒密码","栅栏密码","培根密码","云影密码(01248)","当铺密码","四方密码","仿射密码","a1z26密码","埃特巴什码","维吉尼亚密码","棋盘密码(ADFGX)"));
            Class_Decrypt_go myThread = new Class_Decrypt_go();
            myThread.setValue(appController.encode_decrypt_go,selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            myThread.setTwo(key1,key2);
            t.setDaemon(true);
            t.start();
        }


        //关闭当前窗口
        Stage stage = (Stage) line_text_1.getScene().getWindow();
        // do what you have to do
        stage.close();

    }

    @FXML
    void button_close(ActionEvent event) {
        Stage stage = (Stage) line_text_1.getScene().getWindow();
        // do what you have to do
        stage.close();

    }

}
