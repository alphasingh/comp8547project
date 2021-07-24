package com.comp8547.comp8547project.controller;

import com.comp8547.comp8547project.service.DefaultService;
import com.comp8547.comp8547project.service.webcrawler.SearchEngineService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class DefaultController {

    private final DefaultService defaultService;
    private final SearchEngineService searchClass;

    @Hidden
    @GetMapping(value = {"/", "/docs", "/api-docs"})
    public void defaultLandingController(final HttpServletResponse response) {
        defaultService.redirectToApiDocs(response);
    }

    @GetMapping(value = "/search",
            produces = APPLICATION_JSON_VALUE)
    public List<?> search(@RequestParam(name = "keyword") final String keyword) {
        return searchClass.search(keyword);
    }

    @GetMapping(value = "/crawl")
    public String crawlText(@RequestParam(name = "url") final String url) {
        return searchClass.crawlTextFromUrl(url);
    }

    @GetMapping(value = "/bm")
    public long bm(@RequestParam(name = "word") final String word) {
        return searchClass.findWordInCrawledText(word);
    }

    @GetMapping(value = "/patterns")
    public long patterns(@RequestParam(name = "pattern") final String pattern) {
        return searchClass.countRegexPattern(pattern);
    }

    @GetMapping(value = "/closest")
    public Map<String, Integer> closest(@RequestParam(name = "word") final String word) {
        return searchClass.wordsWithSmallestEditDistance(word);
    }
}