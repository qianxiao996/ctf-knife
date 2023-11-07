package com.qianxiao996.ctfknife.Encode;

import com.qianxiao996.ctfknife.Encode.Utils.Zone;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.qianxiao996.ctfknife.Encode.Utils.Result;

public class Class_GetFlag extends Thread {

    private static TextArea textarea_result;
    private String souce_text;
    private String encoding;
    private String key1;
    private String key2;
    private String keyFlag;
    boolean is_getflag =false;

    public ArrayList list_checkbox;
    private Button button;
    public ArrayList<String> Exception_List =  new ArrayList<>(Arrays.asList("java.lang.","失败","out of bounds for length","\" under radix","Exception","Error"));
    public void setValue(ArrayList list_checkbox,TextArea selectedTextAreaResult, Button button,String sourceText, String encoding, String key1, String key2, String keyFlag) {
        this.list_checkbox = list_checkbox;
        this.textarea_result = selectedTextAreaResult;
        this.souce_text = sourceText.trim();
        this.button = button;
        this.encoding = encoding;
        this.key1 =key1;
        this.key2 =key2;
        this.keyFlag =keyFlag;
    }
    //获取类中的所有方法
    public ArrayList<HashMap> get_ALl_Methods(){
        //得到所有源文本换列表
        ArrayList<HashMap> all_method =  new ArrayList<>();
        if(list_checkbox.contains("base")){
            Class_Base_Decode decode_base = new Class_Base_Decode();
            Method[] decode_base_methods = decode_base.getClass().getDeclaredMethods();
            for (Method method : decode_base_methods) {
                if(method.getName().startsWith("Fuc_")){
                    HashMap<String, Object> temp_ = new HashMap<>();
                    temp_.put("obj",decode_base);
                    temp_.put("methods",method);
                    temp_.put("type",method.getName());
                    all_method.add(temp_);
                }
            }

        }
        if(list_checkbox.contains("decode")){
            Class_Decode decode = new Class_Decode();
            Method[] decode_methods = decode.getClass().getDeclaredMethods();
            for (Method method : decode_methods) {
                if(method.getName().startsWith("Fuc_") && !((method.getName()).contains("图片"))){
                    HashMap<String, Object> temp_ = new HashMap<>();
                    temp_.put("obj",decode);
                    temp_.put("methods",method);
                    temp_.put("type",method.getName());
                    all_method.add(temp_);
                }
            }
        }
        if(list_checkbox.contains("decrypt")){
            Class_Decrypt decrypt = new Class_Decrypt();
            Method[] decrypt_methods = decrypt.getClass().getDeclaredMethods();
            for (Method method : decrypt_methods) {
                if(method.getName().startsWith("Fuc_")) {
                    HashMap<String, Object> temp_ = new HashMap<>();
                    temp_.put("obj", decrypt);
                    temp_.put("methods", method);
                    temp_.put("type", method.getName());
                    all_method.add(temp_);
                }
            }
        }
        return  all_method;

    }
    ArrayList<Zone> list_result = new ArrayList<>();
    public void run()  {
        textarea_result.clear();
        keyFlag  = keyFlag.toLowerCase();
        if(keyFlag==null||keyFlag.isEmpty()){
            textarea_result.setText("请输入FLAG关键字！");
            return;
        }
        button.setDisable(true);
        ArrayList<HashMap> all_method = get_ALl_Methods();
        String temp_text = souce_text;
        Result aa;
        try {
            int current_id=1;
            int parent_id = 0;
            Result temp_flag_result = null;
            aa = gogogo(all_method,temp_text,current_id,parent_id,temp_flag_result);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        textarea_result.appendText("该功能速度较慢，请耐心等待...\r\n");
        if(aa==null){
            textarea_result.appendText("解密失败! 可能的加密方式如下:\r\n");
        }else{
            textarea_result.appendText("恭喜你，GetFlag成功！\r\nFlag: "+aa.getResult_text()+"\r\n解密链如下:\r\n");
//            textarea_result.appendText(aa.getResult_text());
        }
//        System.out.println(list_result);
        List<Zone> tree_result = DiGui_Out_Result(list_result);
        String aaa =out_tree(tree_result);
        textarea_result.appendText(aaa);
//        System.out.println(aaa);
        button.setDisable(false);

    }
    public  String out_tree(List<Zone> tree_list){
        ArrayList ut_result= new ArrayList();
        for(Zone i :tree_list){
            if(i.getChildren().size()>0){
                ut_result.add(i.getName());
                ut_result.add(out_tree(i.getChildren()));

            }else{
//                System.out.println();
                ut_result.add(i.getName()+":"+i.getResultText());
                ut_result.add("\r\n");
            }
        }
        String res_ult = (String.join("-->", ut_result)).replace("-->\r\n-->","\r\n\r\n");
        if (res_ult.trim().endsWith("-->")){
            res_ult  =res_ult.trim().substring(0,res_ult.trim().length()-3)+"\r\n";

        }
        return res_ult;
//        return (String.join("-->", ut_result)).replace("-->\r\n-->","\r\n\r\n").replace("-->\r\n","\r\n");
    }
    public List<Zone> DiGui_Out_Result(ArrayList<Zone> all_result){
        List<Zone> rootMenu = new ArrayList<>();
        for (Zone menu : all_result) {
            if(menu.getParentId() == 0){
                rootMenu.add(menu);
            }
        }

        for (Zone nav : rootMenu) {
            List<Zone> childList = getChildren(nav.getId(),all_result);
            nav.setChildren(childList);
        }
        return rootMenu;

    }
    private static ArrayList<Zone> getChildren(int id, List<Zone> allMenu) {
        ArrayList<Zone> childList = new ArrayList<>();
        for (Zone nav : allMenu) {
            if(nav.getParentId() == id){
                childList.add(nav);
            }
        }
        for (Zone nav : childList) {
            nav.setChildren(getChildren(nav.getId(),allMenu));
        }
        if(childList.size()==0){
            return new ArrayList<Zone>();
        }
        return childList;
    }
    public Result gogogo(ArrayList<HashMap> all_method , String temp_text,int current_id,int parent_id,Result temp_flag_result) throws InvocationTargetException, IllegalAccessException {
        ArrayList<Result> tee_result = xunhuna_decrypt(all_method,temp_text);
        for(Result i : tee_result) {
            if(is_getflag){
                return temp_flag_result;
            }
            //判断可读字符串
            HashMap temp = new HashMap();
            temp.put("id",current_id);
            temp.put("parent_id",parent_id);
            temp.put("name",i.getName());
            temp.put("result_text",i.getResult_text());
            temp.put("source_text",temp_text);
            list_result.add(new Zone(current_id,i.getName(),parent_id,temp_text,i.getResult_text()));
            if(i.getResult_text().toLowerCase().contains(keyFlag)){
                is_getflag = true;
                temp_flag_result = i;
                break;
            }else{
                temp_flag_result = gogogo(all_method,i.getResult_text(),current_id+1,current_id,temp_flag_result);
            }
        }
        return  temp_flag_result;
    }
    public boolean is_Contains_Exception_Str(String str){
        boolean contains= false;
        for (String item : Exception_List) {
            if (str.contains(item)) {
                contains = true;
                break;
            }
        }
        return  contains;
    }

    //判断是不是可见字符
    public boolean isAllVisible(String str) {
        if(str.isEmpty()){
            return false;
        }
        //判断是否包含不可见，包括 Unicode 中的所有非可打印字符，有返回true
        if(str.matches(".*\\p{Cntrl}.*") ){
            return false;
        }else if(str.matches(".*\\p{C}.*")){
            return  true;
        }else{
            return  true;
        }
    }


    public ArrayList<Result> xunhuna_decrypt(ArrayList<HashMap> all_method , String text) throws InvocationTargetException, IllegalAccessException {
        ArrayList<Result> all_result =new ArrayList<>();
        for(HashMap i : all_method){
            String type = (String) i.get("type");
            try {
                Object obj = i.get("obj");
                Method method = (Method) i.get("methods");
//                System.out.println(method.getName());
                Result result = (Result) method.invoke(obj,method.getName(),text);
                if (result.is_success()&& isAllVisible(result.getResult_text())&&!Objects.equals(result.getResult_text(), "") && !result.getResult_text().isEmpty()&&!is_Contains_Exception_Str(result.getResult_text())){
                    all_result.add(result);
                }
//                System.out.println(result.getResult_text());
            }catch (Exception e){
//                System.out.println(type+"解密失败！");
                throw new RuntimeException(e);
            }


        }
        return all_result;
    }
}