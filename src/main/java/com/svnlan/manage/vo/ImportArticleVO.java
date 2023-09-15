package com.svnlan.manage.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

@Data
@NoArgsConstructor
public class ImportArticleVO {
    private Element contentElement;
    private Element authorElement;
    private Element titleElement;
    private Element descriptionElement;
    private String name;
    private String url;

    public ImportArticleVO(Element contentElement, Element authorElement, Element titleElement, Element descriptionElement,
                           String name, String url){

        this.contentElement = contentElement;
        this.authorElement = authorElement;
        this.titleElement = titleElement;
        this.descriptionElement = descriptionElement;
        this.name = name;
        this.url = url;
    }

}
