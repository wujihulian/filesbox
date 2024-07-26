package com.svnlan.user.domain;

import lombok.Data;

@Data
public class PostContent {
    private String url;
    private String message;
    private String at;
    private String msgType;


    public PostContent(){

    }

    public PostContent(String url, String message){
        this.url = url;
        this.message = message;
    }

    public PostContent(String url, String message, String msgType){
        this.url = url;
        this.message = message;
        this.msgType = msgType;
    }
}
