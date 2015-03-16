package hr.antivo.extractwebpagedata;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.KeepEverythingWithMinKWordsExtractor;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by antivo on 3/12/15.
 */
public class ContentExtractor {

    static private final int YOUTUBE_MIN_WORDS_TRESHOLD = 20;

    static public String extract(String url, String title) throws MalformedURLException, BoilerpipeProcessingException {
        URL target = new URL(url);

        if(url.contains("youtube") || title.toUpperCase().contains("youtube".toUpperCase())) {
            KeepEverythingWithMinKWordsExtractor extractor = new KeepEverythingWithMinKWordsExtractor(YOUTUBE_MIN_WORDS_TRESHOLD);
            String extracted = extractor.getText(target);
            if(extracted.length() != 0) {
                return extracted;
            }
        }
        return ArticleExtractor.INSTANCE.getText(target);
    }
}
