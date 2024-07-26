package com.svnlan.jwt.tool;

import com.svnlan.exception.SvnlanRuntimeException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Objects;

/**
 * 请求工具封装
 *
 */
public class RestRequestUtil {

    public static <T> T executePost(RestTemplate restTemplate,
                                    @NotNull String requestUrl,
                                    @NotNull HashMap<String, Object> requestBody,
                                    HttpHeaders headers,
                                    ParameterizedTypeReference<T> responseType) {
        if (Objects.isNull(headers)) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, request, responseType);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SvnlanRuntimeException(String.valueOf(responseEntity.getStatusCodeValue()), "请求出错");
        }
        return responseEntity.getBody();
    }

    public static <T> T executeGet(RestTemplate restTemplate,
                                    @NotNull String requestUrl,
                                    @NotNull HashMap<String, Object> requestBody,
                                    HttpHeaders headers,
                                    ParameterizedTypeReference<T> responseType) {
        if (Objects.isNull(headers)) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, request, responseType);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new SvnlanRuntimeException(String.valueOf(responseEntity.getStatusCodeValue()), "请求出错");
        }
        return responseEntity.getBody();
    }

}
