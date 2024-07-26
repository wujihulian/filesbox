package com.svnlan.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpSessionUtil<T> {
    private static final String LANGAUAGE_KEY = "Language"; //lang-country

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    public HttpSessionUtil() {
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public String getLanage() {
        try {
            return this.request.getHeader(LANGAUAGE_KEY);
        } catch (Exception var2) {
            return null;
        }
    }

    public String getRequestId() {
        try {
            return this.request.getHeader("REQUESTID");
        } catch (Exception var2) {
            return null;
        }
    }

}
