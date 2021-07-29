package com.comp8547.comp8547project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class DefaultService {

    public void redirectToApiDocs(final HttpServletResponse response) {
        response.addHeader(HttpHeaders.LOCATION, "/swagger-ui.html");
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
    }
}
