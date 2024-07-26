package com.svnlan.webdav.servlet;

import com.ithit.webdav.server.DavRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class HttpServletDavRequest extends DavRequest {
    private HttpServletRequest httpServletRequest;

    public HttpServletDavRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getHeader(String name) {
        return this.httpServletRequest.getHeader(name);
    }

    public String getMethod() {
        return this.httpServletRequest.getMethod();
    }

    public String getRequestURI() {
        return this.httpServletRequest.getRequestURI();
    }

    public String getQueryString() {
        return this.httpServletRequest.getQueryString();
    }

    public String getContextPath() {
        return this.httpServletRequest.getContextPath();
    }

    public String getServerPath() {
        return this.httpServletRequest.getServletPath();
    }

    public int getServerPort() {
        return this.httpServletRequest.getServerPort();
    }

    public String getScheme() {
        return this.httpServletRequest.getScheme();
    }

    public String getServerName() {
        return this.httpServletRequest.getServerName();
    }

    public String getCharacterEncoding() {
        return this.httpServletRequest.getCharacterEncoding();
    }

    public InputStream getInputStream() throws IOException {
        return this.httpServletRequest.getInputStream();
    }

    public long getContentLength() {
        String contentLength = this.httpServletRequest.getHeader("Content-Length");
        if (contentLength != null) {
            try {
                return Long.decode(contentLength);
            } catch (NumberFormatException var3) {
            }
        }

        return (long) this.httpServletRequest.getContentLength();
    }

    public String getContentType() {
        return this.httpServletRequest.getContentType();
    }

    public Enumeration<String> getHeaderNames() {
        return this.httpServletRequest.getHeaderNames();
    }

    public Object getOriginalRequest() {
        return this.httpServletRequest;
    }
}
