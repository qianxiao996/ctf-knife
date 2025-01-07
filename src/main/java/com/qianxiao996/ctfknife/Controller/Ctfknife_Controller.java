package com.qianxiao996.ctfknife.Controller;
import javafx.scene.input.KeyCode;
import cn.hutool.core.swing.clipboard.ClipboardUtil;
import com.qianxiao996.ctfknife.Base.Class_Base_Decode;
import com.qianxiao996.ctfknife.Base.Class_Base_Decode_go;
import com.qianxiao996.ctfknife.Base.Class_Base_Encode;
import com.qianxiao996.ctfknife.Binary.Class_Binary;
import com.qianxiao996.ctfknife.Crypto.Class_Crypto;
import com.qianxiao996.ctfknife.CtfknifeApplication;
import com.qianxiao996.ctfknife.Encode.Class_Decode;
import com.qianxiao996.ctfknife.Encode.Class_Decode_go;
import com.qianxiao996.ctfknife.Encode.Class_Encode;
import com.qianxiao996.ctfknife.Encrypt.Class_Decrypt;
import com.qianxiao996.ctfknife.Encrypt.Class_Encrypt;
import com.qianxiao996.ctfknife.Tools.Class_Tools;
import com.qianxiao996.ctfknife.Utils.Conn;
import com.qianxiao996.ctfknife.WebView.Class_Html;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.*;

import static com.qianxiao996.ctfknife.CtfknifeApplication.Style_Css;
import static com.qianxiao996.ctfknife.Utils.Conn.set_button_icon;

public class Ctfknife_Controller {

    @FXML
    private Tab tab_996;
    public  Menu encode_zaixian_tools;
    public  Menu encode_plugins;
    public Map<TreeItem<String>,EventHandler<ActionEvent>> Global_Click_event = new HashMap<>(); //所有的ree点击事件

    public static Connection c = null;
    public static Statement stmt = null;

    public static final String db_name="conf/data.db";
    private  static final String tools_table_name="encode_tools";
    public static Map<String,String> Global_Config = new HashMap<>();
    private  static final String plugins_table_name="encode_plugins";
    public  Boolean show_sider=false;
    //存放ui窗口的值
    public String temp_ui_one_text="";
    public String temp_ui_two_text_1="";
    public String temp_ui_two_text_2="";
    public TextArea TextArea_Source;
    public TextArea TextArea_Result;
    @FXML
    private final TreeItem<String> rootItem = new TreeItem<>("Root");

    @FXML
    private Button encode_base_go;

    @FXML
    Button encode_decrypt_go;

    @FXML
    private VBox sider_vbox;

    @FXML
    private TextField sider_search;

    @FXML
    private TreeView<String> sider_tree;
    @FXML
    private Button encode_decode_go;

    @FXML
    public ComboBox<String> input_encodeing;

    @FXML
    public ComboBox<String> output_encodeing;



    @FXML
    public Button encode_getflag_go;

    @FXML
    public CheckBox encode_line;



    @FXML
    private TabPane encode_tab;
    @FXML
    private MenuBar menubar;


    private String temp_ui_getflag_key1;
    private String temp_ui_getflag_key2;
    private String temp_ui_getflag_key_flag;
    @FXML
    private Button source_textarea_copy;

    @FXML
    private Button source_textarea_paste;
    @FXML
    private Button button_to_source;
    @FXML
    private Button source_textarea_clear;
    @FXML
    private Button is_open_sider;
    @FXML
    private Button result_textarea_copy;

    @FXML
    private Button result_textarea_paste;

    @FXML
    private Button result_textarea_clear;
    public void initialize() throws SQLException {
        change_sider(show_sider);
        //加载tree列表
        load_encode();
        //加载在线工具
        load_tools();
        init_sider_tree_data();
        //加载插件,此功能暂时不启用
//        load_plugins();
        set_button_icon(is_open_sider,"img/expand.png");
        set_button_icon(source_textarea_copy,"img/copy.png");
        set_button_icon(result_textarea_copy,"img/copy.png");
        set_button_icon(source_textarea_paste,"img/paste.png");
        set_button_icon(result_textarea_paste,"img/paste.png");
        set_button_icon(source_textarea_clear,"img/clear.png");
        set_button_icon(result_textarea_clear,"img/clear.png");
        set_button_icon(button_to_source,"img/to.png");
        tab_996.setContextMenu(get_ContextMenu(tab_996));

    }

