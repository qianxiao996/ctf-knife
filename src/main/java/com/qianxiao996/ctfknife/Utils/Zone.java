package com.qianxiao996.ctfknife.Utils;

import java.util.ArrayList;
import java.util.List;

public class Zone {

    int id;

    String name;
    String source_text;
    String result_text;

    int parentId;

    List<Zone> children;

    public Zone(int id, String name, int parentId,String source_text,String result_text) {

        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.source_text = source_text;
        this.result_text = result_text;

    }

    public void addChildren(Zone zone){

        if(children == null) {

            children = new ArrayList<>();

        }

        children.add(zone);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<Zone> getChildren() {
        return children;
    }

    public void setChildren(List<Zone> children) {
        this.children = children;
    }
    public String getSourceText() {
        return source_text;
    }

    public void setSourceText(String source_text) {
        this.source_text = source_text;
    }

    public String getResultText() {
        return result_text;
    }

    public void setResultText(String result_text) {
        this.result_text = result_text;
    }

}