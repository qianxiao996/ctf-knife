package com.qianxiao996.ctfknife.Utils;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import com.qianxiao996.ctfknife.Controller.Encrypt.Utils;
import com.qianxiao996.ctfknife.CtfknifeApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.bouncycastle.util.encoders.Hex;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

import static com.qianxiao996.ctfknife.Controller.Encrypt.Controller_Xor.hexStringToByteArray;
import static com.qianxiao996.ctfknife.Encode.Class_Encode.read_file;

public class Conn {

    public static String ExEjs(String jsifilename, String method_name, String text) {
        try (InputStream inputStream = CtfknifeApplication.class.getResourceAsStream(jsifilename);
             Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)) {
            // 获取 JavaScript 引擎
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("nashorn");

            // 执行 JavaScript 代码
            engine.eval(reader);

            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction(method_name, text);
            return (String) result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public static String ExEjs_3(String jsifilename, String method_name,String source_text, String params1,String params2) {
        try (InputStream inputStream = CtfknifeApplication.class.getResourceAsStream(jsifilename);
             Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)) {
            // 获取 JavaScript 引擎
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("nashorn");
            // 执行 JavaScript 代码
            engine.eval(reader);
            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction(method_name, source_text,params1,params2);
            return (String) result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public static String ExEjs_2(String jsifilename, String method_name, String params1,String params2) {
        try (InputStream inputStream = CtfknifeApplication.class.getResourceAsStream(jsifilename);
             Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)) {
            // 获取 JavaScript 引擎
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("nashorn");
            // 执行 JavaScript 代码
            engine.eval(reader);
            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction(method_name, params1,params2);
            return (String) result;
        } catch (Exception e) {
            return  e.getMessage();
        }
    }
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static String generateRandomString(int LENGTH) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    public static void   set_button_icon(Button button, String icon_path){
        Image icon_image= new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream(icon_path)));
        ImageView iconView = new ImageView(icon_image);
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);
        iconView.setPreserveRatio(true); // 保持宽高比
        // 设置按钮的图形
        button.setGraphic(iconView);
        button.setText("");
        button.setPrefWidth(20);
        button.setPrefHeight(20);
//        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button.setStyle("-fx-padding: 0;-fx-background-color: transparent; -fx-border-color: transparent;");

    }
    public static void   set_button_copy(Button button, TextArea temp_textarea){
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClipboardUtil.setStr(temp_textarea.getText());
            }
        });
    }
    public static void   set_button_paste(Button button, TextArea temp_textarea) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                temp_textarea.setText(ClipboardUtil.getStr());
            }
        });
    }

    public static void   set_button_clear(Button button, TextArea temp_textarea) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                temp_textarea.clear();
            }
        });
    }

    public static void   set_button_to_source(Button button, TextArea source_textarea,TextArea result_textarea) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                source_textarea.setText(result_textarea.getText());
            }
        });
    }
    public static byte[] Str_To_Bytes(String encoding, String text){
        switch (encoding) {
            case "UTF-8":
                return text.getBytes(StandardCharsets.UTF_8);
            case "GBK":
                return text.getBytes(Charset.forName("GBK"));
            case "GB2312":
                return text.getBytes(Charset.forName("GB2312"));
            case "GB18030":
                return text.getBytes(Charset.forName("GB18030"));
            case "ISO-8859-1":
            case "Latin1":
                return text.getBytes(StandardCharsets.ISO_8859_1);
            case "UTF-16":
                return text.getBytes(StandardCharsets.UTF_16);
            case "UTF-16BE":
                return text.getBytes(StandardCharsets.UTF_16BE);
            case "UTF-16LE":
                return text.getBytes(StandardCharsets.UTF_16LE);
            case "Base64":
                return Base64.getDecoder().decode(text);
            case "Hex":
                return hexStringToByteArray(text);
            case "File":
                return  read_file(text);
            case "Raw":
            default:
                return text.getBytes();
        }
    }
    public static byte[] read_file(String path) {
        try{
            if(! Files.exists(Paths.get(path))) {
                return  "Error: 文件不存在！".getBytes();
            }else{
                return Files.readAllBytes(Paths.get(path));
            }
        }catch (Exception e){
            return  ("Error: "+e.getMessage()).getBytes();
        }
    }
    public static String Get_Real_Str(String text, String encoding) {
        if (encoding.equals("Hex")) {
            return new String(Hex.decode(text));
        } else if (encoding.equals("Base64")) {
            return new String(Base64.getDecoder().decode(text));
        }else{
            return text;
        }
    }

    public static String Bytes_To_Str(byte[] text, String encding) {
        String  result;
        switch (encding) {
            case "UTF8":
            case "File":
                result = new String(text, StandardCharsets.UTF_8);
                break;
            case "UTF16":
                result = new String(text, StandardCharsets.UTF_16);
                break;
            case "UTF16BE":
                result = new String(text, StandardCharsets.UTF_16BE);
                break;
            case "UTF16LE":
                result = new String(text, StandardCharsets.UTF_16LE);
                break;
            case "Base64":
                result = new String(Base64.getEncoder().encode(text));
                break;
            case "Hex":
                result = Utils.bytesToHex(text);
                break;
            case "Latin1":
                result = new String(text, StandardCharsets.ISO_8859_1);
                break;
            case "Raw":
            default:
                result = new String(text);
                break;
        }
        return result;
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
    public static String File_Open_Dialog(boolean is_img) { // 处理单击事件
        FileChooser chooser = new FileChooser(); // 创建一个文件对话框
        chooser.setTitle("打开文件"); // 设置文件对话框的标题
//        chooser.setInitialDirectory(new File("E:\\")); // 设置文件对话框的初始目录
        // 给文件对话框添加多个文件类型的过滤器
        if(is_img){
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("所有文件", "*.*"),
                    new FileChooser.ExtensionFilter("所有图片", "*.jpg", "*.gif", "*.bmp", "*.png"));
        }else{
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("所有文件", "*.*"));
        }

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
}
