package hr.antivo.extractwebpagedata;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by antivo on 3/12/15.
 */

// http://webia.lip6.fr/~labroche/documents/publications/labroche_fqas09.pdf
public class AuthorExtractor {
    static public String extract(Document document) {
        Elements meta = document.head().select("meta");
        for(Element elem : meta) {
            if (elem.attr("name").equals("author")) {
                return elem.attr("content");
            }
        }

        Elements elements = document.getAllElements();
        for(Element elem : elements) {
            if(elem.attr("rel").equals("author")) {
                return elem.text();
            }
        }

        return null;
    }
}
