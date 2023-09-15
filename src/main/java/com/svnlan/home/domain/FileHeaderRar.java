package com.svnlan.home.domain;

public class FileHeaderRar {

  private String fileNameW;
  private Boolean isDirectory;
  private Long size;
  private Integer index;
  private Boolean encrypted;

  public FileHeaderRar(String fileNameW, Boolean isDirectory, Long size) {
    this.fileNameW = fileNameW;
    this.isDirectory = isDirectory;
    this.size = size;
  }
  public FileHeaderRar(String fileNameW, Boolean isDirectory, Long size, Integer index) {
    this.fileNameW = fileNameW;
    this.isDirectory = isDirectory;
    this.size = size;
    this.index = index;
  }
  public FileHeaderRar(String fileNameW, Boolean isDirectory, Long size, Integer index, Boolean encrypted) {
    this.fileNameW = fileNameW;
    this.isDirectory = isDirectory;
    this.size = size;
    this.index = index;
    this.encrypted = encrypted;
  }
  public Boolean getEncrypted() {
    return encrypted;
  }

  public void setEncrypted(Boolean encrypted) {
    this.encrypted = encrypted;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public String getFileNameW() {
    return fileNameW;
  }

  public void setFileNameW(String fileNameW) {
    this.fileNameW = fileNameW;
  }

  public Boolean getDirectory() {
    return isDirectory;
  }

  public void setDirectory(Boolean directory) {
    isDirectory = directory;
  }
}
