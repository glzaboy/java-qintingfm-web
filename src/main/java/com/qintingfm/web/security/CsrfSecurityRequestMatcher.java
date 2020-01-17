package com.qintingfm.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    @Override
    public boolean matches(HttpServletRequest request) {
        List<String> allowedUrl = new ArrayList<>();
        allowedUrl.add("/xmlrpc/xmlrpcserver");
        String servletPath = request.getServletPath();
        for (String url:allowedUrl) {
            if(servletPath.contains(url)){
                return false;
            }
        }
        return allowedMethods.matcher(request.getMethod()).matches();
    }
}
