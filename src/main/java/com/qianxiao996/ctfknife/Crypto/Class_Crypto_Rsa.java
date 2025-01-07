package com.qianxiao996.ctfknife.Crypto;

import com.qianxiao996.ctfknife.CtfknifeApplication;
import com.qianxiao996.ctfknife.Utils.Conn;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import static com.qianxiao996.ctfknife.Controller.Ctfknife_Controller.f_alert_informationDialog;
import static com.qianxiao996.ctfknife.Utils.Conn.*;

public class Class_Crypto_Rsa extends Thread {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private static TextArea textarea_mingwen;
    private static TextArea textarea_miwen;
    private String method;
    private static String mingwen_text;
    private static String miwen_text;
    private static String mingwen_encoding;
    private static String miwen_encoding;
    private static String sign_encode;
    private static String sign_text;



    private static String p;
    private static String p_encode;
    private static String q;
    private static String q_encode;
    private static String e;
    private static String e_encode;
    private static String n;
    private static String n_encode;

    private static TextField textField_p;
    private static TextField textField_q;
    private static TextField textField_e;
    private static TextField textField_n;
    private static TextField textField_dp;
    private static TextField textField_dq;
    private static TextField  textField_qinv;
    private static TextField  textField_d;
    private static String d;
    private static String d_encode;
    private static String dp;
    private static String dp_encode;
    private static String dq;
    private static String dq_encode;
    private static String qinv;
    private static String qinv_encode;
    private static PublicKey publicKey = null;
    private static String publicKey_encode="Raw";
    private static String publicKey_Str;

    private static TextArea textarea_PublicKey;

    private static PrivateKey privateKey = null;
    private static String privateKey_encode="Raw";
    private static String privateKey_Str;

    private static TextArea textarea_PrivateKey;


    public void setValue(TextArea textarea_mingwen,TextArea textarea_miwen, String method, String mingwen_text,String miwen_text, String mingwen_encoding_str, String miwen_encodin_str) {
        this.textarea_mingwen = textarea_mingwen;
        this.textarea_miwen = textarea_miwen;
        this.method = method;
        this.mingwen_text =  mingwen_text.trim();
        this.miwen_text =  miwen_text.trim();
        this.mingwen_encoding = mingwen_encoding_str;
        this.miwen_encoding = miwen_encodin_str;
    }

    public void setPubPriObj(TextArea textarea_publicKey, TextArea textarea_privateKey,String PublicKey_encode,String PrivateKey_encode) {
        textarea_PublicKey = textarea_publicKey;
        textarea_PrivateKey = textarea_privateKey;
        publicKey_encode =PublicKey_encode;
        privateKey_encode =PrivateKey_encode;

    }
    public void setPublicKey(String pubKey) {
        try{
            publicKey_Str = pubKey;
            if(pubKey.isEmpty()){
                publicKey=null;
                return;
            }
            if(Objects.equals(publicKey_encode, "Hex")){
                publicKey = hexToECPublicKey(pubKey);
            } else if (Objects.equals(publicKey_encode, "Base64")) {
                pubKey = get_key(pubKey);
                publicKey = base64ToECPublicKey(pubKey);
            }else {
                publicKey=null;
                f_alert_informationDialog("错误","","公钥格式错误");
            }
        } catch (Exception ex) {
            publicKey=null;
            f_alert_informationDialog("错误","","公钥格式错误"+ex.getMessage());
        }

    }
    public void setPrivateKey(String pri_key) {
        try{
            privateKey_Str =pri_key;
            if(pri_key.isEmpty()){
                privateKey=null;
                return;
            }
            if(Objects.equals(privateKey_encode, "Hex")){
                privateKey = hexToECPrivateKey(pri_key);
            } else if (Objects.equals(privateKey_encode, "Base64")) {
                pri_key = get_key(pri_key);
                privateKey = base64ToECPrivateKey(pri_key);
            }else {
                privateKey=null;
                f_alert_informationDialog("错误","","私钥格式错误");
            }
        } catch (Exception ex) {
            privateKey=null;
            f_alert_informationDialog("错误","","私钥格式错误"+ex.getMessage());
        }

    }
    public String get_key(String key) {
        // 使用换行符分割字符串
        String[] lines = key.split("\\r?\\n");
        // 创建 StringBuilder 来构建最终的结果字符串
        StringBuilder result = new StringBuilder();
        // 遍历每一行
        for (String line : lines) {
            // 去除行首尾的空白字符，并检查是否以 "--" 开头
            if (!line.trim().startsWith("-----")) {
                // 如果不以 "--" 开头，添加到结果中，并在行末添加换行符
                result.append(line).append(System.lineSeparator());
            }
        }
        // 返回结果字符串，去除最后一行多余的换行符
        return result.toString().trim();
    }

