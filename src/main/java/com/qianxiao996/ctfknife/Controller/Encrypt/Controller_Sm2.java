package com.qianxiao996.ctfknife.Controller.Encrypt;

import com.qianxiao996.ctfknife.CtfknifeApplication;
import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Controller_Sm2 {
    static {
        // 确保BouncyCastle提供者已注册
        Security.addProvider(new BouncyCastleProvider());
    }

    @FXML
    private TextArea public_key;

    @FXML
    private TextArea private_key;

    @FXML
    private ComboBox<String> pub_encoding;
    @FXML
    private ComboBox<String> encrypt_shunxu;



    @FXML
    private ComboBox<String> input_encoding;

    @FXML
    private ComboBox<String> output_encoding;

    @FXML
    private ComboBox<String> pri_encoding;

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
        pub_encoding.getItems().addAll( "Hex", "Base64");
        pub_encoding.getSelectionModel().select(0);
        pri_encoding.getItems().addAll( "Hex", "Base64");
        pri_encoding.getSelectionModel().select(0);
        input_encoding.getItems().addAll( "Raw","Hex", "Base64");
        input_encoding.getSelectionModel().select(0);
        output_encoding.getItems().addAll( "Raw","Hex", "Base64");
        output_encoding.getSelectionModel().select(1);
        encrypt_shunxu.getItems().addAll( "C1C2C3", "C1C3C2","未指定");
        encrypt_shunxu.getSelectionModel().select(1);
    }
    @FXML
    void func_decrypt(ActionEvent event) {
        try{
            String text_source = TextArea_Source.getText();
            if(text_source.isEmpty()){
                TextArea_Result.setText("请输入要解密的内容");
                return;
            }
            String input_encoding_str =input_encoding.getValue();
            byte[] data;
            if (Objects.equals(input_encoding_str, "Raw")){
                data= text_source.getBytes();
            } else if (Objects.equals(input_encoding_str, "Hex")) {
                data= Hex.decode(text_source);
            } else if (Objects.equals(input_encoding_str, "Base64")) {
                data= Base64.decode(text_source);

            } else{
                data =text_source.getBytes();
            }
            String pri_key=private_key.getText();
            PrivateKey privateKey;
            if(Objects.equals(pub_encoding.getValue(), "Hex")){
                privateKey = hexToECPrivateKey(pri_key);
            } else if (Objects.equals(pub_encoding.getValue(), "Base64")) {
                privateKey = base64ToECPrivateKey(pri_key);
            }else {
                TextArea_Result.setText("私钥格式错误");
                return;
            }
            SM2Engine localSM2Engine;
            if(encrypt_shunxu.getValue().equals("C1C2C3")){
                localSM2Engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
            } else if (encrypt_shunxu.getValue().equals("C1C3C2")) {
                localSM2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            }else{
                localSM2Engine = new SM2Engine();
            }
//            SM2Engine localSM2Engine = new SM2Engine();
            BCECPrivateKey sm2PriK = (BCECPrivateKey) privateKey;
            ECParameterSpec localECParameterSpec = sm2PriK.getParameters();
            ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(), localECParameterSpec.getG(), localECParameterSpec.getN());
            ECPrivateKeyParameters localECPrivateKeyParameters = new ECPrivateKeyParameters(sm2PriK.getD(), localECDomainParameters);
            localSM2Engine.init(false, localECPrivateKeyParameters);
            byte[] arrayOfByte2 = new byte[0];
            try {
                arrayOfByte2 = localSM2Engine.processBlock(data, 0, data.length);
            } catch (InvalidCipherTextException e) {
                TextArea_Result.setText(e.getMessage());
            }
            String out_encoding_str =output_encoding.getValue();
            if(Objects.equals(out_encoding_str, "Raw")){
                TextArea_Result.setText(new  String(arrayOfByte2));
            }else if(Objects.equals(out_encoding_str, "Hex")){
                TextArea_Result.setText(Hex.toHexString(arrayOfByte2));
            } else if (Objects.equals(out_encoding_str, "Base64")) {
                TextArea_Result.setText(Base64.toBase64String(arrayOfByte2));
            }else{
                TextArea_Result.setText(new String(arrayOfByte2));
            }
        }catch (Exception e){
            TextArea_Result.setText(e.getMessage());
        }

    }

    @FXML
    void func_encrypt(ActionEvent event) {
        try{
            String text_source = TextArea_Source.getText();
            if(text_source.isEmpty()){
                TextArea_Result.setText("请输入要加密的内容");
                return;
            }
            String input_encoding_str =input_encoding.getValue();
            byte[] data;
            if (Objects.equals(input_encoding_str, "Raw")){
                data= text_source.getBytes();
            } else if (Objects.equals(input_encoding_str, "Hex")) {
                data= Hex.decode(text_source);
            } else if (Objects.equals(input_encoding_str, "Base64")) {
                data= Base64.decode(text_source);

            } else{
                data =text_source.getBytes();
            }
            String pub_key=public_key.getText();
            PublicKey publicKey;
            if(Objects.equals(pub_encoding.getValue(), "Hex")){
                publicKey = hexToECPublicKey(pub_key);
            } else if (Objects.equals(pub_encoding.getValue(), "Base64")) {
                publicKey = base64ToECPublicKey(pub_key);
            }else {
                TextArea_Result.setText("公钥格式错误");
                return;
            }

            ECPublicKeyParameters localECPublicKeyParameters = null;

            if (publicKey instanceof BCECPublicKey) {
                BCECPublicKey localECPublicKey = (BCECPublicKey) publicKey;
                ECParameterSpec localECParameterSpec = localECPublicKey.getParameters();
                ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(), localECParameterSpec.getG(), localECParameterSpec.getN());
                localECPublicKeyParameters = new ECPublicKeyParameters(localECPublicKey.getQ(), localECDomainParameters);
            }
            SM2Engine localSM2Engine;
            if(encrypt_shunxu.getValue().equals("C1C2C3")){
                localSM2Engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
            } else if (encrypt_shunxu.getValue().equals("C1C3C2")) {
                localSM2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            }else{
                localSM2Engine = new SM2Engine();
            }
            localSM2Engine.init(true, new ParametersWithRandom(localECPublicKeyParameters, new SecureRandom()));
            byte[] arrayOfByte2 = new byte[0];
            try {
                arrayOfByte2 = localSM2Engine.processBlock(data, 0, data.length);
            } catch (InvalidCipherTextException e) {
                TextArea_Result.setText(e.getMessage());
            }
            String out_encoding_str =output_encoding.getValue();
            if(Objects.equals(out_encoding_str, "Raw")){
                TextArea_Result.setText(new  String(arrayOfByte2));
            }else if(Objects.equals(out_encoding_str, "Hex")){
                TextArea_Result.setText(Hex.toHexString(arrayOfByte2));
            } else if (Objects.equals(out_encoding_str, "Base64")) {
                TextArea_Result.setText(Base64.toBase64String(arrayOfByte2));
            }else{
                TextArea_Result.setText(new String(arrayOfByte2));
            }
        }catch (Exception e){
            TextArea_Result.setText(e.getMessage());
        }

    }

    @FXML
    void func_generateKey(ActionEvent event) {
        try{
            KeyPairGenerator keyPairGenerator = null;
            SecureRandom secureRandom = new SecureRandom();
            ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
            keyPairGenerator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
            keyPairGenerator.initialize(sm2Spec);
            keyPairGenerator.initialize(sm2Spec, secureRandom);
            KeyPair keyPair =  keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            // 提取私钥和公钥的参数
            ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
            ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
            // 获取私钥的D值（大整数）
            BigInteger d = ecPrivateKey.getD();
            byte[] privateKeyBytes = d.toByteArray();
            if(Objects.equals(pri_encoding.getValue(), "Hex")){
                String hexEncodedPrivateKey = Hex.toHexString(privateKeyBytes);
                private_key.setText(hexEncodedPrivateKey);
            } else if (Objects.equals(pri_encoding.getValue(), "Base64")) {
                String result = Base64.toBase64String(privateKeyBytes);
                private_key.setText(result);
            }else{
                String result = new String(privateKeyBytes, StandardCharsets.ISO_8859_1);
                private_key.setText(result);
            }
            // 获取公钥的Q点（椭圆曲线上的点）
            ECPoint q = ecPublicKey.getQ();
            byte[] publicKeyBytes = q.getEncoded(false); // false 表示非压缩形式
            if(Objects.equals(pub_encoding.getValue(), "Hex")){
                String hexEncodedPublicKey = Hex.toHexString(publicKeyBytes);
                public_key.setText(hexEncodedPublicKey);
            }else if (Objects.equals(pub_encoding.getValue(), "Base64")) {
                String EncodedPublicKey = Base64.toBase64String(publicKeyBytes);
                public_key.setText(EncodedPublicKey);
            }else {
                String EncodedPublicKey = new String(publicKeyBytes, StandardCharsets.ISO_8859_1);
                public_key.setText(EncodedPublicKey);
            }
        }catch (Exception e){
            TextArea_Result.setText(e.getMessage());
        }
    }

    @FXML
    void func_sign(ActionEvent event) {
        try{
            String input_encoding_str = input_encoding.getValue();
            String text_source = TextArea_Source.getText();
            if(text_source.isEmpty()){
                TextArea_Result.setText("输入为空！");
                return;
            }
            byte[] data;
            if (Objects.equals(input_encoding_str, "Raw")){
                data= text_source.getBytes();
            } else if (Objects.equals(input_encoding_str, "Hex")) {
                data= Hex.decode(text_source);
            } else if (Objects.equals(input_encoding_str, "Base64")) {
                data= Base64.decode(text_source);

            } else{
                data =text_source.getBytes();
            }
            String pri_key_str = private_key.getText();
            String pri_encoding_str = pri_encoding.getValue();
            PrivateKey pri_key = null;
            if(pri_encoding_str.equals("Hex")){
                pri_key =hexToECPrivateKey(pri_key_str);
            } else if (pri_encoding_str.equals("Base64")) {
                pri_key =base64ToECPrivateKey(pri_key_str);
            }else{
                TextArea_Result.setText("私钥格式错误！");
                return;
            }
            Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
            sig.initSign(pri_key);
            sig.update(data);
            byte[] ret = sig.sign();
            String result = Conn.Bytes_To_Str(ret,output_encoding.getValue());
            TextArea_Result.setText(result);
        }catch (Exception e){
            TextArea_Result.setText("签名失败："+e.getMessage());
        }

    }
    public static PrivateKey base64ToECPrivateKey(String base64String) throws Exception {
        // 将Base64字符串解码为字节数组
        byte[] encodedPrivateKeyBytes = Base64.decode(base64String);
        // 将字节数组转换为BigInteger (D值)
        BigInteger d = new BigInteger(1, encodedPrivateKeyBytes);
        // 获取SM2椭圆曲线参数
        ECParameterSpec ecParams = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        // 使用BC的KeyFactory生成私钥
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        ECPrivateKeySpec privSpec = new ECPrivateKeySpec(d, new ECParameterSpec(
                ecParams.getCurve(),
                ecParams.getG(),
                ecParams.getN(),
                ecParams.getH(),
                ecParams.getSeed()
        ));
        return keyFactory.generatePrivate(privSpec);
    }
    public static PublicKey base64ToECPublicKey(String base64String) throws Exception {
        // 将十六进制字符串转换为字节数组
        byte[] encodedPublicKeyBytes = Base64.decode(base64String);
        // 获取SM2椭圆曲线参数
        ECParameterSpec ecParams = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        // 创建ECPoint对象
        ECPoint q = ecParams.getCurve().decodePoint(encodedPublicKeyBytes);

        // 使用BC的KeyFactory生成公钥
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(q, new ECParameterSpec(
                ecParams.getCurve(),
                ecParams.getG(),
                ecParams.getN(),
                ecParams.getH(),
                ecParams.getSeed()
        ));
        return keyFactory.generatePublic(pubSpec);
    }

    public static PrivateKey hexToECPrivateKey(String hexString) throws Exception {
        // 将十六进制字符串转换为字节数组
        byte[] keyBytes = Hex.decode(hexString);
        // 将字节数组转换为BigInteger (D值)
        BigInteger d = new BigInteger(1, keyBytes);
        // 获取SM2椭圆曲线参数
        ECParameterSpec ecParams = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        // 创建 ECParameterSpec
        ECParameterSpec params = new ECParameterSpec(
                ecParams.getCurve(),
                ecParams.getG(),
                ecParams.getN(),
                ecParams.getH(),
                ecParams.getSeed()
        );

        // 使用BC的KeyFactory生成私钥
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(d, params);
        return keyFactory.generatePrivate(privateKeySpec);
    }
    public static PublicKey hexToECPublicKey(String hexString) throws Exception {
        // 将十六进制字符串转换为字节数组
        byte[] encodedPublicKeyBytes = Hex.decode(hexString);

        // 获取SM2椭圆曲线参数
        ECParameterSpec ecParams = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        // 创建ECPoint对象
        ECPoint q = ecParams.getCurve().decodePoint(encodedPublicKeyBytes);

        // 使用BC的KeyFactory生成公钥
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(q, new ECParameterSpec(
                ecParams.getCurve(),
                ecParams.getG(),
                ecParams.getN(),
                ecParams.getH(),
                ecParams.getSeed()
        ));
        return keyFactory.generatePublic(pubSpec);
    }
    @FXML
    void func_verify(ActionEvent event) {
        String pub_key=public_key.getText();
        String message = TextArea_Source.getText();
        if(pub_key.isEmpty()){
            TextArea_Result.setText("请输入公钥");
            return;
        }
        if(message.isEmpty()){
            TextArea_Result.setText("请输入要验证的内容");
            return;
        }
        open_one_input_ui(pub_key,message);


//        String pri_key =hexToECPublicKey(pri_key_str);

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

            // 创建Label、TextField和Button，并添加到HBox中
            Label label = new Label("签名:");
            TextArea lineText = new TextArea();
            ComboBox<String> combox = new ComboBox<>();
            combox.getItems().addAll("Hex","Base64");

            lineText.setPromptText("请输入签名。编码格式为输入编码");
            lineText.setPrefRowCount(10);
            lineText.setWrapText(true);
            Button button = new Button("验签");
            combox.getSelectionModel().select(0);
            hbox.setAlignment(Pos.CENTER);
            Button cancelbutton = new Button("取消");
            cancelbutton.setOnAction(event -> {
                dialogStage.close();
            });

            // 将控件添加到HBox中
            hbox.getChildren().addAll(label, combox, button,cancelbutton);

            // 设置TextField的宽度增长优先级
            HBox.setHgrow(lineText, Priority.ALWAYS);
            VBox vbox = new VBox(5);
            vbox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0)); // 设置内边距
            vbox.getChildren().addAll(hbox,lineText);
            // 设置按钮点击事件（这里仅做示意）
            button.setOnAction(event -> {
                // 关闭对话框
                dialogStage.close();
                String sign_text = lineText.getText();
                if(sign_text.isEmpty()){
                    TextArea_Result.setText("请输入签名");
                    return;
                }


                verify_sign(public_key,message,sign_text,combox.getValue());
            });
            // 设置场景
            Scene scene = new Scene(vbox, 650, 200);
            dialogStage.setScene(scene);
            Image icon_image= new Image(Objects.requireNonNull(CtfknifeApplication.class.getResourceAsStream("img/ico.png")));
            dialogStage.getIcons().add(icon_image);
            scene.getStylesheets().add(Objects.requireNonNull(CtfknifeApplication.class.getResource(CtfknifeApplication.Style_Css)).toExternalForm());

            // 显示对话框
            dialogStage.showAndWait(); // 使用showAndWait以确保对话框是模态的
        });

    }
    private void verify_sign(String pub_key,String message,String  signature,String sign_encoding) {
        try{
            byte[] sign_text_bytes = new byte[0];
            if(Objects.equals(sign_encoding, "Hex")){
                sign_text_bytes =Hex.decode(signature);
            } else if (Objects.equals(sign_encoding, "Base64")) {
                sign_text_bytes = Base64.decode(signature);
            } else if (Objects.equals(sign_encoding, "Raw")) {
                sign_text_bytes = signature.getBytes();
            }else{
                sign_text_bytes = signature.getBytes();
            }
            byte[] message_bytes = new byte[0];
            if(Objects.equals(input_encoding.getValue(), "Hex")){
                message_bytes = Hex.decode(message);
            } else if(Objects.equals(input_encoding.getValue(), "Base64")){
                message_bytes = Base64.decode(message);
            } else if (Objects.equals(input_encoding.getValue(), "Raw")) {
                message_bytes = message.getBytes(StandardCharsets.UTF_8);
            }else{
                message_bytes = message.getBytes();
            }
            PublicKey publicKey;
            if(Objects.equals(pub_encoding.getValue(), "Hex")){
                publicKey = hexToECPublicKey(pub_key);
            } else if (Objects.equals(pub_encoding.getValue(), "Base64")) {
                publicKey = base64ToECPublicKey(pub_key);
            }else {
                TextArea_Result.setText("公钥格式错误");
                return;
            }
            Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
            sig.initVerify(publicKey);
            sig.update(message_bytes);
            boolean ret = sig.verify(sign_text_bytes);
            if (ret){
                String reuslt = "明文: "+message+"\n"+"公钥: "+pub_key+"\n"+"签名: "+signature+"\n"+"验签结果: 验签成功";
                TextArea_Result.setText(reuslt);
            }else{
                String reuslt = "明文: "+message+"\n"+"公钥: "+pub_key+"\n"+"签名: "+signature+"\n"+"验签结果: 验签失败";
                TextArea_Result.setText(reuslt);
            }

        }catch (Exception e){
            TextArea_Result.setText("验签错误："+e.getMessage());
        }
    }

}
