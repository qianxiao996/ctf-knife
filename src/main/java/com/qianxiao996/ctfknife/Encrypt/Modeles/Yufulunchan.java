package com.qianxiao996.ctfknife.Encrypt.Modeles;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Yufulunchan {
    private static final byte[] KEY = "XDXDtudou@KeyFansClub^_^Encode!!".getBytes(StandardCharsets.UTF_8);
    private static final byte[] IV = "Potato@Key@_@=_=".getBytes(StandardCharsets.UTF_8);
    private static List<Character> BYTEMARK = Arrays.asList('冥', '奢', '梵', '呐', '俱', '哆', '怯', '諳', '罰', '侄', '缽', '皤');
    private static List<Character> foYue = Arrays.asList(
            '滅', '苦', '婆', '娑', '耶', '陀', '跋', '多', '漫', '都', '殿', '悉', '夜', '爍', '帝', '吉',
            '利', '阿', '無', '南', '那', '怛', '喝', '羯', '勝', '摩', '伽', '謹', '波', '者', '穆', '僧',
            '室', '藝', '尼', '瑟', '地', '彌', '菩', '提', '蘇', '醯', '盧', '呼', '舍', '佛', '參', '沙',
            '伊', '隸', '麼', '遮', '闍', '度', '蒙', '孕', '薩', '夷', '迦', '他', '姪', '豆', '特', '逝',
            '朋', '輸', '楞', '栗', '寫', '數', '曳', '諦', '羅', '曰', '咒', '即', '密', '若', '般', '故',
            '不', '實', '真', '訶', '切', '一', '除', '能', '等', '是', '上', '明', '大', '神', '知', '三',
            '藐', '耨', '得', '依', '諸', '世', '槃', '涅', '竟', '究', '想', '夢', '倒', '顛', '離', '遠',
            '怖', '恐', '有', '礙', '心', '所', '以', '亦', '智', '道', '。', '集', '盡', '死', '老', '至'
    );
    private  static  List<Character> ruShiWoWen = Arrays.asList(
            '謹', '穆', '僧', '室', '藝', '瑟', '彌', '提', '蘇', '醯', '盧', '呼', '舍', '參', '沙', '伊',
            '隸', '麼', '遮', '闍', '度', '蒙', '孕', '薩', '夷', '他', '姪', '豆', '特', '逝', '輸', '楞',
            '栗', '寫', '數', '曳', '諦', '羅', '故', '實', '訶', '知', '三', '藐', '耨', '依', '槃', '涅',
            '竟', '究', '想', '夢', '倒', '顛', '遠', '怖', '恐', '礙', '以', '亦', '智', '盡', '老', '至',
            '吼', '足', '幽', '王', '告', '须', '弥', '灯', '护', '金', '刚', '游', '戏', '宝', '胜', '通',
            '药', '师', '琉', '璃', '普', '功', '德', '山', '善', '住', '过', '去', '七', '未', '来', '贤',
            '劫', '千', '五', '百', '万', '花', '亿', '定', '六', '方', '名', '号', '东', '月', '殿', '妙',
            '尊', '树', '根', '西', '皂', '焰', '北', '清', '数', '精', '进', '首', '下', '寂', '量', '诸',
            '多', '释', '迦', '牟', '尼', '勒', '阿', '閦', '陀', '中', '央', '众', '生', '在', '界', '者',
            '行', '于', '及', '虚', '空', '慈', '忧', '各', '令', '安', '稳', '休', '息', '昼', '夜', '修',
            '持', '心', '求', '诵', '此', '经', '能', '灭', '死', '消', '除', '毒', '害', '高', '开', '文',
            '殊', '利', '凉', '如', '念', '即', '说', '曰', '帝', '毘', '真', '陵', '乾', '梭', '哈', '敬',
            '禮', '奉', '祖', '先', '孝', '雙', '親', '守', '重', '師', '愛', '兄', '弟', '信', '朋', '友',
            '睦', '宗', '族', '和', '鄉', '夫', '婦', '教', '孫', '時', '便', '廣', '積', '陰', '難', '濟',
            '急', '恤', '孤', '憐', '貧', '創', '廟', '宇', '印', '造', '經', '捨', '藥', '施', '茶', '戒',
            '殺', '放', '橋', '路', '矜', '寡', '拔', '困', '粟', '惜', '福', '排', '解', '紛', '捐', '資'
    );
    public static String encryptFoYue(String plaintext) throws Exception {
        // 将明文转换为UTF-16LE编码的字节数组
        byte[] plainBytes = plaintext.getBytes(StandardCharsets.UTF_16LE);
        // 使用AES加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedData = cipher.doFinal(plainBytes);
        // 将加密后的字节数组转换为字符表示
        StringBuilder ciphertextBuilder = new StringBuilder();
        for (byte b : encryptedData) {
            int index = b & 0xFF; // 将byte转换为无符号整数 [0, 255]
            if (index >= 128) {
                // 对于大于等于128的值，添加BYTEMARK前缀
                char bytemarkChar = BYTEMARK.get((index - 128) % BYTEMARK.size());
                ciphertextBuilder.append(bytemarkChar);
                index = index - 128;
            }
            if (index < foYue.size()) {
                ciphertextBuilder.append(foYue.get(index));
            } else {
                throw new IllegalArgumentException("Invalid index after encryption: " + index);
            }
        }
        return "佛曰：" + ciphertextBuilder.toString(); // 添加分隔符
    }

    public static String decryptFoYue(String ciphertext) throws Exception {

        String[] parts = ciphertext.split("[:：]");
        if (parts.length > 1) {
            ciphertext = String.join("", Arrays.copyOfRange(parts, 1, parts.length)).trim();
        } else {
            ciphertext = parts[0].trim();
        }

        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        for (int i = 0; i < ciphertext.length(); ) {
            char c = ciphertext.charAt(i);
            int index = BYTEMARK.indexOf(c);
            if(index!=-1){
                i+=1;
                char d = ciphertext.charAt(i);
                dataStream.write(foYue.indexOf(d)+128);
            }else{
                dataStream.write(foYue.indexOf(c));
            }
            i+=1;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedData = cipher.doFinal(dataStream.toByteArray());
        return new String(decryptedData, StandardCharsets.UTF_16LE).trim();
    }
    public static String encryptRuShiWoWen(String plaintext) throws Exception {
// 假设有一个方法可以将明文转换为7z格式的字节数组
        byte[] plainBytes = create7zContent(plaintext.getBytes(StandardCharsets.UTF_8));
        // 使用AES加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedData = cipher.doFinal(plainBytes);
        // 将加密后的字节数组转换为字符表示
        StringBuilder ciphertextBuilder = new StringBuilder();
        for (byte b : encryptedData) {
            int index = b & 0xFF; // 将byte转换为无符号整数 [0, 255]
            if (index < ruShiWoWen.size()) {
                ciphertextBuilder.append(ruShiWoWen.get(index));
            } else {
                throw new IllegalArgumentException("Invalid index after encryption: " + index);
            }
        }
        return "如是我闻：" + ciphertextBuilder.toString(); // 添加分隔符
    }
    private static byte[] create7zContent(byte[] data) throws IOException {
        File tempFile = File.createTempFile("temp", ".7z");
        try (SevenZOutputFile sevenZOutputFile = new SevenZOutputFile(new File(String.valueOf(tempFile)))) {
            // 创建新的条目
            SevenZArchiveEntry entry = sevenZOutputFile.createArchiveEntry(new File("default"), "default");
            sevenZOutputFile.putArchiveEntry(entry);

            // 写入内容
            sevenZOutputFile.write(data);
            sevenZOutputFile.closeArchiveEntry();
        }
        return  readFile(tempFile);
    }

    private static byte[] readFile(File file) throws IOException {
        try (FileInputStream fin = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fin.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray(); // 返回字节数组
        }finally {
            if (file.exists()) {
                file.delete(); // 删除临时文件
            }
        }
    }

    public static String decryptRuShiWoWen(String ciphertext) throws Exception {

        // 分割并清理ciphertext
        if (ciphertext.contains(":") || ciphertext.contains("：")) {
            ciphertext = ciphertext.split("[:：]", 2)[1].trim();
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        for (char c : ciphertext.toCharArray()) {
            data.write(ruShiWoWen.indexOf(c));
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedData = cipher.doFinal(data.toByteArray());
        return read7zContent(decryptedData);
    }

    private static String read7zContent(byte[] data) throws IOException {
            // 将字节数组写入临时文件
        File tempFile = File.createTempFile("temp", ".7z");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data);
        }
        try {
            StringBuilder content = new StringBuilder();
            // 使用 SevenZFile 读取 7z 文件 StringBuilder content = new StringBuilder();
            try (SevenZFile sevenZFile = new SevenZFile(tempFile)) {
                SevenZArchiveEntry entry;
                while ((entry = sevenZFile.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        try (InputStream is = sevenZFile.getInputStream(entry)) {
                            while ((len = is.read(buffer)) > 0) {
                                baos.write(buffer, 0, len);
                            }
                        }
                        content.append(baos.toString("UTF-8")); // 将内容添加到 StringBuilder
                    }
                }
            }
            return content.toString(); // 返回解压缩后的内容
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (tempFile.exists()) {
                tempFile.delete(); // 删除临时文件
            }
        }
    }

}