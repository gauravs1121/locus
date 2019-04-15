package com.locus.locusdemo.model;

public class DataModel {

    private String type;
    private String id;
    private String title;
    private DataMap dataMap;

    public DataModel(String type, String id, String title, DataMap dataMap) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.dataMap = dataMap;
    }

    public DataModel() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataMap getOptionModel() {
        return dataMap;
    }

    public void setOptionModel(DataMap dataMap) {
        this.dataMap = dataMap;
    }
}
