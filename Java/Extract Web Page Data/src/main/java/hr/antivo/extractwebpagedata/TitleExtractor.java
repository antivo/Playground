package hr.antivo.extractwebpagedata;

import org.jsoup.nodes.Document;

/**
 * Created by antivo on 3/12/15.
 */
public class TitleExtractor {
    static public String extract(Document document) {
        return document.title();
    }
}
