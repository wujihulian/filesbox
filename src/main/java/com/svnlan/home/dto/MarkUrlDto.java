package com.svnlan.home.dto;

import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/15 11:06
 */
@Data
public class MarkUrlDto {

    private String name;
    private String url;
    private String position;
    private String colorchannelmixer;
    private String format;
    private String scale;

    public String getColorchannelmixer() {
        return colorchannelmixer;
    }

    public void setColorchannelmixer(String colorchannelmixer) {
        this.colorchannelmixer = colorchannelmixer;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
