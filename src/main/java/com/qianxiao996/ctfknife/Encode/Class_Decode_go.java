package com.qianxiao996.ctfknife.Encode;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.qianxiao996.ctfknife.Encode.Utils.Result;

//一键base解码
public class Class_Decode_go extends Thread {

    private TextArea textarea_result;
    private ArrayList<String> Encode_type;
    private String souce_text;
    private String encoding;

    private Boolean is_line;
    private Button button;
    public  String temp_key_1 = "";
    public void setValue(Button encode_base_go, TextArea textarea_result, ArrayList<String> Encode_type, String souce_text, String encoding, Boolean is_line) {
        this.textarea_result = textarea_result;
        this.Encode_type = Encode_type;
        this.souce_text = souce_text.trim();
        this.encoding = encoding;
        this.is_line =is_line;
        this.temp_key_1 = textarea_result.getText();
        this.button = encode_base_go;
    }

    public void run()  {
        button.setDisable(true);
        textarea_result.clear();

        //得到所有源文本换列表
        List<String> all_souce_text = new ArrayList<>();
        if(is_line==Boolean.TRUE){
            textarea_result.appendText("该模式暂不支持按行处理！");
            button.setDisable(false);
            return;
        }else{
            all_souce_text.add(souce_text);
        }

        //遍历编码
//        Class_Encode obj = new Class_Encode();
//        Class clazz = obj.getClass();
//        Class clazz = this.getClass();
        Class_Decode obj =new Class_Decode();
        Class<? extends Class_Decode> clazz = obj.getClass();
        String methond_text = "";
        for(String text:all_souce_text){
            if(text.isEmpty()){
                continue;
            }
//            textarea_result.appendText(text+"解码结果如下:\r\n");
            for(String methods : Encode_type){
                methond_text =methods;
                String real_methods = methods.replace("(", "_").replace(")", "_").replace("->", "_").replace("%", "_");
                Method method = null;
//                Method method_setvalue = null;
                try {
                    method = clazz.getMethod("Fuc_"+real_methods, String.class, String.class);
                } catch (NoSuchMethodException e) {
                    textarea_result.appendText(methond_text+": 无法匹配的方法:Fuc_"+real_methods+"\r\n");
                    throw new RuntimeException(e);
//                    continue;
                }
                try{
//                    obj.setValue(textarea_result,methods,text,encoding,is_line);
                    Result result = (Result) method.invoke(obj,methond_text,text);
                    textarea_result.appendText(methond_text+": \r\n"+result.getResult_text()+"\r\n************************************************************"+"\r\n");
//                    textarea_result.appendText(methond_text+": "+result+"\r\n");
                }catch (Exception e){
                    textarea_result.appendText(methond_text+": 解码失败\r\n************************************************************"+"\r\n");
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            textarea_result.appendText("\r\n");


        }

        button.setDisable(false);

    }
}

