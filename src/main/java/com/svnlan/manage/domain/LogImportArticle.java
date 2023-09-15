package com.svnlan.manage.domain;

/**
 * @Author:
 * @Description:
 */
public class LogImportArticle {
    private String url;
    private String savePath;
    private Long userId;
    private Integer platformId;
    private Integer schoolId;
    private String title;
    private String infoSource;
    private String introduce;
    private InfoSource infoSourceVO;
    private String content;
//    private String

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(String infoSource) {
        this.infoSource = infoSource;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


    public InfoSource getInfoSourceVO() {
        return infoSourceVO;
    }

    public void setInfoSourceVO(InfoSource infoSourceVO) {
        this.infoSourceVO = infoSourceVO;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
