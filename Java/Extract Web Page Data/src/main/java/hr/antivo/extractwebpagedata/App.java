package hr.antivo.extractwebpagedata;

import de.l3s.boilerpipe.BoilerpipeProcessingException;

import org.codehaus.jackson.map.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by antivo on 3/12/15.
 */
public class App {
    private static void assetArgs( String[] args ) {
        if(args.length != 1) {
            System.out.println("Provide exactly one argument. Argument must be valid URL");
            System.exit(1);
        }
    }

    private static String resolveRedirect(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");

            boolean redirect = false;
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            if (redirect) {
                String newUrl = conn.getHeaderField("Location");
                url = newUrl;
            }
        } catch (Exception e) {

        }
        return url;
    }

    public static void main( String[] args ) throws IOException, BoilerpipeProcessingException {
        assetArgs(args);
        String url = resolveRedirect(args[0]);

        Document webPage = Jsoup.connect(url).get(); // default encoding UTF-8

        WebPageData wpd = new WebPageData();
        wpd.setMainArticleTitle(TitleExtractor.extract(webPage));
        wpd.setMainArticleContent(ContentExtractor.extract(url, wpd.getMainArticleTitle()));
        wpd.setAuthor(AuthorExtractor.extract(webPage));
        wpd.setPublishingDateOfTheArticle(DateExtractor.extract(webPage));

        ObjectMapper om = new ObjectMapper();
        String wpdJSON = om.writeValueAsString(wpd);
        System.out.println(wpdJSON);
    }
}
