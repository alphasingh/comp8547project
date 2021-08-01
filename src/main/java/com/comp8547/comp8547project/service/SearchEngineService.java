package com.comp8547.comp8547project.service;

import com.comp8547.comp8547project.webcrawler.TST;
import lombok.SneakyThrows;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.comp8547.comp8547project.webcrawler.BoyerMoore;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SearchEngineService {

    static ArrayList<String> resultsFromGoogle; 
    static String textCrawl = "textCrawl";
    static TST<Integer>tst;
    @SneakyThrows
    public HashMap<String, Integer> wordsWithSmallestEditDistance(final String wordToSearch) {
        final HashMap<String, Integer> totalWords = new HashMap<>();
        String[] crawledWords = textCrawl.split(" ");
        totalWords.put("total", crawledWords.length);
        System.out.println("Total crawledWords found: " + crawledWords.length);
        for (String word : crawledWords) {
            int diff = editDistance(wordToSearch, word);
            if (diff < wordToSearch.length()) {
            	totalWords.put(word, diff);
            }
        }
        return sortByValue(totalWords);
    }

    @SneakyThrows
    public String crawlTextFromUrl(final String url) throws IOException {
        System.out.println(url);
        final Document documentObject = Jsoup.connect(url).ignoreContentType(true).get();
        final String cleanText = Jsoup.clean(documentObject.html(), new Whitelist());
        textCrawl = cleanText;
        return cleanText.replaceAll(" ", "\n");//to beautify
    }
    @SneakyThrows
    public static Integer get_value(String word)
    {
        if(tst==null)
        {
            tst = create_tst();
        }
        return tst.get(word);
    }
    @SneakyThrows
    public long countRegexPattern(final String pattern) {
        long patternCount = 0;
        final Matcher patternMatcher = Pattern.compile(pattern).matcher(textCrawl);
        while (patternMatcher.find()) patternCount += 1;
        return patternCount;
    }

    @SneakyThrows
    public long findWordInCrawledText(final String wordToSearch) {
    	final String searchSpace = Optional.ofNullable(textCrawl).orElse("");
        BoyerMoore bmObject = new BoyerMoore(wordToSearch);
        return bmObject.search(searchSpace);
    }
    @SneakyThrows
    public String getLongestPrefix(final String word)
    {
        if(tst==null)
        {
            tst = create_tst();
        }
        return tst.longestPrefixOf(word);
    }
    @SneakyThrows
    public Iterable<String> getPrefixMatch(final String word)
    {
        if(tst==null)
        {
            tst = create_tst();
        }
        return tst.prefixMatch(word);

    }
    @SneakyThrows
    public List<String> searchFromGoogle(final String keyword) throws IOException {
    	resultsFromGoogle = new ArrayList<>();
        String keywordForGoogle = "https://www.google.com/search?q=";
        String setOfCharacters = "UTF-8";
        String browser = "Mozilla";
        
        System.out.println("Finding on Google ");
        String str = keywordForGoogle + URLEncoder.encode(keyword, "UTF-8");
        Document documentObject = Jsoup.connect(str).userAgent(browser).get();
        Elements allLinks = documentObject.select("a");

        for (Element selectedLink : allLinks) {
            String url = selectedLink.attr("abs:href");
            String urlLink = "";
            if (url.indexOf("=") != -1 && url.indexOf("&") != -1) {
                urlLink = URLDecoder.decode(url.substring(url.indexOf("=") + 1, url.indexOf("&")), setOfCharacters);
            }
            if(!(urlLink.startsWith("https")) || urlLink.contains("youtube") || urlLink.contains("image"))
            {
                continue;
            }
            resultsFromGoogle.add(urlLink);
        }
        return resultsFromGoogle;
    }
    // This method is taken from black board
    private int editDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }

    private HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        return hm.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }
    static TST<Integer> create_tst()
    {
        TST<Integer>tst = new TST<>();
        List<String>words = get_words();
        for(int i=0;i<words.size();i++)
        {
            tst.put(words.get(i),i);
        }
        return tst;
    }
    static List<String> get_words()
    {
        List<String> words = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-Z]+");
        Matcher m1 = p.matcher(textCrawl);
        while(m1.find())
        {
            words.add(m1.group());
        }
        return words;
    }
}
