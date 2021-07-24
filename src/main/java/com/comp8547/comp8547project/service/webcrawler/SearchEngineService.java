package com.comp8547.comp8547project.service.webcrawler;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class SearchEngineService {

    static String crawledText = "crawledText";

    @SneakyThrows
    public HashMap<String, Integer> wordsWithSmallestEditDistance(final String search) {
        final HashMap<String, Integer> words = new HashMap<>();
        words.put(search, 1);
        return words;
    }

    @SneakyThrows
    public String crawlTextFromUrl(final String url) {
        crawledText = "cleanText" + url;
        return crawledText;
    }

    @SneakyThrows
    public long countRegexPattern(final String pattern) {
        return pattern.length();
    }

    @SneakyThrows
    public long findWordInCrawledText(final String word) {
        return word.length();
    }

    @SneakyThrows
    public List<String> search(final String keyword) {
        return Collections.singletonList(keyword);
    }
}
