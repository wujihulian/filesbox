package com.svnlan.NettyWebchat.utils;

import com.svnlan.NettyWebchat.Common.SpringManager;
import com.svnlan.utils.ParamUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * @Author:
 * @Description:
 */
public class RequestTransUtil {
    //Netty转Spring请求
    public static MockHttpServletRequest transRequest2Spring(FullHttpRequest nettyRequest){
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(nettyRequest.uri()).build();
        ServletContext servletContext = SpringManager.getInstance().getDispatcherServlet().getServletConfig().getServletContext();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest(servletContext);
        servletRequest.setRequestURI(uriComponents.getPath());
        servletRequest.setPathInfo(uriComponents.getPath());
        servletRequest.setMethod(nettyRequest.method().name());

        if (uriComponents.getScheme() != null) {
            servletRequest.setScheme(uriComponents.getScheme());
        }
        if (uriComponents.getHost() != null) {
            servletRequest.setServerName(uriComponents.getHost());
        }
        if (uriComponents.getPort() != -1) {
            servletRequest.setServerPort(uriComponents.getPort());
        }

        for (String name : nettyRequest.headers().names()) {
            servletRequest.addHeader(name, nettyRequest.headers().get(name));
        }

        ByteBuf content = nettyRequest.content();
        content.readerIndex(0);
        byte[] data = new byte[content.readableBytes()];
        content.readBytes(data);
        servletRequest.setContent(data);

        if (uriComponents.getQuery() != null) {
            String query = UriUtils.decode(uriComponents.getQuery(), "UTF-8");
            servletRequest.setQueryString(query);
        }

        Map<String, String> paramMap = ParamUtil.getRequestParams(nettyRequest);
        if(! CollectionUtils.isEmpty(paramMap)){
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                servletRequest.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return servletRequest;
    }

    //WebSocket转Spring请求
    public static MockHttpServletRequest transFrame2Spring(WebSocketFrame frame, String url){
        ServletContext servletContext = SpringManager.getInstance().getDispatcherServlet().getServletConfig().getServletContext();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest(servletContext);
        servletRequest.setRequestURI(url);
        servletRequest.setPathInfo(url);
        servletRequest.setMethod(HttpMethod.POST.name());
        servletRequest.setContentType(HttpHeaderValues.TEXT_PLAIN.toString());

        if(frame != null){
            ByteBuf content = frame.content();
            content.readerIndex(0);
            byte[] data = new byte[content.readableBytes()];
            content.readBytes(data);
            servletRequest.setContent(data);
        }
        return servletRequest;
    }


    //Spring转Netty响应
    public static FullHttpResponse transResponse2Netty(MockHttpServletResponse servletResponse){
        HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(servletResponse.getContentAsByteArray()));

        for (String name : servletResponse.getHeaderNames()) {
            for (Object value : servletResponse.getHeaderValues(name)) {
                response.headers().add(name, value);
            }
        }
        return response;
    }
}
