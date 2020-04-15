package com.qintingfm.web.service;

import com.qintingfm.web.htmlsucker.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;


/**
 * @author guliuzhong
 */
@Slf4j
@Service
public class HtmlService {

    public String filter(String html) {
        return filter(html, Whitelist.basicWithImages());
    }

    public String filterNone(String html) {
        String filter = filter(html, Whitelist.none());
        return decodeEntityHtml(filter);
    }

    public String filterSimpleText(String html) {
        return filter(html, Whitelist.simpleText());
    }

    public String filterRelaxed(String html) {
        return filter(html, Whitelist.relaxed());
    }


    private String filter(String html, Whitelist whitelist) {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        return Jsoup.clean(html, "", whitelist, outputSettings);
    }

    public String decodeEntityHtml(String html) {
        if (html==null){
            return null;
        }
        html = html.replaceAll("&ldquo;", "“");
        html = html.replaceAll("&rdquo;", "”");
        html = html.replaceAll("&amp;", "&");
        html = html.replaceAll("&nbsp;", " ");
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        return html;
    }

    /**
     * 最大文本长度抽取
     */
    public final static int MAX_TEXT_EXTRACTOR = 1;
    /**
     * 文本密度算法抽取
     */
    public final static int TEXT_DENSITY_EXTRACTOR = 2;
    int extractor = 2;

    public void setExtractor(int extractor) {
        this.extractor = extractor;
    }

    /**
     * 根据 URL 来解析文章信息
     *
     * @param url
     * @return
     */
    public Article parse(String url, int timeMillis) throws IOException {
        return parse(Jsoup.parse(new URL(url), timeMillis));
    }

    /**
     * 根据 html 内容来解析文章信息
     *
     * @param html
     * @return
     */
    public Article parse(String html) {
        return parse(Jsoup.parse(html));
    }

    private Article parse(Document doc) {
        Article art = new Article();
        art.setTitle(MetadataExtractor.title(doc));
        art.setDescription(MetadataExtractor.description(doc));
        art.setKeywords(MetadataExtractor.keywords(doc));
        art.setAuthor(MetadataExtractor.author(doc));
        art.setDate(MetadataExtractor.date(doc));
        art.setImage(MetadataExtractor.image(doc));
        ContentExtractor extractor2 = null;
        switch (extractor) {
            case 1:
                extractor2 = new MaxTextContentExtractor();
                break;
            case 2:
                extractor2 = new TextDensityExtractor();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + extractor);
        }

        //开始解析内容
        art.setContent(extractor2.content(doc.body()));

        if (art.getImage() == null || art.getImage().isEmpty()) {
            Document body = Jsoup.parse(art.getContent());
            Element img = body.select("img").first();
            if (img != null) {
                String src = img.attr("abs:src");
                if (src == null || src.isEmpty()) {
                    src = img.attr("data-src");
                }
                art.setImage(src);
            }
        }

        return art;
    }
}
