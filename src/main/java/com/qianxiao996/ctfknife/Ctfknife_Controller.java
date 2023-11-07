package com.qianxiao996.ctfknife;

import com.qianxiao996.ctfknife.Encode.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class Ctfknife_Controller {

    public  Menu encode_zaixian_tools;
    public  Menu encode_plugins;


    private static Connection c = null;
    private static Statement stmt = null;

    private  static final String db_name="conf/data.db";
    private  static final String tools_table_name="encode_tools";

    private  static final String plugins_table_name="encode_plugins";

    //存放ui窗口的值
    public String temp_ui_one_text="";
    public String temp_ui_two_text_1="";
    public String temp_ui_two_text_2="";

    @FXML
    private Button encode_add_tab;

    @FXML
    private Button encode_base_go;

    @FXML
    Button encode_decrypt_go;


    @FXML
    private Button encode_decode_go;

    @FXML
    private ComboBox<String> encode_encodeing;

    @FXML
    private Button encode_encrypt_fenxi;

    @FXML
    public Button encode_getflag_go;

    @FXML
    private CheckBox encode_line;

    @FXML
    private TextArea encode_result_text;

    @FXML
    private TextArea encode_source_text;

    @FXML
    private TabPane encode_tab;

    @FXML
    private Button encode_to_source;


    private String temp_ui_getflag_key1;
    private String temp_ui_getflag_key2;
    private String temp_ui_getflag_key_flag;



    public void initialize() throws SQLException {
        //加载tree列表
        load_data();
        //加载在线工具
        load_tools();
        //加载插件,此功能暂时不启用
//        load_plugins();

    }
    public void load_data(){
        encode_encodeing.getItems().addAll("UTF-8", "GBK", "GB2312", "GB18030");
        encode_encodeing.getSelectionModel().select(0);;

    }
    public void load_tools() throws SQLException {
        String sql = "SELECT distinct * from "+tools_table_name+" where tools_type is not null";
        ResultSet reslut = sql_search(db_name,sql);
        HashMap<String, ArrayList<ArrayList>> result_map = new HashMap<>();
        while (reslut.next()) {
            String tools_name = reslut.getString("tools_name");
            String tools_path = reslut.getString("tools_path");
            String tools_type = reslut.getString("tools_type");
            if (result_map.containsKey(tools_type)) {
                result_map.get(tools_type).add(new ArrayList<>(Arrays.asList(tools_name,tools_path,tools_type)));
            }
            // 如果字符不在 HashMap 中，将其添加到 HashMap 并设置计数为 1
            else {
                ArrayList<String> aaaaa= new ArrayList<>(Arrays.asList(tools_name,tools_path,tools_type));
                result_map.put(tools_type,new ArrayList<>(Arrays.asList(aaaaa)));
            }
        }
        for(String key:result_map.keySet()){
            Menu menu = new Menu(key);
            for(ArrayList<String> value:result_map.get(key)){
                MenuItem menuItem1 = new MenuItem(value.get(0));
                menuItem1.setOnAction(e -> {
                    try {
                        Zaxiangongju_Click(e,key+"/"+value.get(0),value.get(1));
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                menu.getItems().addAll(menuItem1);
            }
            encode_zaixian_tools.getItems().add(menu);
        }
        reslut.close();
        stmt.close();
        c.close();
    }
    public void load_plugins() throws SQLException {
        String sql = "SELECT distinct * from "+plugins_table_name+" where plugins_path is not null";
        ResultSet reslut = sql_search(db_name,sql);
        while (reslut.next()) {
            String plugins_name = reslut.getString("plugins_name");
            String plugins_path = reslut.getString("plugins_path");
            MenuItem menuItem1 = new MenuItem(plugins_name);
            menuItem1.setOnAction(e -> {
                Plugins_Click(e,plugins_name,plugins_path);
            });
            encode_plugins.getItems().add(menuItem1);

        }
        reslut.close();
        stmt.close();
        c.close();
    }
    public String Plugins_Click(Event event,String plugins_name,String plugins_path){
        System.out.println(plugins_name);
        System.out.println(plugins_name);
        return null;
    }
    @FXML
    void Zaxiangongju_Click(Event event,String name,String value) throws URISyntaxException, IOException {
        if(value.startsWith("http://") || value.startsWith("https://")){
            Desktop.getDesktop().browse(new URI(value));
        } else if(Files.exists(Paths.get(value))){
            if(is_image(value)){
                Show_Image(value,name);
            }else{
                f_alert_informationDialog("错误", "",value+"该位置暂时只支持图片显示！");
            }
            //调用图片显示
        }else{
            f_alert_informationDialog("错误", "",value+"文件不存在！");
        }
    }
    public void Show_Image(String iamge_path,String ui_label){
        // 创建一个新的Stage对象
        Stage secondaryStage = new Stage();
        // 创建ImageView，并设置图片
        ImageView imageView = new ImageView(new File(iamge_path).toURI().toString());
        // 创建StackPane容器，将ImageView添加到容器中
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        // 创建一个新的Scene对象，并将其设置为新窗口的根节点
        Scene scene = new Scene(root, imageView.getImage().getWidth(), imageView.getImage().getHeight());
        secondaryStage.setScene(scene);
        // 打开新窗口并显示它
        secondaryStage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        secondaryStage.setTitle(ui_label);
        secondaryStage.show();
        //图片显示
    }
    public boolean is_image(String path){
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i > 0) {
            extension = path.substring(i);
        }
        if(".jpg".equals(extension) ||".jpeg".equals(extension) ||".png".equals(extension)||".gif".equals(extension)  ){
            return  true;
        }else{
            return false;
        }
    }
    @FXML
    void encode_close_tab(Event event) {
//        String text;
        try{
            //获取Tab的标题
            Tab item = (Tab) event.getSource();
//            text = item.getText();
        }catch (Exception e){
            //获取TabPane下Tab的标题
            TabPane item = (TabPane) event.getSource();
//            text = item.getTabs().toString();
//            System.out.println(text);
        }
//        System.out.println("Mouse click on label:"+text);
        if(encode_tab.getTabs().size()<=1){
            f_alert_informationDialog("禁止关闭", "","只剩下一个标签页了，无法关闭！");
//            阻止关闭事件
            event.consume();
        }
    }

    @FXML
    void func_base_decode(ActionEvent event) throws IOException {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
//            ArrayList<String> open_file_list = new ArrayList<>();
            ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Base64(自定义)"));
            if(open_file_list.contains(encode_type)){
                Open_One_Ui("Base64自定义编码","base64编码表:","decode_base",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            }else{
                Class_Base_Decode myThread = new Class_Base_Decode();
                myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                Thread t = new Thread(myThread);

                t.setDaemon(true);
                t.start();
            }

        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }

    @FXML
    void func_base_encode(ActionEvent event) throws IOException {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
//            ArrayList<String> open_file_list = new ArrayList<>();
            ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Base64(自定义)"));
            if(open_file_list.contains(encode_type)){
                Open_One_Ui("Base64自定义编码","base64编码表:","encode_base",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            }else{
                Class_Base_Encode myThread = new Class_Base_Encode();
                myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                t.start();
            }


        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }

    @FXML
    void func_decode(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        String source_text = selectedTextArea_Source.getText();
//        ArrayList<String> open_file_list = new ArrayList<>();
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Hex->图片","Base64->图片"));
        if(open_file_list.contains(encode_type)){
            String img_path = File_Save_Dialog();
            selectedTextArea_Result.setText(img_path);
        }

        Boolean is_line = encode_line.isSelected();
        //设置文本
        if(!source_text.isEmpty()){
            Class_Decode myThread = new Class_Decode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }

    @FXML
    void func_decrypt(ActionEvent event) throws IOException {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            if (Objects.equals(encode_type, "四方密码")) {
                Open_Two_Ui("四方密码","Key1:","Key1:","decrypt",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            } else if (Objects.equals(encode_type, "仿射密码")) {
                Open_Two_Ui("仿射密码","Key1:","Key1:","decrypt",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            } else if(Objects.equals(encode_type,"维吉尼亚密码")){
                Open_One_Ui("维吉尼亚密码","请输入Key:","decrypt",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            }else if(Objects.equals(encode_type, "棋盘密码(ADFGX)")) {
                Open_One_Ui("棋盘密码(ADFGX)", "请输入编码表，长度为25:", "decrypt", selectedTextArea_Result, encode_type, source_text, encoding, is_line);
            } else if(Objects.equals(encode_type, "与佛论禅(2.0)")) {
                Open_One_Ui("与佛论禅(2.0)", "请输入Key:", "decrypt", selectedTextArea_Result, encode_type, source_text, encoding, is_line);
            }else{
                Class_Decrypt myThread = new Class_Decrypt();
                myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                t.start();
            }

        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }



    @FXML
    void func_tools(ActionEvent event) throws IOException {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            if(encode_type.equals("分割")){
                Open_One_Ui("字符分割","请输入分隔符号:","tools",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            }else if(encode_type.equals("拆分")){
                Open_One_Ui("拆分","请输入字符串长度:","tools",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            }else if(encode_type.equals("替换")){
                Open_Two_Ui("字符替换","需要替换字符:","替换后的字符:","tools",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            }else{
                Class_Tools myThread = new Class_Tools();
                myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                t.start();
            }


        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }
    @FXML
    void func_encode_add_tab(ActionEvent event) {
        add_tab_();
    }

    @FXML
    void func_encode_base_go(ActionEvent event) {
        ArrayList<String> encode_type = new ArrayList<>(Arrays.asList("Base16","Base32","Base36","Base58","Base62","Base64","Base85","Base91","Base92"));
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            Class_Base_Decode_go myThread = new Class_Base_Decode_go();
            myThread.setValue(encode_base_go,selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();

        }else{
            f_alert_informationDialog("提示", "","憨批，没有源文本你解个dei~！");
        }

    }

    @FXML
    void func_encode_decode_go(ActionEvent event) {
        ArrayList<String> encode_type = new ArrayList<>(Arrays.asList("URL","Unicode","Escape(%U)","HtmlEncode","ASCII(2进制)","ASCII(8进制)","ASCII(10进制)","ASCII(16进制)","Shellcode","Qwerty","Hex->Str","百家姓编码","核心价值观编码"));
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            Class_Decode_go myThread = new Class_Decode_go();
            myThread.setValue(encode_decode_go,selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            Thread t = new Thread(myThread);

            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","憨批，没有源文本你解个dei~！");
        }

    }

    @FXML
    void func_encode_decrypto_go(ActionEvent event) throws IOException {
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            Open_Two_Ui("一键解密","Key1:","Key1:","decrypt_go",selectedTextArea_Result,"",source_text,encoding,is_line);
        }else{
            f_alert_informationDialog("提示", "","憨批，没有源文本你解个dei~！");
        }

    }

    @FXML
    void func_encode_encrypt_fenxi(ActionEvent event) {

    }

    @FXML
    void func_encode_getflag_go(ActionEvent event) throws IOException {
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            Open_GetFlag_Ui(selectedTextArea_Result,source_text,encoding);
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }
    }

    @FXML
    void func_encode_to_source(ActionEvent event) {
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        selectedTextArea_Source.setText(selectedTextArea_Result.getText());
        selectedTextArea_Result.clear();
    }

    @FXML
    void func_encrypt(ActionEvent event) throws IOException {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            if(Objects.equals(encode_type, "栅栏密码")){
                Open_One_Ui("栅栏密码","请输入分栏数:","encrypt",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            } else if (Objects.equals(encode_type, "四方密码")) {
                Open_Two_Ui("四方密码","Key1:","Key1:","encrypt",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            } else if (Objects.equals(encode_type, "仿射密码")) {
                Open_Two_Ui("仿射密码","Key1:","Key1:","encrypt",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            } else if(Objects.equals(encode_type, "维吉尼亚密码")) {
                Open_One_Ui("维吉尼亚密码", "请输入Key:", "encrypt", selectedTextArea_Result, encode_type, source_text, encoding, is_line);
            }else if(Objects.equals(encode_type, "棋盘密码(ADFGX)")) {
                Open_One_Ui("棋盘密码(ADFGX)", "请输入编码表，长度为25:", "encrypt", selectedTextArea_Result, encode_type, source_text, encoding, is_line);
            }else if(Objects.equals(encode_type, "与佛论禅(2.0)")) {
                Open_One_Ui("与佛论禅(2.0)", "请输入Key:", "encrypt", selectedTextArea_Result, encode_type, source_text, encoding, is_line);
            }else{
                Class_Encrypt myThread = new Class_Encrypt();
                myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                t.start();
            }

        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }

    @FXML
    void func_jinzhi(ActionEvent event) throws IOException {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            try {
                Integer.parseInt(source_text,16);
                if (Objects.equals(encode_type, "自定义")) {
                    Open_Two_Ui("自定义","转换前的进制:","转后前的进制:","binary",selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                }else{
                    Class_Binary myThread = new Class_Binary();
                    myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
                    Thread t = new Thread(myThread);
                    t.setDaemon(true);
                    t.start();
                }
            }catch (Exception e){
                f_alert_informationDialog("提示", "","请输入数字！");
            }
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }

    }



    @FXML
    void func_encode(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String encode_type = item.getText();
        String encoding = encode_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        boolean is_line = encode_line.isSelected();
//        ArrayList<String> open_file_list = new ArrayList<>();
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("图片->Hex","图片->Base64"));
        if(open_file_list.contains(encode_type)){
            String img_path = File_Open_Dialog();
            selectedTextArea_Source.setText(img_path);
        }
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            Class_Encode myThread = new Class_Encode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,encoding,is_line);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }
//        System.out.println(source_text+selectedTextArea_Result.getText());
    }
    public Object[] get_tab_textarea_obj(){
        Object[] result = new Object[2];
        //获取当前激活页
        Tab selectedTab = encode_tab.getSelectionModel().getSelectedItem();
//        String tab_title = selectedTab.getText();
        Node selectedContent = selectedTab.getContent();

        if (selectedContent.lookup("#TextArea_Source") != null) {
            //获取选中的tab内的textarea的文本
            TextArea selectedTextArea_Source = (TextArea) selectedContent.lookup("#TextArea_Source");
            TextArea selectedTextArea_Result = (TextArea) selectedContent.lookup("#TextArea_Result");
//            System.out.println("Mouse click on label:"+selectedTextArea_Source.getText()+selectedTextArea_Result.getText());
            result[0] = selectedTextArea_Source;
            result[1] = selectedTextArea_Result;
            return result;
        }else {
            f_alert_informationDialog("提示", "","无法获取到源文本！");
        }
        return result;
    }
    public static void f_alert_informationDialog(String title,String p_header, String p_message){
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle(title);
        _alert.setHeaderText(p_header);
        _alert.setContentText(p_message);
//        _alert.initOwner();
        _alert.show();
    }
    //双击添加tab pane
    @FXML
    void tab_add_pane(MouseEvent event) {
        if (event.getClickCount()==2) {
            add_tab_();
        }

    }

    public void add_tab_() {
        ArrayList<String> all_tab_name = new ArrayList<>(Arrays.asList("梦媛", "涵钰", "妲可", "含钰", "连倩", "辰泽", "涵博", "海萍", "祖儿", "佳琪", "诗晗", "之言", "清妍", "淑媛", "智妍", "晴然",
                "树静",
                "娜娜", "瑞楠", "晓满", "婉雅", "雨婷", "筱满", "雅文", "玉琪", "敖雯", "文殊", "喻喧", "海英", "舒欣", "云亿", "莨静", "雅芝",
                "蕴兵",
                "乐乐", "之恋", "小满", "悦心", "可人", "忆初", "衬心", "诠释", "尘封", "奔赴", "心鸢", "晴栀", "堇年", "青柠", "埋葬", "夏墨",
                "随风",
                "屿暖", "深邃", "途往", "迷离", "槿城", "零落", "余笙", "梦呓", "墨凉", "晨曦", "纪年", "未央", "失语", "柠栀", "梦巷", "九离",
                "暮雨",
                "木兮", "浅歌", "沐北", "惜颜", "素笺", "锁心", "柠萌", "卿歌", "归期", "予别", "情笙", "缥缈", "轩辕", "浮光", "缠绵", "碧影",
                "星愿",
                "星月", "星雨", "沧澜", "醉月", "春媱", "夏露", "秋颜", "冬耀", "缱绻", "涟漪", "若溪", "微凉", "暖阳", "半夏", "崖悔", "洛尘",
                "矜柔",
                "绚烂", "矫情", "真淳", "明媚", "迷离", "隐忍", "灼热", "幻灭", "落拓", "锦瑟", "妖娆", "邪殇", "离殇", "恋夏", "梦琪", "忆柳",
                "之桃",
                "慕青", "问兰", "尔岚", "元香", "初夏", "沛菡", "傲珊", "曼文", "乐菱", "痴珊", "恨玉", "惜文", "香寒", "新柔", "语蓉", "海安",
                "夜蓉",
                "涵柏", "水桃", "醉蓝", "春儿", "语琴", "从彤", "傲晴", "语兰", "又菱", "碧彤", "元霜", "怜梦", "紫寒", "妙彤", "曼易", "南莲",
                "紫翠",
                "雨寒", "易烟", "如萱", "若南", "寻真", "晓亦", "向珊", "慕灵", "以蕊", "寻雁", "映易", "雪柳", "孤岚", "笑霜", "海云", "凝天",
                "沛珊",
                "寒云", "冰旋", "宛儿", "绿真", "盼儿", "晓霜", "碧凡", "夏菡", "若烟", "半梦", "雅绿", "冰蓝", "灵槐", "平安", "书翠", "翠风",
                "香巧",
                "代云", "梦曼", "幼翠", "友巧", "听寒", "梦柏", "醉易", "访旋", "亦玉", "凌萱", "访卉", "怀亦", "笑蓝", "春翠", "靖柏", "夜蕾",
                "冰夏",
                "梦松", "书雪", "乐枫", "念薇", "靖雁", "寻春", "恨山", "从寒", "忆香", "觅波", "静曼", "凡旋", "以亦", "念露", "芷蕾", "千兰",
                "新波",
                "代真", "新蕾", "雁玉", "冷卉", "紫山", "千琴", "恨天", "傲芙", "盼山", "怀蝶", "冰兰", "山柏", "翠萱", "恨松", "问旋", "从南",
                "白易",
                "问筠", "如霜", "半芹", "丹珍", "冰彤", "亦寒", "寒雁", "怜云", "寻文", "乐丹", "翠柔", "谷山", "之瑶", "冰露", "尔珍", "谷雪",
                "乐萱",
                "涵菡", "海莲", "傲蕾", "青槐", "冬儿", "易梦", "惜雪", "宛海", "之柔", "夏青", "亦瑶", "妙菡", "春竹", "痴梦", "紫蓝", "晓巧",
                "幻柏",
                "元风", "冰枫", "访蕊", "南春", "芷蕊", "凡蕾", "凡柔", "安蕾", "天荷", "含玉", "书兰", "雅琴", "书瑶", "春雁", "从安", "夏槐",
                "念芹",
                "怀萍", "代曼", "幻珊", "谷丝", "秋翠", "白晴", "海露", "代荷", "含玉", "书蕾", "听白", "访琴", "灵雁", "秋春", "雪青", "乐瑶",
                "含烟",
                "涵双", "平蝶", "雅蕊", "傲之", "灵薇", "绿春", "含蕾", "从梦", "从蓉", "初丹", "听兰", "听蓉", "语芙", "夏彤", "凌瑶", "忆翠",
                "幻灵",
                "怜菡", "紫南", "依珊", "妙竹", "访烟", "怜蕾", "映寒", "友绿", "冰萍", "惜霜", "凌香", "芷蕾", "雁卉", "迎梦", "元柏", "代萱",
                "紫真",
                "千青", "凌寒", "紫安", "寒安", "怀蕊", "秋荷", "涵雁", "以山", "凡梅", "盼曼", "翠彤", "谷冬", "新巧", "冷安", "千萍", "冰烟",
                "雅阳",
                "友绿", "南松", "诗云", "飞风", "寄灵", "书芹", "幼蓉", "以蓝", "笑寒", "忆寒", "秋烟", "芷巧", "水香", "映之", "醉波", "幻莲",
                "夜山",
                "芷卉", "向彤", "小玉", "幼南", "凡梦", "尔曼", "念波", "迎松", "青寒", "笑天", "涵蕾", "YYDS", "栓Q", "辣鸡CTF,毁我青春"));
        Label temp_label_source = new Label("Source");
        Label temp_label_result = new Label("Result");
        TextArea temp_textarea_source = new TextArea();
        TextArea temp_textarea_result = new TextArea();
        temp_textarea_source.setId("TextArea_Source");
        temp_textarea_result.setId("TextArea_Result");
        VBox temp_vbox = new VBox();
        VBox.setVgrow(temp_textarea_result, Priority.ALWAYS);
        VBox.setVgrow(temp_textarea_source, Priority.ALWAYS);
        temp_vbox.setPadding(new Insets(5,5,5,5));
        temp_vbox.setSpacing(3);
        temp_vbox.getChildren().addAll(temp_label_source,temp_textarea_source,temp_label_result,temp_textarea_result);
        //随机取名字
        Random random = new Random();
        int n = random.nextInt(all_tab_name.size());
        String tab_name = all_tab_name.get(n);
        Tab temp_tab = new Tab(tab_name);
        temp_tab.setContent(temp_vbox);
        encode_tab.getTabs().add(temp_tab);
        encode_tab.getSelectionModel().select(temp_tab);
        temp_tab.setOnCloseRequest(this::encode_close_tab);
    }


    public String File_Open_Dialog() { // 处理单击事件
        FileChooser chooser = new FileChooser(); // 创建一个文件对话框
        chooser.setTitle("打开文件"); // 设置文件对话框的标题
//        chooser.setInitialDirectory(new File("E:\\")); // 设置文件对话框的初始目录
        // 给文件对话框添加多个文件类型的过滤器
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件", "*.*"),
                new FileChooser.ExtensionFilter("所有图片", "*.jpg", "*.gif", "*.bmp", "*.png"));
        // 显示文件打开对话框，且该对话框支持同时选择多个文件
        File file = chooser.showOpenDialog(null); // 显示文件打开对话框
        if (file == null) {
            // 文件对象为空，表示没有选择任何文件
            return "";
//            label.setText("未选择任何文件");
        } else { // 文件对象非空，表示选择了某个文件
            return file.getAbsolutePath();
//            label.setText("准备打开的文件路径是："+file.getAbsolutePath());
        }
    }
    public static String File_Save_Dialog() {
        FileChooser chooser = new FileChooser(); // 创建一个文件对话框
        chooser.setTitle("保存文件"); // 设置文件对话框的标题
//        chooser.setInitialDirectory(new File("E:\\")); // 设置文件对话框的初始目录
        // 创建一个文件类型过滤器
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件", "*.*"),
                new FileChooser.ExtensionFilter("所有图片", "*.jpg", "*.gif", "*.bmp", "*.png"));
        // 给文件对话框添加文件类型过滤器
        File file = chooser.showSaveDialog(null); // 显示文件保存对话框
        if (file == null) { // 文件对象为空，表示没有选择任何文件
            return null;
        } else { // 文件对象非空，表示选择了某个文件
            return file.getAbsolutePath();
        }
    }
    //type encode_base64,decode_base64
    public void Open_One_Ui(String title,String label_text,String type,TextArea selectedTextArea_Result,String encode_type,String source_text,String encoding,boolean is_line) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui_One.fxml"));
        Parent parent = loader.load();
        Controller_One target = loader.getController();
        target.label.setText(label_text);
        target.line_text.setText(temp_ui_one_text);
        Scene scene = new Scene(parent);
//        scene.getStylesheets().addAll(
//                Objects.requireNonNull(MainApplication.class.getResource("css/color.css")).toExternalForm(),// 添加色彩
//                Objects.requireNonNull(MainApplication.class.getResource("css/core.css")).toExternalForm()// 添加css
//        );
        stage.setScene(scene);
        stage.setTitle(title);

        target.setMainController(this,selectedTextArea_Result,encode_type,source_text,encoding,is_line,type);
        stage.show();
    }
    public void Open_Two_Ui(String title,String label_text1,String label_text2,String type,TextArea selectedTextArea_Result,String encode_type,String source_text,String encoding,boolean is_line) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui_Two.fxml"));
        Parent parent = loader.load();
        Controller_Two target = loader.getController();
        target.label_1.setText(label_text1);
        target.label_2.setText(label_text2);
        target.line_text_1.setText(temp_ui_two_text_1);
        target.line_text_2.setText(temp_ui_two_text_2);
        Scene scene = new Scene(parent);
//        scene.getStylesheets().addAll(
//                Objects.requireNonNull(MainApplication.class.getResource("css/color.css")).toExternalForm(),// 添加色彩
//                Objects.requireNonNull(MainApplication.class.getResource("css/core.css")).toExternalForm()// 添加css
//        );
        stage.setScene(scene);
        stage.setTitle(title);
        target.setMainController(this,selectedTextArea_Result,encode_type,source_text,encoding,is_line,type);
        stage.show();
    }
    //点击去顶修改主窗口的变量，方便下次打开初始设置。
    public void Change_Ui_One_Value(String text){
        temp_ui_one_text = text;
    }
    public void Change_Ui_Two_Value(String text_1,String text_2){
        temp_ui_two_text_1 = text_1;
        temp_ui_two_text_2 = text_2;
    }
    public static ResultSet sql_search(String db_name, String sql)
    {
        stmt=null;
        c=null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + db_name);
            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            return stmt.executeQuery(sql);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            f_alert_informationDialog("DB : Connection:", "", e.toString());
//            //System.exit(0);
        }
        finally {

        }
//        System.out.println("Operation done successfully");

        return null;
    }

    public void Change_Ui_get_flag_Value(String key_1,String key_2,String flag){
        temp_ui_getflag_key1 = key_1;
        temp_ui_getflag_key2 = key_2;
        temp_ui_getflag_key_flag = flag;
    }

    public void Open_GetFlag_Ui(TextArea selectedTextArea_Result,String source_text,String encoding) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui_GetFlag.fxml"));
        Parent parent = loader.load();
        Controller_GetFlag target = loader.getController();
        Scene scene = new Scene(parent);
//        scene.getStylesheets().addAll(
//                Objects.requireNonNull(MainApplication.class.getResource("css/color.css")).toExternalForm(),// 添加色彩
//                Objects.requireNonNull(MainApplication.class.getResource("css/core.css")).toExternalForm()// 添加css
//        );
        stage.setScene(scene);
        stage.setTitle("一键GetFlag");
        target.line_text_key1.setText(temp_ui_getflag_key1);
        target.line_text_key2.setText(temp_ui_getflag_key2);
        if(temp_ui_getflag_key_flag!=null&&!temp_ui_getflag_key_flag.isEmpty()){
            target.line_text_getflag.setText(temp_ui_getflag_key_flag);
        }
        target.setMainController(this,selectedTextArea_Result,source_text,encoding);
        stage.show();
    }
}