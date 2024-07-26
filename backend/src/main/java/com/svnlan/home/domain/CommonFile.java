package com.svnlan.home.domain;

/**
 * @Author:
 * @Description:
 * @Date:
 */
public class CommonFile {
    private String name;
    private String path;
    private Boolean isDirectory;

    public CommonFile() {
    }

    public CommonFile(String name, String path, Boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }
}
