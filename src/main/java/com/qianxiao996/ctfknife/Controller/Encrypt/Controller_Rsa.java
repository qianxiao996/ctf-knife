package com.qianxiao996.ctfknife.Controller.Encrypt;

import com.qianxiao996.ctfknife.Crypto.Class_Crypto_Rsa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.*;
import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Rsa {

    public ComboBox<String> combo_dp;
    public TextField text_dp;
    public TextField text_dq;
    public ComboBox<String> combo_dq;
    public TextField text_qinv;
    public ComboBox<String> combo_qinv;
    @FXML
    private TextField text_p;

    @FXML
    private ComboBox<String> combo_p;

    @FXML
    private ComboBox<String> combo_q;

    @FXML
    private TextField text_q;

    @FXML
    private ComboBox<String> combo_n;

    @FXML
    private TextField text_n;



    @FXML
    private TextField text_d;

    @FXML
    private ComboBox<String> combo_d;

    @FXML
    private TextArea text_pub;

    @FXML
    private ComboBox<String> combo_pub;

    @FXML
    private TextArea text_pri;

    @FXML
    private ComboBox<String> combo_pri;

    @FXML
    private ComboBox<String> mingwen_encoding;

    @FXML
    private Button source_textarea_copy;

    @FXML
    private Button source_textarea_paste;

    @FXML
    private Button source_textarea_clear;

    @FXML
    private TextArea TextArea_Source;

    @FXML
    private ComboBox<String>  miwen_encoding;

    @FXML
    private Button result_textarea_copy;

    @FXML
    private Button result_textarea_paste;

    @FXML
    private Button result_textarea_clear;

    @FXML
    private TextArea TextArea_Result;

    @FXML
    private ComboBox<String> combo_e;

    @FXML
    private TextField text_e;
    private  static List<String> set_pub_pri_list = new ArrayList<>();
    @FXML
    private ListView<String> listview_go;
    public void initialize() {
        set_button_icon(source_textarea_copy,"img/copy.png");
        set_button_icon(result_textarea_copy,"img/copy.png");
        set_button_icon(source_textarea_paste,"img/paste.png");
        set_button_icon(result_textarea_paste,"img/paste.png");
        set_button_icon(source_textarea_clear,"img/clear.png");
        set_button_icon(result_textarea_clear,"img/clear.png");
        set_button_copy(source_textarea_copy,TextArea_Source);
        set_button_copy(result_textarea_copy,TextArea_Result);
        set_button_paste(source_textarea_paste,TextArea_Source);
        set_button_paste(result_textarea_paste,TextArea_Result);
        set_button_clear(source_textarea_clear,TextArea_Source);
        set_button_clear(result_textarea_clear,TextArea_Result);
        List<String> number_encode = new ArrayList<>();
        number_encode.add("10进制");
        number_encode.add("16进制");
        combo_p.getItems().addAll( number_encode);
        combo_p.getSelectionModel().select(0);
        combo_q.getItems().addAll( number_encode);
        combo_q.getSelectionModel().select(0);
        combo_n.getItems().addAll( number_encode);
        combo_n.getSelectionModel().select(0);
        combo_qinv.getItems().addAll( number_encode);
        combo_qinv.getSelectionModel().select(0);
        combo_d.getItems().addAll( number_encode);
        combo_d.getSelectionModel().select(0);
        combo_e.getItems().addAll(number_encode);
        combo_e.getSelectionModel().select(0);
        text_e.setText("65537");
        combo_dp.getItems().addAll( number_encode);
        combo_dp.getSelectionModel().select(0);
        combo_dq.getItems().addAll( number_encode);
        combo_dq.getSelectionModel().select(0);

        List<String> str_encode = new ArrayList<>();
        str_encode.add("Raw");
        str_encode.add("Hex");
        str_encode.add("Base64");
        combo_pub.getItems().addAll( "Hex","Base64");
        combo_pub.getSelectionModel().select(1);
        combo_pri.getItems().addAll( "Hex","Base64");
        combo_pri.getSelectionModel().select(1);
        miwen_encoding.getItems().addAll( str_encode);
        miwen_encoding.getItems().add( "File");
        miwen_encoding.getSelectionModel().select(2);
        mingwen_encoding.getItems().addAll( str_encode);
        mingwen_encoding.getItems().add( "File");
        mingwen_encoding.getSelectionModel().select(0);

        Map <String,String> map_method = new LinkedHashMap<>();
        map_method.put("【无】生成随机密钥对","generateKey");
        map_method.put("【公钥】加密明文","encrypt");
        map_method.put("【私钥】解密密文","decrypt");
        map_method.put("【私钥】生成签名","sign");
        map_method.put("【公钥】验证签名","verify_sign");
        map_method.put("【p、q】计算模数n","calc_n");
        map_method.put("【p、q、e】计算私钥d","calc_d");
        map_method.put("【p、q、e】生成公钥","calc_publicKey");
        map_method.put("【p、q、e】生成私钥","calc_privateKey");
        set_pub_pri_list.add("encrypt");
        set_pub_pri_list.add("decrypt");
        set_pub_pri_list.add("verify_sign");
        set_pub_pri_list.add("sign");
        // 获取keySet并将其转换为ObservableList
        Set<String> keySet = map_method.keySet();
        ObservableList<String> items = FXCollections.observableArrayList(keySet);
        listview_go.setItems(items);
        // 添加鼠标点击事件监听器
        listview_go.setOnMouseClicked(event -> {
            // 检查是否是双击（MouseEvent.DOUBLE_CLICK == 2）
            if (event.getClickCount() == 2) {
                // 获取当前选中的项
                String selectedItem = listview_go.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // 执行双击后要进行的操作
                    //明文
                    String mingwen_text = TextArea_Source.getText();
                    //，密文
                    String miwen_text = TextArea_Result.getText();

                    String mingwen_encoding_str = mingwen_encoding.getValue();
                    String miwen_encodin_str = miwen_encoding.getValue();
                    String method = map_method.get(selectedItem);
                    Class_Crypto_Rsa myThread = new Class_Crypto_Rsa();
                    myThread.setPubPriObj(text_pub,text_pri,combo_pub.getValue(),combo_pri.getValue());
                    myThread.setTextFieldObj(text_p,text_q,text_e,text_n,text_dp,text_dq,text_d,text_qinv);
                    myThread.setValue(TextArea_Source,TextArea_Result,method,mingwen_text,miwen_text,mingwen_encoding_str,miwen_encodin_str);
                    myThread.setComboboxObj(combo_p.getValue(),combo_q.getValue(),combo_e.getValue(),combo_n.getValue(),combo_dp.getValue(),combo_dq.getValue(),combo_d.getValue(),combo_qinv.getValue());
                    if(set_pub_pri_list.contains(method)){
                        myThread.setPublicKey(text_pub.getText());
                        myThread.setPrivateKey(text_pri.getText());
                    }else{
                        myThread.setParams(text_p.getText(),text_q.getText(),text_e.getText(),text_n.getText(),text_dp.getText(),text_dq.getText(),text_d.getText(),text_qinv.getText());
                    }
                    Thread t = new Thread(myThread);
                    t.setDaemon(true);
                    t.start();
                }
            }
        });

    }
    @FXML
    void func_pri_open_file(ActionEvent event) {

    }

    @FXML
    void func_pri_save_file(ActionEvent event) {

    }

    @FXML
    void func_pub_open_file(ActionEvent event) {

    }

    @FXML
    void func_pub_save_file(ActionEvent event) {

    }

    @FXML
    void func_text_mingwen_open_file(ActionEvent event) {
        String path = File_Open_Dialog(false);
        if(!path.isEmpty()){
            mingwen_encoding.getSelectionModel().select("File");
            TextArea_Source.setText(path);
        }
    }

    @FXML
    void func_text_miwen_open_file(ActionEvent event) {
        String path = File_Open_Dialog(false);
        if(!path.isEmpty()){
            miwen_encoding.getSelectionModel().select("File");
            TextArea_Result.setText(path);
        }
    }
    @FXML
    void func_clear(ActionEvent event) {
        text_d.clear();
        text_dp.clear();
        text_dq.clear();
        text_e.clear();
        text_n.clear();
        text_p.clear();
        text_q.clear();
        text_qinv.clear();
        text_pub.clear();
        text_pri.clear();
        TextArea_Result.clear();
        TextArea_Source.clear();
    }

}
