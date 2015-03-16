package hr.antivo.extractwebpagedata;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by antivo on 3/12/15.
 */
public class DateExtractor {
    static public String extract(Document document) {
        Elements time = document.select("time");
        for(Element elem : time) {
            if (null != elem.attr("datetime")) {
                return elem.attr("datetime");
            }
        }

        Elements meta = document.select("meta");
        for(Element elem : meta) {
            if (elem.attr("property").equals("article:published_time")
                    || elem.attr("itemprop").equals("datePublished")) {
                return elem.attr("content");
            }
        }

        return null;
    }
}
