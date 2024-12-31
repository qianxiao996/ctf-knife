package com.qianxiao996.ctfknife.WebView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.f_alert_informationDialog;

public class Class_Html {
    public static VBox Open_Html(String htmlType) throws MalformedURLException, URISyntaxException {
        if(Objects.equals(htmlType, "零宽隐写")){
            Open_System_Browse("html/Zero-Width-Characters/index.html");
            return null;
        }else if (Objects.equals(htmlType, "与佛论禅(v2)")){
            return  Return_Browse("html/talk-with-buddha/index.html");
        }else if (Objects.equals(htmlType, "Sealed Book(天书)")){
            return  Return_Browse("html/Sealed-Book/index.html");
        }else if (Objects.equals(htmlType, "Rabbit Legacy")){
            return Return_Browse("html/Rabbit-Legacy/index.html");
        }else if (Objects.equals(htmlType, "CryptoJS AES/DES/3DES/RC4/Rabbit")){
            return Return_Browse("html/CryptoJS-AES-DES-3DES-RC4-Rabbit/index.html");
        }else if (Objects.equals(htmlType, "CyberChef")){
            Open_System_Browse("html/CyberChef/index.html");
            return null;
        }else if (Objects.equals(htmlType, "蝌蚪文加解密")){
            return Return_Browse("html/Tadpole/index.html");
        }


        else{
            f_alert_informationDialog("Prompt", "", "该加密没有Html文件! ");
            return null;
        }
//        return Return_Browse("index.html");
    }
    public static void Open_System_Browse(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            f_alert_informationDialog("Prompt", "", filePath+" 文件不存在! ");
            return;
        }
        // 指定HTML文件的路径
        File htmlFile =new File(filePath);
        // 检查Desktop是否支持打开文件
        if (!Desktop.isDesktopSupported()) {
            f_alert_informationDialog("提示", "", "此系统不支持桌面。");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(htmlFile.toURI());
        } catch (IOException e) {
            f_alert_informationDialog("提示", "", e.getMessage());
        }
    }
    public static VBox Return_Browse(String html_path) throws MalformedURLException {
        // 创建WebView并加载HTML文件
        WebView webview = new WebView();
        Path path = Paths.get(html_path).toAbsolutePath(); // 获取绝对路径
        // 将 Path 转换为 URI，再转换为 URL
        URI uri = path.toUri();
        URL url = uri.toURL();
//        URL url =  new URL(html_path);
        webview.getEngine().load(url.toExternalForm());
        // 设置WebView的尺寸策略使其可以自由扩展
        webview.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        webview.setMinSize(10, 10); // 设置一个合理的最小尺寸
        // 使用VBox作为容器，并设置其增长属性
        VBox temp_vbox = new VBox(webview);
        VBox.setVgrow(webview, Priority.ALWAYS);
        return temp_vbox;
    }
}
