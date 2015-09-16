package com.drmtx.reddit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Created by antivo on 8/27/15.
 */
@Component
public class RedditHttpClient implements RedditClient {
    private static final Logger logger = Logger.getLogger(RedditHttpClient.class);

    private static final HttpClient client = HttpClientBuilder.create().build();
    private static final String CONTENT_TYPE = "application/json";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_HEADER = "java";

    @Override
    public Optional<String> getCommentsResponseFor(String redditCommentURL) throws IllegalArgumentException {
        assertRedditCommentUrl(redditCommentURL);
        logger.info("Reddit Comment URL is well formed: " + redditCommentURL);
        return getResponse(redditCommentURL);
    }

    private static void assertRedditCommentUrl(String redditCommentURL) throws IllegalArgumentException {
        if(redditCommentURL.startsWith("http://www.reddit.com/r/java/comments/") ||
                redditCommentURL.startsWith("https://www.reddit.com/r/java/comments/")) {
            if(redditCommentURL.endsWith(".json")) {
                return;
            }
        }
        throw new IllegalArgumentException("Reddit Comment Url - invalid argument. Must define http protocol, point to reddit comments api and request json representation");
    }

    private static HttpGet makeHTTPGet(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader(USER_AGENT_HEADER, USER_AGENT);
        request.addHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE);
        return request;
    }

    private Optional<String> getResponse(String url) {
        try {
            HttpGet request = makeHTTPGet(url);
            HttpResponse response = client.execute(request);
            logger.debug("received response");
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer resultBuffer = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                resultBuffer.append(line);
            }
            String result = resultBuffer.toString();
            logger.debug("Collected response into string");
            return Optional.of(result);
        } catch(IOException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

}
