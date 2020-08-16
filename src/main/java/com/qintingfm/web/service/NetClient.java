package com.qintingfm.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author guliuzhong
 */
@Slf4j
@Component
public class NetClient {
    public static final String URL_START = "?";
    private static final String URL_SPLIT = "&";
    ObjectMapper objectMapper;
    private final Request.Builder builder = new Request.Builder();
    private OkHttpClient okHttpClient;

    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setPostMap(Map<String, String> postMap) {
        if (postMap.size() > 0) {
            FormBody.Builder builder1 = new FormBody.Builder();
            for (Map.Entry<String, String> postItem : postMap.entrySet()) {
                builder1.add(postItem.getKey(), postItem.getValue());
            }
            RequestBody build = builder1.build();
            builder.post(build);
        }
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        if (headerMap.size() > 0) {
            for (Map.Entry<String, String> postItem : headerMap.entrySet()) {
                builder.addHeader(postItem.getKey(), postItem.getValue());
            }
        }
    }

    public void setJson(String json) {
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json));
    }

    public void setJson(Object jsonObject) throws JsonProcessingException {
        setJson(objectMapper.writeValueAsString(jsonObject));
    }

    public void setBin(String contextType, byte[] bytes) {
        builder.post(RequestBody.create(MediaType.parse(contextType), bytes));
    }


    public void setUrl(String url) {
        setUrl(url, null);
    }

    private String urlEncode(String encodeString) {
        try {
            return URLEncoder.encode(encodeString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("url参数编码出错{}直接返回原始内容", encodeString);
            return encodeString;
        }
    }

    public void setUrl(String url, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder();
        if (queries != null && queries.size() > 0) {
            for (Map.Entry<String, String> next : queries.entrySet()) {
                String key = next.getKey();
                Pattern pattern = Pattern.compile("\\{" + key + "\\}");
                if (pattern.matcher(url).find()) {
                    url = url.replaceAll(pattern.toString(), urlEncode(next.getValue()));
                } else {
                    sb.append(next.getKey());
                    sb.append("=");
                    sb.append(urlEncode(next.getValue()));
                    sb.append("&");
                }

            }
        }
        if (sb.length() > 0) {
            if (url.contains(URL_START)) {
                url = url + URL_SPLIT + sb.toString().substring(0, sb.length() - 1);
            } else {
                url = url + URL_START + sb.toString().substring(0, sb.length() - 1);
            }
        }
        builder.url(url);
    }
    public byte[] requestToBytes() {
        Response execute;
        try {
            execute = okHttpClient.newCall(builder.build()).execute();
            if (!execute.isSuccessful()) {
                assert execute.body() != null;
                log.info("http return httpCode {},BODY {}", execute.code(), execute.body().string());
            }
            if (execute.isSuccessful()) {
                assert execute.body() != null;
                return execute.body().bytes();
            }
        } catch (IOException e) {
            log.error("request http error {}", e.getMessage());
        }
        return null;
    }
    public String requestToString() {
        Response execute;
        try {
            execute = okHttpClient.newCall(builder.build()).execute();
            if (!execute.isSuccessful()) {
                assert execute.body() != null;
                log.info("http return httpCode {},BODY {}", execute.code(), execute.body().string());
            }
            if (execute.isSuccessful()) {
                assert execute.body() != null;
                return execute.body().string();
            }
        } catch (IOException e) {
            log.error("request http error {}", e.getMessage());
        }
        return null;
    }

    public <T> T requestToObject(Class<T> classzz) {
        String s = requestToString();
        if (s != null) {
            T t = null;
            try {
                t = objectMapper.readValue(s, classzz);
            } catch (IOException e) {
                log.error("http return object error{}", e.getMessage());
            }
            return t;
        }
        return null;
    }
}
