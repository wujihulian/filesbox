package com.svnlan.webdav.servlet;

import com.ithit.webdav.server.DavResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class HttpServletDavResponse extends DavResponse {
    private final HttpServletResponse httpServletResponse;

    public HttpServletDavResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public void addHeader(String name, String value) {
        this.httpServletResponse.addHeader(name, value);
    }

    public void setStatus(int code, String description) {
        this.httpServletResponse.setStatus(code, description);
    }

    public void setContentLength(long length) {
        this.setHeader("Content-Length", Long.toString(length));
    }

    public void setContentType(String s) {
        this.httpServletResponse.setContentType(s);
    }

    public void setCharacterEncoding(String s) {
        this.httpServletResponse.setCharacterEncoding(s);
    }

    public OutputStream getOutputStream() throws IOException {
        return this.httpServletResponse.getOutputStream();
    }

    public void setHeader(String s, String s1) {
        this.httpServletResponse.setHeader(s, s1);
    }

    public Object getOriginalResponse() {
        return this.httpServletResponse;
    }
}

