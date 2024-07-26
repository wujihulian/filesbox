package com.svnlan.home.dto.convert;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:
 * @Description:
 */
public class H5CallBackDTO {
    private MultipartFile file;
    private Long id;
    private String name;
    private String arg;
    private Integer totalpage;
    private Integer isppt2img;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public Integer getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(Integer totalpage) {
        this.totalpage = totalpage;
    }

    public Integer getIsppt2img() {
        return isppt2img;
    }

    public void setIsppt2img(Integer isppt2img) {
        this.isppt2img = isppt2img;
    }
}
