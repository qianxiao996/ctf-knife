package com.qianxiao996.ctfknife;

import com.qianxiao996.ctfknife.Controller.Ctfknife_Controller;
import com.qianxiao996.ctfknife.Utils.Tray;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.*;

public class CtfknifeApplication extends Application {
//    public GridPane Plugins_start() throws IOException {
//        return new FXMLLoader(CtfknifeApplication.class.getResource("ctfknife.fxml")).load();
//    }
//    public static String Style_Css = "css/RedBlue.css";
    public static String Style_Css = "";
    public static String Tools_Name = "CTF-Knife";
    public static String Tools_Version = "v1.2";
    public static String Tools_Author = "浅笑996";
    public static String Tools_Github = "https://github.com/qianxiao996/ctf-knife";
    @Override // javafx.application.Application
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CtfknifeApplication.class.getResource("Ui/ctfknife.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setResizable(false);
        stage.setTitle(Tools_Name+" "+Tools_Version+" by "+Tools_Author);
        Image icon_image= new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png")));
        stage.getIcons().add(icon_image);
        Ctfknife_Controller.read_config();
        Style_Css = Global_Config.getOrDefault("css", "css/default.css");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(Style_Css)).toExternalForm());
        stage.setScene(scene);
        Tray taryWindow = new Tray(stage, Objects.requireNonNull(getClass().getResource("img/ico.png")).toString(), "CTF-Knife");

        // 添加窗口改变状态的监听器
        stage.iconifiedProperty().addListener((obs, wasIconified, isNowIconified) -> {
            if (isNowIconified ) {
                // 当窗口最小化时执行的代码
                // Hide the window and show the tray icon.
                stage.hide();
                //实例化Tray对象,传参Stage，icon的url和要显示的标题

            }
        });

        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume(); // Consume the event to prevent default behavior.
            stage.hide(); // Hide the window instead of closing it.
        });
        taryWindow.tray();
        stage.show();

    }


    @Override
    public void stop() {
        // Remove the tray icon when the application stops.
//        if (trayapp != null) {
//            trayapp.removeTrayIcon();
//        }
    }

    public static void main(String[] args) {
        launch();
    }
}
