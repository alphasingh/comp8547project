package com.comp8547.comp8547project.webcrawler;

import com.comp8547.comp8547project.service.SearchEngineService;

import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static java.awt.Desktop.Action.BROWSE;

public class Test {
    private static List<String> clean_urls;
    private static String selected_url;
    private static SearchEngineService service = new SearchEngineService();
    private static String keyword;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            String selected_url = start_and_get_selected_url();
            String clean_txt = service.crawlTextFromUrl(selected_url);
            System.out.println("OPENING IN BROWSER");
            browser_open(selected_url);
            System.out.println("ENTER A KEYWORD TO PERFORM SEARCH OPERATIONS");
            keyword = sc.next();
            int i = 1;
            while (i == 1) {

                System.out.println("PRESS 1 TO SEARCH EXACT POSITION OF THE WORD");
                System.out.println("PRESS 2 TO FIND THE FREQUENCY OF THE WORD IN THE PAGE");
                System.out.println("PRESS 3 TO FIND THE WORDS CLOSEST TO GIVEN KEYWORD IN SORTED ORDER");
                System.out.println("PRESS 4 TO FIND THE LONGEST PREFIX OF GIVEN KEYWORD");
                System.out.println("PRESS 5 TO FIND THE ALL THE WORDS WITH THE COMMON PREFIX OF GIVEN KEYWORD");
                System.out.println("PRESS 6 TO FIND THE POSITION OF WORD AMONG ALL THE ALPHABETIC WORDS");
                System.out.println("PRESS 7 TO CHANGE THE ENTERED WORD");
                System.out.println("PRESS 0 TO EXIT");
                int input = sc.nextInt();
                switch (input) {
                    case (0):
                        i = 0;
                        break;
                    case (1):
                        System.out.println("POSITION OF WORD IN THE PAGE IS  " + service.findWordInCrawledText(keyword));
                        break;
                    case (2):
                        System.out.println("THE FREQUENCY OF WORD IN THE PAGE IS " + service.countRegexPattern(keyword));
                        break;
                    case (3):
                        System.out.println("THE WORDS CLOSEST TO GIVEN WORD ALONG WITH THEIR DISTANCE ARE ");
                        HashMap<String, Integer> result = service.wordsWithSmallestEditDistance(keyword);
                        for (String key : result.keySet()) {
                            System.out.println("WORD " + key + " COUNT " + result.get(key));
                        }
                        break;
                    case (4):
                        String output = service.getLongestPrefix(keyword);
                        if (output == null) {
                            System.out.println("LONGEST PREFIX WORD DOESN'T EXIST");
                        } else {
                            System.out.println("THE LONGEST PREFIX FOR THE KEYWORD IS :" + output);
                        }
                        break;
                    case (5):
                        System.out.println("THE COMMON PREFIX MATCHES FOR KEYWORD ARE ");
                        Iterable<String> matches = service.getPrefixMatch(keyword);
                        Iterator<String> it = matches.iterator();
                        while (it.hasNext()) {
                            System.out.println(it.next());
                        }
                        break;
                    case (6):
                        Integer pos = service.get_value(keyword);
                        String print = pos == null ? "KEYWORD NOT FOUND" : "AMONG ALL THE WORDS PRESENT IN THE DOCUMENT, KEYWORD PRESENT AT POSITION " + pos.intValue();
                        System.out.println(print);
                        break;
                    case (7):
                        System.out.println("ENTER NEW WORD");
                        keyword = sc.next();
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void browser_open(String clean_url) throws Exception {
        final URI cleanUri = new URI(clean_url);
        final Desktop supportedDesktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (supportedDesktop != null && supportedDesktop.isSupported(BROWSE)) {
            try {
                supportedDesktop.browse(cleanUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String start_and_get_selected_url() throws Exception {
        System.out.println("............WELCOME TO SEARCH ENGINE.............");
        System.out.println("ENTER STRING TO SEARCH");
        String keyword = sc.next();
        clean_urls = service.searchFromGoogle(keyword);
        if (clean_urls.size() == 0) {
            System.out.println("NO URL FOUND");
            return null;
        } else {
            System.out.println("CLEANED URLS ARE");
            int num = 1;
            for (String url : clean_urls) {
                System.out.println(num + ". " + url);
                num++;
            }
            System.out.println("SELECT URL NUMBER");
            int url_num = sc.nextInt();
            if (url_num > clean_urls.size() || url_num < 1) {
                System.out.println("INVALID OPTION");
                url_num = 1;
            }
            selected_url = clean_urls.get(url_num - 1);
        }
        return selected_url;
    }
}
