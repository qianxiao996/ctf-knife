package com.qianxiao996.ctfknife.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.db_name;
import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.sql_search;

public class Controller_Image {
    private final String tools_image_table_name="encode_images";
    @FXML
    private TextField search_text;
    @FXML
    private ListView<String> listview_image;
    @FXML
    private Pane stackpane_image;

    @FXML
    private ImageView image_show;
    public Map<String,String> image_list=new HashMap<>();


    public void initialize() throws SQLException {
        load_images();
        // 监听选中项的变化
        listview_image.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                set_select(newValue);
            }
        });
        image_show.setPreserveRatio(true);
        image_show.setSmooth(true);
        //放大缩小时 不参与计算
//        image_show.setManaged(false);

        stackpane_image.widthProperty().addListener((observable, oldValue, newValue) -> updateImageSize());
        stackpane_image.heightProperty().addListener((observable, oldValue, newValue) -> updateImageSize());
        updateImageSize();
    }

    private void updateImageSize() {

        if (image_show != null && stackpane_image != null) {
//            double width =image_show.getImage().getHeight();
//            double height = image_show.getImage().getWidth();
            double parent_width = stackpane_image.getWidth();
            double parent_height = stackpane_image.getHeight();
            if(parent_width>image_show.getImage().getWidth()){
                image_show.setLayoutX((parent_width-image_show.getImage().getWidth())/2);
                image_show.setFitWidth(image_show.getImage().getWidth());

            }else{
                image_show.setFitWidth(parent_width);

                image_show.setLayoutX(0);
            }
            image_show.setFitHeight(parent_height);
            image_show.setLayoutY(0);
        }
    }
    public void setSelectItem(String select_item) {
        if(select_item!=null && image_list.containsKey(select_item)){
            listview_image.getSelectionModel().select(select_item);
            set_select(select_item);
//            image_show.setFitHeight(stackpane_image.getHeight());
//            image_show.setFitWidth(stackpane_image.getWidth());
        }
    }
    public void load_images() throws SQLException {
        String sql = "SELECT distinct * from "+tools_image_table_name+" where contents is not null and name is not null";
        ResultSet reslut = sql_search(db_name,sql);
//        HashMap<String, ArrayList<ArrayList>> result_map = new HashMap<>();
        if (reslut != null) {
            while (reslut.next()) {
                String name = reslut.getString("name");
                String contents = reslut.getString("contents");
                image_list.put(name.trim(), contents.trim());
            }
        }
        // 使用流 API 分离并排序
        List<String> chineseKeys = image_list.keySet().stream()
                .filter(this::containsChinese)
                .sorted()
                .collect(Collectors.toList());

        List<String> nonChineseKeys = image_list.keySet().stream()
                .filter(key -> !containsChinese(key))
                .sorted()
                .collect(Collectors.toList());

        // 合并两个列表
        List<String> sortedKeys = new ArrayList<>(chineseKeys);
        sortedKeys.addAll(nonChineseKeys);
        // 更新 ListView
        listview_image.getItems().setAll(sortedKeys);

//        image_list = new TreeMap<>(image_list);
//        listview_image.getItems().addAll(image_list.keySet());
        listview_image.getSelectionModel().select(0);
        String  item = listview_image.getItems().get(0);
        set_select(item);
    }
    public boolean containsChinese(String str) {
        for (char c : str.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                return true;
            }
        }
        return false;
    }

    public void set_select(String itme_name) {
        byte[] imageBytes = Base64.getDecoder().decode(image_list.get(itme_name));
        // 使用字节数组创建 Image 对象
        Image image = new Image(new ByteArrayInputStream(imageBytes));
        double parent_width = stackpane_image.getWidth();
        image_show.setLayoutX((parent_width-image_show.getFitWidth())/2);
        image_show.setImage(image);
    }

    @FXML
    void search_(KeyEvent event) {
        List<String> new_data = new ArrayList<>();
        for (String key : image_list.keySet()) {
            if (key.contains(search_text.getText())) {
                new_data.add(key);
            }
        }
        //排序  中文最前面
        List<String> chineseKeys = new_data.stream()
                .filter(this::containsChinese)
                .sorted()
                .collect(Collectors.toList());

        List<String> nonChineseKeys = new_data.stream()
                .filter(key -> !containsChinese(key))
                .sorted()
                .collect(Collectors.toList());

        // 合并两个列表
        List<String> sortedKeys = new ArrayList<>(chineseKeys);
        sortedKeys.addAll(nonChineseKeys);
        listview_image.getItems().clear();
        listview_image.getItems().addAll(sortedKeys);
    }
}
