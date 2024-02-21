package com.app.twittercloneapi.Posts;

public class PostBody {
    private String text;
    private String fileBase64;
    private int parent;
    private String fileType;
    public PostBody() {}
    public void setText(String text) {
        this.text = text;
    }
    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }
    public String getText() {
        return text;
    }
    public String getFileBase64() {
        return fileBase64;
    }
    public void setParent(int parent) {
        this.parent = parent;
    }
    public int getParent() {
        return parent;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getFileType() {
        return fileType;
    }
}