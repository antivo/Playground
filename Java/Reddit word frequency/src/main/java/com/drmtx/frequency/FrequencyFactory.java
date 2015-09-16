package com.drmtx.frequency;

import com.drmtx.reddit.RedditClient;
import com.drmtx.reddit.RedditResponseExtractor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by antivo on 8/27/15.
 */
@Component
public class FrequencyFactory {
    private static final Logger logger = Logger.getLogger(FrequencyFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedditClient redditClient;

    @Autowired
    RedditResponseExtractor redditResponseExtractor;

    /**
     * Creates frequency and returns a frequency if no error/exception happened
     * on the server side. If error happened on the server side returns empty
     * optional object. If Client caused error throws IllegalArgumentException
     * @param url url on Reddit comments api to be used in frequency analysis
     * @return frequency if no error/exception happened on the server side
     * @throws IllegalArgumentException if client's input caused error
     */
    public Optional<Frequency> makeFrequency(String url) throws IllegalArgumentException {
        Optional<String> response = redditClient.getCommentsResponseFor(url);
        logger.debug("Received response " + response + ". For url: " + url);
        Optional<Frequency> retFrequency = Optional.empty();
        try {
            if (response.isPresent()) {
                List<String> comments = redditResponseExtractor.extractCommentsFromResponse(response.get());
                logger.info("Extracted comments: " + comments);
                Map<String, Long> frequency = commentsToFrequency(comments);
                logger.info("Frequency analysis: " + frequency);
                retFrequency = Optional.of(new Frequency(frequency));
            }
        } catch(Throwable e) {
            logger.error(e.getMessage());
        }
        return retFrequency;
    }

    /**
     * Transform list of sentences and returns analysis of word frequency
     * @param comments list of sentences
     * @return stored analysis of word frequency
     */
    public static Map<String, Long> commentsToFrequency(List<String> comments) {
        return comments.stream().
                map(comment -> comment.replaceAll("[^a-zA-Z0-9 ]+","")). // replace all non word-ish charachters
                map(comment -> comment.toLowerCase()). // to lowercase
                map(ss -> ss.split("\\s+")).  // split on one or more whitespaces
                flatMap(array -> Arrays.stream(array)). // flatten stream from stream of arrays
                collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); // collect to hashmap
    }
}
