package com.comp8547.comp8547project.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.comp8547.comp8547project.service.DefaultService;
import com.comp8547.comp8547project.service.SearchEngineService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@Slf4j
//@RequiredArgsConstructor(onConstructor = @__({@Autowired, @Lazy}))
public class Controller {

	@Autowired
    private DefaultService defaultServiceobject;
	@Autowired
    private SearchEngineService searchEngineServiceObject ;

    @Hidden
    @GetMapping(value = {"/", "/docs", "/api-docs"})
    public void defaultLandingController(final HttpServletResponse response) {
    	defaultServiceobject.redirectToApiDocs(response);
    }

    @GetMapping(value = "/search",
            produces = APPLICATION_JSON_VALUE)
    public List<?> searchTheWord(@RequestParam(name = "keyword") final String keyword) throws IOException {
        return searchEngineServiceObject.searchFromGoogle(keyword);
    }

    @GetMapping(value = "/crawl")
    public String crawlText(@RequestParam(name = "url") final String url) throws IOException {
        return searchEngineServiceObject.crawlTextFromUrl(url);
    }

    @GetMapping(value = "/bm")
    public long bm(@RequestParam(name = "word") final String word) {
        return searchEngineServiceObject.findWordInCrawledText(word);
    }

    @GetMapping(value = "/patterns")
    public long patterns(@RequestParam(name = "pattern") final String pattern) {
        return searchEngineServiceObject.countRegexPattern(pattern);
    }

    @GetMapping(value = "/closest")
    public Map<String, Integer> closest(@RequestParam(name = "word") final String word) {
        return searchEngineServiceObject.wordsWithSmallestEditDistance(word);
    }
}