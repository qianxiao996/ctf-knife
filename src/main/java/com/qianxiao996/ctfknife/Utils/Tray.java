package com.qianxiao996.ctfknife.Utils;

import javafx.application.Platform;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Tray {
    public Stage primaryStage;
    public SystemTray tray;
    public String icon;
    public String title;

    public Tray() {
        super();
    }

    public Tray(Stage primaryStage, String icon, String title) {
        super();
        this.primaryStage = primaryStage;
        this.tray = SystemTray.getSystemTray();
        this.icon =icon;
        this.title = title;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public SystemTray getTray() {
        return tray;
    }

    public void setTray(SystemTray tray) {
        this.tray = tray;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void tray() {
//        Platform.runLater(new Runnable() {
//            public void run() {
//                primaryStage.hide();
//            }
//        });
        Platform.setImplicitExit(false);
        URL url = null;
        try {
            url = new URL(icon);
        } catch (MalformedURLException e3) {
            e3.printStackTrace();
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(url.openStream());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        PopupMenu pop = new PopupMenu();
        MenuItem show = new MenuItem("打开程序");
        MenuItem exit = new MenuItem("退出程序");
        pop.add(show);
        pop.add(exit);

        TrayIcon trayIcon = new TrayIcon(image, title, pop);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(title);
        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
//                    tray.remove(trayIcon);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            primaryStage.show();
                            primaryStage.toFront();
                            primaryStage.requestFocus();
                            primaryStage.setIconified(false);
                            primaryStage.setAlwaysOnTop(true); // 设置窗口始终置顶
                            primaryStage.setAlwaysOnTop(false); // 取消窗口始终置顶
                        }
                    });
                }
            }
        });

        show.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
//                tray.remove(trayIcon);
                Platform.runLater(new Runnable() {
                    public void run() {
                        primaryStage.show();
                        primaryStage.toFront();
                        primaryStage.requestFocus();
                        primaryStage.setIconified(false);
                        primaryStage.setAlwaysOnTop(true); // 设置窗口始终置顶
                        primaryStage.setAlwaysOnTop(false); // 取消窗口始终置顶
                    }
                });
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                tray.remove(trayIcon);
                Platform.runLater(new Runnable() {
                    public void run() {
                        primaryStage.close();
                    }
                });
                Platform.exit();
            }
        });
    }
}