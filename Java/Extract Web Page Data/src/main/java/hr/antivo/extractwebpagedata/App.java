package hr.antivo.extractwebpagedata;

import de.l3s.boilerpipe.BoilerpipeProcessingException;

import hr.antivo.extractwebpagedata.extractor.AuthorExtractor;
import hr.antivo.extractwebpagedata.extractor.ContentExtractor;
import hr.antivo.extractwebpagedata.extractor.DateExtractor;
import hr.antivo.extractwebpagedata.extractor.TitleExtractor;
import hr.antivo.extractwebpagedata.model.WebPageData;
import hr.antivo.extractwebpagedata.network.NetworkResolver;
import hr.antivo.extractwebpagedata.util.StringUtil;
import org.codehaus.jackson.map.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by antivo on 3/12/15.
 */
public class App {
    private static void assetArgs( String[] args ) throws IllegalArgumentException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please provide exactly one argument. Argument must be valid URL.");
        }
    }

    private static String cleanText(String text) {
        if(null != text) {
            text = StringUtil.cleanText(text);
        }
        return text;
    }

    public static void main( String[] args ) throws IOException, BoilerpipeProcessingException {
        try {
            assetArgs(args);
            String url = NetworkResolver.resolveRedirect(args[0]);

            Document webPage = Jsoup.connect(url).get(); // default encoding UTF-8

            String title = TitleExtractor.extract(webPage);
            title = cleanText(title);
            String content = ContentExtractor.extract(url);
            content = cleanText(content);
            String author = AuthorExtractor.extract(webPage);
            author = cleanText(author);
            String date = DateExtractor.extract(webPage);
            date = cleanText(date);

            WebPageData wpd = new WebPageData.WebPageDataBuilder()
                    .withMainArticleTitle(title)
                    .withMainArticleContent(content)
                    .withAuthor(author)
                    .withPublishingDateOfTheArticle(date)
                    .build();

            ObjectMapper om = new ObjectMapper();
            String wpdJSON = om.writeValueAsString(wpd);
            System.out.println(wpdJSON);
        } catch (Exception e) {
            System.out.println("Program terminated.");
            System.out.println(e.getMessage());
        }
    }
}
