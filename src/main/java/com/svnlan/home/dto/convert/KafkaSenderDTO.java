package com.svnlan.home.dto.convert;

import javax.validation.constraints.NotBlank;

/**
 * @Author:
 * @Description:
 */
public class KafkaSenderDTO {
    @NotBlank
    private String key;
    @NotBlank
    private String topic;
    @NotBlank
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