    public void setTextFieldObj(TextField textField_p, TextField textField_q, TextField textField_e, TextField textField_n, TextField textField_dp, TextField textField_dq, TextField textField_d, TextField  textField_qinv) {
        this.textField_p = textField_p;
        this.textField_q = textField_q;
        this.textField_e = textField_e;
        this.textField_n = textField_n;
        this.textField_dp = textField_dp;
        this.textField_dq = textField_dq;
        this.textField_qinv = textField_qinv;
        this.textField_d = textField_d;


    }
    public void setComboboxObj(String p_encode,  String q_encode,  String e_encode,  String n_encode, String dp_encode, String dq_encode, String d_encode, String qinv_encode) {
        this.p_encode = p_encode;
        this.q_encode = q_encode;
        this.e_encode =  e_encode;
        this.n_encode = n_encode;
        this.dp_encode = dp_encode;
        this.dq_encode = dq_encode;
        this.qinv_encode = qinv_encode;
        this.d_encode = d_encode;
    }
    public void setParams(String p,  String q,  String e,String n, String dp,String dq, String d,  String qinv) {
        this.p = p;
        this.q = q;
        this.e =  e;
        this.n = n;
        this.dp = dp;
        this.dq = dq;
        this.qinv = qinv;
        this.d = d;

    }
    public void run() {
        Class<? extends Class_Crypto_Rsa> clazz = this.getClass();
        Method method = null;
        try {
//            text = Get_Real_Str(souce_text,input_encoding);
            method = clazz.getMethod(this.method);
            method.invoke(this);
//                System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            f_alert_informationDialog("错误","","错误！\r\n"+e.getMessage());
        }

    }
    public void  encrypt() {
        try{

            byte[] bytes = Str_To_Bytes(mingwen_encoding,mingwen_text);
            if(new String(bytes).startsWith("Error:")){
                textarea_miwen.setText(new  String(bytes));
                return;
            }
            if(bytes.length<=0){
                f_alert_informationDialog("错误","","请输入要加密的内容!");
                return;
            }
            if(publicKey==null){
                f_alert_informationDialog("错误","","请输入公钥!");
                return;
            }
            //指定加密算法为 RSA
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //指定 Cipher 对象的模式为加密模式  使用前面生成的 pubKey 对象来初始化 Cipher 对象。
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //使用 Cipher 对消息进行加密。
//            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            try (ByteArrayOutputStream byteArrayOutputStream = chunkDigest(bytes, cipher, 245)) {
                String result =  Bytes_To_Str(byteArrayOutputStream.toByteArray(),miwen_encoding);
                textarea_miwen.setText(result);
            }
        }catch (Exception e){
            f_alert_informationDialog("错误","",e.getMessage());
        }
    }
    public void   decrypt() {
        if(miwen_text.isEmpty()){
            f_alert_informationDialog("错误","","请输入要解密的内容!");
            return;
        }
        if(privateKey==null){
            f_alert_informationDialog("错误","","请输入私钥!");
            return;
        }
        try {
            byte[] decode = Str_To_Bytes(miwen_encoding,miwen_text);
            if(new String(decode).startsWith("Error:")){
                textarea_mingwen.setText(new  String(decode));
                return;
            }
            //创建一个 Cipher 实例，指定加密算法为 RSA。
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //指定 Cipher 对象的模式为解密模式，并使用前面生成的 priKey 对象来初始化 Cipher 对象。
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //加密的字节数组进行解密，返回解密后的字节数组
            ByteArrayOutputStream byteArrayOutputStream = chunkDigest(decode, cipher, 256);
            String result =  Conn.Bytes_To_Str(byteArrayOutputStream.toByteArray(),mingwen_encoding);
            textarea_mingwen.setText(result);
        } catch (Exception e) {
            f_alert_informationDialog("错误","",e.getMessage());
        }
    }
    public static PublicKey hexToECPublicKey(String hexString) throws Exception {
        byte[] publicBytes = Hex.decode(hexString);
        //将解码后的字节数组封装为一个 X509EncodedKeySpec 对象。
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        //获取一个用于处理 RSA 密钥的工厂实例。
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //根据提供的公钥规范（keySpec）生成一个实际的 PublicKey 对象，供加密使用。
        return keyFactory.generatePublic(keySpec);
    }
    public static PublicKey base64ToECPublicKey(String base64String) throws Exception {
        //将传入的 Base64 编码的公钥字符串解码为字节数组
        byte[] publicBytes = Base64.decode(base64String);
        //将解码后的字节数组封装为一个 X509EncodedKeySpec 对象。
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        //获取一个用于处理 RSA 密钥的工厂实例。
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //根据提供的公钥规范（keySpec）生成一个实际的 PublicKey 对象，供加密使用。
        return keyFactory.generatePublic(keySpec);
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
    public static PrivateKey hexToECPrivateKey(String hexString) throws Exception {
        byte[] privateBytes = Hex.decode(hexString);
        //将解码后的字节数组封装为一个 PKCS8EncodedKeySpec 对象。
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        //通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //使用 KeyFactory 将 PKCS8EncodedKeySpec 转换为一个 PrivateKey 对象。  供解密使用
        return keyFactory.generatePrivate(keySpec);
    }
    public static PrivateKey base64ToECPrivateKey(String base64String) throws Exception {
        //将传入的 Base64 编码的私钥字符串解码为字节数组。
        byte[] privateBytes = Base64.decode(base64String);
        //将解码后的字节数组封装为一个 PKCS8EncodedKeySpec 对象。
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        //通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //使用 KeyFactory 将 PKCS8EncodedKeySpec 转换为一个 PrivateKey 对象。  供解密使用
        return keyFactory.generatePrivate(keySpec);
    }

    public static void generateKey() {
        try{
            //始化一个 KeyPairGenerator 对象，用于生成密钥对。 指定RSA 算法
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为 2048 位。 密钥长度越大，安全性越高，但同时加解密的速度会变慢。
            // new SecureRandom() RSA 密钥生成依赖于随机数来保证密钥的不可预测性和安全性。
            keyPairGenerator.initialize(2048, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            if(Objects.equals( publicKey_encode , "Hex")){
                String hexEncodedPublicKey = Hex.toHexString(keyPair.getPublic().getEncoded());
                textarea_PublicKey.setText(hexEncodedPublicKey);
            } else if (Objects.equals(publicKey_encode, "Base64")) {
                // 获取公钥和私钥 转为Base64 字符串
                String pubKey = Base64.toBase64String(keyPair.getPublic().getEncoded());
                textarea_PublicKey.setText("-----BEGIN PUBLIC KEY-----\n"+pubKey+"\n-----BEGIN PUBLIC KEY-----");
            }else{
                String result = new String(keyPair.getPublic().getEncoded(), StandardCharsets.ISO_8859_1);
                textarea_PublicKey.setText(result);
            }
            if(Objects.equals(privateKey_encode, "Hex")){
                String  hexEncodedPrivateKey= Hex.toHexString(keyPair.getPrivate().getEncoded());
                textarea_PrivateKey.setText(hexEncodedPrivateKey);
            } else if (Objects.equals(privateKey_encode, "Base64")) {
                String priKey = Base64.toBase64String(keyPair.getPrivate().getEncoded());
                textarea_PrivateKey.setText("-----BEGIN RSA PRIVATE KEY-----\n"+priKey+"\n-----END RSA PRIVATE KEY-----");
            }else{
                String result2 = new String(keyPair.getPrivate().getEncoded(), StandardCharsets.ISO_8859_1);
                textarea_PrivateKey.setText(result2);
            }


            // 提取公钥和私钥
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 提取公钥参数
            RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            BigInteger int_n = rsaPublicKey.getModulus();
            n=int_to_String(int_n,n_encode);
            textField_n.setText(n);
            BigInteger int_e = rsaPublicKey.getPublicExponent();
            e=int_to_String(int_e,e_encode);
            textField_e.setText(e);
            // 提取私钥参数
            RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey) privateKey;
            BigInteger int_d = rsaPrivateKey.getPrivateExponent();
            d=int_to_String(int_d,d_encode);
            textField_d.setText(d);
            BigInteger int_p = rsaPrivateKey.getPrimeP();
            p=int_to_String(int_p,p_encode);
            textField_p.setText(p);
            BigInteger int_q = rsaPrivateKey.getPrimeQ();
            q=int_to_String(int_q,q_encode);
            textField_q.setText(q);
            textarea_mingwen.setText("公共指数："+e+"（"+e_encode+"）\n" +
                    "密钥长度："+int_n.bitLength());

        }catch (Exception e){
            f_alert_informationDialog("提示","","生成失败："+e.getMessage());
        }
    }
    public static  void sign() {
        if(privateKey==null){
            f_alert_informationDialog("提示","","请输入私钥!");
            return;
        }
        if(mingwen_text.isEmpty()){
            f_alert_informationDialog("提示","","请输入要签名的内容!");
            return;
        }

        try {
            byte[] decode = Str_To_Bytes(mingwen_encoding,mingwen_text);
            if(new String(decode).startsWith("Error:")){
                textarea_miwen.setText(new  String(decode));
                return;
            }
//            pri_key = pri_key.replaceAll("-----END RSA PRIVATE KEY-----","").replaceAll("-----BEGIN RSA PRIVATE KEY-----","").replaceAll("\n","").replaceAll("\r\n","");
//            pri_key = pri_key.trim();
            // 通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 将传入的 Base64 编码的私钥字符串解码为字节数组。
//            byte[] privateBytes = Base64.decode(pri_key);
            // 将解码后的字节数组封装为 PKCS8EncodedKeySpec 对象。
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            // 使用 KeyFactory 将 PKCS8EncodedKeySpec 转换为一个 PrivateKey 对象。  供签名使用
//            PrivateKey priKey = keyFactory.generatePrivate(keySpec);
            // 创建一个 Signature 实例，指定签名算法为 SHA256withRSA。   SHA256withRSA：指定使用 SHA-256 哈希算法和 RSA 签名算法的组合。
            Signature signature = Signature.getInstance("SHA256withRSA");
            //使用私钥初始化 Signature 对象，并设置为签名模式。
            signature.initSign(privateKey);
            //方法将字节数组添加到签名对象中，以便进行签名。 将消息字符串转换为 UTF-8 编码的字节数组。
            signature.update(decode);
            // 执行签名操作 将签名后的字节数组进行 Base64 编码，转换为字符串
            String result = Conn.Bytes_To_Str(signature.sign(),miwen_encoding);
            textarea_miwen.setText(result);
        }catch (Exception e){
            textarea_miwen.setText("签名失败："+e.getMessage());

        }

    }
    //     * @param message      原始消息（字符串），用于与签名进行匹配。
//            * @param signatureStr 签名的 Base64 编码字符串。
//            * @param publicKey    公钥的 Base64 编码字符串。
    public static  void verify_sign() {
        if(publicKey==null){
            f_alert_informationDialog("提示","","请输入公钥!");
            return;
        }
        if(miwen_encoding.isEmpty()){
            f_alert_informationDialog("提示","","请输入要验签的内容");
            return;
        }
        open_one_input_ui(publicKey,miwen_encoding);
    }
    private static void open_one_input_ui(PublicKey public_key, String message){

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
            ComboBox<String> sign_encode = new ComboBox<>();
            sign_encode.getItems().addAll("Hex","Base64");
            sign_encode.getSelectionModel().select(1);
            Button button = new Button("验签");
            // 将控件添加到HBox中
            hbox.getChildren().addAll(label, lineText, sign_encode,button);

            // 设置TextField的宽度增长优先级
            HBox.setHgrow(lineText, Priority.ALWAYS);

            // 设置按钮点击事件（这里仅做示意）
            button.setOnAction(event -> {
                // 关闭对话框
                dialogStage.close();
                String sign_text = lineText.getText();
                verify_rsa_sign(public_key,mingwen_text,sign_text,sign_encode.getValue());
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


    private static void verify_rsa_sign(PublicKey publicKey,String message,String sign_text,String sign_encode) {
        try{
            byte[] message_bytes = Conn.Str_To_Bytes(mingwen_encoding,message);
            if(new String(message_bytes).startsWith("Error:")){
                textarea_miwen.setText(new  String(message_bytes));
                return;
            }
            if(publicKey==null){
                f_alert_informationDialog("提示","","请输入公钥!");
                return;
            }
//            pub_key = pub_key.replaceAll("-----BEGIN PUBLIC KEY-----","").replaceAll("-----END PUBLIC KEY-----","").replaceAll("\n","").replaceAll("\r\n","");
//            pub_key = pub_key.trim();
            //通过 KeyFactory.getInstance("RSA") 获取一个用于处理 RSA 密钥的工厂实例。
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            //将传入的 Base64 编码的公钥字符串解码为字节数组。
//            byte[] publicBytes = Base64.decode(pub_key);
            //将解码后的字节数组封装为 X509EncodedKeySpec 对象。
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

            //使用 KeyFactory 将 X509EncodedKeySpec 转换为一个 PublicKey 对象。  供验签使用
//            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            //创建一个 Signature 实例，指定签名算法为 SHA256withRSA。   SHA256withRSA：指定使用 SHA-256 哈希算法和 RSA 签名算法的组合。
            Signature signature = Signature.getInstance("SHA256withRSA");
            //使用公钥初始化 Signature 对象，并设置为验证模式。
            signature.initVerify(publicKey);
            //将原始消息数据传递给 Signature 对象进行验证。
            signature.update(message_bytes);
            // 使用 Signature 对象验证签名的有效性。
            byte[] sign_text_byte = Conn.Str_To_Bytes(sign_encode,sign_text);
            if(new String(sign_text_byte).startsWith("Error:")){
                textarea_miwen.setText(new  String(sign_text_byte));
                return;
            }
            boolean isVerify = signature.verify(sign_text_byte);
            if(isVerify){
                String reuslt = "明文: \n"+message+"\n\n"+"公钥: \n"+publicKey_Str+"\n\n"+"签名: \n"+sign_text+"\n\n"+"验签结果: 成功";
                textarea_miwen.setText(reuslt);
            }else{
                String reuslt = "明文: \n"+message+"\n\n"+"公钥: \n"+publicKey_Str+"\n\n"+"签名: \n"+sign_text+"\n\n"+"验签结果: 失败";
                textarea_miwen.setText(reuslt);
            }
        }catch (Exception e){
            textarea_miwen.setText("验签失败："+e.getMessage());
        }
    }
    public static  void calc_n() {
        try{
            if(p==null || q==null || p.isEmpty() || q.isEmpty()){
                f_alert_informationDialog("提示","","请输入p和q");
                return;
            }
            BigInteger int_p = str_to_int(p,p_encode);
            BigInteger int_q =  str_to_int(q,q_encode);
            // 计算 n = p * q
            BigInteger int_n = int_p.multiply(int_q);
            n= int_to_String(int_n,n_encode);
            textField_n.setText(n);
        }catch (Exception e){
            f_alert_informationDialog("提示","","计算n失败："+e.getMessage());
        }

    }
    public static BigInteger str_to_int(String str,String encode){
        try{
            if(Objects.equals(encode, "10进制")){
                return new BigInteger(str,10);
            }else if(Objects.equals(encode, "16进制")){
                return new BigInteger(str, 16);
            }else {
                return new BigInteger(str,10);
            }
        }catch (Exception e){
            f_alert_informationDialog("提示","","转换为数字失败："+e.getMessage());
            return null;
        }

    }
    public static String int_to_String(BigInteger int_value,String encode){
        try{
            if(Objects.equals(encode, "10进制")){
                return int_value.toString();
            }else if(Objects.equals(encode, "16进制")){
                return  int_value.toString(16);
            }else {
                return int_value.toString();
            }
        }catch (Exception e){
            f_alert_informationDialog("提示","","转换为字符串失败："+e.getMessage());
            return null;
        }

    }
    public static  void calc_d() {
        try{
            if(p==null || q==null || e==null || p.isEmpty() || q.isEmpty() || e.isEmpty()){
                f_alert_informationDialog("提示","","请输入p、q、e");
                return;
            }
//            calc_n();
            BigInteger int_p = str_to_int(p,p_encode);
            BigInteger int_q =  str_to_int(q,q_encode);
            // 计算 n 和 phi(n)
//            BigInteger int_n = int_p.multiply(int_q);
//            n= int_to_String(int_n,n_encode);
            BigInteger phiN = int_p.subtract(BigInteger.ONE).multiply(int_q.subtract(BigInteger.ONE));
            // 常用的公钥指数 e
            BigInteger int_e =  str_to_int(e,e_encode);
            // 计算私钥指数 d
            // 确保 e 和 phiN 互质
            if (!int_e.gcd(phiN).equals(BigInteger.ONE)) {
                f_alert_informationDialog("提示","","e 和 phi(n) 必须互质。");
                return;
            }
            // 使用 modInverse 方法计算 e 关于 phiN 的模逆元
            BigInteger int_d =  int_e.modInverse(phiN);
            d= int_to_String(int_d,d_encode);
            textField_d.setText(d);
        }catch (Exception e){
            f_alert_informationDialog("提示","","计算d失败："+e.getMessage());
        }

    }
    public static  void calc_publicKey() {
        try{
            if(p==null || q==null || e==null || p.isEmpty() || q.isEmpty() || e.isEmpty()){
                f_alert_informationDialog("提示","","请输入p、q、e");
                return;
            }
            BigInteger int_p = str_to_int(p,p_encode);
            BigInteger int_q =  str_to_int(q,q_encode);
            BigInteger int_n = int_p.multiply(int_q);
            n= int_to_String(int_n,n_encode);
            textField_n.setText(n);
            // 3. 计算欧拉函数 φ(n) = (p-1)(q-1)
            BigInteger phiN = int_p.subtract(BigInteger.ONE).multiply(int_q.subtract(BigInteger.ONE));
            BigInteger int_e =  str_to_int(e,e_encode);

            // 4. 确保 e 和 φ(n) 互质
            if (!int_e.gcd(phiN).equals(BigInteger.ONE)) {
                f_alert_informationDialog("提示","","e 和 phi(n) 必须互质。");
                return;
            }
            // Create RSA public key specification with modulus and exponent
            RSAPublicKeySpec spec = new RSAPublicKeySpec(int_n, int_e);
            // Get a KeyFactory instance for RSA
            KeyFactory factory = KeyFactory.getInstance("RSA");
            // Generate the public key from the specification
            publicKey =  factory.generatePublic(spec);
            if(Objects.equals( publicKey_encode , "Hex")){
                String hexEncodedPublicKey = Hex.toHexString(publicKey.getEncoded());
                textarea_PublicKey.setText(hexEncodedPublicKey);
            } else if (Objects.equals(publicKey_encode, "Base64")) {
                // 获取公钥和私钥 转为Base64 字符串
                String pubKey = Base64.toBase64String(publicKey.getEncoded());
                textarea_PublicKey.setText(pubKey);
            }else{
                String result = new String(publicKey.getEncoded());
                textarea_PublicKey.setText(result);
            }
        }catch (Exception e){
            f_alert_informationDialog("提示","","生成公钥失败："+e.getMessage());
        }
    }
    public static  void calc_privateKey() {
        try{
            if(p==null || q==null || e==null || p.isEmpty() || q.isEmpty() || e.isEmpty()){
                f_alert_informationDialog("提示","","请输入p、q、e");
                return;
            }
            BigInteger int_p = str_to_int(p,p_encode);
            BigInteger int_q =  str_to_int(q,q_encode);
            BigInteger int_n = int_p.multiply(int_q);
            n= int_to_String(int_n,n_encode);
            textField_n.setText(n);
            // 3. 计算欧拉函数 φ(n) = (p-1)(q-1)
            BigInteger phiN = int_p.subtract(BigInteger.ONE).multiply(int_q.subtract(BigInteger.ONE));
            BigInteger int_e =  str_to_int(e,e_encode);

            // 4. 确保 e 和 φ(n) 互质
            if (!int_e.gcd(phiN).equals(BigInteger.ONE)) {
                f_alert_informationDialog("提示","","e 和 phi(n) 必须互质。");
                return;
            }
            BigInteger int_d =  int_e.modInverse(phiN);
            RSAPrivateKeySpec spec = new RSAPrivateKeySpec(int_n, int_d);
            // Get a KeyFactory instance for RSA
            KeyFactory factory = KeyFactory.getInstance("RSA");
            privateKey =  factory.generatePrivate(spec);
            if(Objects.equals( privateKey_encode , "Hex")){
                String hexEncoded= Hex.toHexString(privateKey.getEncoded());
                textarea_PrivateKey.setText(hexEncoded);
            } else if (Objects.equals(privateKey_encode, "Base64")) {
                // 获取公钥和私钥 转为Base64 字符串
                String priKey = Base64.toBase64String(privateKey.getEncoded());
                textarea_PrivateKey.setText(priKey);
            }else{
                String result = new String(privateKey.getEncoded());
                textarea_PrivateKey.setText(result);
            }
        }catch (Exception e){
            f_alert_informationDialog("提示","","计算d失败："+e.getMessage());
        }
    }



}

