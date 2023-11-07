package com.qianxiao996.ctfknife;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CtfknifeApplication extends Application {
    public GridPane Plugins_start() throws IOException {
        return new FXMLLoader(CtfknifeApplication.class.getResource("ctfknife.fxml")).load();
    }
    @Override // javafx.application.Application
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CtfknifeApplication.class.getResource("ctfknife.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setResizable(false);
        stage.setTitle("编码解码工具v1.0");
        stage.getIcons().add(new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