    private void init_sider_tree_data() {
        ObservableList<Menu> menus = menubar.getMenus();
        // 创建根节点
//        TreeItem<String> rootItem = new TreeItem<>("Root");
        rootItem.setExpanded(true);
        // 遍历每个 Menu 并创建相应的 TreeItem
        for (Menu menu : menus) {
            // 创建一个 TreeItem 表示 Menu
            TreeItem<String> menuTreeItem = new TreeItem<>(menu.getText());
            // 处理 Menu 的子项
            addMenuItemsToTreeItem(menuTreeItem, menu.getItems());
            // 将 Menu 的 TreeItem 添加到根节点
            rootItem.getChildren().add(menuTreeItem);
        }
        // 设置 TreeView 的根节点
        sider_tree.setRoot(rootItem);
        sider_tree.setShowRoot(false);
        init_sider_tree_event();



    }
    private void init_sider_tree_event() {
        // 为 TreeView 添加点击事件
//        sider_tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                EventHandler<ActionEvent> handler = Global_Click_event.get(newValue);
//                if (handler != null) {
//                    Platform.runLater(() -> {
//                        // 创建一个新的 ActionEvent 实例
//                        ActionEvent event = new ActionEvent(newValue,newValue);
//                        // 触发事件处理器
//                        handler.handle(event);
//                    });
//                }
//            }
//        });
        sider_tree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 检查是否为双击
                TreeItem<String> selectedItem = sider_tree.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    EventHandler<ActionEvent> handler = Global_Click_event.get(selectedItem);
                    if (handler != null) {
                        Platform.runLater(() -> {
//                            TreeItem<String>  select_item  = sider_tree.getSelectionModel().getSelectedItem();
                            // 创建一个新的 ActionEvent 实例
                            ActionEvent actionEvent = new ActionEvent(selectedItem, null);
                            // 触发事件处理器
                            handler.handle(actionEvent);
                        });
                    }
                }
            }
        });
    }
    // 递归函数：处理 MenuItem 并添加到 TreeItem
    private void addMenuItemsToTreeItem(TreeItem<String> parentTreeItem, ObservableList<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            TreeItem<String> itemTreeItem = new TreeItem<>(menuItem.getText());
            parentTreeItem.getChildren().add(itemTreeItem);
            // 如果 menuItem 是 Menu 类型，递归处理其子项
            if (menuItem instanceof Menu) {
                addMenuItemsToTreeItem(itemTreeItem, ((Menu) menuItem).getItems());
            }else{
                Global_Click_event.put(itemTreeItem, menuItem.getOnAction());
            }
        }
    }

    public void load_encode(){
        input_encodeing.getItems().addAll("UTF-8", "GBK", "GB2312", "GB18030","ISO-8859-1");
        input_encodeing.getSelectionModel().select(0);
        output_encodeing.getItems().addAll("UTF-8", "GBK", "GB2312", "GB18030","ISO-8859-1");
        output_encodeing.getSelectionModel().select(0);

    }
    public void load_tools() throws SQLException {
        String sql = "SELECT distinct * from "+tools_table_name+" where tools_type is not null";
        ResultSet reslut = sql_search(db_name,sql);
        HashMap<String, ArrayList<ArrayList<String>>> result_map = new HashMap<>();
        if (reslut != null) {
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
        }
        for(String key:result_map.keySet()){
            Menu menu = new Menu(key);
            for(ArrayList<String> value:result_map.get(key)){
                MenuItem menuItem1 = new MenuItem(value.get(0));
                menuItem1.setOnAction(e -> {
                    try {
                        Zaxiangongju_Click(e,key+"/"+value.get(0),value.get(1));
                    } catch (URISyntaxException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                menu.getItems().addAll(menuItem1);
            }
            encode_zaixian_tools.getItems().add(menu);
        }
        if (reslut != null) {
            reslut.close();
        }
        stmt.close();
        c.close();
    }
    public void load_plugins() throws SQLException {
        String sql = "SELECT distinct * from "+plugins_table_name+" where plugins_path is not null";
        ResultSet reslut = sql_search(db_name,sql);
        if (reslut != null) {
            while (reslut.next()) {
                String plugins_name = reslut.getString("plugins_name");
                String plugins_path = reslut.getString("plugins_path");
                MenuItem menuItem1 = new MenuItem(plugins_name);
                menuItem1.setOnAction(e -> {
                    Plugins_Click(e,plugins_name,plugins_path);
                });
                encode_plugins.getItems().add(menuItem1);

            }
        }
        if (reslut != null) {
            reslut.close();
        }
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
        return ".jpg".equals(extension) || ".jpeg".equals(extension) || ".png".equals(extension) || ".gif".equals(extension);
    }
    @FXML
    void encode_close_tab(Event event) {
//        String text;
//        try{
            //获取Tab的标题
//            Tab item = (Tab) event.getSource();
//            text = item.getText();
//        }catch (Exception e){
            //获取TabPane下Tab的标题
//            TabPane item = (TabPane) event.getSource();
//            text = item.getTabs().toString();
//            System.out.println(text);
//        }
//        System.out.println("Mouse click on label:"+text);
        if(encode_tab.getTabs().size()<=1){
            f_alert_informationDialog("关闭", "","需要保留一个页面哦！");
//            阻止关闭事件
            event.consume();
        }
    }

    @FXML
    void func_base_decode(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Base64(自定义)"));
        if(open_file_list.contains(encode_type)){
            Add_Tag_One_Ui(encode_type, "decode_base",encode_type);
//          Open_One_Ui("Base64自定义编码","base64编码表:","decode_base",selectedTextArea_Result,encode_type,source_text);
            return;
        }
        String input_encodeing_str = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encodeing_str = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            Class_Base_Decode myThread = new Class_Base_Decode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encodeing_str,output_encodeing_str,is_line);
            Thread t = new Thread(myThread);

            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }



    }

    @FXML
    void func_base_encode(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Base64(自定义)"));
        if(open_file_list.contains(encode_type)){
            Add_Tag_One_Ui(encode_type, "encode_base",encode_type);
//                Open_One_Ui("Base64自定义编码","base64编码表:","encode_base",selectedTextArea_Result,encode_type,source_text);
            return;
        }
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            Class_Base_Encode myThread = new Class_Base_Encode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }
    }

    @FXML
    void func_decode(ActionEvent event) {
//        MenuItem item = (MenuItem) event.getSource();
        String encode_type = Get_Event_Source_Text(event);
//        String encode_type = item.getText();
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        String source_text = selectedTextArea_Source.getText();
        if(source_text.isEmpty()) {
            f_alert_informationDialog("提示", "","请输入源文本！");
            return;
        }
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("Hex->图片","Base64->图片","Hex->文件","Base64->文件"));
        if(open_file_list.contains(encode_type)){
            String img_path = Conn.File_Save_Dialog();
            if (img_path == null || img_path.isEmpty()) {
                return;
            }
            selectedTextArea_Result.setText(img_path);
        }
        Boolean is_line = encode_line.isSelected();
        //设置文本
        Class_Decode myThread = new Class_Decode();
        myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        t.start();

    }
    @FXML
    void func_encrypt_one_key(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_One_Ui(encode_type, "encrypt",encode_type);
    }
    @FXML
    void func_encrypt_two_key(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Two_Ui(encode_type, "encrypt",encode_type);
    }

    @FXML
    void func_decrypt(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            Class_Decrypt myThread = new Class_Decrypt();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }
    }
    @FXML
    void func_html(ActionEvent event) throws MalformedURLException, URISyntaxException {
        String html_type = Get_Event_Source_Text(event);
        VBox temp_vbox = Class_Html.Open_Html(html_type);
        if(temp_vbox!=null){
            // 创建新的Tab并添加WebView到其中
            Tab temp_tab = new Tab(html_type, temp_vbox);
            temp_tab.setContextMenu(get_ContextMenu(temp_tab));
            encode_tab.getTabs().add(temp_tab);
            encode_tab.getSelectionModel().select(temp_tab);
            // 添加关闭Tab时的事件处理
            temp_tab.setOnCloseRequest(this::encode_close_tab);
        }
    }


    @FXML
    void func_tools(ActionEvent event) throws IOException {
        String encode_type = Get_Event_Source_Text(event);
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            switch (encode_type) {
                case "分割":
                    Open_One_Ui("字符分割", "请输入分隔符号:", "tools", selectedTextArea_Result, encode_type, source_text);
                    break;
                case "拆分":
                    Open_One_Ui("拆分", "请输入字符串长度:", "tools", selectedTextArea_Result, encode_type, source_text);
                    break;
                case "替换":
                    Open_Two_Ui("字符替换", "需要替换字符:", "替换后的字符:", "tools", selectedTextArea_Result, encode_type, source_text);
                    break;
                default:
                    Class_Tools myThread = new Class_Tools();
                    myThread.setValue(selectedTextArea_Result, encode_type, source_text, input_encoding, output_encoding, is_line);
                    Thread t = new Thread(myThread);
                    t.setDaemon(true);
                    t.start();
                    break;
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
        ArrayList<String> encode_type = new ArrayList<>(Arrays.asList("Base16","Base32","Base36","Base58","Base62","Base64","Base85","Base91","Base92","Base100"));
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
            String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
            if(input_encoding.equals("Hex") || input_encoding.equals("Base64") || output_encoding.equals("Hex")|| output_encoding.equals("Base64")){
                f_alert_informationDialog("提示", "", "该功能不支持输入输出Hex、Base64编码！");
                return;
            }
            Class_Base_Decode_go myThread = new Class_Base_Decode_go();
            myThread.setValue(encode_base_go,selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
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
//        String encoding = input_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
            String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
            if(input_encoding.equals("Hex") || input_encoding.equals("Base64") || output_encoding.equals("Hex")|| output_encoding.equals("Base64")){
                f_alert_informationDialog("提示", "", "该功能不支持输入输出Hex、Base64编码！");
                return;
            }
            Class_Decode_go myThread = new Class_Decode_go();
            myThread.setValue(encode_decode_go,selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);

            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","憨批，没有源文本你解个dei~！");
        }

    }

    @FXML
    void func_encode_decrypto_go(ActionEvent event) throws IOException {
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
//        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(!source_text.isEmpty()){
            String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
            String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
            if(input_encoding.equals("Hex") || input_encoding.equals("Base64") || output_encoding.equals("Hex")|| output_encoding.equals("Base64")){
                f_alert_informationDialog("提示", "", "该功能不支持输入输出Hex、Base64编码！");
                return;
            }
            Open_Two_Ui("一键解密","Key1:","Key2:","decrypt_go",selectedTextArea_Result,"",source_text);
        }else{
            f_alert_informationDialog("提示", "","憨批，没有源文本你解个dei~！");
        }

    }

    @FXML
    void func_encode_encrypt_fenxi(ActionEvent event) {

    }

    @FXML
    void func_encode_getflag_go(ActionEvent event) throws IOException {
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }

        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
            String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
            if(input_encoding.equals("Hex") || input_encoding.equals("Base64") || output_encoding.equals("Hex")|| output_encoding.equals("Base64")){
                f_alert_informationDialog("提示", "", "该功能不支持输入输出Hex、Base64编码！");
                return;
            }
            Open_GetFlag_Ui(selectedTextArea_Result,source_text);
            f_alert_informationDialog("提示", "","此功能尚在试验阶段，结果可能不准确！");
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
//        selectedTextArea_Result.clear();
    }

    @FXML
    void func_encrypt(ActionEvent event) throws IOException {
//        MenuItem item = (MenuItem) event.getSource();
//        String encode_type = item.getText();
        String encode_type = Get_Event_Source_Text(event);
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();

        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        if(Objects.equals(encode_type, "栅栏密码")){
            Open_One_Ui("栅栏密码","请输入分栏数:","encrypt",selectedTextArea_Result,encode_type,source_text);
        }else {
            if(!source_text.isEmpty()){
                Class_Encrypt myThread = new Class_Encrypt();
                myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
                Thread t = new Thread(myThread);
                t.setDaemon(true);
                t.start();
            }else{
                f_alert_informationDialog("提示", "","请输入源文本！");
            }

        }
    }

    @FXML
    void func_jinzhi(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            try {
                Integer.parseInt(source_text,16);
                if (Objects.equals(encode_type, "自定义")) {
                    Open_Two_Ui("自定义","转换前的进制:","转后前的进制:","binary",selectedTextArea_Result,encode_type,source_text);
                }else{
                    Class_Binary myThread = new Class_Binary();
                    myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
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
        String encode_type = Get_Event_Source_Text(event);
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
//        ArrayList<String> open_file_list = new ArrayList<>();
        ArrayList<String> open_file_list = new ArrayList<>(Arrays.asList("图片->Hex","图片->Base64","文件->Hex","文件->Base64"));
        if(open_file_list.contains(encode_type)){
            String img_path = Conn.File_Open_Dialog(true);
            if(img_path.isEmpty()){
                return;
            }else{
                selectedTextArea_Source.setText(img_path);
            }
        }
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(!source_text.isEmpty()){
            Class_Encode myThread = new Class_Encode();
            myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            t.start();
        }else{
            f_alert_informationDialog("提示", "","请输入源文本！");
        }
//        System.out.println(source_text+selectedTextArea_Result.getText());
    }
    public String Get_Event_Source_Text(Event  event){
        Object source = event.getSource();
        if(source  instanceof MenuItem){
            MenuItem item = (MenuItem) source;
            return item.getText();
        } else if (source instanceof TreeItem) {
            TreeItem item =  (TreeItem)source;
            return (String) item.getValue();
        } else if (source instanceof Button) {
            Button button = (Button) source;
            return button.getText();
        } else if (source instanceof TextField) {
            TextField textField = (TextField) source;
            return textField.getText();
        } else {
            f_alert_informationDialog("提示", "","无法获取到源文本！");
            System.exit(1);
            return "";
        }

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
            result[0] = null;
            result[1] = null;
//            f_alert_informationDialog("提示", "","无法获取到源文本！");
        }
        return result;
    }
    public static void f_alert_informationDialog(String title,String p_header, String p_message){
        Platform.runLater(() -> {
            Alert _alert = new Alert(Alert.AlertType.INFORMATION);
            _alert.setTitle(title);
            // 设置图标
            Image image =new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png")));
            Stage stage = (Stage) _alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(image);  // 设置标题栏图标
    //        _alert.getDialogPane().setGraphic(new ImageView(image));

            // 添加样式表
            _alert.getDialogPane().getStylesheets().add(
                    Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm()
            );
            _alert.setHeaderText(p_header);
            _alert.setContentText(p_message);
    //        _alert.initOwner();
            _alert.show();
        });
    }

    private void renameTab(Tab tab) {
        // 如果 Tab 已经有 TextField，则不处理重复点击事件
        if (tab.getGraphic() instanceof TextField) return;

        // 记录原始文本以便取消操作时恢复
        String originalText = tab.getText();

        // 创建一个新的 TextField 并设置初始文本为当前 Tab 的文本
        TextField textField = new TextField(originalText);

        // 设置 TextField 样式以匹配 Tab 标题外观（可选）
        textField.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

        // 将 TextField 设置为 Tab 的图形属性，覆盖默认文本
        tab.setGraphic(textField);
        tab.setText(""); // 清空原始文本，使 TextField 可见

        // 焦点自动移到 TextField，并选中所有文本
        textField.requestFocus();
        textField.selectAll();

        // 监听 Enter 键以确认新名称
        textField.setOnAction(event -> {
            tab.setText(textField.getText());
            tab.setGraphic(null); // 移除图形属性，恢复默认显示
        });

        // 监听 Escape 键以取消重命名
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                tab.setText(originalText);
                tab.setGraphic(null); // 移除图形属性，恢复默认显示
            }
        });

        // 监听失去焦点事件
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // 当 TextField 失去焦点时
                tab.setText(textField.getText());
                tab.setGraphic(null); // 移除图形属性，恢复默认显示
            }
        });

    }
    private ContextMenu get_ContextMenu(Tab this_tab){
        // 创建右键菜单项
        MenuItem menuItem_close_this = new MenuItem("关闭");
        MenuItem menuItem_rename = new MenuItem("重命名");
        MenuItem menuItem_close_left = new MenuItem("关闭左侧标签页");
        MenuItem menuItem_close_right = new MenuItem("关闭右侧标签页");
        MenuItem menuItem_close_other = new MenuItem("关闭其他标签页");

        // 为菜单项添加事件处理程序（可选）
        menuItem_rename.setOnAction(e -> {
            if (this_tab != null) {
                renameTab(this_tab);
            }
        });
        menuItem_close_left.setOnAction(e -> {
            if (this_tab != null) {
                int selectedIndex = encode_tab.getTabs().indexOf(this_tab);
                if (selectedIndex > 0) {
                    // 移除所有在选定 Tab 左侧的 Tabs
                    encode_tab.getTabs().remove(0, selectedIndex);
                }
            }
        });
        menuItem_close_right.setOnAction(e -> {
            if (this_tab != null) {
                int selectedIndex = encode_tab.getTabs().indexOf(this_tab);
                if (selectedIndex >= 0 && selectedIndex < encode_tab.getTabs().size() - 1) {
                    // 移除所有在选定 Tab 右侧的 Tabs
                    encode_tab.getTabs().remove(selectedIndex + 1, encode_tab.getTabs().size());
                }
            }
        });
        menuItem_close_other.setOnAction(e -> {
            if (this_tab != null) {
                // 创建一个新的 Tabs 列表，只包含当前选中的 Tab
                encode_tab.getTabs().retainAll(this_tab);
            }
        });
        menuItem_close_this.setOnAction(e -> {
            if(encode_tab.getTabs().size()<=1){
                f_alert_informationDialog("关闭", "","需要保留一个页面哦！");
            }else{
                encode_tab.getTabs().remove(this_tab);
            }
        });



        // 创建上下文菜单并添加菜单项
        return new ContextMenu(menuItem_close_this,menuItem_rename,menuItem_close_left,menuItem_close_right,menuItem_close_other);
    }
    @FXML
    void tab_add_pane(MouseEvent event) {
        if (event.getClickCount()==2) {
            add_tab_();
        }
    }

    public void add_tab_() {
        ArrayList<String> all_tab_name = new ArrayList<>(Arrays.asList("梦媛", "涵钰", "妲可", "含钰", "连倩", "辰泽", "涵博", "海萍", "祖儿", "佳琪", "诗晗", "之言", "清妍", "淑媛", "智妍", "晴然",
                "树静",
                "瑞楠", "晓满", "婉雅", "雨婷", "筱满", "雅文", "玉琪", "敖雯", "文殊", "喻喧", "海英", "舒欣", "云亿", "莨静", "雅芝",
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


        try{
            //随机取名字
            Random random = new Random();
            int n = random.nextInt(all_tab_name.size());
            String tab_name = all_tab_name.get(n);
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Main_Zero.fxml"));
            Node fxmlNode = loader.load();

            // 创建新的Tab并添加WebView到其中
            Tab temp_tab = new Tab(tab_name,fxmlNode);
            temp_tab.setContextMenu(get_ContextMenu(temp_tab));
            encode_tab.getTabs().add(temp_tab);
            encode_tab.getSelectionModel().select(temp_tab);

            // 添加关闭Tab时的事件处理
            temp_tab.setOnCloseRequest(this::encode_close_tab);
        }catch (Exception e){
            f_alert_informationDialog("提示", "", "打开界面错误！"+e.getMessage());
        }
    }




    //type encode_base64,decode_base64
    public void Open_One_Ui(String title,String label_text,String type,TextArea selectedTextArea_Result,String encode_type,String source_text) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Ui_One.fxml"));
        Parent parent = loader.load();
        Controller_One target = loader.getController();
        target.label.setText(label_text);
        target.line_text.setText(temp_ui_one_text);
        Scene scene = new Scene(parent);
//        scene.getStylesheets().addAll(
//                Objects.requireNonNull(MainApplication.class.getResource("css/color.css")).toExternalForm(),// 添加色彩
//                Objects.requireNonNull(MainApplication.class.getResource("css/core.css")).toExternalForm()// 添加css
//        );
        stage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(title);
        target.setMainController(this,selectedTextArea_Result,encode_type,source_text,type);
        stage.show();
    }
    public void Open_Two_Ui(String title,String label_text1,String label_text2,String type,TextArea selectedTextArea_Result,String encode_type,String source_text) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Ui_Two.fxml"));
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
        stage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(title);
        target.setMainController(this,selectedTextArea_Result,encode_type,source_text,type);
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
//        System.out.println("Operation done successfully");

        return null;
    }

    public void Change_Ui_get_flag_Value(String key_1,String key_2,String flag){
        temp_ui_getflag_key1 = key_1;
        temp_ui_getflag_key2 = key_2;
        temp_ui_getflag_key_flag = flag;
    }

    public void Open_GetFlag_Ui(TextArea selectedTextArea_Result,String source_text) throws IOException {

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Ui_GetFlag.fxml"));
        Parent parent = loader.load();
        Controller_GetFlag target = loader.getController();
        Scene scene = new Scene(parent);
//        scene.getStylesheets().addAll(
//                Objects.requireNonNull(MainApplication.class.getResource("css/color.css")).toExternalForm(),// 添加色彩
//                Objects.requireNonNull(MainApplication.class.getResource("css/core.css")).toExternalForm()// 添加css
//        );
        stage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm());
        stage.setScene(scene);
        stage.setTitle("一键获取Flag");
        target.line_text_key1.setText(temp_ui_getflag_key1);
        target.line_text_key2.setText(temp_ui_getflag_key2);
        if(temp_ui_getflag_key_flag!=null&&!temp_ui_getflag_key_flag.isEmpty()){
            target.line_text_getflag.setText(temp_ui_getflag_key_flag);
        }
        target.setMainController(this,selectedTextArea_Result,source_text);
        stage.show();
    }
    @FXML
    void open_encode_images(ActionEvent event) throws IOException {
//        //打开巨多的编码图
        Stage secondaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Ui_Image.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setResizable(false);
        secondaryStage.setTitle("编码图");
        secondaryStage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm());
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }
    @FXML
    void open_encode_images_ascii(ActionEvent event) throws IOException {
        open_images("Ascii");

    }
    @FXML
    void open_encode_images_file(ActionEvent event) throws IOException {
        open_images("文件头");
    }
    void open_images(String text) throws IOException {
        //        //打开巨多的编码图
        Stage secondaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Ui_Image.fxml"));
        Parent root = fxmlLoader.load();
        // 获取控制器实例
        Controller_Image controller = fxmlLoader.getController();
        controller.setSelectItem(text);
        Scene scene = new Scene(root);
//        stage.setResizable(false);
        secondaryStage.setTitle("编码图");
        secondaryStage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm());
        secondaryStage.setScene(scene);
        secondaryStage.show();
    }

    @FXML
    void clear_text(ActionEvent event) {
        // 获取触发事件的对象
        Object source = event.getSource();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        // 检查对象是否是Node类型，以确保它可以有ID
        if (source instanceof Node) {
            Node node = (Node) source;
            String id = node.getId();
            if(id.toLowerCase().contains("source")){
                selectedTextArea_Source.clear();
            }else{
                selectedTextArea_Result.clear();
            }
        }
    }

    @FXML
    void copy_text(ActionEvent event) {
        Object source = event.getSource();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        // 检查对象是否是Node类型，以确保它可以有ID
        if (source instanceof Node) {
            Node node = (Node) source;
            String id = node.getId();
            if(id.toLowerCase().contains("source")){
                ClipboardUtil.setStr(selectedTextArea_Source.getText());

            }else{
                ClipboardUtil.setStr(selectedTextArea_Result.getText());
            }
        }
    }
    @FXML
    void paste_text(ActionEvent event) {
        Object source = event.getSource();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        // 检查对象是否是Node类型，以确保它可以有ID
        if (source instanceof Node) {
            Node node = (Node) source;
            String id = node.getId();
            if(id.toLowerCase().contains("source")){
                selectedTextArea_Source.setText(ClipboardUtil.getStr());
            }else{
                selectedTextArea_Result.setText(ClipboardUtil.getStr());
            }
        }
    }

    @FXML
    void show_hidden_search_sider(ActionEvent event) {
        show_sider =  !show_sider;
        change_sider(show_sider);

    }

    private void change_sider(Boolean  show_sider) {
//        sider_vbox.setVisible(show_sider);
//        sider_vbox.setManaged(show_sider);
        sider_search.setVisible(show_sider);
        sider_search.setManaged(show_sider);
        sider_tree.setVisible(show_sider);
        sider_tree.setManaged(show_sider);
        String image_path = "img/expand.png";
        if(show_sider){
            image_path= "img/collapse.png";
        }
        set_button_icon(is_open_sider,image_path);
    }
    @FXML
    void tree_search(KeyEvent event) {
        String  filterText = sider_search.getText();
        TreeItem<String> root = buildFilteredTree(rootItem, filterText);
        sider_tree.setRoot(root);
//        init_sider_tree_event();
    }
    /**
     * 递归遍历 TreeItem 及其子项，根据过滤条件更新可见性。
     */
    private TreeItem<String> buildFilteredTree(TreeItem<String> originalItem, String filterText) {
        // 检查当前项是否应包括
        boolean shouldInclude = false;
        if (originalItem.getValue() != null && originalItem.getValue().toLowerCase().contains(filterText.toLowerCase())) {
            shouldInclude = true;
        }
        // 递归处理子项，并收集匹配的子项
        List<TreeItem<String>> matchingChildren = new ArrayList<>();
        for (TreeItem<String> child : originalItem.getChildren()) {
            TreeItem<String> filteredChild = buildFilteredTree(child, filterText);
            if (filteredChild != null) {
                shouldInclude = true;
                matchingChildren.add(filteredChild);
            }
        }
        // 如果该项应该包括，则创建一个新的 TreeItem 并添加匹配的子项
        if (shouldInclude) {
            TreeItem<String> newItem = new TreeItem<>(originalItem.getValue());
            Global_Click_event.put(newItem, Global_Click_event.get(originalItem));
            // 确保新节点是展开的
            newItem.setExpanded(!filterText.isEmpty());
            newItem.getChildren().addAll(matchingChildren);
            return newItem;
        } else {
            return null; // 不包括此节点
        }
    }
    //开启一个仅有key的窗口
    @FXML
    void func_encrypt_xor(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/xor.fxml");
    }
    @FXML
    void func_encrypt_rc4(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/rc4.fxml");
    }

    private void Add_Tag_Ui(String tag_name,String fxml_path) {
        try{
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource(fxml_path));
            Node fxmlNode = loader.load();
            // 创建新的Tab并添加WebView到其中
            Tab temp_tab = new Tab(tag_name, fxmlNode);
            temp_tab.setContextMenu(get_ContextMenu(temp_tab));
            encode_tab.getTabs().add(temp_tab);
            encode_tab.getSelectionModel().select(temp_tab);
            // 添加关闭Tab时的事件处理
            temp_tab.setOnCloseRequest(this::encode_close_tab);
        }catch (Exception e){
            e.printStackTrace();
            f_alert_informationDialog("提示", "", "打开界面错误！"+e.getMessage());
        }

    }

    private void Add_Tag_One_Ui(String tag_name, String encode_catagory, String encode) {
        try{
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Main_One.fxml"));
            Node fxmlNode = loader.load();
            Controller_Main_One target = loader.getController();
            target.setMainController(this,encode_catagory,encode);
            if(encode.startsWith("HMAC")){
                target.button_encrypt.setText("签名");
                target.button_decrypt.setVisible(false);
                target.button_decrypt.setManaged(false);
            } else if (encode_catagory.startsWith("encode_")|| encode_catagory.startsWith("decode")) {
                target.button_encrypt.setText("编码");
                target.button_decrypt.setText("解码");
            }
            // 创建新的Tab并添加WebView到其中
            Tab temp_tab = new Tab(tag_name, fxmlNode);
            temp_tab.setContextMenu(get_ContextMenu(temp_tab));
            encode_tab.getTabs().add(temp_tab);
            encode_tab.getSelectionModel().select(temp_tab);
            // 添加关闭Tab时的事件处理
            temp_tab.setOnCloseRequest(this::encode_close_tab);
        }catch (Exception e){
            f_alert_informationDialog("提示", "", "打开界面错误！"+e.getMessage());
        }

    }
    private void Add_Tag_Two_Ui(String tag_name, String encode_catagory, String encode) {
        try{
            // 加载 FXML 文件
            FXMLLoader loader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/Main_Two.fxml"));
            Node fxmlNode = loader.load();
            Controller_Main_Two target = loader.getController();
            target.setMainController(this,encode_catagory,encode);
            // 创建新的Tab并添加WebView到其中
            Tab temp_tab = new Tab(tag_name, fxmlNode);
            temp_tab.setContextMenu(get_ContextMenu(temp_tab));
            encode_tab.getTabs().add(temp_tab);
            encode_tab.getSelectionModel().select(temp_tab);
            // 添加关闭Tab时的事件处理
            temp_tab.setOnCloseRequest(this::encode_close_tab);
        }catch (Exception e){
            f_alert_informationDialog("提示", "", "打开界面错误！"+e.getMessage());
        }

    }



    @FXML
    void func_aes(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/aes.fxml");
    }
    @FXML
    void func_des_3des(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/des.fxml");

    }

    @FXML
    void func_sm4(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/sm4.fxml");

    }
    @FXML
    void func_rsa(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/rsa.fxml");

    }
    @FXML
    void func_sm2(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        Add_Tag_Ui(encode_type,"Ui/Encrypt/sm2.fxml");
    }


    //现代密码
    @FXML
    void func_crypto(ActionEvent event) {
        String encode_type = Get_Event_Source_Text(event);
        if (encode_type.startsWith("HMAC")) {
            Add_Tag_One_Ui(encode_type, "crypto",encode_type);
            return;
        }
        String input_encoding = input_encodeing.getSelectionModel().getSelectedItem();
        String output_encoding = output_encodeing.getSelectionModel().getSelectedItem();
        Object[] result_obj = get_tab_textarea_obj();
        TextArea selectedTextArea_Source = (TextArea) result_obj[0];
        TextArea selectedTextArea_Result = (TextArea) result_obj[1];
        if(selectedTextArea_Source==null || selectedTextArea_Result==null ){
            f_alert_informationDialog("提示", "","当前页面未获取到源文本框或结果输出框，请切换Tab页！");
            return ;
        }
        boolean is_line = encode_line.isSelected();
        String source_text = selectedTextArea_Source.getText();
        //设置文本
        if(source_text.isEmpty()) {
            f_alert_informationDialog("提示", "","请输入源文本！");
            return;
        }
        Class_Crypto myThread = new Class_Crypto();
        myThread.setValue(selectedTextArea_Result,encode_type,source_text,input_encoding,output_encoding,is_line);
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        t.start();
    }
   @FXML
    void func_about(ActionEvent event) throws IOException {
       Stage secondaryStage = new Stage();
       FXMLLoader fxmlLoader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/About.fxml"));
       Scene scene = new Scene(fxmlLoader.load());
//        stage.setResizable(false);
       secondaryStage.setTitle("关于作者");
       secondaryStage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
       scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm());
       secondaryStage.setScene(scene);
       secondaryStage.show();
    }
    @FXML
    void func_change_css(ActionEvent event) {
        String type = Get_Event_Source_Text(event);
        switch (type) {
            case "红蓝":
                Style_Css = "css/RedBlue.css";
                break;
            case "暗黑":
                Style_Css = "css/Black.css";
                break;
            case "无":
            default:
                Style_Css = "css/default.css";
                input_encodeing.getScene().getWindow().getScene().getStylesheets().clear();
                break;
        }
        Global_Config.put("css", Style_Css);
        if(!Style_Css.isEmpty()){
            Scene scene = input_encodeing.getScene().getWindow().getScene();
            scene.getStylesheets().clear(); // 移除旧的样式表
            scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(Style_Css)).toExternalForm()); // 添加新的样式表
        }
        save_config();
    }
    public static void   read_config() {
        try{
            String sql = "SELECT distinct * from config";
            ResultSet reslut = sql_search(db_name,sql);
            if (reslut != null) {
                while (reslut.next()) {
                    String name = reslut.getString("name");
                    String value = reslut.getString("value");
                    Global_Config.put(name,value);
                }
            }
            if (reslut != null) {
                reslut.close();
            }
            stmt.close();
            c.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void   save_config() {
        // 假设 Global_Config 是一个静态的 HashMap<String, String>
        // 使用 try-with-resources 确保资源被正确关闭
        try{
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db_name);
                 PreparedStatement pstmt = conn.prepareStatement(
                                 "INSERT OR REPLACE INTO config (name, value) VALUES (?, ?)")) {

                for (Map.Entry<String, String> entry : Global_Config.entrySet()) {
                    pstmt.setString(1, entry.getKey());
                    pstmt.setString(2, entry.getValue());
                    pstmt.addBatch();
                }
                pstmt.executeBatch(); // 执行批量插入/更新操作

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @FXML
    void func_github(ActionEvent event) {
        try{
            Desktop.getDesktop().browse(new URI(CtfknifeApplication.Tools_Github));
        }catch (Exception e){
            f_alert_informationDialog("提示", "", "打开浏览器失败！\r\n"+e.getMessage());
        }
    }

}