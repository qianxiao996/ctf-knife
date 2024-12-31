package com.qianxiao996.ctfknife.Controller.Encrypt;

import com.qianxiao996.ctfknife.CtfknifeApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.f_alert_informationDialog;
import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Rsa {

    @FXML
    private TextArea public_key;

    @FXML
    private TextArea private_key;

    @FXML
    private Button source_textarea_copy;

    @FXML
    private Button source_textarea_paste;

    @FXML
    private Button source_textarea_clear;

    @FXML
    private TextArea TextArea_Source;

    @FXML
    private Button button_to_source;

    @FXML
    private Button result_textarea_copy;

    @FXML
    private Button result_textarea_paste;

    @FXML
    private Button result_textarea_clear;

    @FXML
    private TextArea TextArea_Result;
    public void initialize() {
        set_button_icon(source_textarea_copy,"img/copy.png");
        set_button_icon(result_textarea_copy,"img/copy.png");
        set_button_icon(source_textarea_paste,"img/paste.png");
        set_button_icon(result_textarea_paste,"img/paste.png");
        set_button_icon(source_textarea_clear,"img/clear.png");
        set_button_icon(result_textarea_clear,"img/clear.png");
        set_button_icon(button_to_source,"img/to.png");

        set_button_copy(source_textarea_copy,TextArea_Source);
        set_button_copy(result_textarea_copy,TextArea_Result);
        set_button_paste(source_textarea_paste,TextArea_Source);
        set_button_paste(result_textarea_paste,TextArea_Result);
        set_button_clear(source_textarea_clear,TextArea_Source);
        set_button_clear(result_textarea_clear,TextArea_Result);
        set_button_to_source(button_to_source,TextArea_Source,TextArea_Result);
    }

    @FXML
    void func_generateKey(ActionEvent event) {
        try{
            //始化一个 KeyPairGenerator 对象，用于生成密钥对。 指定RSA 算法
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为 2048 位。 密钥长度越大，安全性越高，但同时加解密的速度会变慢。
            // new SecureRandom() RSA 密钥生成依赖于随机数来保证密钥的不可预测性和安全性。
            keyPairGenerator.initialize(2048, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 获取公钥和私钥 转为Base64 字符串
            String pubKey = Base64.toBase64String(keyPair.getPublic().getEncoded());
            String priKey = Base64.toBase64String(keyPair.getPrivate().getEncoded());
            public_key.setText("-----BEGIN PUBLIC KEY-----\n"+pubKey+"\n-----BEGIN PUBLIC KEY-----");
            private_key.setText("-----BEGIN RSA PRIVATE KEY-----\n"+priKey+"\n-----END RSA PRIVATE KEY-----");
            TextArea_Result.setText("公共指数:65537\n" +
                    "密钥长度:2048");
        }catch (Exception e){
            f_alert_informationDialog("提示","","生成失败："+e.getMessage());
        }
    }
    @FXML
    void func_decrypt(ActionEvent event) {
        String pri_key = private_key.getText();
        if(pri_key.isEmpty()){
            TextArea_Result.setText("请输入私钥");
            return;
        }
        String message = TextArea_Source.getText();
        if(message.isEmpty()){
            TextArea_Result.setText("请输入要解密的内容");
            return;
        }
        try {
            pri_key = pri_key.replaceAll("-----END RSA PRIVATE KEY-----","").replaceAll("-----BEGIN RSA PRIVATE KEY-----","");
            pri_key = pri_key.trim();
            //将传入的 Base64 编码的私钥字符串解码为字节数组。
            byte[] privateBytes = Base64.decode(pri_key);
            //将解码后的字节数组封装为一个 PKCS8EncodedKeySpec 对象。
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            //通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //使用 KeyFactory 将 PKCS8EncodedKeySpec 转换为一个 PrivateKey 对象。  供解密使用
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);
            //创建一个 Cipher 实例，指定加密算法为 RSA。
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //指定 Cipher 对象的模式为解密模式，并使用前面生成的 priKey 对象来初始化 Cipher 对象。
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            //加密的字节数组进行解密，返回解密后的字节数组
            byte[] decode = Base64.decode(message.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream byteArrayOutputStream = chunkDigest(decode, cipher, 256);
            String result =  byteArrayOutputStream.toString("UTF-8");
            TextArea_Result.setText(result);
        } catch (Exception e) {
            TextArea_Result.setText("解密失败"+e.getMessage());
        }

    }

    @FXML
    void func_encrypt(ActionEvent event) {
        try{
            String pub_key = public_key.getText();
            if(pub_key.isEmpty()){
                TextArea_Result.setText("请输入公钥");
                return;
            }
            String message = TextArea_Source.getText();
            if(message.isEmpty()){
                TextArea_Result.setText("请输入要加密的内容");
                return;
            }
            pub_key = pub_key.replaceAll("-----BEGIN PUBLIC KEY-----","").replaceAll("-----END PUBLIC KEY-----","");
            pub_key = pub_key.trim();
            //将传入的 Base64 编码的公钥字符串解码为字节数组
            byte[] publicBytes = Base64.decode(pub_key);
            //将解码后的字节数组封装为一个 X509EncodedKeySpec 对象。
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            //获取一个用于处理 RSA 密钥的工厂实例。
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //根据提供的公钥规范（keySpec）生成一个实际的 PublicKey 对象，供加密使用。
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            //指定加密算法为 RSA
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //指定 Cipher 对象的模式为加密模式  使用前面生成的 pubKey 对象来初始化 Cipher 对象。
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            //使用 Cipher 对消息进行加密。
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            try (ByteArrayOutputStream byteArrayOutputStream = chunkDigest(bytes, cipher, 245)) {
                String result = new String(Base64.encode(byteArrayOutputStream.toByteArray()), StandardCharsets.UTF_8);
                TextArea_Result.setText(result);
            }
        }catch (Exception e){
            TextArea_Result.setText(e.getMessage());
        }
    }

    @FXML
    void func_sign(ActionEvent event) {
        String pri_key = private_key.getText();
        if(pri_key.isEmpty()){
            TextArea_Result.setText("请输入私钥");
            return;
        }
        String message = TextArea_Source.getText();
        if(message.isEmpty()){
            TextArea_Result.setText("请输入要签名的内容");
            return;
        }

        try {
            pri_key = pri_key.replaceAll("-----END RSA PRIVATE KEY-----","").replaceAll("-----BEGIN RSA PRIVATE KEY-----","");
            pri_key = pri_key.trim();
            // 通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 将传入的 Base64 编码的私钥字符串解码为字节数组。
            byte[] privateBytes = Base64.decode(pri_key);
            // 将解码后的字节数组封装为 PKCS8EncodedKeySpec 对象。
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            // 使用 KeyFactory 将 PKCS8EncodedKeySpec 转换为一个 PrivateKey 对象。  供签名使用
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);
            // 创建一个 Signature 实例，指定签名算法为 SHA256withRSA。   SHA256withRSA：指定使用 SHA-256 哈希算法和 RSA 签名算法的组合。
            Signature signature = Signature.getInstance("SHA256withRSA");
            //使用私钥初始化 Signature 对象，并设置为签名模式。
            signature.initSign(priKey);
            //方法将字节数组添加到签名对象中，以便进行签名。 将消息字符串转换为 UTF-8 编码的字节数组。
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            // 执行签名操作 将签名后的字节数组进行 Base64 编码，转换为字符串
            String result = new String(Base64.encode(signature.sign()));
            TextArea_Result.setText(result);
        }catch (Exception e){
            TextArea_Result.setText("签名失败："+e.getMessage());

        }

    }
//     * @param message      原始消息（字符串），用于与签名进行匹配。
//            * @param signatureStr 签名的 Base64 编码字符串。
//            * @param publicKey    公钥的 Base64 编码字符串。
    @FXML
    void func_verify(ActionEvent event) {
        String pub_key = public_key.getText();
        if(pub_key.isEmpty()){
            TextArea_Result.setText("请输入公钥");
            return;
        }
        String message = TextArea_Source.getText();
        if(message.isEmpty()){
            TextArea_Result.setText("请输入要验签的内容");
            return;
        }
        open_one_input_ui(pub_key,message);


    }
    /**
     * 加解密分块处理
     *
     * @param textBytes 需要加、解密的文本字节数组
     * @param cipher    加、解密cipher
     * @param MAX_BLOCK 加密块限长
     * @return 加、解密处理后的字节数组流
     * @throws IllegalBlockSizeException 块大小异常
     * @throws BadPaddingException       填充异常
     */
    private static ByteArrayOutputStream chunkDigest(byte[] textBytes, Cipher cipher, final int MAX_BLOCK)
            throws  IllegalBlockSizeException, BadPaddingException {
        int textLen = textBytes.length;
        int offset = 0;
        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] cache;
        int remaining = textLen - offset;
        while (remaining > 0) {
            if (remaining > MAX_BLOCK) {
                cache = cipher.doFinal(textBytes, offset, MAX_BLOCK);
            } else {
                cache = cipher.doFinal(textBytes, offset, remaining);
            }
            out.write(cache, 0, cache.length);
            offset = (++i) * MAX_BLOCK;
            remaining = textLen - offset;
        }

        return out;
    }

    private void open_one_input_ui(String public_key,String message){

        // 确保UI更新在JavaFX Application Thread中进行
        Platform.runLater(() -> {
            // 创建新的Stage
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL); // 设置为模态窗口
            dialogStage.setTitle("验证签名");
            // 创建HBox并设置其属性
            HBox hbox = new HBox(5); // 设置控件之间的间距为5px
            hbox.setPadding(new Insets(20.0, 20.0, 20.0, 20.0)); // 设置内边距

            // 创建Label、TextField和Button，并添加到HBox中
            Label label = new Label("签名:");
            TextArea lineText = new TextArea();
            lineText.setPromptText("请输入签名,使用Base64编码");
            lineText.setPrefRowCount(10);
            lineText.setPrefWidth(400);
            lineText.setText("请输入签名,使用Base64编码");
            lineText.setWrapText(true);
            Button button = new Button("验签");
            // 将控件添加到HBox中
            hbox.getChildren().addAll(label, lineText, button);

            // 设置TextField的宽度增长优先级
            HBox.setHgrow(lineText, javafx.scene.layout.Priority.ALWAYS);

            // 设置按钮点击事件（这里仅做示意）
            button.setOnAction(event -> {
                // 关闭对话框
                dialogStage.close();
                String sign_text = lineText.getText();
                verify_sign(public_key,message,sign_text);
            });
            // 设置场景
            Scene scene = new Scene(hbox, 680, 200);
            dialogStage.setScene(scene);
            Image icon_image= new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png")));
            dialogStage.getIcons().add(icon_image);
            scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(CtfknifeApplication.Style_Css)).toExternalForm());
            // 显示对话框
            dialogStage.showAndWait(); // 使用showAndWait以确保对话框是模态的
        });

    }
    private void verify_sign(String pub_key,String message,String sign_text) {
        try{
            pub_key = pub_key.replaceAll("-----BEGIN PUBLIC KEY-----","").replaceAll("-----END PUBLIC KEY-----","");
            pub_key = pub_key.trim();
            //通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            //将传入的 Base64 编码的公钥字符串解码为字节数组。
            byte[] publicBytes = Base64.decode(pub_key);
            //将解码后的字节数组封装为 X509EncodedKeySpec 对象。
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

            //使用 KeyFactory 将 X509EncodedKeySpec 转换为一个 PublicKey 对象。  供验签使用
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            //创建一个 Signature 实例，指定签名算法为 SHA256withRSA。   SHA256withRSA：指定使用 SHA-256 哈希算法和 RSA 签名算法的组合。
            Signature signature = Signature.getInstance("SHA256withRSA");
            //使用公钥初始化 Signature 对象，并设置为验证模式。
            signature.initVerify(pubKey);
            //将原始消息数据传递给 Signature 对象进行验证。
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            // 使用 Signature 对象验证签名的有效性。
            boolean isVerify = signature.verify(Base64.decode(sign_text.getBytes()));
            if(isVerify){
                String reuslt = "明文: "+message+"\n"+"公钥: "+pub_key+"\n"+"签名: "+sign_text+"\n"+"验签结果: 验签成功";
                TextArea_Result.setText(reuslt);
            }else{
                String reuslt = "明文: "+message+"\n"+"公钥: "+pub_key+"\n"+"签名: "+sign_text+"\n"+"验签结果: 验签失败";
                TextArea_Result.setText(reuslt);
            }
        }catch (Exception e){
            TextArea_Result.setText("验签失败："+e.getMessage());
        }
    }

}
