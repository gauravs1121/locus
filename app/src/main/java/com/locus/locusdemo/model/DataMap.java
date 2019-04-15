package com.locus.locusdemo.model;

import java.util.List;

public class DataMap {

    private String comment  = "";
    private boolean showComment =false;
    private List<String> options = null;


    public boolean isShowComment() {
        return showComment;
    }

    public void setShowComment(boolean showComment) {
        this.showComment = showComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getOptions() {
        return options;
    }

    public DataMap() {
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
