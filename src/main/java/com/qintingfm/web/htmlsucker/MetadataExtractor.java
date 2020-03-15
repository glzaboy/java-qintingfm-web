package com.qintingfm.web.htmlsucker;

import org.jsoup.nodes.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 网页的元数据抽取
 */
public class MetadataExtractor {
    private static final String WHITESPACE = "[ \r\t\n]+";
    public static String author(Document doc) {
        try {
            return new HeuristicString(null)
                    .or(innerTrim(doc.select("meta[property=article:author]").text()))
                    .or(innerTrim(doc.select("meta[name=article:author_name]").text()))
                    .or(innerTrim(doc.select("meta[itemprop=author]").attr("content")))
                    .or(innerTrim(doc.select("meta[name=blogger_name]").attr("content")))
                    .or(innerTrim(doc.select("meta[name=author]").attr("content")))
                    .or(innerTrim(doc.select("meta[property=article:author]").attr("content")))
                    .or(innerTrim(doc.select("a[id=post-user]").text()))
                    .toString();
        } catch (HeuristicString.CandidateFound candidateFound) {
            return cleanTitle(candidateFound.candidate);
        }
    }

    private static String[] DATE_FORMATS = {"yyyy-MM-dd'T'hh:mm:ss","EEE MMM dd HH:mm:ss yyyy", "yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss", "yyyy-MM-dd"};

    public static Date date(Document doc) {
        String sdate;
        try {
            sdate = new HeuristicString(null)
                    .or(doc.select("meta[property=article:published_time]").attr("content"))
                    .or(doc.select("meta[name=date]").attr("content"))
                    .or(doc.select("meta[name=DisplayDate]").attr("content"))
                    .or(doc.select("meta[itemprop=datePublished]").attr("datetime"))
                    .or(doc.select("meta[itemprop=datePublished]").attr("content"))
                    .or(doc.select("meta[name=published_at]").attr("content"))
                    .or(doc.select("meta[property=article:modified_time]").attr("content"))
                    .or(doc.select("time[itemprop=datePublished]").attr("content"))
                    .or(doc.select("meta[name=dcterms.created]").attr("content"))
                    .or(doc.select("head meta[name=article:author_name]").attr("content"))
                    .or(doc.select("meta[name=pdate]").attr("content"))
                    .or(doc.select("meta[property=og:updated_time]").attr("content"))
                    .or(doc.select("meta[name=timestamp]").attr("content"))
                    .or(doc.select("meta[property=article:published]").attr("content"))
                    .or(doc.select("meta[property=bt:pubDate]").attr("content"))
                    .or(doc.select("em[id=post-date]").text())
                    .toString();
        } catch (HeuristicString.CandidateFound candidateFound) {
            sdate = candidateFound.candidate;
        }

        if(sdate != null) {
            for (String fmt : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(fmt, Locale.ENGLISH).parse(sdate);
                } catch (ParseException ignored) {
                }
            }
        }
        return null;
    }

    public static String image(Document doc) {
        try {
            return new HeuristicString(null)
                    .or(urlEncodeSpaceCharacter(doc.select("meta[name=twitter:image]").attr("content")))
                    .or(urlEncodeSpaceCharacter(doc.select("meta[property=og:image]").attr("content")))
                    .or(urlEncodeSpaceCharacter(doc.select("link[rel=image_src]").attr("href")))
                    .or(urlEncodeSpaceCharacter(doc.select("meta[name=thumbnail]").attr("content")))
                    .or(urlEncodeSpaceCharacter(doc.select("meta[itemprop=image]").attr("content")))
                    .toString();
        } catch (HeuristicString.CandidateFound candidateFound) {
            return candidateFound.candidate;
        }
    }

    public static String title(Document doc) {
        try {
            return cleanTitle(new HeuristicString(doc.title())
                    .or(innerTrim(doc.select("head title").text()))
                    .or(innerTrim(doc.select("meta[name=title]").attr("content")))
                    .or(innerTrim(doc.select("meta[property=og:title]").attr("content")))
                    .or(innerTrim(doc.select("meta[name=twitter:title]").attr("content")))
                    .toString());
        } catch (HeuristicString.CandidateFound candidateFound) {
            return cleanTitle(candidateFound.candidate);
        }
    }

    public static String description(Document doc) {
        try {
            return new HeuristicString(null)
                    .or(innerTrim(doc.select("meta[name=description]").attr("content")))
                    .or(innerTrim(doc.select("meta[property=og:description]").attr("content")))
                    .or(innerTrim(doc.select("meta[name=twitter:description]").attr("content")))
                    .toString();
        } catch (HeuristicString.CandidateFound candidateFound) {
            return candidateFound.candidate;
        }
    }

    public static Collection<String> keywords(Document doc) {
        String content = innerTrim(doc.select("meta[name=keywords]").attr("content"));
        if (content.startsWith("[") && content.endsWith("]"))
            content = content.substring(1, content.length() - 1);

        String[] split = content.split("\\s*,\\s*");
        if (split.length > 1 || (split.length > 0 && !"".equals(split[0])))
            return Arrays.asList(split);

        return Collections.emptyList();
    }

    /**
     * remove more than two spaces or newlines
     */
    public static String innerTrim(String str) {
        return str.replaceAll(WHITESPACE, " ").trim();
    }

    public static String cleanTitle(String title) {
        StringBuilder res = new StringBuilder();
        int index = title.lastIndexOf("|");
        if (index > 0 && title.length() / 2 < index)
            title = title.substring(0, index + 1);

        int counter = 0;
        String[] strs = title.split("\\|");
        for (String part : strs) {
            if (counter == strs.length - 1 && res.length() > part.length()) {
                continue;
            }
            if (counter > 0) {
                res.append("|");
            }
            res.append(part);
            counter++;
        }
        return innerTrim(res.toString());
    }

    public static String urlEncodeSpaceCharacter(String url) {
        return url.isEmpty() ? url : url.trim().replaceAll(WHITESPACE, "%20");
    }
}
